package com.ericsson.oss.cex.taf.ui.validators;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.cifwk.taf.osgi.client.ApiContainerClient;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.cifwk.taf.osgi.client.JavaApi;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.cifwk.taf.utils.cluster.PortForwardingUtility;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

@Operator
@Singleton
public class OsgiApiOperator implements ApiOperator, OsgiClientOperator {

	private static Logger log = Logger.getLogger(OsgiApiOperator.class);

	private static final Host masterHost = DataHandler.getHostByName("ossmaster");

	private static final Long CEX_START_TIME = 50000L;

	private static final String CEX_CONFIG = DataHandler.getAttribute("cex_config").toString();

	private static final String CEX_SCRIPT = DataHandler.getAttribute("cex_script").toString();

	private static final String XDISPLAY = DataHandler.getAttribute("xdisplay").toString();

	private static final String Cli_PORT = DataHandler.getAttribute("osgiCliPort").toString();

	private static final String DATA_PORT = DataHandler.getAttribute("osgiDataPort").toString();

	private static ApiClient client;

	public static void main(final String[] args) {

		checkAndUpdateConfigFile();
	}

	@Override
	public synchronized ApiClient getClient() {

		return getOsgiClient();
	}

	@Override
	public synchronized void startCex() throws ContainerNotReadyException, IOException {

		prepareCex();
	}

	@Override
	public void close() {

		ApiContainerClient.getInstance().stopApplication();
	}

	public static void prepareCex() throws ContainerNotReadyException, IOException {

		// check whether the two ports are configured in gateway; if not,
		// configure them
		//checkAndConfigPortInGateway();

		// construct ApiContainer, and return endPoint Uri
		final String endPoint = ApiContainerClient.constructEndpoint(masterHost, CEX_SCRIPT, CEX_CONFIG);
		if (!ApiContainerClient.getInstance().canConnect()) {
			checkAndUpdateConfigFile();
			ApiContainerClient.getInstance().prepare(XDISPLAY, CEX_START_TIME);
		}

		//create ApiClient
		if(client == null){
			client = JavaApi.createApiClient(endPoint);
		}
		if (client != null && client.isAlive()) {
			registerOSGIRemoteParts(client);
		} else {
			throw new RuntimeException("OsgiApiClient cannot connect to OSGI container via: " + endPoint);
		}

		//create ApiClient
		//		client = JavaApi.createApiClient(endPoint);
		//		if (client.isAlive()) {
		//			registerOSGIRemoteParts(client);
		//		}
		//		else {
		//			throw new RuntimeException("OsgiApiClient cannot connect to OSGI container via: " + endPoint);
		//		}
	}

	public static ApiClient getOsgiClient() {

		return client;
	}

	private static void checkAndConfigPortInGateway() {

		final Host gateway = CexApiGetter.getGateway();
		final String targetIp = "192.168.0.5";
		final String gatewayIp = gateway.getIp();
		final String gatewayRootPassword = gateway.getPass(UserType.ADMIN);

		final PortForwardingUtility utility = new PortForwardingUtility();
		String portToUse = utility.configureForwarding(targetIp, Cli_PORT, gatewayIp, gatewayRootPassword);
		System.out.println(portToUse);
		portToUse = utility.configureForwarding(targetIp, DATA_PORT, gatewayIp, gatewayRootPassword);
		System.out.println(portToUse);
	}

	private static void checkAndUpdateConfigFile() {

		final CexRemoteCommandExecutor rootExecutor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());

		final String updateCommand = "chown nmsadm:nms " + CEX_CONFIG;
		rootExecutor.simplExec(updateCommand);
		log.info("Change the owner of file [" + CEX_CONFIG + "] to nmsadm:nms ");

		final String checkCommand = "ls -l " + CEX_CONFIG;
		final String resp = rootExecutor.simplExec(checkCommand, false);
		log.info("The check result of the owner of CEX config file:\r\n" + resp);
		// final List<String> vals = Arrays.asList(resp.split("\\s+"));
		// if (vals.contains("nmsadm") && vals.contains("nms")) {
		// log.info("The config file [" + CEX_CONFIG + "] has the right owner");
		// }
		// else {
		// final String updateCommand = "chown nmsadm:nms " + CEX_CONFIG;
		// rootExecutor.simplExec(updateCommand);
		// log.info("Change the owner of fie [" + CEX_CONFIG +
		// "] to nmsadm:nms ");
		// }
	}

	private static void registerOSGIRemoteParts(final ApiClient client) throws IOException {

		final String workingDir = System.getProperty("user.dir");
		System.out.println("Current working directory =====> " + workingDir);

		final LinkedHashMap<String, String> groovyFiles = searchFilesInWorkspace(".groovy", "/target/classes/scripts/");
		if (groovyFiles.size() > 0) {
			for (final String resource : groovyFiles.values()) {
				// get the class name of groovy file
				final String fileName = resource.substring(resource.lastIndexOf(File.separator) + 1);
				final String remotePart = fileName.substring(0, fileName.indexOf(".groovy"));

				// register groovy class to Osgi container
				if (client.register(readResource(resource)).getValue().equals(remotePart)) {
					log.info("Deploy OSGI remote part " + remotePart + " successfully.");
				}
				else {
					System.out.println(client.register(readResource(resource)).getValue().toString());
					throw new IOException("Cannot deploy OSGi remote part: " + remotePart);
				}
			}
		}
	}

	public static LinkedHashMap<String, String> searchFilesInWorkspace(final String searchTerm, final String localDirFilterKeyword) {

		final LinkedHashMap<String, String> fileNamePathMap = new LinkedHashMap<String, String>();

		final List<String> searchedFiles = FileFinder.findFile(searchTerm);
		for (final String fullFilePath : searchedFiles) {
			final String dirKeyword = localDirFilterKeyword.replace("/", File.separator).replace("\\", File.separator);
			if (fullFilePath.toLowerCase().contains(dirKeyword)) {
				final int idx = fullFilePath.lastIndexOf(File.separator);
				final String fileName = idx >= 0 ? fullFilePath.substring(idx + 1) : fullFilePath;
				fileNamePathMap.put(fileName, fullFilePath);
			}
		}

		return fileNamePathMap;
	}
	private static String readResource(final String path) throws IOException {

		// final ClassLoader classLoader =
		// OsgiApiOperator.class.getClassLoader();
		// final InputStream in = classLoader.getResourceAsStream(path);
		final InputStream in = new FileInputStream(path);
		Scanner scanner = null;
		try {
			scanner = new Scanner(in);
			return scanner.useDelimiter("\\A").next();
		}
		finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
}

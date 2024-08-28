
package com.ericsson.oss.cex.taf.ui.validators;


import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;
import com.ericsson.cifwk.taf.tools.cli.handlers.impl.RemoteObjectHandler;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;

@SuppressWarnings("deprecation")
public class CexPreCheckApiOperator implements ApiOperator {

	private static Logger log = Logger.getLogger(CexPreCheckApiOperator.class);;

	private final boolean isShowDetails = true;

	private String consoleMessage;

	private static final String FAILED = "FAILED";

	private static final String PASSED = "OK";

	private static final String TRIGGER_PATH = "/netsim/inst/POC/";
	private static final String FileName = "simulationTextFile.txt";

	private static CexRemoteCommandExecutor preCheckSshRemoteCommandExecutor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	private static CexRemoteCommandExecutor rootRemoteExecutor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getRootHostMaster());
	private static RemoteObjectHandler remoteExecutor = CexApiGetter.getRemoteObjectHandler(CexApiGetter.getRootHostMaster());
	private static CexRemoteCommandExecutor netsimRemoteExecutor = CexApiGetter.getRemoteCommandExecutor(HostGroup.getAllNetsims().get(0));
	User user = new User("netsim","netsim",UserType.CUSTOM);
	
	public void netsimRestart(){
		try{
			CLICommandHelper sshExecutor  = new CLICommandHelper(HostGroup.getAllNetsims().get(0),user);
			String initialCommand ="/netsim/inst/restart_netsim" ;
			log.info("Netsim restarting......");
			String result =sshExecutor.execute(initialCommand);
			log.info("Result from the script = " + result.toString());
			if(result.toString().contains("NETSim started successfully")){
               
				log.info("Netsim restarted sucessfully");
			}else{
				log.info("Failed to  restarted Netsim");
			}
		}catch (Exception e){
			log.debug(e.getMessage());
		}
		
	}

	public boolean simulationOperator(String networkElement, String script){

		try{
			CLICommandHelper sshExecutor  = new CLICommandHelper(HostGroup.getAllNetsims().get(0),user);


			String neList[] = networkElement.split(";");

			String initialCommand = "rm -rf " + TRIGGER_PATH + FileName + " | touch " + TRIGGER_PATH + FileName;

			log.info("Creating file on the path :  " +  TRIGGER_PATH);
			sshExecutor.execute(initialCommand);

			for(String ne : neList ){
				sshExecutor.execute("echo " + ne + " >> " + TRIGGER_PATH + FileName);
			}
			log.info("Waiting for the script to execute....");

			//log.info(sshExecutor.execute("bash /netsim/inst/POC/Configuration.sh"));

			String result = sshExecutor.execute("bash /netsim/inst/POC/"+script  +" " +"/netsim/inst/POC/simulationTextFile.txt");

			log.info("Result from the script = " + result.toString());
			if(result.toString().contains("0")){

				return true;
			}
		}catch (Exception e){
			log.debug(e.getMessage());
		}
		/*try{
			log.info("<font color=purple><B>1> Starting the Nodes in Cex & Waiting for 4m...</B></font>");
			Thread.sleep(240000);
		}catch (InterruptedException e){
			log.debug(e.getMessage());
		}*/
		return false;
	}

	public String runPreChecks() {

		String testCaseResult = FAILED;
		final List<String> resultsList = new ArrayList<String>();

		//Important to make client launch consistency
		updateOwnerConfigFile();
		//updateCexConfigFile();

		resultsList.add(checkMCOnline());
		//		resultsList.add(checkOsgiBundles());    
		//		resultsList.add(checkFTPServices());

		for (final String result : resultsList) {
			log.info("Result is: " + result);
			if (result.contains(FAILED)) {
				log.info("<font color=red><U>Pre check failed!</U></font>");
				return testCaseResult = result;
			}
			else if (result.contains(PASSED)) {
				log.info("<font color=green><U>Pre check passed </U></font>");
				testCaseResult = result;
			}
		}
		return testCaseResult;
	}

	/**
	 * Overriding cex_client_application.ini to same path in server
	 * @param cex_client_application
	 */
	public void updateCexConfigFile() {

		String scriptFolder  = DataHandler.getAttribute("cex_config_file").toString();
		scriptFolder = scriptFolder + File.separator;
		final List<String> groovyFiles = FileFinder.findFile(".ini",scriptFolder);
		log.info("Overriding cex_client_application.ini in same path - /opt/ericsson/nms_cex_client/bin");
		remoteExecutor.copyLocalFileToRemote(groovyFiles.get(0), "/opt/ericsson/nms_cex_client/bin");
	}

	/**
	 * Killing the Cex Client Process
	 * 
	 */
	public void clientProcessKiller(){

		try{

			log.info("Killing all the cex client process");

			String clientProcess = rootRemoteExecutor.simplExec("ps -eaf | grep -i /opt/ericsson/nms_cex_client/bin/cex_client_application");

			log.info(clientProcess);

			List<String> listProcess = new ArrayList<String>(Arrays.asList(clientProcess.split("\n")));

			for(int i=0;i<listProcess.size();i++){
				rootRemoteExecutor.simplExec("kill -9 "+ listProcess.get(i).substring(8,15));
			}
		}
		catch(Exception e){
			log.info(e.getMessage());
		}

	}
	/**
	 * Changing the permission root cex_client_application.ini to nmsadm
	 * @param cex_client_application
	 */
	public void updateOwnerConfigFile() {

		try{
			log.info("Changing the permission root cex_client_application.ini to nmsadm");

			rootRemoteExecutor.simplExec("chown nmsadm:nms /opt/ericsson/nms_cex_client/bin/cex_client_application.ini");

			rootRemoteExecutor.simplExec("ls -ltr /opt/ericsson/nms_cex_client/bin/cex_client_application.ini");
		}
		catch(Exception e){
			log.info(e.getMessage());
		}

	}
	/**
	 * Restarting Cex Mc
	 * @param oss_cex
	 */
	public void restartMc() {

		log.info("Restarting oss_cex & Waiting for 3m...");

		rootRemoteExecutor.simplExec(
				"/opt/ericsson/nms_cif_sm/bin/smtool cold oss_cex -reason=other -reasontext=TAF_RUN");
		try{
			Thread.sleep(180000);
		}catch (InterruptedException e){
			log.debug(e.getMessage());
		}
	}

	public String getSystemProperties() {

		final String SYSTEM_ENV = "/etc/opt/ericsson/system.env";
		String IM_ROOT = rootRemoteExecutor.simplExec("cat " + SYSTEM_ENV + " |  grep -i IM_ROOT | sed 's/.*=//'");
		if(IM_ROOT!=null && !IM_ROOT.equals("")){
			return IM_ROOT;
		}
		return null;
	}

	/**
	 * Check does the Ftp Server and Ftp Services exist
	 * 
	 * @return OK if Ftp Server and Ftp Services exist
	 */
	public String checkFTPServices() {

		log.info("<font color=purple><B>5> Start to check FtpServer and FtpService...</B></font>");
		final LinkedHashMap<String, String> outputMap = new LinkedHashMap<String, String>();
		final LinkedHashMap<String, String> unexpectedResultMap = new LinkedHashMap<String, String>();
		final String ftpServerName = "SMRSSLAVE-LRAN-nedssv4";
		final String ftpServicePattern = "aif,back,config,key,sws";
		final String[] ftpServices = ftpServicePattern.split(",");
		final String successMessage = "Pre-check on FtpServer & FtpService is SUCCESSFUL.";
		final String failMessage = "Pre-check on FtpServer & FtpService FAILED, details below:";
		final String consoleMessage = "Available FtpServer & FtpServices:";

		try {
			checkDoesFtpServerExist(outputMap, unexpectedResultMap, ftpServerName);

			checkDoesFtpServiceExist(outputMap, unexpectedResultMap, ftpServerName, ftpServices);
		}
		catch (final Exception ex) {
			log.equals("EXCEPTION " + ex.getMessage());
		}

		return analyseResultsAndReturnToTestCase(outputMap, unexpectedResultMap, successMessage, failMessage,
				consoleMessage);
	}

	@SuppressWarnings("resource")
	private void checkDoesFtpServiceExist(final LinkedHashMap<String, String> outputMap,
			final LinkedHashMap<String, String> unexpectedResultMap, final String ftpServerName,
			final String[] ftpServices) {

		String line;
		String myCmdString;
		myCmdString = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s ONRM_CS lt FtpService | grep "
				+ ftpServerName;

		final String checkFtpService = preCheckSshRemoteCommandExecutor.simplExec(myCmdString);

		final Pattern pattern = Pattern.compile("FtpService=(.*)");
		final StringBuilder sbServices = new StringBuilder();
		final Scanner scanner = new Scanner(checkFtpService);
		while (scanner.hasNextLine() && (line = scanner.nextLine()) != null) {

			final String key = parseLine(line, 0, pattern);
			log.info("Key ==> " + key);
			log.info("Value ==> " + line);
			outputMap.put("FtpService-" + key, line);
			sbServices.append(key + ",");
		}
		final String servicesStr = sbServices.toString();
		for (final String serviceKey : ftpServices) {
			if (!servicesStr.contains(serviceKey)) {
				log.info("<font color=red>FtpService Not Found ==> " + serviceKey + "</font");
				unexpectedResultMap.put(serviceKey, "FtpService Not Found");
			}
		}
	}

	@SuppressWarnings("resource")
	private void checkDoesFtpServerExist(final LinkedHashMap<String, String> outputMap,
			final LinkedHashMap<String, String> unexpectedResultMap, final String ftpServerName) {

		String line;
		String myCmdString;
		myCmdString = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s ONRM_CS lt FtpServer | grep "
				+ ftpServerName;

		final String checkFtpServer = preCheckSshRemoteCommandExecutor.simplExec(myCmdString);
		final Scanner checkFtpScanner = new Scanner(checkFtpServer);
		while (checkFtpScanner.hasNextLine() && (line = checkFtpScanner.nextLine()) != null) {
			log.info("FtpServer ==>" + line);
			outputMap.put("FtpServer", line);
		}
		if (outputMap.size() == 0) {
			log.info("<font color=red>No FtpServer found! </font>");
			unexpectedResultMap.put(ftpServerName, "FtpServer Not Found");
		}
	}

	@SuppressWarnings("resource")
	public String checkMCOnline() {

		log.info("<font color=purple><B>1> Start to check required online MCs...</B></font>");

		final LinkedHashMap<String, String> outputMap = new LinkedHashMap<String, String>();
		final LinkedHashMap<String, String> unexpectedResultMap = new LinkedHashMap<String, String>();
		final String mcs = "ONRM_CS,Seg_masterservice_CS,OsgiFwk,oss_cex";

		final String command = "/opt/ericsson/bin/smtool ";
		final String option = "list ";
		final String[] requiredMCs = mcs.trim().split(",");
		String testResult;

		try {

			for (final String mc : requiredMCs) {

				final String checkMcCommand = command + option + mc;
				final String checkMCs = preCheckSshRemoteCommandExecutor.simplExec(checkMcCommand);
				preCheckSshRemoteCommandExecutor.simplExec("rm -rf ~/cex ~/ocp");

				final Scanner scanner = new Scanner(checkMCs);
				String line = null;
				while (scanner.hasNextLine() && (line = scanner.nextLine()) != null) {
					boolean mcFound = false;
					// for (final String mc : requiredMCs) {
					if (line.toLowerCase().contains(mc.toLowerCase())) {
						mcFound = true;
						if (line.toLowerCase().contains("started".toLowerCase())) {
							// the mc is online
							outputMap.put(mc, "started");
						}
						else {
							// the mc is installed but not online
							final int index = line.lastIndexOf(" ");
							unexpectedResultMap.put(mc, line.substring(index));
							mcFound = false;
							break;
						}
						break;
					}
					if (!mcFound) {
						unexpectedResultMap.put("Unknown", line);
					}
				}
			}
		}
		catch (final Exception e) {
			unexpectedResultMap.put("EXCEPTION ", e.toString());
		}

		if (outputMap.size() == requiredMCs.length && unexpectedResultMap.size() == 0) {

			log.info("<font color=green>The pre-check on required online MCs is SUCCESSFUL. </font>");
			testResult = PASSED;
		}
		else {
			consoleMessage = "Pre-check on required online MCs is failed. See problems as below:";
			testResult = failTestCase(unexpectedResultMap, consoleMessage);
		}
		consoleMessage = "Online MCs:";
		printOutDetails(outputMap, consoleMessage);
		return testResult;

	}

	public String checkOsgiBundles() {

		log.info("<font color=purple><B>2> Start to check checkOsgiBundles ......</B></font>");
		String testResult = null;
		final LinkedHashMap<String, String> outputMap = new LinkedHashMap<String, String>();

		final String myCmdString = "/opt/ericsson/nms_cif_sm/bin/smtool -action OsgiFwk listBundles com.ericsson.oss! com.ericsson.oss!";

		try {

			final StringBuilder sb = new StringBuilder();

			final String result = preCheckSshRemoteCommandExecutor.simplExec(myCmdString, false);
			log.info("Executing smtool command \n");

			final Scanner scanner = new Scanner(result);
			final int count = scanThroughLineToFindOsgiBundles(sb, scanner, result);

			outputMap.put("OsgiBundles", result);
			log.info("bundle found-->"+count);
			testResult = "OK";

		}
		catch (final Exception ex) {
			log.warn("Exception Thrown == > " + ex.toString());
		}

		log.info("End to check checkOsgiBundles ......");

		return testResult;
	}

	private int scanThroughLineToFindOsgiBundles(final StringBuilder sb, final Scanner scanner, final String result) {

		String line = null;

		final String[] bundleKeywords = new String[] { "cex","utilities","common","service","nsd","manager","cp" };
		final LinkedHashMap<String, String> outputMap = new LinkedHashMap<String, String>();
		final Pattern pattern = Pattern.compile("(com.+)_[\\d|\\.]{5}");
		System.out.println("pattern" + pattern);

		int count = 0;

		while (scanner.hasNextLine() && (line = scanner.nextLine()) != null) {
			for (final String keyword : bundleKeywords) {
				if (line.contains(keyword)) {
					final String key = MyFixtureHelper.parseLine(line, 2, pattern);
					outputMap.put(key, line);
					count++;
					break;
				}
			}
		}
		consoleMessage = "Relevant OsgiBundles Online\n";
		printOutDetails(outputMap, consoleMessage);
		return count;

	}

	/*private void addTemplateToTemplatesFoundMap(final LinkedHashMap<String, String> templatesFoundMap,
			final String templateName) {

		templatesFoundMap.put(templateName, "this template is created.");
		log.info("Template found ==> " + templateName);
		log.info("TemplatesFoundMap size = " + templatesFoundMap.size());
	}*/

	private String analyseResultsAndReturnToTestCase(final LinkedHashMap<String, String> outputMap,
			final LinkedHashMap<String, String> unexpectedResultMap, final String successMessage,
			final String failMessage, final String consoleMessage) {

		String testResult;
		if (unexpectedResultMap.size() == 0) {
			log.info(successMessage);
			testResult = PASSED;
		}
		else {
			testResult = failTestCase(unexpectedResultMap, failMessage);
		}
		printOutDetails(outputMap, consoleMessage);
		return testResult;
	}

	private String failTestCase(final LinkedHashMap<String, String> unexpectedResultMap, final String consoleMessage) {

		String testResult;
		final StringBuilder sb = new StringBuilder(consoleMessage + "\r\n");
		for (final Entry<String, String> pair : unexpectedResultMap.entrySet()) {
			sb.append(String.format("%1$s: %2$s\r\n", pair.getKey(), pair.getValue()));
		}
		log.error(sb.toString().replaceAll("\r\n$", ""));

		testResult = FAILED;
		return testResult;
	}

	private void printOutDetails(final LinkedHashMap<String, String> outputMap, final String consoleMessage) {

		if (isShowDetails) {
			final StringBuilder sb = new StringBuilder(consoleMessage + "\r\n");
			for (final Entry<String, String> entry : outputMap.entrySet()) {
				sb.append(String.format("%1$s: %2$s\r\n", entry.getKey(), entry.getValue()));
			}

			log.info(sb.toString());
		}
	}

	static String parseLine(final String line, final int index, final Pattern pattern) {

		String key = null;

		final String[] arr = line.trim().split("\\s+");

		final String tmp = arr[index];
		if (pattern != null) {
			final Matcher m = pattern.matcher(tmp);
			if (m.find()) {
				key = m.group(1);
			}
			else {
				key = "[KeyNotFound: " + line + "]";
			}

		}
		else {
			key = tmp;
		}

		return key;
	}

	private static final class MyFixtureHelper {

		static String getCurrentTimeStamp() {

			final java.util.Date date = new java.util.Date();
			return new Timestamp(date.getTime()).toString();
		}

		@SuppressWarnings("unused")
		static void log(final String content) {

			log(LogType.INFO, content);
		}

		static void log(final LogType type, final String content) {

			System.out.println(String.format("[%1$s] - %2$s: %3$s", type, getCurrentTimeStamp(), content));
		}

		static String parseLine(final String line, final int index, final Pattern pattern) {

			String key = null;

			final String[] arr = line.trim().split("\\s+");

			final String tmp = arr[index];
			if (pattern != null) {
				final Matcher m = pattern.matcher(tmp);
				if (m.find()) {
					key = m.group(1);
				}
				else {
					key = "[KeyNotFound: " + line + "]";
				}

			}
			else {
				key = tmp;
			}

			return key;
		}

		enum LogType {
			INFO, WARNING, ERROR;
		}

	}

}

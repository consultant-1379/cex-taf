package com.ericsson.oss.cex.taf.ui.getters;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.oss.cex.taf.ui.validators.CexPreCheckApiOperator;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;

@SuppressWarnings("deprecation")
public class PrepareCexTest extends TorTestCaseHelper implements TestCase {

	private CexOperators operatorInstance ;

	private static final Logger log = Logger.getLogger(PrepareCexTest.class);
	final CexPreCheckApiOperator cexPreCheckOperator = new CexPreCheckApiOperator();
	private boolean activationStartTimedOut = true;
	private int checkHeartBeat;
	private int maxActivationTime = 0;

	/**
	 * @throws InterruptedException 
	 * @DESCRIPTION This test case verifies the launching of the CEx GUI
	 * @Nodes are connected and synchronised. MC is online and MBeans objects exist.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-43991_Func_1", title = "Prepare CEX and get Osgi Container ready")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void prepareTheRun() throws ContainerNotReadyException, IOException, InterruptedException {

		setTestStep("Pre-check before running Cex test suite");

		assertEquals(cexPreCheckOperator.runPreChecks(), "OK");

		do{
			cexPreCheckOperator.clientProcessKiller();   //Killing all the cex client process
			checkHeartBeat = checkClientReturn();

			if(maxActivationTime == 4){
				cexPreCheckOperator.restartMc();
			}
			maxActivationTime++;
			if(maxActivationTime == 5)     //Retrying CEXOperator 5 times to make a successful connection.
				break;

		}while(checkHeartBeat == 1);

		if(checkHeartBeat == 0){
			log.info("Application is launched successfully");
			assertTrue(activationStartTimedOut);
		}else{
			assertFalse(activationStartTimedOut);
		}
	}
	public int checkClientReturn(){

		try{
			operatorInstance = new CexOperators(HostGroup.getOssmaster());
			operatorInstance.prepareCex();
			operatorInstance.setClient(operatorInstance.getOsgiClient());

			return 0;

		}catch(Exception e){
			log.info("Application startup has been interrupted,Re-trying Again....", e);

			try{
				operatorInstance.stopApplication();

			}catch(Exception ex){
				log.info("Application close has been interrupted.", e);
			}	
			return 1;
		}

	}

	@TestId(id="OSS-43991_Func_1", title = "Application Closed After Suite")
	@AfterSuite
	public void stopClient() throws ContainerNotReadyException, IOException {

		try{
			operatorInstance.stopApplication();
			log.info("Application is Closed successfully");

		}
		catch(Exception e){
			log.info("Application close has been interrupted.", e);
			cexPreCheckOperator.clientProcessKiller();
		}

	}

}

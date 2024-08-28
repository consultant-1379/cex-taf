package com.ericsson.oss.cex.taf.tests.son;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.NetsimCommand;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.taf.cshandler.model.Fdn;


@Operator(context = Context.API)
public class ActivateDeactivateFeatureOperator implements IActivateDeactivateFeatureOperator{

	private static Logger log = Logger.getLogger(ActivateDeactivateFeatureOperator.class);

	// Features
	private static final String ULMobility = "ULMobility"; // UL Mobility Feature
	private static final String NBIOT = "NBIOT"; //NarrowBand IoT Access
	private static final String MicroSleepTx = "MicroSleepTx";

	// OptionalFeatureLicense
	private static final String OptionalFeatureLicense = "OptionalFeatureLicense";
	private static final String[] erbsLicense = {"UlTrigInterFreqMob","MobCtrlAtPoorCov","NarrowbandIoTAccess","MicroSleepTx"};

	//FeatureState
	private static final String FeatureState = "FeatureState";
	private static final String[] ssr_erbsLicense = {"CXC4011072","CXC4011345","CXC4012081","CXC4011803"};
	//	"CXC4011072";  //UlTrigInterFreqMob
	//	"CXC4011345";  //MobCtrlAtPoorCov
	//	"CXC4012081";  //NarrowbandIoTAccess
	//	"CXC4011803";  //MicroSleepTx



	private String ulTrigFeature = null;
	private String ulTrigFunction = null;

	private CexCsHandler cexCsHandler;
	private static final String SUCCESS = "OK";
	@Inject
	private TopologyManager topologyManager;
	@Inject
	private GroovyTestOperators groovyOperator;

	private static String GROOVY_FILE = "ActivateDeactivateFeature";

	public ActivateDeactivateFeatureOperator() {
		cexCsHandler = new CexCsHandler();
	}


	@Override
	public String getRequiredFdn(String nodeType) {

		NetsimCommand netsim = new NetsimCommand();
		String requiredFdn = null;
		boolean createMoResult = false;
		int createMoRetryCount = 0;

		if (nodeType.equalsIgnoreCase("ERBS")) {

			// Version Greater than G will be used to activate/deactivate UlTrig
			requiredFdn = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "getRequiredFdn",nodeType);
			List<String> erbs_license = Arrays.asList(erbsLicense);
			try{
				for(String iLicense : erbs_license){
					createMoResult = false;
					createMoRetryCount = 0;
					while(!createMoResult && createMoRetryCount <=3){
						netsim.CreateMoCommandNetsim(requiredFdn,OptionalFeatureLicense, iLicense);
						Thread.sleep(2000);
						createMoResult = verifyLicense(requiredFdn, OptionalFeatureLicense, iLicense);
						createMoRetryCount++;
						if(createMoResult == false){
							log.info("License not Present.... will be Retrying");
						}
					}
				}
			}catch(Exception e){
				log.info(" Exception While Inserting License : "+e.getMessage());
			}


		}else 	if (nodeType.equalsIgnoreCase("SSR-ERBS")) {

			// Version Greater than G will be used to activate/deactivate UlTrig
			requiredFdn = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "getRequiredFdn",nodeType);

			List<String> ssr_license = Arrays.asList(ssr_erbsLicense);
			try{
				for(String iLicense : ssr_license){
					createMoResult = false;
					createMoRetryCount = 0;
					while(!createMoResult && createMoRetryCount<=3){
						netsim.CreateMoCommandNetsim(requiredFdn,FeatureState, iLicense);
						Thread.sleep(2000);
						createMoResult = verifyLicense(requiredFdn, FeatureState, iLicense);
						createMoRetryCount++;
						if(createMoResult == false){
							log.info("License not Present.... will be Retrying");
						}
					}
				}
			}catch(Exception e){
				log.info(" Exception While Inserting License : "+e.getMessage());
			}


		} 
		return requiredFdn;
	}

	@Override
	public boolean activateDeactivateFeature(String requiredFdn,
			String nodeType, String operationType, String featureType) {
		String fdn[]=requiredFdn.split(":");
		String requireFdn =null;

		if(nodeType.equalsIgnoreCase("ERBS")){
			requireFdn = fdn[0];
		}else if(nodeType.equalsIgnoreCase("SSR-ERBS")){
			requireFdn = fdn[1] ;
		}

		String result = null;

		switch(featureType){

		case (ULMobility) :

			String mcpcResult = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "activateMCPC",requireFdn,nodeType);
		String mcpcMo = null;
		if(mcpcResult.equalsIgnoreCase(SUCCESS)){
			if(nodeType.equals("ERBS")){
				mcpcMo = requireFdn+",ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=MobCtrlAtPoorCov";
			}else if(nodeType.equals("SSR-ERBS")){
				mcpcMo = requireFdn+",ManagedElement=" +requireFdn.substring(requireFdn.lastIndexOf('=')+1 )+ ",SystemFunctions=1,Lm=1,FeatureState=CXC4011345";
			}
			String csState= cexCsHandler.getAttributeByFdn(mcpcMo,"featureState");
			if(csState.equals("1")){
				result = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "activateDeactivateUlTrig",operationType,requireFdn,nodeType,featureType);
			}
		}
		if(nodeType.equals("ERBS")){

			ulTrigFeature = "featureState";
			ulTrigFunction = ",ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=UlTrigInterFreqMob";
		}else if(nodeType.equals("SSR-ERBS")){

			ulTrigFeature = "featureState";
			ulTrigFunction = ",ManagedElement=" +requireFdn.substring(requireFdn.lastIndexOf('=')+1 )+ ",SystemFunctions=1,Lm=1,FeatureState=CXC4011072";
		}

		break;

		case NBIOT :
			result = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "activateDeactivateUlTrig",operationType,requireFdn,nodeType,featureType);

			if(nodeType.equals("ERBS")){

				ulTrigFeature = "featureState";
				ulTrigFunction = ",ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=NarrowbandIoTAccess";
			}else if(nodeType.equals("SSR-ERBS")){

				ulTrigFeature = "featureState";
				ulTrigFunction = ",ManagedElement=" +requireFdn.substring(requireFdn.lastIndexOf('=')+1 )+ ",SystemFunctions=1,Lm=1,FeatureState=CXC4012081";
			}
			break;

		case MicroSleepTx : {
			result = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "activateDeactivateUlTrig",operationType,requireFdn,nodeType,featureType);

			if(nodeType.equals("ERBS")){

				ulTrigFeature = "featureState";
				ulTrigFunction = ",ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=MicroSleepTx";
			}else if(nodeType.equals("SSR-ERBS")){

				ulTrigFeature = "featureState";
				ulTrigFunction = ",ManagedElement=" +requireFdn.substring(requireFdn.lastIndexOf('=')+1 )+ ",SystemFunctions=1,Lm=1,FeatureState=CXC4011803";
			}
		}
		break;
		}


		if (result.equalsIgnoreCase(SUCCESS)) {
			return true;
		}
		return false;

	}

	@Override
	public boolean verifyChangeInCS(String requiredFdn, String nodeType,
			String operationType, String featureType) {
		boolean status = false;

		String fdn[]=requiredFdn.split(":");
		String requireFdn =null;

		if(nodeType.equalsIgnoreCase("ERBS")){
			requireFdn = fdn[0];
		}else if(nodeType.equalsIgnoreCase("SSR-ERBS")){
			requireFdn = fdn[1] ;
		}
		String functionMo = requireFdn+ulTrigFunction;
		String csState= cexCsHandler.getAttributeByFdn(functionMo,ulTrigFeature);

		if(operationType.equalsIgnoreCase("activate")){
			if(csState.contains("1")){
				status=true;
			}
		}else if(operationType.equalsIgnoreCase("deactivate")){
			if(csState.contains("0")){
				status=true;
			}
		}

		return status;
	}

	boolean verifyLicense(String fdn, String childMotoBeCreated, String license){
		fdn = fdn + " | grep -i " + childMotoBeCreated;
		List<Fdn> fdnList = cexCsHandler.getChildMos(fdn);
		if(fdnList.toString().contains(license)){
			return true;	
		}
		return false;
	}

}


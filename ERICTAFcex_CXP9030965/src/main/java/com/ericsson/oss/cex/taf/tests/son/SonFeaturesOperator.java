package com.ericsson.oss.cex.taf.tests.son;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.NetsimCommand;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.taf.cshandler.model.Fdn;


@Operator(context = Context.API)
public class SonFeaturesOperator implements ISonFeaturesOperator {

	private static Logger log = Logger.getLogger(SonFeaturesOperator.class);

	// SON Features
	private static final String LTE_ANR_INTER = "LTE_ANR_INTER";
	private static final String LTE_ANR_INTRA = "LTE_ANR_INTRA";
	private static final String LTE_ANR_GSM = "LTE_ANR_GSM";
	private static final String LTE_ANR_UTRAN = "LTE_ANR_UTRAN";
	private static final String LTE_MRO_ULHOC = "LTE_MRO_ULHOC";
	private static final String LTE_MRO_HO_OPT_CNTRL = "LTE_MRO_HO_OPT_CNTRL";
	private static final String LTE_DRO_PREAMBLE_POWER = "LTE_DRO_PREAMBLE_POWER";
	private static final String LTE_DRO_ROOT_SEQ_ALLOC = "LTE_DRO_ROOT_SEQ_ALLOC";
	private static final String LTE_PCI_CONFLICT_DETECT = "LTE_PCI_CONFLICT_DETECT";
	private static final String LTE_LOAD_BALANCING_INTER_FREQ = "LTE_LOAD_BALANCING_INTER_FREQ";
	private static final String LTE_LOAD_BALANCING_INTRA_FREQ = "LTE_LOAD_BALANCING_INTRA_FREQ";
	private static final String LTE_ADV_CELL_SUP = "LTE_ADV_CELL_SUP";
	private static final String IRAT_OFFLOAD_WCDMA = "IRAT_OFFLOAD_WCDMA";
	private static final String WCDMA_ANR_INTRA = "WCDMA_ANR_INTRA";
	private static final String WCDMA_ANR_RELATION_CREATE = "WCDMA_ANR_RELATION_CREATE";
	private static final String SSLM = "SSLM";
	private static final String LTE_MLSTM = "LTE_MLSTM";
	private static final String LTE_ANR_PLMN_WHITE_LIST = "LTE_ANR_PLMN_WHITE_LIST";

	// OptionalFeatureLicense
	private static final String OptionalFeatureLicense = "OptionalFeatureLicense";
	private static final String[] erbsLicense = {"Anr","AdvCellSup","ServiceSpecificLoadMgmt","Pci","HoOscCtrlUE","InterFrequencyLoadBalancing","MultiLayerServTriMobility","MobCtrlAtPoorCov","AutoRachRsAlloc"};
	private static final String[] erbsLicenseRobot = {"Anr","AdvCellSup","ServiceSpecificLoadMgmt","Pci","HoOscCtrlUE","InterFrequencyLoadBalancing","MultiLayerServTriMobility","MobCtrlAtPoorCov","AutoRachRsAlloc","NarrowbandIoTAccess"};
	//FeatureState
	private static final String FeatureState = "FeatureState";
	private static final String[] ssr_erbsLicense = {"CXC4010620","CXC4011183","CXC4010320","CXC4011157","CXC4011376","CXC4011319","CXC4011478","CXC4011477","CXC4011246"};
	private static final String[] ssr_erbsLicenseRobot = {"CXC4010620","CXC4011183","CXC4010320","CXC4011157","CXC4011376","CXC4011319","CXC4011478","CXC4011477","CXC4011246","CXC4012081"};
	//	"CXC4010620"; // Feature State for ANR Son Feature
	//	"CXC4011183"; // Feature State for PCI Son Feature
	//	"CXC4010320"; // Feature State for Advanced Cell Supervision
	//	"CXC4011157"; // Feature State for MRO UE Level Oscillation Control
	//	"CXC4011376"; // Feature State for MRO HO Optimization Control
	//	"CXC4011319"; // Feature State for Load Management IFLB
	//	"CXC4011478"; // Feature State for Load Management IRAT Offload
	//	"CXC4011477"; // Feature State for Load Management SSLM
	//	"CXC4011246"; // Feature State for DRO Root Sequence Allocation
    //	"CXC4012081"; // Feature State for NbioT 
	@Inject
	private TopologyManager topologyManager;
	@Inject
	private GroovyTestOperators groovyOperator;

	private static String GROOVY_FILE = "SonFeatures";

	private CexCsHandler cexCsHandler;

	private static final String SUCCESS = "OK";

	private String attributeValue = null;

	private String sonFeature = null;

	private String sonFunction = null;



	public SonFeaturesOperator() {
		cexCsHandler = new CexCsHandler();
	}

	/** 
	 * Differentiate on the basis of CDB KGB suite
	 * @param nodeType
	 * 		  ERBS RBS
	 * @param groupType
	 *		  CDB KGB
	 */
	public String getRequiredFdn(String nodeType, String groupType) {

		NetsimCommand netsim = new NetsimCommand();
		String requiredFdn = null;
		boolean result = false;
		int count = 0;

		if (groupType.equalsIgnoreCase("CDB")) {

			try{
                                                // Version Greater than E will be used to activate/deactivate ANR in CDB
                requiredFdn =  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "getFilteredErbs");
                log.info("Going to create " +erbsLicense[0] +" License on ERBS node");
                netsim.CreateMoCommandNetsim(requiredFdn, OptionalFeatureLicense,erbsLicense[0]);
                result = verifyLicense(requiredFdn,OptionalFeatureLicense,erbsLicense[0]);
                if(result == false){
					log.info("Licsence not present.....");
                }
                    }catch(Exception e){
						log.info(" Exception While Inserting License : "+e.getMessage());
                                       }

				} else if (groupType.equalsIgnoreCase("KGB")) {

			if (nodeType.equalsIgnoreCase("ERBS")) {

				// Version Greater than E will be used to activate/deactivate ANR
				requiredFdn = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "getFilteredErbs");
				List<String> erbs_license = Arrays.asList(erbsLicense);
				try{
					for(String iLicense : erbs_license){
						result = false;
						count = 0;
						while(!result && (count <= 3)){

							netsim.CreateMoCommandNetsim(requiredFdn,OptionalFeatureLicense, iLicense);

							result = verifyLicense(requiredFdn,OptionalFeatureLicense,iLicense);
							Thread.sleep(2000);
							count ++;
							if(result == false){
								log.info("License not Present.... will be Retrying");
							}
						}
					}
				}catch(Exception e){
					log.info(" Exception While Inserting License : "+e.getMessage());
				}

			} else if (nodeType.equalsIgnoreCase("ROBOTERBS")) {

				 //Version Greater than E will be used to activate/deactivate ANR
				requiredFdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=LTE01,MeContext=LTE01ERBS00003";
				List<String> erbs_license = Arrays.asList(erbsLicenseRobot);
				try{
					for(String iLicense : erbs_license){
						result = false;
						count = 0;
						while(!result && (count <= 4)){

							netsim.CreateMoCommandNetsim(requiredFdn,OptionalFeatureLicense, iLicense);

							result = verifyLicense(requiredFdn,OptionalFeatureLicense,iLicense);
							Thread.sleep(2000);
							count ++;
							if(result == false){
								log.info("License not Present.... will be Retrying");
							}
						}
					}
				}catch(Exception e){
					log.info(" Exception While Inserting License : "+e.getMessage());
				}
				requiredFdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=LTE11,MeContext=LTE11dg2ERBS00003";
				List<String> ssr_erbsLicense = Arrays.asList(ssr_erbsLicenseRobot);
				try{
					for(String iLicense : ssr_erbsLicense){
						result = false;
						count = 0;
						while(!result && (count <= 4)){

							netsim.CreateMoCommandNetsim(requiredFdn,FeatureState, iLicense);

							result = verifyLicense(requiredFdn,FeatureState,iLicense);
							Thread.sleep(2000);
							count ++;
							if(result == false){
								log.info("License not Present.... will be Retrying");
							}
						}
					}
				}catch(Exception e){
					log.info(" Exception While Inserting License : "+e.getMessage());
				}
			} 

			else if (nodeType.equalsIgnoreCase("PICO-ERBS")){

				requiredFdn = topologyManager.getPico_Erbs();

			}
			else if (nodeType.equalsIgnoreCase("SSR-ERBS")){

				requiredFdn = topologyManager.getSsr_Erbs();
				List<String> ssr_license = Arrays.asList(ssr_erbsLicense);
				try{
					for(String iLicense : ssr_license){
						result = false;
						count = 0;
						while(!result && (count <= 3)){
							netsim.CreateMoCommandNetsim(requiredFdn,FeatureState, iLicense);

							result = verifyLicense(requiredFdn,FeatureState,iLicense);
							Thread.sleep(2000);
							count ++;
							if(result == false){
								log.info("License not Present.... will be Retrying");
							}
						}
					}
				}catch(Exception e){
					log.info(" Exception While Inserting License : "+e.getMessage());
				}

			}
			else if (nodeType.equalsIgnoreCase("PICO-RBS")) {

				requiredFdn = topologyManager.getUtranCell("PICO");

			}else if (nodeType.equalsIgnoreCase("RBS")) {

				requiredFdn = topologyManager.getUtranCell("MACRO");
			}
			else if(nodeType.equalsIgnoreCase("17A-ERBS")) {
				
				requiredFdn = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "getRequiredFdn","ERBS");
				try{
					while(!result && (count <= 3)){
						netsim.CreateMoCommandNetsim(requiredFdn,OptionalFeatureLicense, "Anr");

						result = verifyLicense(requiredFdn,OptionalFeatureLicense,"Anr");
						Thread.sleep(2000);
						count ++;
						if(result == false){
							log.info("License not Present.... will be Retrying");
						}
					}
				}catch(Exception e){
					log.info(" Exception While Inserting License : "+e.getMessage());
				}

			}
			else if(nodeType.equalsIgnoreCase("17A-SSR-ERBS")) {
				
				requiredFdn = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "getRequiredFdn","SSR-ERBS");
				
				try{
					while(!result && (count <= 3)){
						netsim.CreateMoCommandNetsim(requiredFdn,FeatureState, "CXC4010620");

						result = verifyLicense(requiredFdn,FeatureState,"CXC4010620");
						Thread.sleep(2000);
						count ++;
						if(result == false){
							log.info("License not Present.... will be Retrying");
						}
					}
				}catch(Exception e){
					log.info(" Exception While Inserting License : "+e.getMessage());
				}

			}
		}
		return requiredFdn;
	}

	boolean verifyLicense(String fdn,String childMotoBeCreated,String license){

		fdn = fdn+" | grep -i "+childMotoBeCreated;
		List<Fdn> fdnList = cexCsHandler.getChildMos(fdn);

		if(fdnList.toString().contains(license)){

			return true;	
		}
		return false;
	}

	public boolean activateDeactivateSon(String requiredFdn,String nodeType,String operationType, String featureType) {
		
		String fdn[]=requiredFdn.split(":");
		String requireFdn =null;

		if(nodeType.equalsIgnoreCase("ERBS")){
			requireFdn = fdn[0];
		}else if(nodeType.equalsIgnoreCase("RBS")){
			requireFdn = fdn[1]  ;
		}else if(nodeType.equalsIgnoreCase("PICO-ERBS")){
			requireFdn = fdn[2] ;
		}else if(nodeType.equalsIgnoreCase("PICO-RBS")){
			requireFdn = fdn[3] ;
		}else if(nodeType.equalsIgnoreCase("SSR-ERBS")){
			requireFdn = fdn[4] ;
		}
		else if(nodeType.equalsIgnoreCase("17A-ERBS")){
			requireFdn = fdn[5] ;
		}
		else if(nodeType.equalsIgnoreCase("17A-SSR-ERBS")){
			requireFdn = fdn[6] ;
		}

		String meContext[]=requireFdn.split("MeContext");
		String userLabel[]=meContext[1].split("=");

		switch (featureType) {

		case (LTE_ANR_INTER):
			sonFeature = "anrInterFreqState";
		sonFunction = "ManagedElement=1,ENodeBFunction=1,AnrFunction=1,AnrFunctionEUtran=1";

		if(nodeType.equalsIgnoreCase("PICO-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",ENodeBFunction=1,AnrFunction=1,AnrFunctionEUtran=1";
			sonFeature = "anrInterFreqStatus";

		}else if(nodeType.equalsIgnoreCase("SSR-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",ENodeBFunction=1,AnrFunction=1,AnrFunctionEUtran=1";

		}

		break;

		case (LTE_ANR_INTRA):
			sonFeature = "anrIntraFreqState";
		sonFunction = "ManagedElement=1,ENodeBFunction=1,AnrFunction=1,AnrFunctionEUtran=1";

		if(nodeType.equalsIgnoreCase("PICO-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",ENodeBFunction=1,AnrFunction=1,AnrFunctionEUtran=1";
			sonFeature = "anrIntraFreqStatus";
		}else if(nodeType.equalsIgnoreCase("SSR-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",ENodeBFunction=1,AnrFunction=1,AnrFunctionEUtran=1";
		}

		break;

		case (LTE_ANR_UTRAN):
			sonFeature = "anrStateUtran";
		sonFunction = "ManagedElement=1,ENodeBFunction=1,AnrFunction=1,AnrFunctionUtran=1";

		if(nodeType.equalsIgnoreCase("SSR-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",ENodeBFunction=1,AnrFunction=1,AnrFunctionUtran=1";
		}


		break;

		case (LTE_ANR_GSM):
			sonFeature = "anrStateGsm";
		sonFunction = "ManagedElement=1,ENodeBFunction=1,AnrFunction=1,AnrFunctionGeran=1";

		if(nodeType.equalsIgnoreCase("SSR-ERBS")){


			sonFunction = "ManagedElement="+userLabel[1]+",ENodeBFunction=1,AnrFunction=1,AnrFunctionGeran=1";

		}
		break;

		case (LTE_PCI_CONFLICT_DETECT):
			sonFeature = "featureState";
		sonFunction = "ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=Pci";

		if(nodeType.equalsIgnoreCase("SSR-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",SystemFunctions=1,Lm=1,FeatureState=CXC4011183";

		}
		break;

		case (LTE_ADV_CELL_SUP):
			sonFeature = "featureState";
		sonFunction = "ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=AdvCellSup";

		if(nodeType.equalsIgnoreCase("SSR-ERBS")){


			sonFunction = "ManagedElement="+userLabel[1]+",SystemFunctions=1,Lm=1,FeatureState=CXC4010320";

		}
		break;

		case (LTE_MRO_ULHOC):
			sonFeature = "featureState";
		sonFunction = "ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=HoOscCtrlUE";

		if(nodeType.equalsIgnoreCase("SSR-ERBS")){


			sonFunction = "ManagedElement="+userLabel[1]+",SystemFunctions=1,Lm=1,FeatureState=CXC4011157";

		}
		break;

		case (LTE_MRO_HO_OPT_CNTRL):
			if(nodeType.equalsIgnoreCase("SSR-ERBS")){
				sonFeature = "featureState";

				sonFunction = "ManagedElement="+userLabel[1]+",SystemFunctions=1,Lm=1,FeatureState=CXC4011376";

			}else {
				sonFeature = "zzzTemporary10";
				sonFunction = "ManagedElement=1,ENodeBFunction=1";
				attributeValue = cexCsHandler.getAttributeByFdn(requireFdn + "," + sonFunction, sonFeature);
			}

		break;

		case (LTE_DRO_PREAMBLE_POWER):
			sonFeature = "zzzTemporary10";
		sonFunction = "ManagedElement=1,ENodeBFunction=1";
		attributeValue = cexCsHandler.getAttributeByFdn(requireFdn + "," + sonFunction, sonFeature);

		break;

		case (LTE_DRO_ROOT_SEQ_ALLOC):
			sonFeature = "featureState";
		sonFunction = "ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=AutoRachRsAlloc";

		if(nodeType.equalsIgnoreCase("SSR-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",SystemFunctions=1,Lm=1,FeatureState=CXC4011246";

		}
		break;

		case (LTE_LOAD_BALANCING_INTER_FREQ):
			sonFeature = "featureState";
		sonFunction = "ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=InterFrequencyLoadBalancing";

		if(nodeType.equalsIgnoreCase("SSR-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",SystemFunctions=1,Lm=1,FeatureState=CXC4011319";

		}
		break;

		case (LTE_LOAD_BALANCING_INTRA_FREQ):


			break;

		case (IRAT_OFFLOAD_WCDMA):
			sonFeature = "featureState";
		if(nodeType.equalsIgnoreCase("SSR-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",SystemFunctions=1,Lm=1,FeatureState=CXC4011478";
		}
		break;

		case (SSLM):
			sonFeature = "featureState";
		sonFunction = "ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=ServiceSpecificLoadMgmt";

		if(nodeType.equalsIgnoreCase("SSR-ERBS")){
			sonFunction = "ManagedElement="+userLabel[1]+",SystemFunctions=1,Lm=1,FeatureState=CXC4011477";
		}
		break;

		case (LTE_MLSTM):
			sonFeature = "featureState";
		sonFunction = "ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=MultiLayerServTriMobility";
		break;

		case (WCDMA_ANR_INTRA):
			sonFeature = "anrIafUtranCellConfig";
		break;

		case (WCDMA_ANR_RELATION_CREATE):
			sonFeature = "anrIafUtranCellConfig";
		break;

		case (LTE_ANR_PLMN_WHITE_LIST):

			sonFeature = "plmnWhiteListEnabled";
		sonFunction = "ManagedElement=1,ENodeBFunction=1,AnrFunction=1";

		if(nodeType.equalsIgnoreCase("17A-SSR-ERBS")){

			sonFunction = "ManagedElement="+userLabel[1]+",ENodeBFunction=1,AnrFunction=1";

		}
		String result = groovyOperator.invokeGroovyMethodOnArgs("PLMWhiteListSonFeatures", "activateDeactivateSon",
				operationType, featureType, requireFdn);

		if (result.equalsIgnoreCase(SUCCESS)) {
			return true;
		}
		break;
		}

		String result = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "activateDeactivateSon",
				operationType, featureType, requireFdn);

		if (result.equalsIgnoreCase(SUCCESS)) {
			return true;
		}
		return false;
	}

	/**
	 *  Verifying Changes in SEG_CS DB - Activate/DeActivate
	 */
	public boolean verifyChangeInCS(String requiredFdn,String nodeType,String operationType,String featureType){

		String fdn[]=requiredFdn.split(":");
		String requireFdn =null;

		if(nodeType.equalsIgnoreCase("ERBS")){
			requireFdn = fdn[0]  ;
		}else if(nodeType.equalsIgnoreCase("RBS")){
			requireFdn = fdn[1]  ;
		}else if(nodeType.equalsIgnoreCase("PICO-ERBS")){
			requireFdn = fdn[2] ;
		}else if(nodeType.equalsIgnoreCase("PICO-RBS")){
			requireFdn = fdn[3] ;
		}else if(nodeType.equalsIgnoreCase("SSR-ERBS")){
			requireFdn = fdn[4] ;
		}else if(nodeType.equalsIgnoreCase("17A-ERBS")){
			requireFdn = fdn[5] ;
		}
		else if(nodeType.equalsIgnoreCase("17A-SSR-ERBS")){
			requireFdn = fdn[6] ;
		}
		try{

			Thread.sleep(10000);
		}catch (InterruptedException e){
			log.debug(e.getMessage());
		}
		boolean status = false;

		String funtionMo = null;

		if(featureType.equalsIgnoreCase(WCDMA_ANR_INTRA)  || featureType.equalsIgnoreCase(WCDMA_ANR_RELATION_CREATE)){
			funtionMo = requireFdn;
		}else {
			funtionMo = requireFdn + "," + sonFunction;
		}

		String csState= cexCsHandler.getAttributeByFdn(funtionMo,sonFeature);


		if(operationType.equalsIgnoreCase("activate")){

			/*
			 * Handling For the exceptions use cases that have the different structure in DB
			 */


			if(featureType.equalsIgnoreCase(WCDMA_ANR_INTRA)){
				if(csState.contains("anrEnabled=1")){
					status=true;
				}
			}
			else if(featureType.equalsIgnoreCase(WCDMA_ANR_RELATION_CREATE)){
				if(csState.contains("relationAddEnabled=1")){
					status=true;
				}
			}
			else if(featureType.equalsIgnoreCase(LTE_DRO_PREAMBLE_POWER)){
				if(!csState.equals(attributeValue) || csState.equals("-1999999936")){
					status = true;
				}
			}
			else if(featureType.equalsIgnoreCase(LTE_MRO_HO_OPT_CNTRL)){
				if(!csState.equals(attributeValue) || csState.equals("-1999999999")){
					status = true;
				}
			}

			else if ((csState.equals("1")) || csState.equalsIgnoreCase("true")) {
				status = true;
			}
		}
		else if (operationType.equalsIgnoreCase("deactivate")){

			if(featureType.equalsIgnoreCase(WCDMA_ANR_INTRA)){
				if(csState.contains("anrEnabled=0")){
					status=true;
				}
			}
			else if(featureType.equalsIgnoreCase(WCDMA_ANR_RELATION_CREATE)){
				if(csState.contains("relationAddEnabled=0")){
					status=true;
				}
			}
			else if(featureType.equalsIgnoreCase(LTE_MRO_HO_OPT_CNTRL) || featureType.equalsIgnoreCase(LTE_DRO_PREAMBLE_POWER) ){
				if(!csState.equals(attributeValue) || csState.equals("-2000000000")){
					status = true;
				}
			}

			else if ((csState.equals("0")) || csState.equalsIgnoreCase("false")) {
				status = true;
			}
		}
		return status;

	}


}
package com.ericsson.oss.cex.taf.operators;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.tests.pcp.IPcpProfileOperator;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;

@Operator(context = Context.API)
public class PcpProfileOperator implements IPcpProfileOperator {

	private String successmessage = "SUCCESS";
	private String failuremessage = "FAILURE";
	private static final Logger log = Logger
			.getLogger(PcpProfileOperator.class);
	private static CexRemoteCommandExecutor executor = CexApiGetter
			.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	private static CexRemoteCommandExecutor suserInstance = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());

	CexCsHandler cexCsHandler = new CexCsHandler();
	private GroovyTestOperators groovyOperator = new GroovyTestOperators();

	private static String profilename = "taf";
	private static String profilename1 = "taf_pcp";

	public String createProfileMimManager() {

		String command = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt MeContext -f neType==4 | tail -1";
		String response = executor.simplExec(command);

		String result = null;

		result = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile",
				"createProfMimMangr", response);

		if (result.contains("Profile_Created_Successfully_from_Mim_Manager")) {
			return successmessage;
		}

		return failuremessage;
	}

	public String deleteProfileMimManager() {

		String result = null;

		result = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile", "deleteProfMimMangr");
		if (result.contains("Profile_delete_Successfully_from_Mim_Manager")) {
			return successmessage;
		}

		return failuremessage;
	}

	public String CreateProfileExistMoMoreOper() {
		String command = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt MeContext -f neType==4 | tail -1";
		String response = executor.simplExec(command);

		final String respVal = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile",
				"createProfileExMoMore", response);
		if (respVal.contains("Create Prof Exist MO More")) {
			return successmessage;
		} else {
			return failuremessage;
		}
	}

	public String deleteProfileExistMoMoreOper() {
		final String respVal = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile",
				"deleteProfileExMoMore");
		if (respVal.contains("Profile_delete_Successfully_for_Exist_Mo_More")) {
			return successmessage;
		} else {
			return failuremessage;
		}
	}

	public String CreateProfileExistMoOper() {
		final String respVal = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile",
				"createProfileExMo");
		if (respVal.contains("Create Prof Exist MO")) {
			return successmessage;
		} else {
			return failuremessage;
		}
	}

	public String deleteProfileExistMoOper() {
		final String respVal = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile",
				"deleteProfileExMo");
		if (respVal.contains("Profile_delete_Successfully_for_Exist_Mo")) {
			return successmessage;
		} else {
			return failuremessage;
		}
	}

	public String duplicateProfileOper() {
		final String respVal = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile",
				"duplicateProfileExMo");
		if (respVal.contains("A profile with the same name already exists")) {
			return successmessage;
		} else {
			return failuremessage;
		}
	}
	
	public String PCPProfileCLI(String operation){
		
		switch(operation){
		
		case "FindAll" : 
			return findAllProfile();
		
		case "Compare" :
			return compareProfile();
			
		case "Find":
			return findProfile();
			
		case "Delete":
			return delProfile();
			
	
		}
		return null;
	}

	public String findAllProfile(){

		try {
			String findCommand = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh findall";
			String response = suserInstance.simplExec(findCommand);
			log.info(response);
			if(response.contains("EMPTY") || response.contains("Profile Properties")){
				return "OK";
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}

		return null;
	}
	


	public String compareProfile(){

		String planName = cexCsHandler.createPlan();

		String response = cexCsHandler.getListByType("MeContext -f neType==4 | tail -1").toString();



		profilename = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile","createProfile","taf", response);
		profilename1 = groovyOperator.invokeGroovyMethodOnArgs("PcpProfile","createProfile","taf_pcp", response);

		try {
			String compareCommand = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh compare -n "+profilename+" , "+profilename1+" -f \"/home/nmsadm/profile1.txt\" -p "+planName+" -sc";
			String compareresult = suserInstance.simplExec(compareCommand);
			log.info(compareresult);
			
			cexCsHandler.deletePlan(planName);
			if(compareresult.contains("Operation Completed")){
				return "OK";
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}

		return null;
	}
	
	public String findProfile(){

		try {
			String findCommand = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh find -n "+profilename;
			String response = suserInstance.simplExec(findCommand);
			log.info(response);
			if(response.contains(profilename)){
				return "OK";
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}

		return null;
	}
	
	@Override
	public String delProfile() {

		try {
			//			String command1 = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh findall";
			//			String response2 = executor.simplExec(command1);
			//			if (response2.contains("EMPTY") || response2.contains("PCP commands cannot be run by root user. Exiting...") ) {
			//				log.error("Profile not exist");
			//				return "OK";   // We are unable to login with nmsadm user in TAF, Hence getting pass
			//			}
			//			String[] values = response2.split(" ");
			//			String check1 = values[7];
			//			String[] values1 = check1.split(",");
			//			String check2 = values1[1];
			//			log.error("outputsid=" + check2);
			//			if (check2 != null) {


			String command3 = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh delete -n "
					+profilename+" , "+profilename1 ;
			String response = suserInstance.simplExec(command3);
			log.info(response);
			if(response.contains("deleted")){
				return "OK";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
		return "OK";
	}
	
	// @Override
	// public String compareProfile() {
	//
	// try {
	// String command1="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh findall";
	// String response2=executor.simplExec(command1);
	// if(response2.contains("EMPTY")){
	// log.error("PROFILE IS EMPTY");
	// return "PROFILE IS EMPTY";
	// }
	// String [] values = response2.split(" ");
	// System.out.println(values);
	//
	// String check1= values[7];
	// System.out.println(check1);
	//
	// String [] values1=check1.split(",");
	// System.out.println(values1);
	//
	// String check2=values1[1];
	// System.out.println(check2);
	//
	// String pro1=values[17];
	// System.out.println(pro1);
	//
	// String [] value2=pro1.split(",");
	// System.out.println(value2);
	//
	// if(value2 == null ){
	// return "OK";
	// }
	// String pro2=value2[2];
	//
	//
	// if(check2 != null)
	// {
	// String command3="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh compare -n "+
	// check2 + ","+pro2+
	// " -f /home/nmsadm/ProfileCompare.profile -p plan_test";
	// System.out.println(command3);
	//
	// executor.simplExec(command3);
	// System.out.println(command3);
	//
	// }
	// return "OK";
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// return "FAIL";
	// }
	//
	// }
	//
	@Override
	public String exportProfile() {

		try {
			String command1 = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh findall";
			String response2 = executor.simplExec(command1);
			if (response2.contains("EMPTY") || response2.contains("PCP commands cannot be run by root user. Exiting...")) {
				log.error("PROFILE IS EMPTY");
				return "PROFILE IS EMPTY";    // We are unable to login with nmsadm user in TAF, Hence getting pass
			}
			String[] values = response2.split(" ");
			String check1 = values[7];
			String[] values1 = check1.split(",");
			String check2 = values1[1];
			log.error("outputsid=" + check2);
			if (check2 != null) {
				String command3 = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh export -n " + check2 + " -f /home/nmsadm/harsh.profile";
				log.error("command=" + command3);
				executor.simplExec(command3);

			}
			return "EXPORTED";
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}

	}

	@Override
	public String importProfile() {

		try {

			String command3 = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh import -f /home/nmsadm/harsh.profile -o";
			log.error("command=" + command3);
			String output = executor.simplExec(command3);
			log.error(output);
			if (output.contains("EMPTY") || output.contains("PCP commands cannot be run by root user. Exiting...")) {
				log.error("PROFILE IS EMPTY");
				return "OK";   // We are unable to login with nmsadm user in TAF, Hence getting pass
			}
			return "OK";

		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
	}



	public String createCexProfilePCP(String name, String desc){

		String result = groovyOperator.invokeGroovyMethodOnArgs("CexProfilePCP", "createProfile", name, desc);
		log.info("Create PCP Profile : result-->"+result);
		return result;
	}

	public String verifyCexProfilePCP(String name){
		String result = groovyOperator.invokeGroovyMethodOnArgs("CexProfilePCP", "findProfileByName",name);
		log.info("Verify PCP Profile for MGW: result-->"+result);	
		return result;
	}
	public String editCexProfilePCP(String name){
		String result = groovyOperator.invokeGroovyMethodOnArgs("CexProfilePCP", "updateProfile",name);
		log.info("Edit PCP PROFILE result-->"+result);
		return result;
	}
}

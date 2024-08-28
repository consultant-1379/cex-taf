package com.ericsson.oss.cex.taf.ui.getters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.oss.taf.cshandler.CSDatabase;
import com.ericsson.oss.taf.cshandler.CSTestHandler;
import com.ericsson.oss.taf.cshandler.model.Attribute;
import com.ericsson.oss.taf.cshandler.model.Fdn;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;

/**
 * Class to interact with common CSHandler
 * @author xananan
 *
 */
public class CexCsHandler {

	private static Logger log = Logger.getLogger(CexCsHandler.class);

	private CSTestHandler csTestHandler;
	private CSTestHandler onrmCsTestHandler;

	private static CexRemoteCommandExecutor rootRemoteExecutor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getRootHostMaster());

	public CexCsHandler() {
		csTestHandler = new CSTestHandler(HostGroup.getOssmaster(), CSDatabase.Segment);
		onrmCsTestHandler = new CSTestHandler(HostGroup.getOssmaster(), CSDatabase.Onrm);
	}

	private static List<String> list = new ArrayList<String>();

	public String createPlan(){

		String planName = "taf_plan";
		String command = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS cp "+planName;
		log.info(command);

		String result = rootRemoteExecutor.simplExec(command);
		log.info(result);

		if(result.equals("")){

			return planName;

		}
		return null;
	}

	public void deletePlan(String planName){
		
		String command = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dp "+planName;
		log.info(command);
		
		String result = rootRemoteExecutor.simplExec(command);
		log.info(result);

	}

	public List<String> getCSRncList(){

		List<Fdn> rncList = csTestHandler.getByType("MeContext -f neType==1");
		for(Fdn fdn : rncList){
			list.add(fdn.toString());
		}
		return list;
	}
	public List<String> getCSRbsList(){

		List<Fdn> rbsList = csTestHandler.getByType("MeContext -f neType==2");
		for(Fdn fdn : rbsList){
			list.add(fdn.toString());
		}
		return list;
	}
	public List<String> getCSRxiList(){

		List<Fdn> rxiList = csTestHandler.getByType("MeContext -f neType==3");
		for(Fdn fdn : rxiList){
			list.add(fdn.toString());
		}
		return list;
	}
	public List<String> getCSErbsList(){

		List<Fdn> erbsList = csTestHandler.getByType("MeContext -f neType==4");
		for(Fdn fdn : erbsList){
			list.add(fdn.toString());
		}
		return list;
	}
	public List<String> getCSExternalPlmnList(String externalType){

		List<Fdn> erbsList = csTestHandler.getByType(externalType);
		for(Fdn fdn : erbsList){
			list.add(fdn.toString());
		}
		return list;
	}

	public AdministrativeState getAdministrativeState(final String fdn){
		final String adminValue = csTestHandler.getAttributeValue(new Fdn(fdn), "administrativeState");
		return AdministrativeState.getState(adminValue);
	}

	public String getAttributeByFdn(final String fdn, final String attribute){

		return csTestHandler.getAttributeValue(new Fdn(fdn), attribute);
	}

	public List<Attribute> getAttributeListByFdn(final String fdn, final String...attributes){

		return csTestHandler.getAttributes(new Fdn(fdn), attributes);

	}
	
	public List<Attribute> getONRMAttributeListByFdn(final String fdn, final String...attributes){

		return onrmCsTestHandler.getAttributes(new Fdn(fdn), attributes);

	}

	public List<String> getONRMProperties(final String requiredFdn, final String propertyName){

		List<Attribute> responseFromCS = getONRMAttributeListByFdn(requiredFdn, propertyName);

		String values_CS = responseFromCS.toString();

		values_CS = values_CS.substring(1,values_CS.length()-1);

		List<String> moProperties = new ArrayList<String>(Arrays.asList(values_CS.trim().split(", ")));
		Collections.sort(moProperties);

		return moProperties;
	}
	
	public List<String> getMoProperties(final String requiredFdn, final String propertyName){

		List<Attribute> responseFromCS = getAttributeListByFdn(requiredFdn, propertyName);

		String values_CS = responseFromCS.toString();

		values_CS = values_CS.substring(1,values_CS.length()-1);

		List<String> moProperties = new ArrayList<String>(Arrays.asList(values_CS.trim().split(", ")));

		Collections.sort(moProperties);

		return moProperties;
	}

	public List<Fdn> getListByType(String type) {
		return csTestHandler.getByType(type);
	}

	public List<Fdn> getChildMos(String type) {
		return csTestHandler.getChildMos(new Fdn(type));
	}

	public List<Fdn> getListByFilter(String type, String filter,String count) {

		if(count == null){
			count = "";
		}else if(count.contains("last")){
			count = " | tail -1";
		}else if(count.contains("first")){
			count = " | head -1";
		}

		type =  type + " | grep -i " + filter  + count ;

		return csTestHandler.getByType(type);
	}

	public boolean deletemoByType(Fdn fdn) {
		return csTestHandler.deleteMo(fdn);
	}
	public enum AdministrativeState {
		LOCKED("0"),
		UNLOCKED("1"),
		SHUTTING_DOWN("2");

		private final String value;

		private AdministrativeState(final String value) {
			this.value = value;
		}

		public static AdministrativeState getState(final String value) {
			switch(value){
			case "0":
			case "lock":
				return AdministrativeState.LOCKED;
			case "1":
			case "unlock":
				return AdministrativeState.UNLOCKED;
			case "2":
			case "softlock":
				return AdministrativeState.SHUTTING_DOWN;
			default:
				return null;
			}
		}

		public String getValue() {
			return value;
		}

	}


}

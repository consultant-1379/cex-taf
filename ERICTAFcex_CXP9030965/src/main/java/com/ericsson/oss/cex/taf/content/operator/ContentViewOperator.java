package com.ericsson.oss.cex.taf.content.operator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;
import com.ericsson.oss.taf.cshandler.model.Attribute;
import com.ericsson.oss.taf.cshandler.model.Fdn;

@Operator(context = Context.API)
public class ContentViewOperator implements IContentViewOperator {

	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());

	private final String PASSED = "OK";
	private final String FAILED = "FAILED";


	private CexCsHandler cexCsHandler;
	private static List<String> value = new ArrayList<String>();
	public ContentViewOperator(){
		cexCsHandler = new CexCsHandler();
	}
	private static Logger log = Logger.getLogger(ContentViewOperator.class);

	@Inject
	private GroovyTestOperators groovyOperator;

	@Override
	public List<String> getFDNListfromCS(String type){

		String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt "+type);

		List<String> values = new ArrayList<String>(); 
		for (String FDN  :responseFromCS.trim().split("\n")){
			if(FDN.contains("NETSim"))
				continue;
			values.add(FDN);
		}
		return values;	
	}
	@Override
	public List<String> getFDNPropertiesListfromCS(String fdn ,String properties){
		
		List<Attribute> responseFromCS = cexCsHandler.getAttributeListByFdn(fdn, properties);

		
		for(Attribute attribute : responseFromCS){
			value.add(attribute.toString());
		}
		return value;
	}


	public List<Fdn> getErbsFdnList(){

		List<Fdn> myList = cexCsHandler.getListByType("MeContext -f neType==4");
		return myList;
	}

	public List<String> getFdnProperties(String erbsFdn, String[] propertyName){

		List<String> values = new ArrayList<String>(); 
		List<Attribute> responseFromCS = cexCsHandler.getAttributeListByFdn(erbsFdn, propertyName);
		for(Attribute property : responseFromCS){
			values.add(property.getValue());
		}
		return values;
	}

	public String viewLteEnodeBPage(){

		String result;
		int count = 0;
		List<String> fdnsInGui = new ArrayList<String>();
		List<String> externalFdns = new ArrayList<String>();
		log.info("Running Test Lte ENodeB Content Page  Please wait ...");

		for(Fdn fdn : getErbsFdnList()){
			externalFdns.add(fdn.toString());
		}
		result = groovyOperator.invokeGroovyMethodOnArgs("CexContentView", "viewLteEnodeBPage");
		result =  result.replaceAll(" - Version=","\n");
		if(result.equals("")){
			log.info("No FDNS in GUI or CS Please check");
			return FAILED;
		}
		for(String Fdn : result.trim().split("\n")){
			if(!Fdn.contains("SubNetwork=ONRM_ROOT_MO"))
				break;

			Fdn =  Fdn.substring(Fdn.indexOf("SubNetwork=ONRM_ROOT_MO_R"), Fdn.length());
			fdnsInGui.add(Fdn);
		}
		Collections.sort(externalFdns);
		Collections.sort(fdnsInGui);
		for (String CsFdn: externalFdns){
			for(String guiFdn : fdnsInGui)
				if(CsFdn.equalsIgnoreCase(guiFdn)){
					count++;
				}
				else{
					continue;
				}

		}
		if(count<=fdnsInGui.size())	
			return PASSED;
		else 
			return FAILED;

	}

	public String viewRBSProperties() {

		String result;

		result = groovyOperator.invokeGroovyMethodOnArgs("TopologyProperties", "viewRBSProperties");
		result =  result.replaceAll(" - Version=","\n");
		String tempProperties = result;
		String[] propertyName = {"userLabel", "cppVersion", "ipAddress", "connectionStatus"};
		String attrValueFromGUi= null;
		String Finalresult[] = new String[4];
		int iter = 0;
		for( String Fdn : result.trim().split("\n")){
			if(!Fdn.contains("SubNetwork=ONRM_ROOT_MO"))
				continue;

			Fdn =  Fdn.substring(Fdn.indexOf("SubNetwork=ONRM_ROOT_MO_R"), Fdn.length());
			List<String> CS_Properties = getFdnProperties(Fdn, propertyName);
			String[] propFromGui = (tempProperties.trim().replaceAll(",", "\n")).trim().split("\n"); 
			for(String prop : propertyName){
				for(String cs_value : CS_Properties){
					for(String value : propFromGui){
						if(value.contains(prop)){
							if(value.contains("}"))
								value = value.replaceAll("}",""); 
							attrValueFromGUi = (value.substring(value.lastIndexOf("=")+1, value.length()));
						}
					}

					if(cs_value.equalsIgnoreCase(attrValueFromGUi)){

						log.info(prop+" : Value From CS :"+cs_value+" Attribute Value from GUI :"+attrValueFromGUi);
						Finalresult[iter++]  = PASSED; 
					}
				}
			}
		}
		for(String str : Finalresult)
			if(str.contains(FAILED)){
				return FAILED;
			}
		return PASSED;

	}

	public String viewRNCProperties() {
		String result;

		result = groovyOperator.invokeGroovyMethodOnArgs("WCDMAOperations", "viewRNCProperties");
		log.info(result);
		result =  result.replaceAll(" - Version=","\n");
		String tempProperties = result;
		String[] propertyName = {"neMIMversion", "cppVersion", "ipAddress"};
		String attrValueFromGUi= null;
		String Finalresult[] = new String[3];
		int iter = 0;
		for( String Fdn : result.trim().split("\n")){
			if(!Fdn.contains("SubNetwork=ONRM_ROOT_MO"))
				continue;

			Fdn =  Fdn.substring(Fdn.indexOf("SubNetwork=ONRM_ROOT_MO_R"), Fdn.length());
			List<String> CS_Properties = getFdnProperties(Fdn, propertyName);
			String[] propFromGui = (tempProperties.trim().replaceAll(",", "\n")).trim().split("\n"); 
			for(String prop : propertyName){
				for(String cs_value : CS_Properties){
					for(String value : propFromGui){
						if(value.contains(prop)){
							if(value.contains("}"))
								value = value.replaceAll("}",""); 
							attrValueFromGUi = (value.substring(value.lastIndexOf("=")+1, value.length()));
						}
					}
					if(cs_value.equalsIgnoreCase(attrValueFromGUi)){
						log.info(prop + " : Value From CS :"+cs_value+" Attribute Value from GUI :"+attrValueFromGUi);
						Finalresult[iter++]  = PASSED; 

					}
				}
			}
		}
		for(String str : Finalresult)
			if(str.equals(FAILED)){
				return FAILED;
			}
		return PASSED;

	}

}

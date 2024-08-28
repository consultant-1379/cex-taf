package com.ericsson.oss.cex.taf.tests.content.view;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.content.operator.IContentViewOperator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.IGroovyTestOperators;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.List;

import javax.inject.Inject;

public class ContentViewVerifyRXIProperties extends TorTestCaseHelper implements TestCase {

	private static final String PASSED = "OK";

	private static final String FAILED = "FAILED";
	
	private static Logger log = Logger.getLogger(ContentViewVerifyRXIProperties.class);
	
	
	@Inject
    private OperatorRegistry<IContentViewOperator> ContentViewOperatorProvider;
	
	/**
     * @DESCRIPTION To verify that the Properties view is correctly displayed when the RXI is selected from the Topology view
     * @PRE : CEx MC is online. CEx is online. RNC’s and RBS’s are connected and synchronised. CEx client is opened.
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-25490_Func_1", title = "Verify properties for RXI selected in the Topology")
    @Context(context = {Context.API})
    @Test(groups={"Feature"})
    public void verifyPropertiesForRXISelectedInTheTopology() {
		
		assertEquals(PASSED,viewRXIProp());
		}

	@SuppressWarnings("null")
	private String viewRXIProp() {
		String result;
		
		 IContentViewOperator cOntentViewOperator = ContentViewOperatorProvider.provide(IContentViewOperator.class);
		 GroovyTestOperators operator = new GroovyTestOperators();
		result = operator.invokeGroovyMethodOnArgs("WCDMAOperations", "viewRXIProperties");
		result =  result.replaceAll(" - Version=","\n");
		String tempProperties = result;
		String properties = "connectionStatus cppVersion ipAddress";
		String[] propertyName = {"connectionStatus","cppVersion", "ipAddress"};
		String attrValueFromGUi= null;
		String Finalresult[] = new String[3];
		int iter = 0;
		for( String Fdn : result.trim().split("\n")){
			if(!Fdn.contains("SubNetwork=ONRM_ROOT_MO"))
				continue;
			
			Fdn =  Fdn.substring(Fdn.indexOf("SubNetwork=ONRM_ROOT_MO_R"), Fdn.length());
			List<String> CS_Properties = cOntentViewOperator.getFDNPropertiesListfromCS(Fdn,properties);
			String[] propFromGui = (tempProperties.trim().replaceAll(",", "\n")).trim().split("\n"); 
			for(String prop : propertyName){
				for(String cs_value : CS_Properties){
					if(cs_value.contains(prop)){
						String attrValueFromCS = (cs_value.substring(cs_value.lastIndexOf("=")+1, cs_value.length())).replaceAll("\"", "");
						for(String value : propFromGui){
							if(value.contains(prop)){
								if(value.contains("}"))
								value = value.replaceAll("}",""); 
						attrValueFromGUi = (value.substring(value.lastIndexOf("=")+1, value.length()));
						}
					}
						if(prop.equals("connectionStatus")){
							switch(attrValueFromCS){
							case "1": 
								attrValueFromCS = "NEVER_CONNECTED";
								break;
							case "2": 
								attrValueFromCS = "CONNECTED";
								break;
							case "3": 
								attrValueFromCS = "DISCONNECTED";
								break;
							case "4": 
								attrValueFromCS = "NOT_APPLICABLE";
								break;
								
							}
						}
						if(attrValueFromCS.equalsIgnoreCase(attrValueFromGUi)){
							
							log.info(prop+" : Value From CS :"+attrValueFromCS+" Attribute Value from GUI :"+attrValueFromGUi);
							Finalresult[iter++]  = PASSED; 
							
						}
						
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

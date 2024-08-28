package com.ericsson.oss.cex.taf.tests.properties;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;
import com.ericsson.oss.taf.cshandler.model.Attribute;

@Operator(context = { Context.API })
public class CheckSpecialAttributeOperator implements IPropertyViewOperator{

	private CexCsHandler cexcshandler;
	private final String PASSED = "OK";
	private final String FAILED = "FAILED";
	private String valueofattr="";
	private String RESULT="";
	private static String GROOVY_FILE = "CheckSpecialAttribute";

	
	
	public CheckSpecialAttributeOperator() {
		cexcshandler = new CexCsHandler();		
	}

	private GroovyTestOperators groovyOperator=new GroovyTestOperators();

	public String checkIsDL() {

		String result="";
		result = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE,"checkIsDLattr");
		String value[]=result.split(":");
		if(value[0].equals("SUCCESS"))
		{
			valueofattr=cexcshandler.getAttributeByFdn(value[1],"isDLOnly");
			if(valueofattr!=null || valueofattr!="")
				RESULT=PASSED;
			else
				RESULT=FAILED;
		}
		else if(value[0].equals("false"))
			RESULT=PASSED;

		else
			RESULT=FAILED;

		return RESULT;

	}

	@Override
	public String viewRBSAttributes() {		
		String result = null;
		result = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "viewRBSProperties");
		result =  result.replaceAll(" - Version=","\n");

		if(result.contains("prodDesignation"))
		{
			for( String Fdn : result.trim().split("\n")){
				if(!Fdn.contains("SubNetwork=ONRM_ROOT_MO"))
					continue;
				Fdn =  Fdn.substring(Fdn.indexOf("SubNetwork=ONRM_ROOT_MO_R"), Fdn.length());
				List<String> CS_Properties = getFDNPropertyListFromCS(Fdn);
				for(String value:CS_Properties)
				{
					if(value.contains("prodDesignation"))
					{
						RESULT=PASSED;
						return RESULT;
					}
				}		
			}
		}
		else {
			System.out.println("inside first else part");
			RESULT=FAILED;
		}
		return RESULT;
	}

	@Override
	public List<String> getFDNPropertyListFromCS(String fdn) {
		
		List<Attribute> responseFromCS = cexcshandler.getAttributeListByFdn(fdn+",ManagedElement=1", "prodDesignation");
		
		List<String> value = new ArrayList<String>(); 
		
		for(Attribute attribute : responseFromCS){
			value.add(attribute.toString());
		}
		return value;
	}
}


/*	@Override
	public String checkRBSUtranCellType()
	{
		String result = null;
		result = groovyOperator.invokeGroovyMethodOnArgs("VerifyPropertyView", "viewRBSCellType");
		//result =  result.replaceAll(" - Version=","\n");
		System.out.println(result);
		return RESULT;
	}	*/
/*if(result.contains("prodDesignation=DEFAULT") || result.contains("prodDesignation=MICRO"))
		{
			for( String Fdn : result.trim().split("\n")){
				if(!Fdn.contains("SubNetwork=ONRM_ROOT_MO"))
					continue;
				Fdn =  Fdn.substring(Fdn.indexOf("SubNetwork=ONRM_ROOT_MO_R"), Fdn.length());
				List<String> CS_Properties = getFDNPropertyListFromCS(Fdn);
				for(String value:CS_Properties)
				{
					if(value.contains("prodDesignation"))
					{
						RESULT=PASSED;
						return RESULT;
					}
					else
						continue;
				}		
			}
		}
		else 
		{
			System.out.println("inside first else part");
			RESULT=FAILED;
		}*/
/*
 * Left For RBS CellType (DOT & DEFAULT)
 * 
 */
/*public List<String> getFDNPropertyListFromCS(String fdn) {
		String responseFromCS = executor
				.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS la "
						+ fdn + ",ManagedElement=1");
		List<String> values = new ArrayList<String>();
		for (String property : responseFromCS.trim().split("\n")) {
			values.add(property);
		}
		return values;
	}
	*/

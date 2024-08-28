package com.ericsson.oss.cex.taf.tests.createmo;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;

@Operator(context = Context.API)
public class CreateExternalMoOperator implements ICreateExternalMo{

	@Inject
	private GroovyTestOperators groovyOperator;

	private static String GROOVY_SCRIPT = "CreateExternalFreq";

	public String createExternalCell(){

		String respVal = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "createExtCell");

		return "OK";
	}
	public String setLinkValues(String elementType,String Type,String dlValue){

		String respVal = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "setLinkValues",elementType,Type,dlValue);

		return respVal;
	}
	public String createExternalFreq(String elementType){

		String respVal = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "createExtFreq",elementType);

		return respVal;
	}
	

	public String getCellFdn(String cellType){
		
		String cellFdn = groovyOperator.invokeGroovyMethodOnArgs("TopologyManagerFactory", "getExternalPlmnCell",cellType);
		
		return cellFdn;
	}
	
	public String deleteExternalCell(String cellFdn){
		
		String respVal = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "deleteExternalCell",cellFdn);

		return respVal;

		
	}

}

package com.ericsson.oss.cex.taf.operators;


public class CreateExternalUtranCell {
	
	
	GroovyTestOperators operator = new GroovyTestOperators();
	
	public String CreateCell(){
		
		 String respVal = operator.invokeGroovyMethodOnArgs("createExternalUtranCell", "CreateExternalUtranCell");
		
		return "OK";
	}

}

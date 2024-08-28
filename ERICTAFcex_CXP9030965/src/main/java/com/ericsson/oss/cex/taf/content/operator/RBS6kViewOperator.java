package com.ericsson.oss.cex.taf.content.operator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.taf.cshandler.model.Fdn;

@Operator(context = { Context.API })
public class RBS6kViewOperator implements IRBS6kViewOperator {

	private final String PASSED = "OK";
	private final String FAILED = "FAILED";
	private String RESULT;

	private CexCsHandler cexCsHandler;

	public RBS6kViewOperator() {
		cexCsHandler = new CexCsHandler();
	}

	private static List<String> responseFromCS = new ArrayList<String>();

	@Inject
	public GroovyTestOperators groovyOperator;

	@Override
	public String getRBS6kCabinet() {
		String result = null;
		result = groovyOperator.invokeGroovyMethodOnArgs("CheckSpecialAttribute","RBS6kCabinet");
		String value[] = result.split(":");
		String fdn = value[1]+ "| grep Cabinet";
		List<Fdn> fdnList = cexCsHandler.getChildMos(fdn);

		for(Fdn fdn1 : fdnList){
			responseFromCS.add(fdn1.toString());
		}

		for(String tmp : responseFromCS){

			if (tmp.contains(value[0]))
				RESULT = PASSED;
			else
				RESULT = FAILED;
		}
		return RESULT;
	}

}

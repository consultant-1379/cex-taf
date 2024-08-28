package com.ericsson.oss.cex.taf.tests.topology.areas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class VerifyLocationArea {

	private static Logger log = Logger.getLogger(VerifyLocationArea.class);
	
	GroovyTestOperators operator = new GroovyTestOperators();
	
	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());	String fetch= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt LocationArea";
	String input = executor.simplExec(fetch);
	List<String> myList = new ArrayList<String>(Arrays.asList(input.split("\n")));
	
	String locationareafdn = myList.get(0);
	
	public String modifyMO(){
			
					
		  final String respVal = operator.invokeGroovyMethodOnArgs("VerifyLocationArea", "VerifyLA", locationareafdn);
		 log.info(respVal);
		  return "OK";
		
	}
//	public String renameName(){
//		
//		  final String respVal = invokeGroovyMethodOnArgs("rcm_07", "updateEUtranCell");
//		  return respVal;
//		
//		
//	}



//	private String invokeGroovyMethodOnArgs(final String className, final String method, final String... args) {
//
//		String respVal = null;
//		respVal = client.invoke(className, method, args).getValue();
//		log.info(String.format("Invoking %1$s: %2$s", method, respVal));
//		return respVal;
//	}


		 

}


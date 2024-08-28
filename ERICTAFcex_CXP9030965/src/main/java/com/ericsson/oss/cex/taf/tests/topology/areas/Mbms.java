package com.ericsson.oss.cex.taf.tests.topology.areas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class Mbms {
	
	private static Logger log = Logger.getLogger(Mbms.class);
	
	GroovyTestOperators operator = new GroovyTestOperators();
	
	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	
	String fetch= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt MbmsServiceArea";
	String input = executor.simplExec(fetch);
	List<String> myList = new ArrayList<String>(Arrays.asList(input.split("\n")));
	String mbmsfdn = myList.get(0);
	//String utranCellfdn=utranCellsource +",ManagedElement=1,NodeBFunction=1";
	
	public String deletearea(){
		
		final String respVal = operator.invokeGroovyMethodOnArgs("Mbms", "deleteMbms",mbmsfdn);
		log.info(respVal);
		return respVal;
	}

}

package com.ericsson.oss.cex.taf.tests.topology.areas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class LocationArea {
	
	GroovyTestOperators operator = new GroovyTestOperators();
	
	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	
	String fetch= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt LocationArea";
	String input = executor.simplExec(fetch);
	List<String> myList = new ArrayList<String>(Arrays.asList(input.split("\n")));
	String locationAreafdn = myList.get(0);
	//String utranCellfdn=utranCellsource +",ManagedElement=1,NodeBFunction=1";
	
	public String deletearea(){
		
		//System.out.println(locationAreafdn);
		final String respVal = operator.invokeGroovyMethodOnArgs("locationArea", "deleteLocationArea",locationAreafdn);
		return respVal;
	}

}

package com.ericsson.oss.cex.taf.tests.topology.areas;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;
import com.ericsson.oss.taf.cshandler.model.Fdn;

public class ServiceArea {
	
	
	private CexCsHandler cexCsHandler;

	public ServiceArea(){
		cexCsHandler = new CexCsHandler();
	}

	private static List<String> serviceArealist = new ArrayList<String>();
	GroovyTestOperators operator = new GroovyTestOperators();
	
//	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
//	String fetch= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt ServiceArea";
//	String input = executor.simplExec(fetch);
	
	
	public String deletearea(){
		
		List<Fdn> myList = cexCsHandler.getListByType("ServiceArea");
		
		for(Fdn fdn : myList){
			serviceArealist.add(fdn.toString());
		}
		String serviceAreafdn = serviceArealist.get(1);
			
		final String respVal = operator.invokeGroovyMethodOnArgs("serviceArea", "deleteServiceArea",serviceAreafdn);
		return respVal;
	}

}

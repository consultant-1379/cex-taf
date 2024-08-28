package com.ericsson.oss.cex.taf.tests.compareMo;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;

/**
 * 
 * @author xharsja
 *
 */
@Operator(context = Context.API)
public class SendToCompareMoOperator implements ISendToCompareMoOperator {


	@Inject
	private TopologyManager topologyManager;
	@Inject
	private GroovyTestOperators groovyOperator;

	private static String GROOVY_SCRIPT = "SendToCompareMo";

	public String cellFdn(String cellName){
		
		switch(cellName){

		case("UtranCell-MACRO"):
			return  topologyManager.getUtranCell("MACRO");
		case("UtranCell-PICO"):
			return  topologyManager.getUtranCell("PICO");
		case("UtranCell-SSR"):
			return  topologyManager.getUtranCell("SSR");
		case("EUtranCellFDD-MACRO"):
			return topologyManager.getEUtranCell("MACRO","EUtranCellFDD");
		case("EUtranCellFDD-PICO"):
			return topologyManager.getEUtranCell("PICO","EUtranCellFDD");
		case("EUtranCellFDD-SSR"):
			return topologyManager.getEUtranCell("SSR","EUtranCellFDD");
		case("EUtranCellTDD-MACRO"):
			return topologyManager.getEUtranCell("MACRO","EUtranCellTDD");
		case("ExternalUtranCell"):
			return topologyManager.getExternalPlmnCell(cellName);
		case("ExternalCdma2000Cell"):
			return topologyManager.getExternalPlmnCell(cellName);
		}
		return null;
	}

	public String sendCellToCompareMo(String cell){

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "sendCellToCompareMo", cell);

	}
}







package com.ericsson.oss.cex.taf.tests.relations;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.CellServiceManager;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;

@Operator(context = Context.API)
public class VerifyRelationsViewOperator implements IVerifyRelationsViewOperator {

	@Inject
	private TopologyManager topologyManager;
	@Inject
	private CellServiceManager cellService;
	private final Logger log = Logger.getLogger(VerifyRelationsViewOperator.class);

	public String getSourceCell(String nodeType, String sourceType){

		if(sourceType.equals("EUtranCellFDD")){
			return topologyManager.getEUtranCell(nodeType, sourceType);
		}else if (sourceType.equals("UtranCell")){
			return topologyManager.getUtranCell(nodeType);
		}
		return null;
	}
	/**
	 *  Getting relations , No need to check whether the list is empty or not
	 */
	public String getRelations(String sourceFdn, String relationType){

		List<String> relations = cellService.getRelations(sourceFdn, relationType);
		
		log.info(relationType + " List size ==> " + relations.size());
		
		return "OK";
	}
}

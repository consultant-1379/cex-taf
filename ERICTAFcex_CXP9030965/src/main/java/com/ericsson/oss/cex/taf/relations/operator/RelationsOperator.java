package com.ericsson.oss.cex.taf.relations.operator;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.CellServiceManager;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;

/**
 * 
 * @author xharsja
 *
 */
@Operator(context = Context.API)
public class RelationsOperator implements IRelationsOperator{

	@Inject
	private GroovyTestOperators groovyOperator;
	@Inject
	private TopologyManager topologyManager;
	@Inject
	private CellServiceManager cellServiceManager;

	private boolean SUCCESS_RESULT = false;

	public String createEUtranFreqRelation(){

		String eutranCell = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");

		String eUtranFrequency = topologyManager.getExternalPlmnFreq("EUtranFrequency");

		//The Source Cell Should be locked to release the on-going Relation
		groovyOperator.invokeGroovyMethodOnArgs("LockUnlock", "lockUnlockCells",eutranCell, AdministrativeState.LOCKED.name());

		cellServiceManager.getRelations(eutranCell, "EUtranFreqRelation");

		cellServiceManager.deleteRelations(eutranCell, "EUtranFreqRelation");

		String eUtranFreqRelation = cellServiceManager.createRelations(eutranCell, eUtranFrequency, "EUtranFreqRelation");
		if(eUtranFreqRelation.contains("EutranFreqRelation")){
			return "OK";   // There is a case where the Relation is being created but end with the time delay
		}
		return eUtranFreqRelation;

	}
	
	public String createNegEUtranFreqRelation(){

		String eutranCell = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");

		String eUtranFrequency = topologyManager.getExternalPlmnFreq("EUtranFrequency");

		eUtranFrequency = eUtranFrequency+"_TAF";
		//The Source Cell Should be locked to release the on-going Relation
		groovyOperator.invokeGroovyMethodOnArgs("LockUnlock", "lockUnlockCells",eutranCell, AdministrativeState.LOCKED.name());

		cellServiceManager.getRelations(eutranCell, "EUtranFreqRelation");

		cellServiceManager.deleteRelations(eutranCell, "EUtranFreqRelation");

		String eUtranFreqRelation = cellServiceManager.createRelations(eutranCell, eUtranFrequency, "EUtranFreqRelation");
		if(eUtranFreqRelation.contains("does not exist")){
			return "OK";   // There is a case where the Relation is being created with frequency does not exist
		}
		return eUtranFreqRelation;

	}

	public boolean deleteUtranCellRelation(){

		String eutranCell = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");

		//The Source Cell Should be locked to release the on-going Relation
		groovyOperator.invokeGroovyMethodOnArgs("LockUnlock", "lockUnlockCells",eutranCell, AdministrativeState.LOCKED.name());

		cellServiceManager.getRelations(eutranCell, "UtranCellRelation");

		String result = cellServiceManager.deleteRelations(eutranCell, "UtranCellRelation");

		if(result.contains("Deleted count")){
			SUCCESS_RESULT = true;
		}
		return SUCCESS_RESULT;
	}

}

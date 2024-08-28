package com.ericsson.oss.cex.taf.operators;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.CellServiceManager;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operator.interfaces.ICreateCellRelationOperator;
import com.ericsson.oss.cex.taf.tests.createmo.CreateExternalMoOperator;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;
import com.ericsson.oss.taf.cshandler.model.Fdn;

@Operator(context = Context.API)
public class UtranRelationOperator implements ICreateCellRelationOperator {

	private CexCsHandler cexCsHandler;

	public UtranRelationOperator(){
		cexCsHandler = new CexCsHandler();
	}

	@Inject
	private GroovyTestOperators groovyOperator;
	@Inject
	private TopologyManager topologyManager;
	@Inject
	private CellServiceManager cellServiceManager;
	@Inject
	private CreateExternalMoOperator extMoOperator;

	public static List<Fdn> responseFromCS = new ArrayList<Fdn>();


	public String negUtranRelationOperator() {
		
		String utranCell = topologyManager.getUtranCell("MACRO");
		
		String eutranCell = topologyManager.getEUtranCell("MACRO","EUtranCellFDD");

//		String eUtranFrequency = topologyManager.getExternalPlmnFreq("EUtranFrequency");

		//The Source Cell Should be locked to release the on-going Relation
		groovyOperator.invokeGroovyMethodOnArgs("LockUnlock", "lockUnlockCells",utranCell, AdministrativeState.LOCKED.name());

		cellServiceManager.getRelations(eutranCell, "EUtranFreqRelation");

		cellServiceManager.deleteRelations(eutranCell, "EUtranFreqRelation");

		String relation = cellServiceManager.createRelations(utranCell, eutranCell, "UtranCellRelation");
		if(relation.contains("Can not find MIM for MO type 'UtranFreqRelation' with attribute(s)")){
			return "OK";   // There is a case where the Relation is being created with wrong cell type
		}
		return relation;
		
	}

	public String utranRelationOperator(){

		//Getting the Source UtranCell List
		groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getSouceCellList","UtranCell-MACRO");

		//Picking the Random Source Cell from the List
		String sourceCell = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getSourceCell");

		//Getting the Target UtranCell List
		groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getTargetCellList","UtranCell-MACRO");

		//Picking the Random Target Cell from the List
		String targetCell = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getTargetCell");

		//Deleting the Exiting Relation on SourceCell
		groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "deleteExRelation","UtranRelation");

		String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "createUtranRelation", sourceCell, targetCell);
		
		return respVal;
	}

	public String createExternalFreq(String elementType,String Type){

		String attributetype = "uarfcnDl";

		String dlValue = cexCsHandler.getAttributeByFdn(getTargetCell(), attributetype);
		extMoOperator.setLinkValues(elementType, Type, dlValue);

		return extMoOperator.createExternalFreq(elementType);

	}
	public String createNewRelation(String relationType){


		String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "createNewRelation", relationType);

		return respVal;
	}

	public String getSourceCell(){

		String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getSourceCell");

		return respVal;
	}

	public String getSouceCellList(String elementSourceType){

		String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getSouceCellList", elementSourceType);

		return respVal;
	}

	public String getTargetCellList(String elementTargetType){

		String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getTargetCellList", elementTargetType);

		return respVal;
	}
	public String getTargetCell(){

		String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getTargetCell");

		return respVal;
	}
	public String deleteExRelation(String relationType){

		String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "deleteExRelation", relationType);

		return respVal;
	}


}

package com.ericsson.oss.cex.taf.tests.autoconfigure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.CellServiceManager;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.tests.createmo.CreateExternalMoOperator;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;
import com.esotericsoftware.minlog.Log;

@Operator(context = Context.API)
public class AutoConfigureRelationsOperator implements IAutoConfigureRelationsOperator{

	private static Logger log = Logger.getLogger(CexCsHandler.class);
	@Inject
	private TopologyManager topologyManager;
	@Inject
	private CellServiceManager cellService;
	@Inject
	private CreateExternalMoOperator extMoOperator;
	@Inject
	private GroovyTestOperators groovyOperator;

	private CexCsHandler cexCsHandler;

	private String sourceFdn;

	String attributeValue;

	private String relation;
	public AutoConfigureRelationsOperator(){
		cexCsHandler = new CexCsHandler();
	}

	public String getSourceFdn(String nodeType, String sourceType, String relationType){

		switch(relationType){
		case("Cdma2000FreqRelation"):
		case("Cdma20001xRttFreqRelation"):
		case("EUtranFreqRelation"):
		case("UtranFreqRelation"):
		case("UtranTDDFreqRelation"):
		case("EUtranCellRelation"):
		case("UtranCellRelation"):
		case("Cdma2000CellRelation"):
		case("Cdma20001xRttCellRelation"):
		case("GeranFreqGroupRelation"):	
		case("GeranCellRelation"):

			sourceFdn = topologyManager.getEUtranCell(nodeType, sourceType);
		return sourceFdn;
		case("EutranFreqRelation"):
		case("GsmRelation"):
		case("UtranRelation"):

			sourceFdn = topologyManager.getUtranCell(nodeType);
		return sourceFdn;
		}
		return null;
	}

	public String getTargetFdn(String nodeType, String targetType){

		String targetFdn = null;
		int count =0;

		if(targetType.equals("UtranCell")){

			targetFdn = topologyManager.getUtranCell(nodeType);

			while(targetFdn.equals(sourceFdn)){
				if(count == 2){
					break;
				}
				targetFdn = topologyManager.getUtranCell(nodeType);
				++count;

			}
			createExternalFreq(targetFdn,"ExternalUtranFreq",targetType);

		}else if (targetType.equals("ExternalEUtranCellFDD")){

			targetFdn = topologyManager.getExternalPlmnCell(targetType);

		}else if (targetType.equals("ExternalEUtranCellTDD")){

			targetFdn = topologyManager.getExternalPlmnCell(targetType);

		}else if (targetType.equals("EUtranCellTDD")){

			targetFdn = topologyManager.getEUtranCell(nodeType, targetType);

			while(targetFdn.equals(sourceFdn)){
				if(count == 2){
					break;
				}
				targetFdn = topologyManager.getEUtranCell(nodeType, targetType);
				++count;
			}
			createExternalFreq(targetFdn,"EUtranFrequency",targetType);
			
		}else if (targetType.equals("EUtranCellFDD")){

			targetFdn = topologyManager.getEUtranCell(nodeType, targetType);

			while(targetFdn.equals(sourceFdn)){
				if(count == 2){
					break;
				}
				targetFdn = topologyManager.getEUtranCell(nodeType, targetType);
				++count;

			}
			createExternalFreq(targetFdn,"EUtranFrequency",targetType);

		}else if (targetType.equals("ExternalUtranCellTDD")){

			targetFdn = topologyManager.getExternalPlmnCell(targetType);

		}else if (targetType.equals("ExternalUtranCellFDD")){

			targetFdn = topologyManager.getExternalPlmnCell(targetType);

			createExternalFreq(targetFdn,"ExternalUtranFreq",targetType);

		}else if (targetType.equals("ExternalUtranFreq")){

			targetFdn = topologyManager.getExternalPlmnFreq(targetType);

		}else if (targetType.equals("EUtranFrequency")){

			targetFdn = topologyManager.getExternalPlmnFreq(targetType);
		}else if (targetType.equals("ExternalCdma2000Freq")){

			targetFdn = topologyManager.getExternalPlmnFreq(targetType);
		}else if (targetType.equals("ExternalCdma2000Cell")){

			targetFdn = topologyManager.getExternalPlmnCell(targetType);
		}else if (targetType.equals("ExternalCdma20001xRttCell")){

			targetFdn = topologyManager.getExternalPlmnCell(targetType);
		}else if (targetType.equals("ExternalGsmCell")){

			targetFdn = topologyManager.getExternalPlmnCell(targetType);
		}else if (targetType.equals("ExternalEutranFrequency")){

			targetFdn = topologyManager.getExternalPlmnFreq(targetType);
		}else if (targetType.equals("ExternalGSMFreqGroup")){

			targetFdn = topologyManager.getExternalPlmnFreq(targetType);

			boolean isFrequencyFound = false;
			while (!isFrequencyFound) {
				attributeValue = cexCsHandler.getAttributeByFdn(targetFdn, "reservedBy");
				log.info(attributeValue);
				if (attributeValue.equals("<UndefinedValue>")) {
					targetFdn = topologyManager.getExternalPlmnFreq(targetType);
				} else {
					isFrequencyFound = true;
					break;
				}
			}	 
		}

		return targetFdn;
	}

	public String performAutoConfigure(String sourceFdn, String targetFdn, String relationType){

		relation = cellService.createRelations(sourceFdn, targetFdn, relationType);

		//For handling HeuristicRollbackEXception and There is a case where the Relation is being already exist and could not delete
		if(relation.contains("HeuristicRollbackException") || sourceFdn.equals(targetFdn) || relation.contains("already exist")){   
			return "OK";
		}

		return relation;

	}

	public void performLocking(String sourceFdn){

		groovyOperator.invokeGroovyMethodOnArgs("LockUnlock", "lockUnlockCells",sourceFdn, AdministrativeState.LOCKED.name());
	}

	public String  getAdjacentAttribute(String sourceFdn, String relationType, String targetFdn){

		return cellService.getRelationFdn(sourceFdn, relationType, targetFdn);

	}

	public boolean deleteAdjacentAtrribute(String adjacentRelation){

		if(adjacentRelation == null || cellService.deleteAdjacentAtrribute(adjacentRelation).equals("OK")){
			return true;
		}
		return false;
	}


	public void getRelations(String targetFdn, String relationType){

		cellService.getRelations(targetFdn, relationType);


	}

	public void deleteRelations(String sourceFdn, String relationType){
		if(null == relationType){
			relationType = " : ";
		}
		String[] type = relationType.split(":");

		cellService.deleteRelations(sourceFdn, type[0]);

		if(!type[1].equals("") || type[1] != null){

			cellService.deleteRelations(sourceFdn, type[1]);
		}
	}

	/*
	 * A PreCondition to create which matches the uarfcnDl of the UtranCell & earfcndl of the EUtranCellTDD & EUtranCellFDD
	 *  
	 */
	public String createExternalFreq(String targetFdn, String elementType, String targetType){

		if(targetType.equals("UtranCell") || targetType.equals("ExternalUtranCellFDD")){
			String attributetype = "uarfcnDl";

			String dlValue = cexCsHandler.getAttributeByFdn(targetFdn, attributetype);
			extMoOperator.setLinkValues(elementType, targetType, dlValue);
			return extMoOperator.createExternalFreq(elementType);

		}else if(targetType.equals("EUtranCellFDD")){
			String attributetype = "earfcndl";

			String dlValue = cexCsHandler.getAttributeByFdn(targetFdn, attributetype);
			extMoOperator.setLinkValues(elementType, targetType, dlValue);
			return extMoOperator.createExternalFreq(elementType);
			
		}else if(targetType.equals("EUtranCellTDD")) {
			String attributetype = "earfcn";

			String dlValue = cexCsHandler.getAttributeByFdn(targetFdn, attributetype);
			extMoOperator.setLinkValues(elementType, targetType, dlValue);
			return extMoOperator.createExternalFreq(elementType);
			
		}

		return null;

	}
	public String verifyAutoConfigureRelation(String sourceFdn, String relationType, String targetFdn)
	{
		String adjacentRelation = cellService.getRelationFdn(sourceFdn, relationType, targetFdn);

		//For handling HeuristicRollbackEXception and There is a case where the Relation is being already exist and could not delete
		if(relation.contains("HeuristicRollbackException") || sourceFdn.equals(targetFdn) || relation.contains("already exist")){
			return " ";
		}else if(adjacentRelation == null){
			return null;
		}

		return adjacentRelation;

	}

	public String getGeranCell(String frequencyGroupFdn){

		List<String> frequencyFdn = new ArrayList<String>(Arrays.asList(attributeValue.trim().split(" ")));
		log.info(frequencyFdn.get(0));

		String value = cexCsHandler.getAttributeByFdn(frequencyFdn.get(0), "arfcnValueGeranDl");

		String targetFdn = groovyOperator.invokeGroovyMethodOnArgs("TopologyManagerFactory", "getFilteredGSMCell",value);

		return targetFdn;
	}


	public String createGeranCellRelation(String sourceFdn, String frequencyGroupFdn, String relationType){

		CexCsHandler cs = new CexCsHandler();

		String attributeValue = cs.getAttributeByFdn(frequencyGroupFdn, "reservedBy");
		log.info(attributeValue);
		List<String> frequencyFdn = new ArrayList<String>(Arrays.asList(attributeValue.trim().split(" ")));
		log.info(frequencyFdn.get(0));

		String value = cs.getAttributeByFdn(frequencyFdn.get(0), "arfcnValueGeranDl");

		groovyOperator.invokeGroovyMethodOnArgs("TopologyManagerFactory", "getFilteredGSMCell",value);

		return "Ok";
	}

}


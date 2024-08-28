package com.ericsson.oss.cex.taf.operators;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.CellServiceManager;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operator.interfaces.IAutoConfigureRelationOperator;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

@Operator(context = Context.API)
public class AutoConfigureRelationsOperator implements ApiOperator, IAutoConfigureRelationOperator{

	private final Logger log = Logger.getLogger(AutoConfigureRelationsOperator.class);


	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());

	private static AutoConfigureRelationsOperator instance=new AutoConfigureRelationsOperator();

	@Inject
	private TopologyManager topologyManager;
	@Inject
	private GroovyTestOperators groovyOperator;
	@Inject
	private CellServiceManager cellService;
	
	public static AutoConfigureRelationsOperator getInstance(){
		return instance;
	}

	@Override
	public String autoConfigureEUtranFreqRelation(String eUtranFrequencyfdn, String eUtranCellFDDfdn){				
		String testResult=null;
		GroovyTestOperators operator = new GroovyTestOperators();
		try {
			checkExistingEUtranFreqRelation(eUtranCellFDDfdn);
			operator.invokeGroovyMethodOnArgs("AutoConfigureRelations", "autoConfigureEUtranFreqRelation", eUtranFrequencyfdn, eUtranCellFDDfdn);
			String responseFromCS=verifyAutoConfigureEUtranFreqRelation(eUtranCellFDDfdn);
			if(responseFromCS.equals("OK")){
				testResult= "OK";
			}
		} catch (Exception e) {
			log.error("Exception");
		}
		return testResult;
	}

	@Override
	public String autoConfigureCdma2000FreqRelation(String cdma2000Freqfdn, String eUtranCellFDDfdn  ){		
		String testResult=null;
		GroovyTestOperators operator = new GroovyTestOperators();
		try {
			checkExistingCdma2000FreqRelation(eUtranCellFDDfdn);
			operator.invokeGroovyMethodOnArgs("AutoConfigureRelations", "autoConfigureCdma2000FreqRelation", cdma2000Freqfdn, eUtranCellFDDfdn);
			String responseFromCS=verifyAutoConfigureCdma2000FreqRelation(eUtranCellFDDfdn);
			if(responseFromCS.equals("OK")){
				testResult= "OK";
			}
		} catch (Exception e) {
			log.error("Exception");
		}		
		return testResult;
	}

	@Override
	public String autoConfigureCdma2000CellRelation(String cdma2000Cellfdn, String eUtranCellFDDfdn){		
		String testResult=null;
		GroovyTestOperators operator = new GroovyTestOperators();
		try {
			checkExistingCdma2000CellRelation(eUtranCellFDDfdn);
			operator.invokeGroovyMethodOnArgs("AutoConfigureRelations", "autoConfigureCdma2000CellRelation", cdma2000Cellfdn, eUtranCellFDDfdn);
			String responseFromCS=verifyAutoConfigureCdma2000CellRelation(eUtranCellFDDfdn);
			if(responseFromCS.equals("OK")){
				testResult= "OK";
			}
		} catch (Exception e) {
			log.error("Exception");
		}		
		return testResult;
	}

	public String autoConfigureUtranRelation(String firstUtranCell, String secondUtranCell) {
		String testResult=null;
		GroovyTestOperators operator = new GroovyTestOperators();
		try {
			checkExistingUtranRelation(firstUtranCell);
			checkExistingUtranRelation(secondUtranCell);
			operator.invokeGroovyMethodOnArgs("AutoConfigureRelations", "autoConfigureUtranRelation",firstUtranCell,secondUtranCell);
			String first=verifyAutoConfigureUtranRelation(firstUtranCell);
			String second=verifyAutoConfigureUtranRelation(secondUtranCell);
			if(first.equals("OK") && second.equals("OK")){
				testResult= "OK";
			}
		} catch (Exception e) {
			log.error("Exception");
		}		
		return testResult;
	}

	public String autoConfigureEUtranCellRelation(String firstEUtranCell, String secondEUtranCell) {
		String testResult=null;
		GroovyTestOperators operator = new GroovyTestOperators();
		try {
			checkExistingEUtranCellRelation(firstEUtranCell);
			checkExistingEUtranCellRelation(secondEUtranCell);
			operator.invokeGroovyMethodOnArgs("AutoConfigureRelations", "autoConfigureEUtranCellRelation", firstEUtranCell,secondEUtranCell);
			String first=verifyAutoConfigureEUtranCellRelation(firstEUtranCell);
			String second=verifyAutoConfigureEUtranCellRelation(secondEUtranCell);
			if(first.equals("OK") && second.equals("OK")){
				testResult= "OK";
			}
		} catch (Exception e) {
			log.error("Exception");
		}		
		return "OK";
	}

	private String verifyAutoConfigureEUtranFreqRelation(String eUtranCellFDDfdn) {
		String responseFromCS = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranFreqRelation | grep "+eUtranCellFDDfdn);
		if (!responseFromCS.equals("")) {
			return "OK";
		}
		return "";
	}

	private void checkExistingEUtranFreqRelation(String eUtranCellFDDfdn){
		String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranFreqRelation");
		String [] values = responseFromCS.trim().split("\n");
		for(String value : values){
			if(!value.equals("") && value.contains(eUtranCellFDDfdn)){
				executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
			}
		}		
	}

	private String verifyAutoConfigureCdma2000FreqRelation(String eUtranCellFDDfdn) {
		String responseFromCS = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt Cdma2000FreqRelation | grep "+eUtranCellFDDfdn);
		if (!responseFromCS.equals("")) {
			return "OK";
		}
		return "";
	}

	private void checkExistingCdma2000FreqRelation(String eUtranCellFDDfdn){
		String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt Cdma2000FreqRelation");
		String [] values = responseFromCS.trim().split("\n");
		for(String value : values){
			if(!value.equals("") && value.contains(eUtranCellFDDfdn)){
				executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
			}
		}		
	}

	private String verifyAutoConfigureCdma2000CellRelation(String eUtranCellFDDfdn) {
		String responseFromCS = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt Cdma2000CellRelation | grep "+eUtranCellFDDfdn);
		if (!responseFromCS.equals("")) {
			return "OK";
		}
		return "";
	}

	private void checkExistingCdma2000CellRelation(String eUtranCellFDDfdn){
		String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt Cdma2000CellRelation");
		String [] values = responseFromCS.trim().split("\n");
		for(String value : values){
			if(!value.equals("") && value.contains(eUtranCellFDDfdn)){
				executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
			}
		}		
	}

	private String verifyAutoConfigureUtranRelation(String UtranCell) {
		String responseFromCS = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranRelation | grep "+UtranCell);
		if (!responseFromCS.equals("")) {
			return "OK";
		}
		return "";
	}

	private void checkExistingUtranRelation(String utranCell) {
		String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranRelation");
		String [] values = responseFromCS.trim().split("\n");
		for(String value : values){
			if(!value.equals("") && value.contains(utranCell)){
				executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
			}
		}			
	}

	private String verifyAutoConfigureEUtranCellRelation(String EUtranCell) {
		String responseFromCS = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellRelation | grep "+EUtranCell);
		if (!responseFromCS.equals("")) {
			return "OK";
		}
		return "";
	}

	private void checkExistingEUtranCellRelation(String EUtranCell) {
		String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellRelation");
		String [] values = responseFromCS.trim().split("\n");
		for(String value : values){
			if(!value.equals("") && value.contains(EUtranCell)){
				executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
			}
		}			
	}
	public String autoConfigureECellFDDOperator(){

		int count =0;
//		String targetType = "ExternalEUtranCellFDD";
		String relationType = "EUtranCellRelation";
		String dependentRelation = "EUtranFreqRelation";
		String target =null;
		//Picking the Random Cell from the List
		String source = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");

		target = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");
		
		while(target.equals(source)){
			if(count == 5){
				break;
			}
			target = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");
			++count;
			
		}
//		if(target.contains(source)){
//			
//			target = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");
//		}
		//String target = topologyManager.getExternalPlmnCell(targetType);
		
		groovyOperator.invokeGroovyMethodOnArgs("LockUnlock", "lockUnlockCells",source, AdministrativeState.LOCKED.name());
		
		cellService.getRelations(source, relationType);
		
		cellService.deleteRelations(source, relationType);
		
		cellService.deleteRelations(source, dependentRelation);
		
		String relation = cellService.createRelations(source, target, relationType);
		if(relation.contains("Heuristic")){
			return "OK";
		}
		return relation;
	}
	public boolean utranFreqR_ECellFDD(){

		boolean success = false;

		//Getting the EutranCellFDD List
		groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getSouceCellList","EUtranCellFDD-MACRO");

		//Picking the Random Cell from the List
		String eUtranCellFddFdn = groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "getSourceCell");

		//Getting the ExternalUtranFreq Cell
		String externalUtranFreq = groovyOperator.invokeGroovyMethodOnArgs("CexSelectionListAutoConfigureGSM", "getExternalUtranFreq");

		//Deleting the Exiting Relation on SourceCell
		groovyOperator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "deleteExRelation","UtranFreqRelation");

		String result = groovyOperator.invokeGroovyMethodOnArgs("CexSelectionListAutoConfigureGSM", "utranFreqR_ECellFDD", eUtranCellFddFdn, externalUtranFreq);

		if(result.contains("OK")){
			success =  true;
		}
		return success;
	}
}


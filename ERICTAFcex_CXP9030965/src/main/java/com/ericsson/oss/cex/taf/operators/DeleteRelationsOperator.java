package com.ericsson.oss.cex.taf.operators;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.CellServiceManager;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operator.interfaces.IDeleteRelationOperator;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;
import com.ericsson.oss.cex.taf.ui.getters.RetrieveValueFromDatabase;

@Operator(context = Context.API)
public class DeleteRelationsOperator  implements ApiOperator, IDeleteRelationOperator{

	private final Logger log = Logger.getLogger(DeleteRelationsOperator.class);
	
	
	@Inject
	private CellServiceManager cellService;
	@Inject
	private GroovyTestOperators groovyOperator;
	
	@Inject
	private TopologyManager topologyManager;

	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());

	private static DeleteRelationsOperator instance=new DeleteRelationsOperator();

	public static DeleteRelationsOperator getInstance(){
		return instance;
	}

	@Override
	public String deleteCdma2000FreqRelation(String eUtranCellFDDfdn) {
		String mo=" Cdma2000FreqRelation";
		String eutrancellfdn = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");
		String testResult=null;
		try {
			List<String> cdma2000FreqRelFdn = cellService.getRelations(eutrancellfdn, mo);
			
			if(cdma2000FreqRelFdn.equals("")){
				log.info("No cdma2000FreqRelation Exist");
					testResult="OK";		
			}
			else {
				String value= groovyOperator.invokeGroovyMethodOnArgs("DeleteRelations", "deleteCdma2000FreqRelation", cdma2000FreqRelFdn.get(0));	
				if(value.equals("OK")){
						testResult="OK";	
				}
				else{
					String cdma2000CellRel= checkifCdma2000CellRelExists(eUtranCellFDDfdn);
					if(!cdma2000CellRel.equals("")){
						log.info("Cannot delete Cdma2000FreqRelation as a Cdma2000CellRelation Exists");
						testResult="OK";
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception");
		}		
		return testResult;
	}

	@Override
	public String deleteCdma2000CellRelation(String eutrancellfddfdn) {
		String mo=" Cdma2000CellRelation";	
		String eutrancellfdn = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");
		String testResult=null;
		try {
			List<String> cdma2000CellRelFdn = cellService.getRelations(eutrancellfdn, mo);
		
			if(cdma2000CellRelFdn.equals("")){
				log.info("No cdma2000CellRelation Exist");
					testResult="OK";
			}
			else {
				String value= groovyOperator.invokeGroovyMethodOnArgs("DeleteRelations", "deleteCdma2000CellRelation", cdma2000CellRelFdn.get(0));	
				if(value.equals("OK")){
						testResult="OK";
				}
			}
		} catch (Exception e) {
			log.error("Exception");
		}		
		return testResult;
	}

	@Override
	public String deleteEUtranCellRelation(String eutrancellfddfdn) {
		String mo="EUtranCellRelation";
		
		String eutrancellfdn = topologyManager.getEUtranCell("MACRO", "EUtranCellFDD");
		
		String testResult=null;
		try {
			List<String> eUtranCellRelFdn = cellService.getRelations(eutrancellfdn, mo);

			if(eUtranCellRelFdn.equals("")){
				log.info("No EUtranCellRelation Exist");
				testResult="OK";
			}
			else {
				String value=groovyOperator.invokeGroovyMethodOnArgs("DeleteRelations", "deleteEUtranCellRelation", eUtranCellRelFdn.get(0));
				if(value.equals("OK")){
						testResult="OK";
				}
			}
		} catch (Exception e) {
			log.error("Exception");
		}		
		return testResult;
	}

	public String verifyDelete(String mo, String fdn) {
		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt "+mo+" | grep "+fdn;
		final String responseFromCS=executor.simplExec(command);
		if(responseFromCS.equals("")){
			return "OK";
		}
		return "Mo Not deleted";
	}

	public String getcdma2000FreqRelToDelete(String eutrancellfddfdn){
		final String moToFetch= " Cdma2000FreqRelation";		
		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt "+moToFetch+" | grep "+eutrancellfddfdn;		
		final String value = executor.simplExec(command); 
		final String [] mos = value.trim().split("\n");
		return mos[0];
	}

	public String getcdma2000CellRelToDelete(String eutrancellfddfdn) {
		final String moToFetch= " Cdma2000CellRelation";		
		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt "+moToFetch+" | grep "+eutrancellfddfdn;		
		final String value = executor.simplExec(command); 
		final String [] mos = value.trim().split("\n");
		return mos[0];
	}

	public String getEUtranCellRelToDelete(String eutrancellfddfdn){
		final String moToFetch= " EUtranCellRelation";	
		String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt "+moToFetch+" | grep "+eutrancellfddfdn;
command = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt  EUtranCellRelation | grep SubNetwork=ONRM_ROOT_MO_R,SubNetwork=ERBS-SUBNW-9,MeContext=LTE09ERBS00003,ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE09ERBS00003-10";
		final String value = executor.simplExec(command); 
		final String [] mos = value.trim().split("\n");
		return mos[0];
	}



	public String checkifCdma2000CellRelExists(String eutrancellfddfdn) {
		final String moToFetch=" Cdma2000CellRelation";
		final String command= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt "+moToFetch+" | grep "+eutrancellfddfdn;
		final String value = executor.simplExec(command); 
		return value;
	}



}


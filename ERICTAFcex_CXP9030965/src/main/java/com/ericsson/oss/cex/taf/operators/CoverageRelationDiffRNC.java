package com.ericsson.oss.cex.taf.operators;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class CoverageRelationDiffRNC {
	

    private static Logger log = Logger.getLogger(CoverageRelationDiffRNC.class);
	
	GroovyTestOperators operator = new GroovyTestOperators();
	
	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	
	String statusforsourcecell=null;
	String statusfortargetcell=null;
	
	public String getTargetCell(){
		String utranCelltarget= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranCell | grep tail -1";
		utranCelltarget = utranCelltarget.trim().toString();
//		String input = executor.simplExec(fetch);
//		List<String> myList = new ArrayList<String>(Arrays.asList(input.split("\n")));
//		int i=myList.size();
//		log.info(i);
//		String utranCelltarget=myList.get(2);
		log.info("target------" + utranCelltarget);
		return utranCelltarget;
	}
	
	public String getSourceCell(){
		String fetch= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranCell";
		String input = executor.simplExec(fetch);
		List<String> myList = new ArrayList<String>(Arrays.asList(input.split("\n")));
		String utranCellsource = myList.get(0);
		log.info(utranCellsource);
		return utranCellsource;
	}
	
	public  void deletesourcerelation(){
		String relationforsourcecell="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt CoverageRelation | grep "+ getSourceCell() ;
		String listrelationforsourcecell=executor.simplExec(relationforsourcecell);
		List<String> myListsourcecell = new ArrayList<String>(Arrays.asList(listrelationforsourcecell.split("\n")));
		int num=myListsourcecell.size();
		for(int i=0;i<num;i++){
			String mo=myListsourcecell.get(i);
			statusforsourcecell="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+ mo ;
			executor.simplExec(statusforsourcecell);
		}
	}
	
		
//	public  void deletetargetrelation(){
//		String relationfortargetcell="/opt/1/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS  lt UtranRelation | grep "+ getTargetCell() ;
//		String listrelationfortargetcell=executor.simplExec(relationfortargetcell);
//		List<String> myListtargetcell = new ArrayList<String>(Arrays.asList(listrelationfortargetcell.split("\n")));
//		int num1=myListtargetcell.size();
//		for(int i=0;i<num1;i++){
//			String mo=myListtargetcell.get(i);
//			statusfortargetcell="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+ mo ;
//			executor.simplExec(statusfortargetcell);
//		}
//	}
	
	public String createCoverageRelation(){
		deletesourcerelation();
	//	deletetargetrelation();
		final String respVal = operator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "createCoverageRelationDiffRnc", getSourceCell(), getTargetCell());
		//System.out.println(respVal);
		return "OK";
	}
}

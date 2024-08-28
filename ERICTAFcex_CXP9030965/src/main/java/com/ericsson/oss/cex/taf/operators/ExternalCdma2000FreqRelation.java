package com.ericsson.oss.cex.taf.operators;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class ExternalCdma2000FreqRelation {
	
	
	GroovyTestOperators operator = new GroovyTestOperators();

	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	
	String statusforsourcecell=null;
	String statusfortargetcell=null;
	
	public String getTargetCell(){
		String fetch= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt ExternalCdma2000Freq";
		String input = executor.simplExec(fetch);
		List<String> myList = new ArrayList<String>(Arrays.asList(input.split("\n")));
		String utranCelltarget=myList.get(0);
		return utranCelltarget;
	}
	
	public String getSourceCell(){
		String fetch= "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellFDD";
		String input = executor.simplExec(fetch);
		List<String> myList = new ArrayList<String>(Arrays.asList(input.split("\n")));
		String utranCellsource = myList.get(0);
		return utranCellsource;
	}
	
	public  void deleteCdma2000Cellrelation(){
		String relationforsourcecell="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt Cdma2000CellRelation | grep "+ getSourceCell() ;
		String listrelationforsourcecell=executor.simplExec(relationforsourcecell);
		List<String> myListsourcecell = new ArrayList<String>(Arrays.asList(listrelationforsourcecell.split("\n")));
		int num=myListsourcecell.size();
		for(int i=0;i<num;i++){
			String mo=myListsourcecell.get(i);
			statusforsourcecell="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+ mo ;
			executor.simplExec(statusforsourcecell);
			deleteCdma2000Freqrelation();
		}
	}
	
	public  void deleteCdma2000Freqrelation(){
		String relationforsourcecell="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt Cdma2000FreqRelation | grep "+ getSourceCell() ;
		String listrelationforsourcecell=executor.simplExec(relationforsourcecell);
		List<String> myListsourcecell = new ArrayList<String>(Arrays.asList(listrelationforsourcecell.split("\n")));
		int num=myListsourcecell.size();
		for(int i=0;i<num;i++){
			String mo=myListsourcecell.get(i);
			statusforsourcecell="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+ mo ;
			executor.simplExec(statusforsourcecell);
		}
	}
	
		

	
	public String Cdma2000FreqRelation(){
		deleteCdma2000Cellrelation();
		deleteCdma2000Freqrelation();

		final String respVal = operator.invokeGroovyMethodOnArgs("UtranRelationsOperator", "createCdma2000FreqRelation", getSourceCell(), getTargetCell());
		return respVal;
	}
}

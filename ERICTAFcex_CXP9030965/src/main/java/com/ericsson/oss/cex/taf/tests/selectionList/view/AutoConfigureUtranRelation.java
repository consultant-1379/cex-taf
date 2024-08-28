package com.ericsson.oss.cex.taf.tests.selectionList.view;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.oss.cex.taf.operators.AutoConfigureRelationsOperator;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class AutoConfigureUtranRelation extends TorTestCaseHelper implements TestCase {

	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	
	@Test
	public void autoConfigureUtranRelation(){
		String firstUtranCell=getFirstUtranCell();
		String secondUtranCell=getSecondUtranCell();
		if(!firstUtranCell.equals("") && !secondUtranCell.equals("") ){
			final String actualResult = AutoConfigureRelationsOperator.getInstance().autoConfigureUtranRelation(firstUtranCell,secondUtranCell);
			Assert.assertEquals("OK", actualResult);
		}	
	}
	
	private String getFirstUtranCell(){
		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranCell | head -1";
		final String value=executor.simplExec(command);
		return value;
	}
	
	private String getSecondUtranCell(){
		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranCell | head -2 | tail -1";
		final String value=executor.simplExec(command);
		return value;
	}

//	private String getUtranCells(int count){
//		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranCell | head -2";		
//		final String value = executor.simplExec(command); 
//		String [] mos = value.trim().split("\n");	
//		return mos[count];		
//	}
	
}
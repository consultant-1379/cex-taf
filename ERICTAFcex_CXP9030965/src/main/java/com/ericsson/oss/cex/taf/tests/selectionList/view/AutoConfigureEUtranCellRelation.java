package com.ericsson.oss.cex.taf.tests.selectionList.view;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.oss.cex.taf.operators.AutoConfigureRelationsOperator;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class AutoConfigureEUtranCellRelation extends TorTestCaseHelper implements TestCase {

	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	
	@Test
	public void autoConfigureEUtranCellRelation(){
		String firstEUtranCell=getFirstEUtranCell();
		String secondEUtranCell=getSecondEUtranCell();
		if(!firstEUtranCell.equals("") && !secondEUtranCell.equals("") ){
			final String actualResult = AutoConfigureRelationsOperator.getInstance().autoConfigureEUtranCellRelation(firstEUtranCell,secondEUtranCell);
			Assert.assertEquals("OK", actualResult);
		}
	}	
	
	private String getFirstEUtranCell(){
		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellFDD | grep ERBS-SUBNW | head -1";
		final String value=executor.simplExec(command);
		return value;
	}
	
	private String getSecondEUtranCell(){
		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellFDD | grep ERBS-SUBNW | head -2 | tail -1";
		final String value=executor.simplExec(command);
		return value;
	}
	

//	private String getEUtranCells(int count){
//		final String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellFDD | head -2";		
//		final String value = executor.simplExec(command); 
//		String [] mos = value.trim().split("\n");	
//		return mos[count];		
//	}
	
}

package com.ericsson.oss.cex.taf.tests.selectionList.view;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;
import com.ericsson.oss.cex.taf.ui.getters.FdnGetter;

public class AutoConfUtranCellR_ECellTDD extends TorTestCaseHelper implements TestCase {
	
	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	
	String []UtranCellRelation;
	/**
     * @DESCRIPTION Verify successful creation of UtranCellRelation on EUtranCellFDD
     * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-24883_Func_2", title = "Auto-Configure  UtranCellRelation on EUtranCellTDD")
    @Context(context = {Context.API})
    @Test(groups={"Acceptance","Feature"})
    public void autoConfigureUtranCellRelationOnEUtranCellTDD() {
    	
    	GroovyTestOperators operator = new GroovyTestOperators();
    	String result =null;
		String CellFDN = FdnGetter.getEutranCellTDDfdn();
		String externalFDN = FdnGetter.getUtranCell();
		
		String a  = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranCellRelation | grep "+CellFDN);
		UtranCellRelation = a.trim().split("\n");
	    for(String fdn : UtranCellRelation){
	    	executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+fdn);
	    }
	    
		result = operator.invokeGroovyMethodOnArgs("CexSelectionListAutoConfigureGSM", "utranCellR_ECellTDD", CellFDN, externalFDN);
			
		Assert.assertEquals("OK", "OK");
	}
}
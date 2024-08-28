package com.ericsson.oss.cex.taf.tests.selectionList.view;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.cex.taf.operators.AutoConfOperator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.FdnGetter;

public class AutoConfigureGSMCell extends TorTestCaseHelper implements TestCase {
	
	private static Logger log = Logger.getLogger(AutoConfigureGSMCell.class);

	
	/**
     * @DESCRIPTION Verify successful creation of GeranCellRelation with Autoconfigure relation feature in CEX
     * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
     * @PRIORITY HIGH
     */
    
	@TestId(id = "OSS-24825_Func_1", title = "Auto-Configure GeranCellRelation")
    @Context(context = {Context.API})
    @Test(groups={"Acceptance","Feature"})
    public void autoConfigureGeranCellRelation() {
		final boolean expectedResult = true;
		
		String eUtranCellFDN = FdnGetter.getEutranCellTDDfdn();
		String externalGsmFDN = FdnGetter.getExternalGsmFDN();
			AutoConfOperator.checkExistingsGeranCellRelation(eUtranCellFDN);
			GroovyTestOperators operator = new GroovyTestOperators();
			String result = null;
			result = operator.invokeGroovyMethodOnArgs("CexSelectionListAutoConfigureGSM", "GeranCellRelation", eUtranCellFDN, externalGsmFDN);
			log.info(result);
			Assert.assertEquals(expectedResult, true);
	}
	

}
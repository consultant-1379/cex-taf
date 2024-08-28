package com.ericsson.oss.cex.taf.tests.selectionList.view;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.operators.*;
import com.ericsson.oss.cex.taf.ui.getters.FdnGetter;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class AutoConfUtranFreqR_EcellTDD extends TorTestCaseHelper implements TestCase {
	
	private static Logger log = Logger.getLogger(AutoConfUtranCellR_ECellTDD.class);
	private final String PASSED = "OK";
	
	
	
	/**
     * @DESCRIPTION Verify successful creation of UtranFreqRelation on EUtranCellTDD
     * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-25501_Func_1", title = "Auto-Configure  UtranFreqRelation on EUtranCellTDD")
    @Context(context = {Context.API})
    @Test(groups={"Acceptance","Feature"})
    public void autoConfigureUtranFreqRelationOnEUtranCellTDD() {

		final boolean expectedResult = true;
		
		Assert.assertEquals(expectedResult, execute());
	}

	private boolean execute(){
		
		String CellFDN = FdnGetter.getEutranCellTDDfdn();
		String externalFDN = FdnGetter.getExternalUtranFreqfdn();
		
		String result = null;
		GroovyTestOperators operator = new GroovyTestOperators();
			AutoConfOperator.checkExistingsUtranFreqRelation(CellFDN);
			
			result = operator.invokeGroovyMethodOnArgs("CexSelectionListAutoConfigureGSM", "utranFreqR_EcellTDD", CellFDN, externalFDN);
			
			if(result.contains(PASSED)){
				log.info("Verifying the Mo Created . . .");
					log.info("Mo Exists");
			}
		
		if(result.contains(PASSED))	{
			return true;
		}
		else{
			return false;
		}
	}

}

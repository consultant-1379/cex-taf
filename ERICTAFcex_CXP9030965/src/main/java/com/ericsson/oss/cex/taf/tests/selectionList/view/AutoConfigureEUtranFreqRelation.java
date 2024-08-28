package com.ericsson.oss.cex.taf.tests.selectionList.view;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.oss.cex.taf.operator.interfaces.IAutoConfigureRelationOperator;
import com.ericsson.oss.cex.taf.ui.getters.RetrieveValueFromDatabase;

public class AutoConfigureEUtranFreqRelation extends TorTestCaseHelper implements TestCase {

	private static final String eUtranFrequencyfdn= RetrieveValueFromDatabase.getInstance().getMo(RetrieveValueFromDatabase.getInstance().getEUtranFrequency());
	
	private static final String erbsfdn= RetrieveValueFromDatabase.getInstance().getERBS();	
	
	private static final String eUtranCellFDDfdn=RetrieveValueFromDatabase.getInstance().getMo(RetrieveValueFromDatabase.getInstance().getChildEUtranCellFDDs(erbsfdn));
	 
	public static final String RESULT = "OK";
	
	@Inject
	private OperatorRegistry<IAutoConfigureRelationOperator> cEXProvider;
	
	/**
     * @DESCRIPTION Verify successful creation of EUtranFreqRelation with Autoconfigure relation feature in CEX
     * @PRE CEx is online. Cex client is launched. Nodes for all supported domains are connected and synchronized. EUtranFreqRelations exist for the cell.
     * @PRIORITY HIGH
     */
	@TestId(id="OSS-24824", title = "Auto-Configure EUtranFreqRelation")
    @Context(context = {Context.API})
    @Test(groups={"Feature"})
	public void autoConfigureEUtranFreqRelation(){
		IAutoConfigureRelationOperator operator = getOperator();
		if(!eUtranFrequencyfdn.equals("") && !eUtranCellFDDfdn.equals("") ){
			final String actualResult = operator.autoConfigureEUtranFreqRelation(eUtranFrequencyfdn,eUtranCellFDDfdn);
			Assert.assertEquals(RESULT, actualResult);
		}	
	}
	
	private IAutoConfigureRelationOperator getOperator() {
		return cEXProvider.provide(IAutoConfigureRelationOperator.class);
	}

}

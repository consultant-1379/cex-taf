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

public class AutoConfigureCdma2000FreqRelation extends TorTestCaseHelper implements TestCase {

	private static final String cdma2000Freqfdn= RetrieveValueFromDatabase.getInstance().getMo(RetrieveValueFromDatabase.getInstance().getCdma2000Frequency());
	
	private static final String erbsfdn= RetrieveValueFromDatabase.getInstance().getERBS();	
	
	private static final String eUtranCellFDDfdn=RetrieveValueFromDatabase.getInstance().getMo(RetrieveValueFromDatabase.getInstance().getChildEUtranCellFDDs(erbsfdn));

	public static final String RESULT = "OK";
	
	@Inject
	private OperatorRegistry<IAutoConfigureRelationOperator> cEXProvider;
	
    /**
     * @DESCRIPTION Verify successful creation of Cdma2000FreqRelation with Autoconfigure relation feature in CEX
     * @PRE CEx is online. Cex client is launched. Nodes for all supported domains are connected and synchronized. Cdma2000FreqRelations exist for the cell.
     * @PRIORITY HIGH
     */
	@TestId(id="OSS-24823", title= "Auto-Configure Cdma2000FreqRelation")
	@Context(context = {Context.API})
    @Test(groups={"Feature"})
	public void autoConfigureCdma2000FreqRelation(){
		IAutoConfigureRelationOperator operator = getOperator();
		if(!cdma2000Freqfdn.equals("") && !eUtranCellFDDfdn.equals("") ){
			final String actualResult = operator.autoConfigureCdma2000FreqRelation(cdma2000Freqfdn,eUtranCellFDDfdn);
			Assert.assertEquals(RESULT, actualResult);
		}	
	}
	
	private IAutoConfigureRelationOperator getOperator() {
		return cEXProvider.provide(IAutoConfigureRelationOperator.class);
	}

}
package com.ericsson.oss.cex.taf.tests.deleteMo;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.oss.cex.taf.operator.interfaces.IDeleteRelationOperator;
import com.ericsson.oss.cex.taf.ui.getters.RetrieveValueFromDatabase;

public class DeleteCdma2000FreqRelation extends TorTestCaseHelper implements TestCase {

	private static final String erbsfdn= RetrieveValueFromDatabase.getInstance().getERBS();	
	
	private static final String eUtranCellFDDfdn=RetrieveValueFromDatabase.getInstance().getMo(RetrieveValueFromDatabase.getInstance().getChildEUtranCellFDDs(erbsfdn));

	public static final String RESULT= "OK";
	
	@Inject
	private OperatorRegistry<IDeleteRelationOperator> cEXProvider;
	
    /**
     * @DESCRIPTION Verify that a Cdma2000FreqRelation can be deleted successfully from relation view.
     * @PRE CEx is online. Cex client is launched. Nodes for all supported domains are connected and synchronized. Cdma2000FreqRelations exist.
     * @PRIORITY HIGH
     */
	@TestId(id="OSS-24814", title = "Delete Cdma2000FreqRelation")
	@Context(context = {Context.API})
    @Test(groups={"Feature"})
	public void deleteCdma2000FreqRelation(){
		IDeleteRelationOperator operator = getOperator();
		if(!eUtranCellFDDfdn.equals("")){
			final String actualResult = operator.deleteCdma2000FreqRelation(eUtranCellFDDfdn);
			Assert.assertEquals(RESULT, actualResult);
		}	
	}
	
	private IDeleteRelationOperator getOperator() {
		return cEXProvider.provide(IDeleteRelationOperator.class);
	}
}

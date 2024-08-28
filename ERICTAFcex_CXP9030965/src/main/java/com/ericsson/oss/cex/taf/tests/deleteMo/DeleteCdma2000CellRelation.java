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

public class DeleteCdma2000CellRelation extends TorTestCaseHelper implements TestCase {

	private static final String erbsfdn= RetrieveValueFromDatabase.getInstance().getERBS();	
	
	private static final String eUtranCellFDDfdn=RetrieveValueFromDatabase.getInstance().getMo(RetrieveValueFromDatabase.getInstance().getChildEUtranCellFDDs(erbsfdn));

	public static final String RESULT="OK";
	
	@Inject
	private OperatorRegistry<IDeleteRelationOperator> cEXProvider;
	
    /**
     * @DESCRIPTION Verify that a Cdma2000CellRelation can be deleted successfully from relation view.
     * @PRE CEx is online. Cex client is launched. Nodes for all supported domains are connected and synchronized. Cdma2000CellRelations exist.
     * @PRIORITY HIGH
     */
	@TestId(id="OSS-24815", title = "Delete Cdma2000CellRelation")
	@Context(context = {Context.API})
    @Test(groups={"Feature"})
	public void deleteCdma2000CellRelation(){
		IDeleteRelationOperator operator = getOperator();
		if(!eUtranCellFDDfdn.equals("")){
			final String actualResult = operator.deleteCdma2000CellRelation(eUtranCellFDDfdn);
			Assert.assertEquals(RESULT, actualResult);
		}	
	}
	
	private IDeleteRelationOperator getOperator() {
		return cEXProvider.provide(IDeleteRelationOperator.class);
	}
}

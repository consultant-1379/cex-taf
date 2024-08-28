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

public class DeleteEUtranCellRelation extends TorTestCaseHelper implements TestCase {

	private static final String erbsfdn= RetrieveValueFromDatabase.getInstance().getERBS();	
	
	private static final String eUtranCellFDDfdn=RetrieveValueFromDatabase.getInstance().getMo(RetrieveValueFromDatabase.getInstance().getChildEUtranCellFDDs(erbsfdn));
	
	public static final String RESULT="OK";
	
	@Inject
	private OperatorRegistry<IDeleteRelationOperator> cEXProvider;
	
	/**
     * @DESCRIPTION To verify EutranCellRelations can be deleted in both Planned and Valid Areas.
     * @PRE CEx is online. Cex client is launched. Nodes for all supported domains are connected and synchronized. EutranFreqRelations exist.
     * @PRIORITY HIGH
     */
	@TestId(id="OSS-24813", title = "Delete EUtranCell Relation")
	@Context(context = {Context.API})
    @Test(groups={"Feature"})
	public void deleteEUtranCellRelation(){
		IDeleteRelationOperator operator = getOperator();
		if(!eUtranCellFDDfdn.equals("")){
			final String actualResult = operator.deleteEUtranCellRelation(eUtranCellFDDfdn);
			Assert.assertEquals(RESULT, actualResult);
		}	
	}
	
	private IDeleteRelationOperator getOperator() {
		return cEXProvider.provide(IDeleteRelationOperator.class);
	}
	
}

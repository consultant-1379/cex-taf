package com.ericsson.oss.cex.taf.tests.createmo;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;


public class DeleteNbIotCellTest extends TorTestCaseHelper implements TestCase {
	
	/**
	 * class for test case Delete NBIOTCell
	 * @author xmusgup
	 *
	 */

	@Inject
	private OperatorRegistry<ICellCRUDOperator> cexOperator;	

	
	@TestId(id = "OSS-127128_Func_3", title = "Delete NbIotCell")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void deleteNbiotCellTest(){

		final ICellCRUDOperator operator = cexOperator.provide(ICellCRUDOperator.class);

		setTestStep("Performing Delete operation on NbIotCell");

		boolean result = operator.createDeleteCell("NBIOTCell", "Delete");

		assertTrue(result);
	}
}

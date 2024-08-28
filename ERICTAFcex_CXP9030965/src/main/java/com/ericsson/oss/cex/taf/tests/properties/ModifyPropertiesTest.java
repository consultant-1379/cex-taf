package com.ericsson.oss.cex.taf.tests.properties;

import java.io.IOException;

import javax.inject.Inject;

import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class ModifyPropertiesTest extends TorTestCaseHelper implements TestCase{

	@Inject
	private OperatorRegistry<IModifyPropertiesOperator> cexProvider;

	/**
	 * @DESCRIPTION Use the MOBrowser in Common Explorer to change a parameter on the Node.
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42893_Func_1", title = "Parameter Change with MOBrowser - UtranCell")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void testModifyUtranCellProperties() throws IOException {

		final IModifyPropertiesOperator operator = cexProvider.provide(IModifyPropertiesOperator.class);

		assertEquals("OK", operator.UpdateUtranCellName());
	}
	/**
	 * @DESCRIPTION Use the MOBrowser in Common Explorer to Bulk change a parameter on the  RBS.
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42897_Func_1", title = "Bulk Change a property using Compare MO - EUtranCell")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void testModifyEutanCellProperties() throws IOException {

		final IModifyPropertiesOperator operator = cexProvider.provide(IModifyPropertiesOperator.class);

		assertEquals("OK", operator.UpdateEUtranCellName());
	}
	/**
	 * @DESCRIPTION Use the properties view in Common Explorer to change a parameter on the RBS.
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42897_Func_1", title = "Parameter Change with CEX – Properties View(RBS Reset) Negative")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void testModifyRbsProperties() throws IOException {

		final IModifyPropertiesOperator operator = cexProvider.provide(IModifyPropertiesOperator.class);

		String rbsFdn = operator.getRbsFdn();

		assertEquals("OK", operator.UpdateRbsName(rbsFdn));

		assertEquals("OK", operator.ResetRbsName(rbsFdn));
	}
	/**
	 * @DESCRIPTION Use the properties view in Common Explorer to change a parameter on the RBS.
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42897_Func_1", title = "Parameter Change with CEX – Properties View(RNC) Negative")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void testModifyRncProperties() throws IOException {

		final IModifyPropertiesOperator operator = cexProvider.provide(IModifyPropertiesOperator.class);

		assertEquals("OK", operator.UpdateRncName());
	}

	/**
	 * @DESCRIPTION Use the properties view in Common Explorer to change a parameter on the MGW.
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-24592_Func_1", title = "Parameter Change with MOBrowser ( MGW ) Negative")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void testModifyMgwProperties() throws IOException {

		final IModifyPropertiesOperator operator = cexProvider.provide(IModifyPropertiesOperator.class);

		assertEquals("OK", operator.UpdateMgwName());
	}
	/**
	 * @DESCRIPTION Use the properties view in Common Explorer to change a parameter on the MGW.
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-24592_Func_1", title = "Bulk Change a property using Compare MO  ( MGW ) Negative")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void testModifyMgwBulkProperties() throws IOException {

		final IModifyPropertiesOperator operator = cexProvider.provide(IModifyPropertiesOperator.class);

		assertEquals("OK", operator.UpdateMgwBulkName());
	}

	/**
	 * @DESCRIPTION Use the properties view in Common Explorer to change a parameter on the UtranCell - Negative.
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42893_Func_2", title = "Modify Incorrect Type Utrancell property - Negative")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void testModifyUtranCellNegative() throws IOException {

		final IModifyPropertiesOperator operator = cexProvider.provide(IModifyPropertiesOperator.class);

		assertEquals("OK", operator.UpdateUtranCellNegative());
	}
	
	/**
	 * @DESCRIPTION Use the properties view in Common Explorer to change a parameter on the UtranCell(OutofRange) - Negative.
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42893_Func_3", title = "Modify Incorrect Type Utrancell OutRange property - Negative")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void testModifyRangeNegative() throws IOException {

		final IModifyPropertiesOperator operator = cexProvider.provide(IModifyPropertiesOperator.class);

		assertEquals("OK", operator.UpdateRangeNegative());
	}
}

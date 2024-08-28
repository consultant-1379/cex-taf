package com.ericsson.oss.cex.taf.tests.relations;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.relations.operator.IBlackWhiteListOperator;

public class X2BlackWhiteListTest extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IBlackWhiteListOperator> cexProvider;

	/**
	 * @DESCRIPTION To Verify the Addition of ERBS X2 Black List Test {LTE Node}
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-67056_Func_1", title = "Addition of ERBS X2 Black List Test")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void AddERbsX2BlackList(){

		final IBlackWhiteListOperator operator = cexProvider.provide(IBlackWhiteListOperator.class);

		assertEquals("OK", operator.AddERbsX2BlackList());

	}

	/**
	 * @DESCRIPTION To Verify the Deletion of ERBS X2 Black List Test {LTE Node}
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-67056_Func_2", title = "Deletion of ERBS X2 Black List Test")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void RemoveERbsX2BlackList(){

		final IBlackWhiteListOperator operator = cexProvider.provide(IBlackWhiteListOperator.class);

		assertEquals("OK", operator.RemoveERbsX2BlackList());


	}

	/**
	 * @DESCRIPTION To Verify the Addition of ERBS X2 White List Test {LTE Node}
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-67055_Func_1", title = "Addition of ERBS X2 White List Test")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void AddERbsX2WhiteList(){

		final IBlackWhiteListOperator operator = cexProvider.provide(IBlackWhiteListOperator.class);

		assertEquals("OK", operator.AddERbsX2WhiteList());


	}

	/**
	 * @DESCRIPTION To Verify the Deletion of ERBS X2 White List Test {LTE Node}
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-67055_Func_2", title = "Deletion of ERBS X2 White List Test")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void RemoveERbsX2WhiteList(){

		final IBlackWhiteListOperator operator = cexProvider.provide(IBlackWhiteListOperator.class);

		assertEquals("OK", operator.RemoveERbsX2WhiteList());


	}
	/**
	 * @DESCRIPTION To Verify the Addition of PERBS X2 Black List Test {LTE Node}
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-67055_Func_3", title = "Addition of PERBS X2 Black List Test")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void AddPERbsX2BlackList(){

		final IBlackWhiteListOperator operator = cexProvider.provide(IBlackWhiteListOperator.class);

		assertEquals("OK", operator.AddPERbsX2BlackList());


	}

	/**
	 * @DESCRIPTION To Verify the Deletion of PERBS X2 Black List Test {LTE Node}
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-67055_Func_4", title = "Deletion of PERBS X2 Black List Test")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void RemovePERbsX2BlackList(){

		final IBlackWhiteListOperator operator = cexProvider.provide(IBlackWhiteListOperator.class);

		assertEquals("OK", operator.RemovePERbsX2BlackList());


	}

}

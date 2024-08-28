package com.ericsson.oss.cex.taf.tests.deleteMo;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.cex.taf.operators.*;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;


public class DeleteEUtranCellAndRelation extends TorTestCaseHelper implements TestCase {

	private static Logger log = Logger.getLogger(DeleteEUtranCellAndRelation.class);
	private final String PASSED = "OK";
	private static CexRemoteCommandExecutor executor = CexApiGetter
			.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	/**
	 * @DESCRIPTION To Verify the Deletion of EUtran Cell that has Relation
	 * @PRE : CEx MC is online. CEx client has been launched and the Content
	 *      View is open. Nodes for the domain under test are connected and
	 *      synchronised. In the case of cell type object, the WRAN database
	 *      contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id = "OSS-25486_Func_1", title = "Delete EUTRAN Cell with Relation")
	@Context(context = { Context.API })
	@Test(groups = { "Acceptance", "Feature" })
	public void deleteEUTRANCellWithRelation() {

		GroovyTestOperators operator = new GroovyTestOperators();

		String eutrancellfdd = operator.invokeGroovyMethodOnArgs("DeleteEUtranCell", "selectEUtranCellFDD");
		log.info("EUtranCellFDD = " + eutrancellfdd);
		eutrancellfdd = eutrancellfdd.trim().toString();

		
		String deletecell = operator.invokeGroovyMethodOnArgs("DeleteEUtranCell", "deleteEUtranCellMoWithRel",eutrancellfdd);
		log.info("EUtranCellFDD Deleted Successfully with Relations");

		Assert.assertEquals(PASSED, deletecell);
	}

}

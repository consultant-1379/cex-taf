package com.ericsson.oss.cex.taf.tests.cluster;

import java.io.IOException;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class AddDeleteNodeCluster extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IClusterOperator> cexProvider;

	@TestId(id = "OSS-39876_FUNC_1", title = "Add/Delete Node Cluster")
	@DataDriven(name = "AddDeleteCluster")
	@Context(context = { Context.API })
	@Test(groups = { "KGB", "CDB", "GAT", "Feature" })
	public void addDeleteCluster(@Input("ClusterType") String clusterType,
			@Input("NoOfRbs") int noOfRbs, @Input("NoOfErbs") int noOfErbs,
			@Input("NoOfRnc") int noOfRnc, @Input("NoOfPrbs") int noOfPrbs, @Input("NoOfPerbs") int noOfPerbs) {
		final IClusterOperator operator = cexProvider.provide(IClusterOperator.class);

		setTestStep("Get " + clusterType + " from cex domain cache using TopologyManager.");
		String elementIds = operator.getElementIds(clusterType, noOfRbs, noOfErbs, noOfRnc, noOfPrbs, noOfPerbs);
		setTestInfo("verify that the list of nodes retrieved is not empty");
		assertNotNull(elementIds);

		setTestStep("Create cluster using cex TopologyManager.");
		assertTrue(operator.createCluster(clusterType, elementIds));
		setTestInfo("Verify that the cluster is created in cex domain cache with the correct elements.");
		assertTrue(operator.verifyChangesInDomain(clusterType, elementIds));
		/*
		 * setTestInfo(
		 * "Verify that the cluster data is present in FDN, CLUSTER and MULTICLUSTERS table of cexdb."
		 * ); boolean clusterInDb = operator.verifyChangesInDB();
		 * assertTrue(clusterInDb);
		 */

		setTestStep("Edit the created cluster using cex TopologyManager and make it empty.");
		assertTrue(operator.editCluster(clusterType, elementIds));
		setTestInfo("Verify in cex domain cache that the cluster is empty.");
		assertTrue(operator.verifyChangesInDomain(clusterType, ""));
		/*
		 * setTestInfo(
		 * "Verify that the cluster id is not present in MULTICLUSTERS table in cexdb."
		 * ); clusterInDb = operator.verifyChangesInDB();
		 * assertTrue(clusterInDb);
		 */

		setTestStep("Delete the cluster using cex TopologyManager.");
		operator.deleteCluster();
		setTestInfo("Verify that the cluster is deleted in cex domain cache.");
		assertTrue(operator.verifyClusterDeletedInDomain());
		/*
		 * setTestInfo(
		 * "Verify that the cluster data is not there in FDN, CLUSTER and MULTICLUSTERS table of cexdb."
		 * ); clusterInDb = operator.verifyChangesInDB();
		 * assertTrue(clusterInDb);
		 */
	}

	/**
	 * @DESCRIPTION To verify that a cluster can be deleted.
	 * @PRE CEx is online. Cex client is launched.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-24818_Func_1", title="Delete an Empty Cluster")
	@Context(context = {Context.API})
	@Test(groups={"KGB"})
	public void deleteAnEmptyCluster() throws IOException {

		final IClusterOperator operator = cexProvider.provide(IClusterOperator.class);

		assertEquals(operator.createDeleteEmptyCluster(), "OK");
	}

}
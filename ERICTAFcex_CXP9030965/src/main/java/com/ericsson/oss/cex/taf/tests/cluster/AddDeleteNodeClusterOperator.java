package com.ericsson.oss.cex.taf.tests.cluster;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.database.handler.SybaseDBHandler;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;

/**
 * Operator class to handle add/delete operations of Node Clusters
 * 
 * @author xananan
 *
 */
@Operator(context = Context.API)
public class AddDeleteNodeClusterOperator implements ApiOperator,IClusterOperator {

	private final Logger log = Logger.getLogger(AddDeleteNodeClusterOperator.class);

	private SybaseDBHandler sybaseDBHandler;

	private String clusterId = "";

	private List<String> elementIdList;

	private static final String CLUSTER_GROOVY = "AddModifyDeleteCluster";

	private static final String SUCCESS = "success";

	private static final String DELIMITER_COLON = "::";

	private static final String DELIMITER_HASH = "##";

	@Inject
	private GroovyTestOperators groovyOperator;

	/**
	 * 
	 * @param clusterType
	 * @param noOfRbs
	 * @param noOfERbs
	 * @param noOfRnc
	 * @return elements as String to be added to cluster.
	 */
	public String getElementIds(final String clusterType, final int noOfRbs,
			final int noOfERbs, final int noOfRnc, final int noOfPrbs, final int noOfPerbs) {
		String elements = "";
		elementIdList = new ArrayList<String>();
		if (clusterType.toLowerCase().contains("rnc")) {
			elements = groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "getRncIds", "" + noOfRnc);
			if (elements.contains("NO_RNC")) {
				//elements = null;
				log.error("No RNCs with ClusterIdentity MO exist.");
			} else {
				for (String rncId : elements.split(DELIMITER_COLON)) {
					elementIdList.add(rncId);
				}
			}
		} else {
			elements = groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "getErbsRbsIds", "" + noOfRbs, "" + noOfERbs, "" + noOfPrbs, "" + noOfPerbs);
			boolean noRbs = false;
			String[] elementsArray = elements.split(DELIMITER_HASH);
			if (noOfRbs != 0 && elementsArray[0].contains("NO_RBS")) {
				if (noOfRbs != 0) {
					log.error("No RBSs exist.");
				}
				noRbs = true;
			} else {
				for (String rbsId : elementsArray[0].split(DELIMITER_COLON)) {
					elementIdList.add(rbsId);
				}
			}
			if (elementsArray[1].contains("NO_ERBS")) {
				if (noOfERbs != 0) {
					log.error("No ERBSs exist.");
				}
				if (noRbs) {
					elements = null;
				}
			} else {
				for (String erbsId : elementsArray[1].split(DELIMITER_COLON)) {
					elementIdList.add(erbsId);
				}
			}
		}

		return elements;
	}

	/**
	 * 
	 * @param clusterType
	 * @param elementIds
	 * @return true if cluster create is returns success.
	 */
	public boolean createCluster(final String clusterType,
			final String elementIds) {
		boolean clusterStatus = false;
		String status = groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "createCluster", clusterType, elementIds);
		if (status.equals(SUCCESS)) {
			clusterStatus = true;
		}
		return clusterStatus;
	}

	/**
	 * 
	 * @param clusterType
	 * @param elementIds
	 * @return true if cluster edit is returns success.
	 */
	public boolean editCluster(final String clusterType, final String elementIds) {
		boolean clusterStatus = true;
		String status = groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "editCluster", clusterType, clusterId, elementIds);
		if (status.equals(SUCCESS)) {
			clusterStatus = true;
		}
		return clusterStatus;
	}

	/**
	 * method to call groovy method to delete cluster.
	 */
	public void deleteCluster() {
		groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "deleteCluster", clusterId);
	}

	/**
	 * 
	 * @return true if changes are there in all the tables in cexdb, else return false
	 */
	public boolean verifyChangesInDB() {
		boolean dbResult = true;
		ResultSet resultSet;
		try {
			resultSet = sybaseDBHandler
					.executeQuery("select * from CLUSTER where ID=" + clusterId);
			if (resultSet.getFetchSize() == 0) {
				dbResult = false;
				log.error("Cluster Id: " + clusterId + " does not exist in CLUSTER table of DB.");
			} else {
				resultSet = sybaseDBHandler.executeQuery("select * from FDN where ID=" + clusterId);
				if (resultSet.getFetchSize() == 0) {
					dbResult = false;
					log.error("Cluster Id: " + clusterId + " does not exist in FDN table of DB.");
				} else {
					resultSet = sybaseDBHandler.executeQuery("select ID from MULTICLUSTERS where CLUSTERID=" + clusterId);
					if (resultSet.getFetchSize() > 0) {
						while (resultSet.next()) {
							if (!elementIdList.contains("" + resultSet.getInt(1))) {
								dbResult = false;
								log.error("Cluster Id: " + clusterId + " does not exist in MULTICLUSTERS table of DB.");
							}
						}

						for (String elementId : elementIdList) {
							resultSet = sybaseDBHandler.executeQuery("select * from FDN where ID=" + elementId);
							if (resultSet.getFetchSize() == 0) {
								dbResult = false;
								log.error("Element Id: " + elementId + " does not exist in FDN table of DB.");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dbResult;
	}

	/**
	 * 
	 * @param clusterType
	 * @param elementIds
	 * @return true if cluster create/edit is verified in cex domain, false otherwise.
	 */
	public boolean verifyChangesInDomain(final String clusterType, final String elementIds) {
		boolean domainResult = true;
		int retryCount = 0;
		if (!elementIds.isEmpty()) {
			while (retryCount < 3) {
				clusterId = groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "getClusterId", clusterType);
				if (clusterId != null && !clusterId.equals("")) {
					break;
				}
				waitForTopologyUpdate(5000);
				retryCount ++;
			}
		}
		if (clusterId != null && !clusterId.equals("")) {
			do{
				String result = groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "verifyClusterElementsinDomain", clusterId,	clusterType, elementIds);
				domainResult = new Boolean(result);
				if(domainResult){
					break;
				}
				waitForTopologyUpdate(5000);
				retryCount ++;
			}while (retryCount < 3);
		}
		return domainResult;
	}

	/**
	 * return true if cluster is deleted from cex domain, false otherwise.
	 */
	public boolean verifyClusterDeletedInDomain() {
		boolean domainResult = false;
		int retryCount = 0;
		while (retryCount < 5) {
			final String result = groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "verifyClusterDeletedInDomain", clusterId);
			domainResult = new Boolean(result);
			if (domainResult) {
				break;
			} else {
				waitForTopologyUpdate(1000);
				retryCount++;
			}
		}

		return true;
	}

	public String createDeleteEmptyCluster(){
		
		return  groovyOperator.invokeGroovyMethodOnArgs(CLUSTER_GROOVY, "createDeleteEmptyCluster");
		
	}
	private void waitForTopologyUpdate(final int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception ex) {
			log.info("Exception occured while Topology Update");
		}
	}
}
package com.ericsson.oss.cex.taf.tests.cluster;

public interface IClusterOperator {

	boolean createCluster(String clusterType, String elementIds);

	boolean editCluster(String clusterType, String elementIds);

	void deleteCluster();

	String getElementIds(String clusterType, int noOfRbs, int noOfErbs, int noOfRnc, int noOfPrbs, int noOfPerbs);

	boolean verifyChangesInDB();

	boolean verifyChangesInDomain(String clusterType, String elementIds);

	boolean verifyClusterDeletedInDomain();

	String createDeleteEmptyCluster();
}
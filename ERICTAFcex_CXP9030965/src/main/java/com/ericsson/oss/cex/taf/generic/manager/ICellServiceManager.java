package com.ericsson.oss.cex.taf.generic.manager;

import java.util.List;

public interface ICellServiceManager {

	List<String> getRelations(String sourcecell, String relationType);

	String deleteRelations(String sourcecell, String relationType);
	
	String createRelations(String sourcecell, String targetcell, String relationType);
	
	String getParentRnc(String rbsFdn);
	
	List<String> getAttachedRBS(String rncFdn);
   
	boolean exportRelation();
}

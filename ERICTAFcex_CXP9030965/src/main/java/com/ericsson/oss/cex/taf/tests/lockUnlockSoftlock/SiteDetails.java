package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to store the site details used during lock/unlock operation
 * 
 * @author xananan
 *
 */
public class SiteDetails {
	
	private String siteFdn;
	
	private Map<String, String> cellsDetailsMap;
	
	public SiteDetails(final String siteFdn, final String cellsDetails) {
		this.siteFdn = siteFdn;
		cellsDetailsMap = new HashMap<String, String>();
		createCellDetailsMap(cellsDetails);
	}
	
	public String getSiteFdn() {
		return this.siteFdn;
	}
	
	public Map<String, String> getCellsDetailsMap() {
		return this.cellsDetailsMap;
	}
	
	private void createCellDetailsMap(final String cellsDetails){
		String [] cellDetailsArray;
		for(String cellDetails : cellsDetails.split(":")) {
			cellDetailsArray = cellDetails.split("@");
			cellsDetailsMap.put(cellDetailsArray[0], cellDetailsArray[1]);
		}
	}
	
}

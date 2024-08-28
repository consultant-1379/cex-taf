package com.ericsson.oss.cex.taf.generic.manager;

import java.util.List;

public interface ITopologyManager {

	String getUtranCell(String cellType);

	String getEUtranCell(String cellType, String moType);

	List<String> getRbsList();

	List<String> getPico_RbsList();

	List<String> getErbsList();

	List<String> getPico_ErbsList();

	List<String> getRncList();

	List<String> getExternalPlmnFreqList(String extPlmnType);

	List<String> getMgwList();
	
	String getSsr_Erbs();
	
	String getErbs();

	String getRbs();

	String getFilteredEUtranCell(String cellType, String moType, String neMIMversion);
	
	String getExternalPlmnFreq(String extPlmnType);

	List<String> getExternalPlmnCellList(String extPlmnType);

	String getExternalPlmnCell(String extPlmnType);

}

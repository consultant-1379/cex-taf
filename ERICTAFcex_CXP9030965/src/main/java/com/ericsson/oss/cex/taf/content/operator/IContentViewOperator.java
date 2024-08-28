package com.ericsson.oss.cex.taf.content.operator;

import java.util.List;

public interface IContentViewOperator {

	List<String> getFDNListfromCS(String externalUtranFreq);

	List<String> getFDNPropertiesListfromCS(String fdn,String properties);

	String viewLteEnodeBPage();

	String viewRBSProperties();

	String viewRNCProperties();

}

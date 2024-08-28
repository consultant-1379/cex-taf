package com.ericsson.oss.cex.taf.tests.pcp;

public interface IPcpProfileOperator {
	
	public String exportProfile();
	public String importProfile();
	
	public String PCPProfileCLI(String operation);
//	public String findProfile();
//	public String findAllProfile();
//	public String compareProfile();
	public String delProfile();
	public String createCexProfilePCP(String name, String desc);
	public String verifyCexProfilePCP(String name);
	public String editCexProfilePCP(String name);
	
}

package com.ericsson.oss.cex.taf.bcg;

public interface IBCGImportOperator {
	
	String transferBcgXmlToServer(String filesToTransfer[], String importFolderPath);
	
	String transferPrerequisitesToServer(String filesToTransfer[], String importFolderPath);
	
	String permissions(String Path);
	
	String executePrerequisites(String files[], String importFolderPath, String exceuteFilesCommand);
	
	boolean bcgImport(String testcase);
	
	boolean UtranCellIUB();
	
	boolean EmergencyID_dg1();
	
	boolean EmergencyID_dg2();
	
	boolean EmergencyID_pico();
	
	boolean Cell_Relation_DG1();
	
	boolean Cell_Relation_DG2();
	
	boolean EutranFreqRelation_dg1();
	
	boolean EutranFreqRelation_dg2();
	
//	boolean BandIndicatorBcchFreq();
	
}

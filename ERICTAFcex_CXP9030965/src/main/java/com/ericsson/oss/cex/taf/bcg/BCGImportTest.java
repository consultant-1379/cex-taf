package com.ericsson.oss.cex.taf.bcg;

import javax.inject.Inject;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;



@SuppressWarnings("deprecation")
public class BCGImportTest extends TorTestCaseHelper implements TestCase{


	@Inject
	private OperatorRegistry<IBCGImportOperator> cexProvider;
	
	String importFolderPath = "/var/opt/ericsson/nms_umts_wran_bcg/files/import/";
	
	String xmlfiles[] = {"UtranCellIUB.xml","dg1freqrelation.xml","emergencyIDdg1.xml","emergencyIDdg2.xml","emergencyIDpico.xml"};
	String PrerequisiteFiles[] = {"prerq_UtranCellIUB.txt","prerq_UtranCellIUB_plan.txt","prerq_EutranFreqRelationDG1.txt","prerq_EutranFreqRelationDG1_plan.txt"};
	
	String exceuteFilesCommand = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS -file ";

	/**
	 * @DESCRIPTION Adding OptionalFeatureLicense with the help of Netsim API
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
//	@BeforeTest
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void prerequisites() {

		final IBCGImportOperator operator = cexProvider.provide(IBCGImportOperator.class);
		setTestcase("Test_id_1", "Transferring File ");

		setTestStep("Permissions to Import Folder...");
		String result = operator.permissions(importFolderPath);

		setTestStep("Transferring Xml Files...");
//		String result1 = operator.transferBcgXmlToServer(xmlfiles, importFolderPath);
		
	//	setTestStep("Transferring Prerequisite Files...");
	//	String result4 = operator.transferPrerequisitesToServer(PrerequisiteFiles, importFolderPath);
		
	//	setTestStep("Permissions to XML Files");
	//	String result2 = operator.permissions(importFolderPath + "*");
		
	//	setTestStep("Executing Prerequisite Files...");

	//	String result3 = operator.executePrerequisites(PrerequisiteFiles, importFolderPath, exceuteFilesCommand);
	}	

	/**
	 * @DESCRIPTION Verify successful Import and Activation of BCG XMLs
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */

	@DataDriven(name = "BCGImportXML")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void bcgImport(
			@TestId @Input("TestID") String testId,
			@Input("Testcase") String testcase){

		setTestcase(testId, " BCG "+testcase);
		final IBCGImportOperator operator = cexProvider.provide(IBCGImportOperator.class);
		assertTrue(operator.bcgImport(testcase));

		

	}

}

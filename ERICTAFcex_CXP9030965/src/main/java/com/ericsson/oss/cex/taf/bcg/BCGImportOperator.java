package com.ericsson.oss.cex.taf.bcg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.cifwk.taf.tools.cli.handlers.impl.RemoteObjectHandler;

@Operator(context = Context.API)
@SuppressWarnings("deprecation")
public class BCGImportOperator implements IBCGImportOperator {

	private static Logger log = Logger.getLogger(CexCsHandler.class);

	@Inject
	private GroovyTestOperators groovyOperator;

	private static String GROOVY_FILE = "TopologyManagerFactory";
	private static String GROOVY_OPERATOR = "CellManager";
	private String Shipment = null;

	private static CexRemoteCommandExecutor preCheckSshRemoteCommandExecutor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	private static CexRemoteCommandExecutor rootRemoteExecutor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getRootHostMaster());
	RemoteObjectHandler remoteFilesHandler = new RemoteObjectHandler(CexApiGetter.getHostMaster());
	CexCsHandler cexCsHandler = new CexCsHandler();



	public String permissions(String Path)
	{
		String fullPermissions = "chmod 777 " + Path;
		//		log.info(fullPermissions);

		String permissionToFolderResult = preCheckSshRemoteCommandExecutor.simplExec(fullPermissions);
		log.info(permissionToFolderResult);

		return null;
	}

	public String executePrerequisites(String files[], String importFolderPath, String exceuteFilesCommand){

		for(int i=0;i<files.length;i++)
		{
			log.info(rootRemoteExecutor.simplExec(exceuteFilesCommand + importFolderPath + files[i]));

		}

		return null;
	}
	
	public String transferPrerequisitesToServer(String filesToTransfer[], String importFolderPath)
	{
		for(String file: filesToTransfer){
			final List<String> localFilePath = FileFinder.findFile(file, "data");
			remoteFilesHandler.copyLocalFileToRemote(localFilePath.get(0), importFolderPath);
		}

		return null;
	}

	public String transferBcgXmlToServer(String filesToTransfer[], String importFolderPath){
		
		log.info(preCheckSshRemoteCommandExecutor.simplExec("rm -rf " + importFolderPath + "*"));
		Shipment = preCheckSshRemoteCommandExecutor.simplExec("ist_run -v");
		log.info(Shipment);
		int index=Shipment.lastIndexOf("t_");
		Shipment = Shipment.substring(index+2,index+8);
		log.info(Shipment);
		index=Shipment.lastIndexOf('.');
		Shipment = (Shipment.substring(0,index)+Shipment.substring(index+1));
		log.info(Shipment);
		for(String file: filesToTransfer){
			final List<String> localFilePath = FileFinder.findFile(file, "data");
			try{
				StringBuffer sb = new StringBuffer();
				File xmlFile = new File(localFilePath.get(0));
				BufferedReader br = new BufferedReader(new FileReader(xmlFile));
				String TextToReplace = null;
				for(int i=1;i<=2;i++){
					if((TextToReplace = br.readLine())!= null){
						if(TextToReplace.contains(".xsd")){
							index = TextToReplace.indexOf(".xsd");
							TextToReplace = TextToReplace.substring(index-5,index);
						}
					}
				}
				String line = null;
				BufferedReader br1 = new BufferedReader(new FileReader(xmlFile));
				while((line = br1.readLine())!= null)
				{
					if(line.indexOf(TextToReplace) != -1)
					{
						line = line.replaceAll(TextToReplace,Shipment);
					}         
					sb.append(line+"\n");                
				}
				br1.close();

				BufferedWriter bw = new BufferedWriter(new FileWriter(xmlFile));
				bw.write(sb.toString());
				bw.close();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			remoteFilesHandler.copyLocalFileToRemote(localFilePath.get(0), importFolderPath);
		}

		return null;
	}

	public boolean bcgImport(String testcase)
	{

		switch(testcase){
		case "UtranCellIUB":
			return UtranCellIUB();

		case "EmergencyID_dg1":
			return EmergencyID_dg1();


		case "EmergencyID_dg2":
			return EmergencyID_dg2();


		case "EmergencyID_pico":
			return EmergencyID_pico();


		case "Cell_Relation_DG1":
			return Cell_Relation_DG1();

		case "Cell_Relation_DG2":
			return Cell_Relation_DG2();


		case "EutranFreqRelation_dg1":
			return EutranFreqRelation_dg1();

		case "EutranFreqRelation_dg2":
			return EutranFreqRelation_dg2();

			//		case "BandIndicatorBcchFreq":
			//			return BandIndicatorBcchFreq();

		default: return false;
		}
	}


	public boolean UtranCellIUB() {

		String CellList = null;
		String planName = "taf_plan1";
		Boolean bool = true;
		String rncFdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=RNC04,MeContext=RNC04";

		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i UtranCellIUB.xml -p "
				+ planName + " -as plan";
		log.info(fileImportCommand);

		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
		log.info(fileImportCommandResult);
		if (fileImportCommandResult.contains("failed")
				|| fileImportCommandResult.contains("partially succeeded")) {
			bool = false;
			//return false;
		} else{

			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
					+ planName;
			log.info(fileActivationCommand);

			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
			log.info(fileActivationCommandResult);
			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
				bool = false;
				//return false;
			}
			else {
				String Rbsfdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=RNC04,MeContext=RNC04RBS10";
				String Cellfdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=RNC04,MeContext=RNC04,ManagedElement=1,RncFunction=1,UtranCell=U2201";

				CellList = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_FILE, "getFilteredUtranCell", Rbsfdn);
				log.info(CellList);
				if(CellList.contains(Cellfdn))
					bool = true;

			} 
		}
		cexCsHandler.deletePlan(planName);


		return true;


	}

	public boolean EmergencyID_dg1() {

		String planName = "taf_plan1";
		Boolean bool = true;

		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i emergencyIDdg1.xml -p "
				+ planName + " -as plan";
		log.info(fileImportCommand);

		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
		log.info(fileImportCommandResult);
		if (fileImportCommandResult.contains("failed")
				|| fileImportCommandResult.contains("partially succeeded")) {
			bool = false;
		}
		else{

			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
					+ planName;
			log.info(fileActivationCommand);

			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
			log.info(fileActivationCommandResult);
			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
				bool = false;
			}
			else{
				String propertyName = "emergencyAreaId ";
				String requiredFdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=ERBS-SUBNW-1,MeContext=LTE01ERBS00001,ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE01ERBS00001-1";
				
				String property = groovyOperator.invokeGroovyMethodOnArgs("CellManager", "getMoProperties",  requiredFdn,  propertyName,  "null");
				log.info(property);
				if(!(property.contains("2")))
				{
					bool = false;
				}
				
			}
		}
		cexCsHandler.deletePlan(planName);
		return true;
	}

	public boolean EmergencyID_dg2() {

		String planName = "taf_plan1";
		Boolean bool = true;

		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i emergencyIDdg2.xml -p "
				+ planName + " -as plan";
		log.info(fileImportCommand);

		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
		log.info(fileImportCommandResult);
		if (fileImportCommandResult.contains("failed")
				|| fileImportCommandResult.contains("partially succeeded")) {
			bool = false;
		}else{

			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
					+ planName;
			log.info(fileActivationCommand);

			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
			log.info(fileActivationCommandResult);
			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
				bool = false;
			}else{
				String propertyName = "emergencyAreaId ";
				String requiredFdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=LTE,MeContext=LTE11dg2ERBS00001,ManagedElement=LTE11dg2ERBS00001,ENodeBFunction=1,EUtranCellFDD=LTE11dg2ERBS00001-1";
				String property = groovyOperator.invokeGroovyMethodOnArgs("CellManager", "getMoProperties",  requiredFdn,  propertyName,  "null");
				log.info(property);
				if(!(property.contains("2")))
				{
					bool = false;
				}
			}
		}
		cexCsHandler.deletePlan(planName);
		return true;
	}

	public boolean EmergencyID_pico() {

		String planName = "taf_plan1";
		Boolean bool = true;
		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i emergencyIDpico.xml -p "
				+ planName + " -as plan";
		log.info(fileImportCommand);

		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
		log.info(fileImportCommandResult);
		if (fileImportCommandResult.contains("failed")
				|| fileImportCommandResult.contains("partially succeeded")) {
			bool = false;
		}
		else{

			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
					+ planName;
			log.info(fileActivationCommand);

			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
			log.info(fileActivationCommandResult);
			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
				bool = false;
			}
			else{
				String propertyName = "emergencyAreaId ";
				String requiredFdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=LTE,MeContext=LTE47pERBS00001,ManagedElement=LTE47pERBS00001,ENodeBFunction=1,EUtranCellFDD=LTE47pERBS00001-1";
				String property = groovyOperator.invokeGroovyMethodOnArgs("CellManager", "getMoProperties",  requiredFdn,  propertyName,  "null");
				log.info(property);
				if(!(property.contains("0")))
				{
					bool = false;
				}
			}
		}
		cexCsHandler.deletePlan(planName);
		return true;
	}

	public boolean Cell_Relation_DG1() {

		String planName = "taf_plan1";
		Boolean bool = true;
		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i eutrancellrelationDG1.xml -p "
				+ planName + " -as plan";
		log.info(fileImportCommand);

		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
		log.info(fileImportCommandResult);
		if (fileImportCommandResult.contains("failed")
				|| fileImportCommandResult.contains("partially succeeded")) {
			bool = false;
		}
		else{

			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
					+ planName;
			log.info(fileActivationCommand);

			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
			log.info(fileActivationCommandResult);
			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
				bool = false;
			}
			else{


				String fdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=ERBS-SUBNW-1,MeContext=LTE01ERBS00001,ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE01ERBS00001-2,EUtranFreqRelation=new1,EUtranCellRelation=test1";
				String fdnCheckResult  = cexCsHandler.getChildMos(fdn).toString();
				log.info(fdnCheckResult);
				if(fdnCheckResult.contains("NotExisting"))
				{
					bool = false;
				}
			}
		}

		cexCsHandler.deletePlan(planName);
		return true;
	}

	public boolean Cell_Relation_DG2() {

		String planName = "taf_plan1";
		Boolean bool = true;
		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i eutrancellrelationDG2.xml -p "
				+ planName + " -as plan";
		log.info(fileImportCommand);

		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
		log.info(fileImportCommandResult);
		if (fileImportCommandResult.contains("failed")
				|| fileImportCommandResult.contains("partially succeeded")) {
			bool = false;
		}
		else{

			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
					+ planName;
			log.info(fileActivationCommand);

			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
			log.info(fileActivationCommandResult);
			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
				bool = false;
			}
			else{
				String fdn = "";
				String fdnCheckResult  = cexCsHandler.getChildMos(fdn).toString();
				log.info(fdnCheckResult);
				if(fdnCheckResult.contains("NotExisting"))
				{
					bool = false;
				}
			}
		}
		cexCsHandler.deletePlan(planName);
		return true;
	}

	public boolean EutranFreqRelation_dg1() {

		String planName = "taf_plan1";
		Boolean bool = true;
		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i dg1freqrelation.xml -p "
				+ planName + " -as plan";
		log.info(fileImportCommand);

		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
		log.info(fileImportCommandResult);
		if (fileImportCommandResult.contains("failed")
				|| fileImportCommandResult.contains("partially succeeded")) {
			bool = false;
		}
		else{

			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
					+ planName;
			log.info(fileActivationCommand);

			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
			log.info(fileActivationCommandResult);
			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
				bool = false;
			}
			else{
				String Relationfdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=ERBS-SUBNW-1,MeContext=LTE01ERBS00001,ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE01ERBS00001-1,EUtranFreqRelation=new";
				String Cellfdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=ERBS-SUBNW-1,MeContext=LTE01ERBS00001,ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE01ERBS00001-1";
				String relationtype = "EUtranFreqRelation";
				log.info(Cellfdn);
				String RelationList = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_OPERATOR, "getRelations", Cellfdn, relationtype);

				if(RelationList.contains(Relationfdn))
					bool = true;
			}

		}
		cexCsHandler.deletePlan(planName);
		return true;
	}

	public boolean EutranFreqRelation_dg2() {

		String planName = "taf_plan1";
		Boolean bool = true;
		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i dg2freqrelation.xml -p "
				+ planName + " -as plan";
		log.info(fileImportCommand);

		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
		log.info(fileImportCommandResult);
		if (fileImportCommandResult.contains("failed")
				|| fileImportCommandResult.contains("partially succeeded")) {
			bool = false;
		}
		else{

			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
					+ planName;
			log.info(fileActivationCommand);

			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
			log.info(fileActivationCommandResult);
			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
				bool = false;
			}
			else{
				String fdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=LTE,MeContext=LTE11dg2ERBS00001,ManagedElement=LTE11dg2ERBS00001,ENodeBFunction=1,EUtranCellFDD=LTE11dg2ERBS00001-1,EUtranFreqRelation=new12";
				String fdnCheckResult  = cexCsHandler.getChildMos(fdn).toString();
				log.info(fdnCheckResult);
				if(fdnCheckResult.contains("NotExisting"))
				{
					bool = false;
				}
			}
		}

		cexCsHandler.deletePlan(planName);
		return true;
	}

	//	public boolean BandIndicatorBcchFreq() {
	//
	//		String planName = "taf_plan1";
	//		Boolean bool = true;
	//		String fileImportCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -i one.xml -p "
	//				+ planName + " -as plan";
	//		log.info(fileImportCommand);
	//
	//		String fileImportCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileImportCommand);
	//		log.info(fileImportCommandResult);
	//		if (fileImportCommandResult.contains("failed")
	//				|| fileImportCommandResult.contains("partially succeeded")) {
	//			bool = false;
	//		}
	//		else{
	//
	//			String fileActivationCommand = "/opt/ericsson/nms_umts_wran_bcg/bin/bcgtool.sh -a "
	//					+ planName;
	//			log.info(fileActivationCommand);
	//
	//			String fileActivationCommandResult = preCheckSshRemoteCommandExecutor.simplExec(fileActivationCommand);
	//			log.info(fileActivationCommandResult);
	//			if (fileActivationCommandResult.contains("FAILED_PRECHECK") || fileActivationCommandResult.contains("FAILED")) {
	//				bool = false;
	//			}
	//			else{
	//				//		String fdn = "SubNetwork=ONRM_ROOT_MO_R,SubNetwork=RNC04,MeContext=RNC04,ManagedElement=1,RncFunction=1,UtranCell=U2";
	//				//		String fdnCheckResult  = cexCsHandler.getChildMos(fdn).toString();
	//				//		log.info(fdnCheckResult);
	//				//		if(fdnCheckResult.contains("NotExisting"))
	//				//		{
	//				//			return false;
	//				//		}
	//			}
	//		}
	//		cexCsHandler.deletePlan(planName);
	//		return bool;
	//	}
	//
}



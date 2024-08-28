import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.ericsson.oss.domain.ConfigurationData;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.domain.modetails.LTEEUtranFreqRelation;
import com.ericsson.oss.domain.modetails.EutranFreqRelation;
import com.ericsson.oss.domain.modetails.EUtranCellRelation;
import com.ericsson.oss.domain.modetails.UtranFreqRelation;
import com.ericsson.oss.domain.modetails.UtranTDDFreqRelation;
import com.ericsson.oss.domain.modetails.Cdma20001xRttFreqRelation;
import com.ericsson.oss.domain.modetails.Cdma20001xRttCellRelation;
import com.ericsson.oss.domain.modetails.RNC_NODE_MODEL.GsmRelation;
import com.ericsson.oss.domain.modetails.CellRelation;
import com.ericsson.oss.domain.Direction;
import com.ericsson.oss.domain.modetails.Cdma2000CellRelation;
import com.ericsson.oss.domain.modetails.Cdma2000FreqBandRelation;
import com.ericsson.oss.domain.modetails.Cdma20001xRttBandRelation;
import com.ericsson.oss.domain.modetails.Cdma2000FreqRelation;
import com.ericsson.oss.domain.modetails.GeranFreqGroupRelation;
import com.ericsson.oss.cex.cellservice.service.impl.ExportRelationMessageJobInfo;

import java.util.concurrent.Executors;

import com.ericsson.oss.domain.modetails.IManagedObject;

class CellManager {

	List<String> filteredList;
	List<String> sourceCellList ;
	List<String> targetCellList ;
	MessageJobInfo job;


	public def getRelationFdn(String sourcecell, String relationType,String targetcell){


		ConfigurationData configData = new ConfigurationData(null, "nmsadm");  //Plan Name is null
		MessageJobInfo<List> relationsList ;
		filteredList = new ArrayList<String>();
		String adjacentRelation = null;
		String targetCellFdn = null;
		switch(relationType){
			case("UtranRelation"):

				try
				{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranRelation.name(), configData);
					checkJobFinished(relationsList);
					for(CellRelation relation : relationsList.getList()){

						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							return adjacentRelation;

						}
					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("GsmRelation"):

				try
				{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.GsmRelation.name(), configData);
					checkJobFinished(relationsList);
					for(CellRelation relation : relationsList.getList()){

						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							return adjacentRelation;

						}
					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("UtranCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranCellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(CellRelation relation : relationsList.getList()){

						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;
						}
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EutranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EutranFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(EutranFreqRelation relation : relationsList.getList()){

						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;
						}
					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;

			case("UtranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(UtranFreqRelation relation : relationsList.getList()){

						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;
						}
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("UtranTDDFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranTDDFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(UtranTDDFreqRelation relation : relationsList.getList()){

						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;

						}
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
			case("EUtranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EUtranFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(LTEEUtranFreqRelation relation : relationsList.getList()){

						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;

						}
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
			case("EUtranCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EUtranCellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(EUtranCellRelation relation : relationsList.getList()){

						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;

						}

					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma2000CellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma2000CellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma2000CellRelation relation : relationsList.getList()){
						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;
						}
					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma2000FreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma2000FreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma2000FreqRelation relation : relationsList.getList()){
						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;

						}
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma20001xRttFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma20001xRttFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma20001xRttFreqRelation relation : relationsList.getList()){
						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;

						}
					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma20001xRttCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma20001xRttCellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma20001xRttCellRelation relation : relationsList.getList()){
						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;

						}
					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("GeranFreqGroupRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.GeranFreqGroupRelation.name(), configData);
					checkJobFinished(relationsList);
					for(GeranFreqGroupRelation relation : relationsList.getList()){
						targetCellFdn = relation.getTargetCellFdn().toString();

						if (targetCellFdn.equals(targetcell)){
							adjacentRelation = relation.toString();
							break;
						}
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;

			case("GeranCellRelation"):
                                try{
                                        relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.GeranCellRelation.name(), configData);
                                        checkJobFinished(relationsList);
                                        for(CellRelation relation : relationsList.getList()){
                                                targetCellFdn = relation.getTargetCellFdn().toString();

                                                if (targetCellFdn.equals(targetcell)){
                                                        adjacentRelation = relation.toString();
                                                        break;
                                                }
                                        }
                                }
                                catch(Exception e){
                                        return e.getMessage().toString();
                                }
                                break;

			default:
				break;
		}

		return adjacentRelation;


	}
	public def deleteAdjacentAtrribute(String adjacentRelation){

		GenericItem genericItem = new GenericItem(adjacentRelation);
		IGenericItem[] genericItemArray = new IGenericItem[1];
		genericItemArray[0] = genericItem;
		MessageJobInfo deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);
		checkJobFinished(deleteRelationJob);

		if (deleteRelationJob.getState() == JobState.FAILED){
			return "failed" ;
		}

		return "OK";

	}

	public def getRelations(String sourcecell, String relationType){

		ConfigurationData configData = new ConfigurationData(null, "nmsadm");  //Plan Name is null
		MessageJobInfo<List> relationsList ;
		filteredList = new ArrayList<String>();

		switch(relationType){
			case("UtranRelation"):

				try
				{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranRelation.name(), configData);
					checkJobFinished(relationsList);
					for(CellRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("GsmRelation"):

				try
				{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.GsmRelation.name(), configData);
					checkJobFinished(relationsList);
					for(CellRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());

					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;

			case("UtranCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranCellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(CellRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EUtranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EUtranFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(LTEEUtranFreqRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EutranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EutranFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(EutranFreqRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;

			case("UtranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(UtranFreqRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EUtranCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EUtranCellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(EUtranCellRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EutranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EutranFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(EutranFreqRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma2000CellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma2000CellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma2000CellRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma2000FreqBandRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma2000FreqBandRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma2000FreqBandRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma20001xRttBandRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma20001xRttBandRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma20001xRttBandRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma2000FreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma2000FreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma2000FreqRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma20001xRttFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma20001xRttFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma20001xRttFreqRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma20001xRttCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.Cdma20001xRttCellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(Cdma20001xRttCellRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("UtranTDDFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranTDDFreqRelation.name(), configData);
					checkJobFinished(relationsList);
					for(UtranTDDFreqRelation relations : relationsList.getList()){

						filteredList.add(relations.getFdn().toString());
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
			case("GeranFreqGroupRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.GeranFreqGroupRelation.name(), configData);
					checkJobFinished(relationsList);
					for(GeranFreqGroupRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("GeranCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.GeranCellRelation.name(), configData);
					checkJobFinished(relationsList);
					for(CellRelation relations : relationsList.getList()){
						filteredList.add(relations.getFdn().toString());
					}
					break;
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			default:
				break;
		}
		return filteredList;
	}

	public def deleteRelations(String sourcecell, String relationType){

		filteredList = new ArrayList<String>();
		ConfigurationData configData = new ConfigurationData(null, "nmsadm");  //Plan Name is null
		MessageJobInfo<List> relationsList;

		filteredList = getRelations(sourcecell, relationType);

		int count = 0;

		if(filteredList.size() > 0){

			for(String relation : filteredList){

				GenericItem genericItem = new GenericItem(relation);
				IGenericItem[] genericItemArray = new IGenericItem[1];
				genericItemArray[0] = genericItem;
				MessageJobInfo deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);


				checkJobFinished(deleteRelationJob);
				if (deleteRelationJob.getState() == JobState.FINISHED){
					count++;
					//return deleteRelationJob.getAdditionalInfo().toString();
				}
			}
		}
		return "Deleted count="+count;
	}
	public def createRelations(String sourcecell, String targetcell, String relationType){

		sourceCellList =  new ArrayList<String>();
		targetCellList =  new ArrayList<String>();

		sourceCellList.add(sourcecell);
		targetCellList.add(targetcell);

		switch(relationType){

			case("UtranRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.UtranRelation, Direction.UNIDIRECTIONAL, sourceCellList, targetCellList,  null, null, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("GsmRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.GsmRelation, Direction.UNIDIRECTIONAL, sourceCellList, targetCellList,  null, null, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("UtranTDDFreqRelation"):
				try
				{
					job = CellServiceManager.getInstance().createFrequencyRelations(
							RelationType.UtranTDDFreqRelation, sourceCellList, targetCellList, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EUtranFreqRelation"):
				try
				{
					job = CellServiceManager.getInstance().createFrequencyRelations(
							RelationType.EUtranFreqRelation, sourceCellList, targetCellList, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EutranFreqRelation"):
				try
				{
					job = CellServiceManager.getInstance().createFrequencyRelations(
							RelationType.EutranFreqRelation, sourceCellList, targetCellList, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;

			case("UtranFreqRelation"):
				try
				{
					job = CellServiceManager.getInstance().createFrequencyRelations(
							RelationType.UtranFreqRelation, sourceCellList, targetCellList, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma20001xRttFreqRelation"):
				try
				{
					job = CellServiceManager.getInstance().createFrequencyRelations(
							RelationType.Cdma20001xRttFreqRelation, sourceCellList, targetCellList, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma2000FreqRelation"):
				try
				{
					job = CellServiceManager.getInstance().createFrequencyRelations(
							RelationType.Cdma2000FreqRelation, sourceCellList, targetCellList, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EUtranCellRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.EUtranCellRelation, Direction.UNIDIRECTIONAL, sourceCellList, targetCellList,  null, null, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("UtranCellRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.UtranCellRelation, Direction.UNIDIRECTIONAL, sourceCellList, targetCellList,  null, null, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma2000CellRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.Cdma2000CellRelation, Direction.UNIDIRECTIONAL, sourceCellList, targetCellList,  null, null, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("Cdma20001xRttCellRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.Cdma20001xRttCellRelation, Direction.UNIDIRECTIONAL, sourceCellList, targetCellList,  null, null, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("GeranFreqGroupRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.GeranFreqGroupRelation, Direction.UNIDIRECTIONAL, sourceCellList, targetCellList,  null, null, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("GeranCellRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.GeranCellRelation, Direction.UNIDIRECTIONAL, sourceCellList, targetCellList,  null, null, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;

		}
		checkJobFinished(job);
		if (job.getState() == JobState.FAILED){
			return job.getAdditionalInfo().toString();
		}
		return "OK";
	}

	public def getMoProperties(String  requiredFdn, String propertyName, String planName){

		Map<String, String> properties = new HashMap<String, String>();

		planName = null;

		final List<IManagedObject> list = CmManager.getInstance().getMOProperties(requiredFdn, planName);

		IManagedObject mo = list.get(0);

		String[] propertyArray = propertyName.trim().split(" ");

		for(String property : propertyArray){

			properties.put(property, mo.getAttributeValue(property).toString());
		}

		return properties;
	}

	public def getParentRNC(String rbsFdn){

		return CmManager.getInstance().getParentRNC(rbsFdn);
	}

	public def getAttachedRBS(String rncFdn){

		return CmManager.getInstance().getAttachedRBS(rncFdn);
	}

	public def exportRelation(String rbsfdn){

		List<String> fdns = new ArrayList<String>();
		List<String> relationTypes = new ArrayList<String>();
		String fileLocation="/home/nmsadm/rbsexport";
		ConfigurationData configurationData= new ConfigurationData();
		final ExecutorService ex = Executors.newFixedThreadPool(1);

		ExportRelationMessageJobInfo job;
		fdns.add(rbsfdn);
		relationTypes.add("GsmRelation");
		configurationData.setUserName("nmsadm");
		configurationData.setPlanName(null);

		try{
			job  = CellServiceManager.getInstance().exportRelations(fdns, relationTypes,fileLocation, configurationData);
			checkJobFinished(job);

			if(job.getAdditionalInfo().toString().contains("File already exists: /home/nmsadm/rbsexport")){
				return "OK";
			}

			int retrycount = 0;
			while (!job.isCompleted() && retrycount < 10){
				sleep(1000);
				retrycount ++;
			}

			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
		return "OK";
	}

	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}

}

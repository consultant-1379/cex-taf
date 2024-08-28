
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.Direction;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.FDN;
import com.ericsson.oss.domain.ConfigurationData;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem
import com.ericsson.oss.domain.UtranCellRelations;
import com.ericsson.oss.domain.ExternalUtranCell;
import com.ericsson.oss.cex.topology.service.impl.TopologyServiceFactory;
import com.ericsson.oss.domain.ExternalUtranPlmn;
import com.ericsson.oss.domain.ExternalPlmn;
import com.ericsson.oss.domain.modetails.ManagedObject;
import com.ericsson.oss.domain.ExternalUtranRnc;
import com.ericsson.oss.domain.ExternalUtranFreq;
import com.ericsson.oss.domain.EUtranCellFDD;
import com.ericsson.oss.domain.EUtranCellTDD;
import com.ericsson.oss.domain.ExternalEutranFrequency;
import ca.odell.glazedlists.EventList;


class UtranRelationsOperator{

	private ICMService cmService;
	final List<String> sourceUtranCell = new ArrayList<String>();
	final List<String> targetUtranCell = new ArrayList<String>();

	final List<String> sourceEUtranFreqCell = new ArrayList<String>();
	final List<String> targetEUtranCell = new ArrayList<String>();

	final List<String> sourceCellGsm = new ArrayList<String>();
	final List<String> targetCellGsm = new ArrayList<String>();

	final List<String> sourceCellEutran = new ArrayList<String>();
	final List<String> targetCellEutran = new ArrayList<String>();

	final List<String> sourceCellutranFreq = new ArrayList<String>();
	final List<String> targetCellutranFreq = new ArrayList<String>();

	final List<String> sourceCellEUtran = new ArrayList<String>();
	final List<String> targetCellExternalCdma2000Freq = new ArrayList<String>();

	final List<String> cellfdn = new ArrayList<String>();
	final List<String> cdma2000freqband = new ArrayList<String>();

	final List<String> coveragesourcefdn = new ArrayList<String>();
	final List<String> coveragetargetfdn = new ArrayList<String>();

	final List<String> coveragesourceRnc1 = new ArrayList<String>();
	final List<String> coveragetargetRnc2= new ArrayList<String>();
	protected HashSet<Object> dirtyAttributesSet = new HashSet<Object>();
	protected Map<String,Object> attributesMap = new HashMap<String,Object>();
	MessageJobInfo job;
	List<String> sourceList = new ArrayList<String>();
	List<String> targetList = new ArrayList<String>();
	List<String> sourceCellList = new ArrayList<String>();
	List<String> targetCellList = new ArrayList<String>();
	List<RBS> rbsList;
	List<ERbs> erbsList;
	String sourcecell = null;
	String targetcell = null;
	Random randomGenerator = new Random();

	public List<String> getUtrancellMacroList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
		} else{
			return null;
		}
		List<String> macroCellList = new ArrayList<String>();

		for(RBS rbs : rbsList){
			if(!rbs.isPicoRbs() && !rbs.isSsrRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0){
				for(UtranCell utrancell : rbs.getUtranCellList()){
					macroCellList.add(utrancell.getFdn().toString());
				}
			}
		}
		return macroCellList;
	}
	public List<String> getUtrancellPicoList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
		} else{
			return null;
		}
		List<String> picoCellList = new ArrayList<String>();
		for(RBS rbs : rbsList){
			if(rbs.isPicoRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0){
				for(UtranCell utrancell : rbs.getUtranCellList()){
					picoCellList.add(utrancell.getFdn().toString());
				}
			}
		}
		return picoCellList;
	}

	public List<String> getEUtrancellMacroList(String elementType){

		String MO_TYPE = "EUtranCellFDD";
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
		} else{
			return null;
		}
		if(elementType.contains("EUtranCellTDD-MACRO")){
			MO_TYPE = "EUtranCellTDD";
		}
		List<String> macroCellList = new ArrayList<String>();
		Set<ERbs> erbsSet = new HashSet<ERbs>(erbsList);
		for (ERbs erbs : erbsSet) {
			if(!erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.isConnected() && erbs.getCellList().size() > 0){
				for (EUtranCell cell : erbs.getCellList()) {
					if(cell.getType().toString().equals(MO_TYPE)){
						macroCellList.add(cell.getFdn().toString());
					}
				}
			}
		}
		return macroCellList;
	}
	public List<String> getEUtrancellPicoList(String elementType){

		String MO_TYPE = "EUtranCellFDD";
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
		} else{
			return null;
		}
		List<String> picoCellList = new ArrayList<String>();
		Set<ERbs> erbsSet = new HashSet<ERbs>(erbsList);
		for (ERbs erbs : erbsSet) {
			if(erbs.isPicoRbs() && erbs.isConnected() && erbs.getCellList().size() > 0){
				for (EUtranCell cell : erbs.getCellList()) {
					if(cell.getType().toString().equals(MO_TYPE)){
						picoCellList.add(cell.getFdn().toString());
					}
				}
			}
		}
		return picoCellList;
	}
	public def getSouceCellList(String elementSourceType){
		try{

			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();

			switch(elementSourceType){
				case("UtranCell-MACRO"):
					sourceList = getUtrancellMacroList();
					break;
				case("UtranCell-PICO"):
					sourceList = getUtrancellPicoList();
					break;
				case("EUtranCellFDD-MACRO"):
					sourceList = getEUtrancellMacroList("EUtranCellFDD-MACRO");
					break;
				case("EUtranCellFDD-PICO"):
					sourceList = getEUtrancellPicoList("EUtranCellFDD-PICO");
					break;
				case("EUtranCellTDD-MACRO"):
					sourceList = getEUtrancellMacroList("EUtranCellTDD-MACRO");
					break;
			}
			if(!(sourceList.isEmpty() && sourceList.size() > 1)){
				return true;
			}
			return null;
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}
	public def getTargetCellList(String elementTargetType){

		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();

			switch(elementTargetType){
				case("UtranCell-MACRO"):
					targetList = getUtrancellMacroList();
					break;
				case("UtranCell-PICO"):
					targetList = getUtrancellPicoList();
					break;
				case("EUtranCellFDD-MACRO"):
					targetList =  getEUtrancellMacroList("EUtranCellFDD-MACRO");
					break;
				case("EUtranCellFDD-PICO"):
					targetList = getEUtrancellPicoList("EUtranCellFDD-PICO");
					break;
				case("EUtranCellTDD-MACRO"):
					targetList =  getEUtrancellMacroList("EUtranCellTDD-MACRO");
					break;
				case("ExternalUtranFreq"):
					List<ExternalUtranFreq> extutranfreqlist ;
					if (subN != null) {
						extutranfreqlist = TopologyManager.getInstance().getExternalUtranFreq(subN);
						if(extutranfreqlist == null || extutranfreqlist.isEmpty()) {
							return null;
						}
					} else{
						return null;
					}
					for(ExternalUtranFreq extfdn: extutranfreqlist){
						targetList.add(extfdn.getFdn().toString());
					}
					break;
				case("ExternalEutranFrequency"):
					List<ExternalEutranFrequency> extutranfreqlist ;
					if (subN != null) {
						extutranfreqlist = TopologyManager.getInstance().getExternalEutranFrequency(subN);
						if(extutranfreqlist == null || extutranfreqlist.isEmpty()) {
							return null;
						}
					} else{
						return null;
					}
					for(ExternalEutranFrequency extfdn: extutranfreqlist){
						targetList.add(extfdn.getFdn().toString());
					}
					break;
			}

			if(!(targetList.isEmpty() && targetList.size() > 1)){
				return true;
			}
			return null;
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}
	public def getSourceCell(){

		sourcecell = sourceList.get(randomGenerator.nextInt(sourceList.size()));
		return "" + sourcecell;
	}

	public def getTargetCell(){

		targetcell = targetList.get(randomGenerator.nextInt(targetList.size()));

		if(targetcell.equals(sourcecell)){
			targetcell = targetList.get(randomGenerator.nextInt(targetList.size()));
		}
		return "" + targetcell;
	}


	public def deleteExRelation(String relationType){

		ConfigurationData configData = new ConfigurationData(null, "nmsadm");
		MessageJobInfo<List> relationsList ;

		switch(relationType){
			case("UtranRelation"):

				try
				{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranRelation.name(), configData);

				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;

			case("UtranCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranCellRelation.name(), configData);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EUtranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EUtranFreqRelation.name(), configData);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("UtranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.UtranFreqRelation.name(), configData);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EUtranCellRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EUtranCellRelation.name(), configData);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("EutranFreqRelation"):
				try{
					relationsList = CellServiceManager.getInstance().getRelations(sourcecell,RelationType.EutranFreqRelation.name(), configData);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
		}

		checkJobFinished(relationsList);
		if (relationsList.getState() == JobState.FAILED){
			return relationsList.getAdditionalInfo().toString();
		}
		List<UtranCellRelations> relations = relationsList.getList();

		for(int i=0; i<relations.size(); i++){

			GenericItem genericItem = new GenericItem(relations.get(i).getFdn().toString());
			IGenericItem[] genericItemArray = new IGenericItem[1];
			genericItemArray[0] = genericItem;
			MessageJobInfo deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);
			
			checkJobFinished(deleteRelationJob);
//			if (deleteRelationJob.getState() == JobState.FAILED){
//				return deleteRelationJob.getAdditionalInfo().toString();
//			}
		}
		return true;
	}

	public def createNewRelation(String relationType){

		sourceCellList.add(sourcecell);
		targetCellList.add(targetcell);

		switch(relationType){
			case("UtranRelation"):
				try
				{
					job = CellServiceManager.getInstance().createFrequencyRelations(
							RelationType.UtranRelation, sourceCellList, targetCellList, null);
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				break;
			case("UtranCellRelation"):
				try
				{
					job = CellServiceManager.getInstance().createCellRelations(
							RelationType.UtranCellRelation, Direction.UNIDIRECTIONAL ,sourceCellList, targetCellList, null, null, null);

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

		}
		checkJobFinished(job);
		if (job.getState() == JobState.FAILED){
			return job.getAdditionalInfo().toString();
		}
		return "OK";
	}

	public def createUtranRelation(String sourceCell, String targetCell ){

		try
		{
			sourceUtranCell.add(sourceCell);
			targetUtranCell.add(targetCell);

			job = CellServiceManager.getInstance().createFrequencyRelations(RelationType.UtranRelation,  sourceUtranCell, targetUtranCell, null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
			return "OK";
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}


	public def createEUtranFreqRelation(String sourceCell, String targetCell ){

		try
		{
			sourceEUtranFreqCell.add(sourceCell);
			targetEUtranCell.add(targetCell);

			job = CellServiceManager.getInstance().createFrequencyRelations(RelationType.EUtranFreqRelation, sourceEUtranFreqCell, targetEUtranCell , null);

			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
			return "OK";
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}


	public def createGsmRelation(String sourceCell, String targetCell ){

		try
		{
			sourceCellGsm.add(sourceCell);
			targetCellGsm.add(targetCell);

			job = CellServiceManager.getInstance().createFrequencyRelations(RelationType.GsmRelation,  sourceCellGsm, targetCellGsm, null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
			return "OK";
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	public def createEUtranRelation(String sourceCell, String targetCell ){

		try
		{
			sourceCellEutran.add(sourceCell);
			targetCellEutran.add(targetCell);

			job = CellServiceManager.getInstance().createFrequencyRelations(RelationType.EUtranCellRelation,  sourceCellEutran, targetCellEutran, null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
			return "OK";
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	public def createUtranFreqRelation(String sourceCell, String targetCell ){

		try
		{
			sourceCellutranFreq.add(sourceCell);
			targetCellutranFreq.add(targetCell);

			job = CellServiceManager.getInstance().createFrequencyRelations(RelationType.UtranFreqRelation,  sourceCellutranFreq, targetCellutranFreq, null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
			return "OK";
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}


	public def createCdma2000FreqRelation(String sourceCell, String targetCell ){

		try
		{
			sourceCellEUtran.add(sourceCell);
			targetCellExternalCdma2000Freq.add(targetCell);

			job = CellServiceManager.getInstance().createFrequencyRelations(RelationType.Cdma2000FreqRelation,  sourceCellEUtran, targetCellExternalCdma2000Freq, null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return "OK";
			}
			return "OK";
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	public def createCoverageRelation(String sourceCell, String targetCell ){

		try
		{
			coveragesourcefdn.add(sourceCell);
			coveragetargetfdn.add(targetCell);

			job = CellServiceManager.getInstance().createFrequencyRelations(RelationType.CoverageRelation,  coveragesourcefdn, coveragetargetfdn,null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
			return "OK";
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	public def createCoverageRelationDiffRnc(String sourceCell, String targetCell ){

		try
		{
			coveragesourceRnc1.add(sourceCell);
			coveragetargetRnc2.add(targetCell);

			job = CellServiceManager.getInstance().createCellRelations(RelationType.CoverageRelation, Direction.UNIDIRECTIONAL,  cellfdn, cdma2000freqband, null,null,null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
			return "Coverage Relation is not allowed between two different RNC's";
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}


	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}
}
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.domain.modetails.IManagedObject;
import com.ericsson.oss.domain.Configuration;
import java.util.Map;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import java.util.List;
import com.ericsson.oss.common.cm.ui.core.CmManager;

import com.ericsson.oss.domain.ConfigurationData;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.domain.PlanningState;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.modetails.Cdma2000FreqRelation;
import com.ericsson.oss.domain.modetails.CellRelation;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;

class DeleteRelations{

	protected TopologyManager topologyManager = TopologyManager.getInstance();

	protected CellServiceManager cellServiceManager = CellServiceManager.getInstance();

	private String cdma2000FreqRelationToDelete = null;

	MessageJobInfo deleteRelationJob;

	GenericItem genericItem;

	IGenericItem[] genericItemArray;

	public def deleteCdma2000FreqRelation(String cdma2000FreqRelFdn){
		try{
			//cdma2000FreqRelationToDelete = selectcdma2000FreqRelation();
			if (cdma2000FreqRelFdn != null){
				genericItem = new GenericItem(cdma2000FreqRelFdn);
				genericItemArray = [genericItem] as IGenericItem[];
				deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);
				checkJobFinished(deleteRelationJob);
				waitForTopologyUpdate(15000);
				if (deleteRelationJob.getState() == JobState.FAILED){
					return deleteRelationJob.getAdditionalInfo().toString();
				}
				return "OK";
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
	}

	public def deleteCdma2000CellRelation(String cdma2000CellRelFdn){
		try{
			if (cdma2000CellRelFdn != null){
				genericItem = new GenericItem(cdma2000CellRelFdn);
				genericItemArray = [genericItem] as IGenericItem[];
				deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);
				checkJobFinished(deleteRelationJob);
				waitForTopologyUpdate(15000);
				if (deleteRelationJob.getState() == JobState.FAILED){
					return deleteRelationJob.getAdditionalInfo().toString();
				}
				return "OK";
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
	}

	public def deleteEUtranCellRelation(String eUtranCellRelFdn){
		try{
			if (eUtranCellRelFdn != null){
				genericItem = new GenericItem(eUtranCellRelFdn);
				genericItemArray = [genericItem] as IGenericItem[];
				deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);
				checkJobFinished(deleteRelationJob);
				waitForTopologyUpdate(15000);
				if (deleteRelationJob.getState() == JobState.FAILED){
					return deleteRelationJob.getAdditionalInfo().toString();
				}
				return "OK";
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
	}

	public def deleteUtranCellRelation(String utranCellRelFdn){
		try{
			if (utranCellRelFdn != null){
				genericItem = new GenericItem(utranCellRelFdn);
				genericItemArray = [genericItem] as IGenericItem[];
				deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);
				checkJobFinished(deleteRelationJob);
				waitForTopologyUpdate(15000);
				if (deleteRelationJob.getState() == JobState.FAILED){
					return deleteRelationJob.getAdditionalInfo().toString();
				}
				return "OK";
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
	}

	//	private String selectcdma2000FreqRelation() {
	//		SubNetwork sn = topologyManager.getSubNetwork();
	//		List<ERbs> erbsList = sn.getErbsList();
	//		List<EUtranCell> cellList;
	//		for (ERbs erbs : erbsList) {
	//			String value = String.valueOf(erbs.getMirrorMIBsynchStatus());
	//			if(value.equals("UNSYNCHRONIZED"))
	//			{
	//				continue;
	//			}
	//			cellList = erbs.getCellList();
	//			for (EUtranCell cell : cellList) {
	//				if (cell.getPlanningState() != null && cell.getPlanningState().equals(PlanningState.DELETED)) {
	//					continue;
	//				}
	//				MessageJobInfo<List> cdma2000CellRelations = cellServiceManager.getInstance().getRelations(cell.getFdn().toString(), RelationType.Cdma2000CellRelation.getName(), null);
	//				checkJobFinished(cdma2000CellRelations);
	//				if (cdma2000CellRelations.getState().equals(JobState.FINISHED)) {
	//					List<CellRelation> cellRelations = cdma2000CellRelations.getList();
	//					if (cellRelations.size() > 0) {
	//						MessageJobInfo<List> cdma2000FreqRelations = cellServiceManager.getInstance().getRelations(cell.getFdn().toString(),RelationType.Cdma2000FreqRelation.getName(),null);
	//						checkJobFinished(cdma2000FreqRelations);
	//						if (cdma2000FreqRelations.getState().equals(JobState.FINISHED)) {
	//							List<Cdma2000FreqRelation> freqRelations = cdma2000FreqRelations.getList();
	//							if (freqRelations.size() > 0) {
	//								return freqRelations.get(0).getFdn().toString();
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//		return null;
	//	}


	public def waitForTopologyUpdate(final int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.getMessage().toString();
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

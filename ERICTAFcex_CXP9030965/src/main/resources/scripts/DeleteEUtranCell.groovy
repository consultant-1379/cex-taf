import java.util.ArrayList;

import java.util.List;

import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.domain.ConfigurationData;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.domain.EUtranCellFDD;
import com.ericsson.oss.domain.PlanningState;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.modetails.EUtranCellRelation;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.IJobListener;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;

public class DeleteEUtranCell {
	
	private int retryCount;
	protected TopologyManager topologyManager = TopologyManager.getInstance();
	protected CellServiceManager cellServiceManager = CellServiceManager.getInstance();
	protected CmManager cmManager = CmManager.getInstance();
	public String planName = null;
	public EUtranCellFDD EUtranCellFDDToDelete = null;
	/*
	 *
	 */

	final ConfigurationData configData = new ConfigurationData(planName,
	System.getProperty("user.name"));




	public def deleteEUtranCellMoWithRel(String eutrancellfdd) {

		String result = null;
		GenericItem genericItem = new GenericItem(eutrancellfdd);
		IGenericItem[] genericItemArray = new IGenericItem[1];
		genericItemArray[0] = genericItem;

		final MessageJobInfo deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, planName);

		return result= "OK";
	}
	/*
	 * Retrieves all EUtranCellFDD from ERbs under SubNetwork using Topology
	 * Manager.
	 *
	 *
	 * @return UtranCell
	 */
	public String selectEUtranCellFDD() {
		SubNetwork subNet = topologyManager.getSubNetwork();
		List<EUtranCell> cellList = new ArrayList<EUtranCell>();
		List<ERbs> erbsList;

		if (subNet != null) {
			erbsList = subNet.getErbsList();
		} else {
			return null;
		}
		for (ERbs erbs : erbsList) {
			String value = erbs.getMirrorMIBsynchStatus();
			if(value.equals("UNSYNCHRONIZED")) {
				continue;
			}


			cellList = erbs.getCellList();
			for (EUtranCell cell : cellList) {
				if (cell instanceof EUtranCellFDD) {

					MessageJobInfo<List> eUtranRelations = cellServiceManager.getInstance().getRelations(cell.getFdn().toString(),RelationType.EUtranCellRelation.getName(),
							configData);

					checkMojobFinished(eUtranRelations);

					if (eUtranRelations.getState().equals(JobState.FINISHED)) {

						List<EUtranCellRelation> relations = eUtranRelations.getList();
						if (relations.size() > 0)
							return cell.getFdn();
					}
				}
			}
		}
		return null;
	}

	/*
	 * From MOCrud Fixture
	 *
	 */
	protected void checkMojobFinished(final MessageJobInfo MoJob){

		retryCount = 0;

		while (!MoJob.isCompleted() && retryCount < 10) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			retryCount ++;
		}
	}

	protected void waitForTopologyUpdate(final int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

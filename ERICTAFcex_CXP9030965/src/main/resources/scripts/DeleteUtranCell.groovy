
import java.util.ArrayList;
import java.util.List;

import com.ericsson.oss.common.cm.ui.core.CmManager;

import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.ConfigurationData;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.RNC;
import com.ericsson.oss.domain.FDN;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.domain.UtranCellRelations;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.logging.Log;


import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.IJobListener;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.modetails.IManagedObject;

public class DeleteUtranCell {

	/*
	 * 
	 * */
	private int retryCount;
	private ICMService cmService;
	protected Map<String,Object> attributesMap = new HashMap<String,Object>();
	protected Map<String,String> dirtyAttributesLogMap = new HashMap<String,String>();
	protected HashSet<Object> dirtyAttributesSet = new HashSet<Object>();
	protected TopologyManager topologyManager = TopologyManager.getInstance();
	protected CellServiceManager cellServiceManager = CellServiceManager.getInstance();
	protected CmManager cmManager = CmManager.getInstance();
	public String planName = null;
	/*
	 * 
	 */

	final ConfigurationData configData = new ConfigurationData(planName,
	System.getProperty("user.name"));

	private UtranCell utranCellToDelete = null;
	String test = null;

	public String deleteUtranCellMo() {

		try {
			String result = null;
			utranCellToDelete = selectUtranCell();

			if (utranCellToDelete == null)
				return result = "OK"+"Either UtranCell or UtranCell with relations Not exist";
			GenericItem genericItem = new GenericItem(utranCellToDelete.getFdn()
					.toString());

			IGenericItem[] genericItemArray = new IGenericItem[1];
			genericItemArray[0] = genericItem;

			final MessageJobInfo deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, planName);

			checkMojobFinished(deleteRelationJob);

			waitForTopologyUpdate(10000);

			if (deleteRelationJob.getState().equals(JobState.FINISHED)
			&& result == null) {
				//String deleteInfo = deleteRelationJob.getAdditionalInfo()
				//		.toString();
				result = "UtranCell deleted, OK : "+utranCellToDelete.getFdn().toString();
			} else {
				result = "UtranCell deletion, FAILED : "+utranCellToDelete.getFdn().toString();
			}
			return result;
		}
		catch(Exception e) {
			return e.getMessage().toString();
		}
	}

	/*
	 * Retrieves all UtranCells from RNCs under SubNetwork using Topology
	 * Manager.
	 *
	 *
	 * @return UtranCell
	 */

	public UtranCell selectUtranCell() {

		try {
			SubNetwork subNet = topologyManager.getSubNetwork();

			List<UtranCell> cellList = new ArrayList<UtranCell>();
			List<RNC> rncList;
			List<RBS> rbsList;
			if (subNet != null) {
				rncList = subNet.getRncList();
			} else {
				return null;
			}

			for (RNC rnc : rncList) {

				rbsList=rnc.getRbsList();

				for( RBS rbs: rbsList){
					if(!rbs.isPicoRbs() && !rbs.isSsrRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0){

						String value = String.valueOf(rbs.getMirrorMIBsynchStatus());
						if(value.equals("UNSYNCHRONIZED")) {
							continue;
						}

						cellList = rbs.getUtranCellList();

						for (UtranCell cell : cellList) {
							MessageJobInfo<List> utranRelations = cellServiceManager
									.getInstance().getRelations(cell.getFdn().toString(),
									RelationType.UtranRelation.getName(),
									configData);

							checkMojobFinished(utranRelations);

							if (utranRelations.getState().equals(JobState.FINISHED)) {

								List<UtranCellRelations> relations = utranRelations
										.getList();
								if (relations.size() > 0)
									return cell;
							}
						}
					}

				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
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
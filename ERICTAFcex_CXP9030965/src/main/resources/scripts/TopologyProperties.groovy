
import java.util.ArrayList;
import java.util.List;

import com.ericsson.oss.common.cm.ui.core.CmManager;

import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.ConfigurationData;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.RNC;
import com.ericsson.oss.domain.FDN;
import com.ericsson.oss.domain.RXI;
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
import com.ericsson.oss.domain.TechnicianState;

import org.apache.commons.logging.Log;


import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.IJobListener;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.modetails.IManagedObject;
import com.ericsson.oss.domain.ConnectionState;

public class TopologyProperties {

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
				return result = "Either UtranCell or UtranCell with relations Not exist";
			GenericItem genericItem = new GenericItem(utranCellToDelete.getFdn()
					.toString());

			IGenericItem[] genericItemArray = new IGenericItem[1];
			genericItemArray[0] = genericItem;

			final MessageJobInfo deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, planName);

			checkMojobFinished(deleteRelationJob);

			waitForTopologyUpdate(10000);

			if (deleteRelationJob.getState().equals(JobState.FINISHED)
			&& result == null) {
				String deleteInfo = deleteRelationJob.getAdditionalInfo()
						.toString();
				result = "UtranCell deleted, OK";
			} else {
				result = "UtranCell deleted, FAILED";
			}
			return result;
		}
		catch(Exception e) {
			return e.getMessage().toString();
		}
	}


	public String viewTechnicianPresentAttribute(){
		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
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

					String value = String.valueOf(rbs.getTechnicianPresent());
					return value +"FDN = >" +rbs.toString();
				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	/*
	 * Method To get VAlue for A RNC from GUI
	 */
	public def viewRNCProperties(){
		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<RNC> rncList;
			Map<String, String> properties = new HashMap<String, String>();
			if (subNet != null) {
				rncList = subNet.getRncList();
			} else {
				return null;
			}
			for (RNC rnc : rncList) {
				if(rnc.isConnected() && !rnc.isPicoRbs() && !rnc.isSsrRbs()){

					properties.put("FDN", rnc.toString());
					properties.put("neMIMversion", rnc.getNeMIMversion());
					properties.put("ipAddress", rnc.getIpAddress());
					properties.put("cppVersion",rnc.getCppVersion());

					return properties;
				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}
	/*
	 * Method To Get Properties of RBS from GUI
	 */
	public def viewRBSProperties(){
		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<RNC> rncList;
			List<RBS> rbsList;
			Map<String, String> properties = new HashMap<String, String>();
			if (subNet != null) {
				rncList = subNet.getRncList();
			} else {
				return null;
			}
			for (RNC rnc : rncList) {
				
				if(rnc.isConnected()){
					rbsList=rnc.getRbsList();
				}
				for( RBS rbs: rbsList){

					if(rbs.isConnected() && !rbs.isPicoRbs() && !rbs.isSsrRbs()){

						properties.put("FDN", rbs.toString());
						properties.put("userLabel", rbs.getUserLabel());
						properties.put("rbsIubId", rbs.getRbsIubId());
						properties.put("ipAddress", rbs.getIpAddress());
						properties.put("cppVersion",rbs.getCppVersion());
						properties.put("connectionStatus", ConnectionState.getValue(rbs.getConnectionStatus()));
						return properties;
					}
				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	/*
	 * Method To Get Properties of RBS from GUI
	 */
	public def viewERBSProperties(){
		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<ERbs> erbsList;
			Map<String, String> properties = new HashMap<String, String>();
			if (subNet != null) {
				erbsList = topologyManager.getERBSList(subNet);
			} else {
				return null;
			}
			for( ERbs erbs: erbsList){

				if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs()){				
					properties.put("FDN", erbs.toString());
					properties.put("userLabel", erbs.getUserLabel());
					properties.put("ipAddress", erbs.getIpAddress());
					properties.put("cppVersion",erbs.getCppVersion());
					properties.put("connectionStatus", ConnectionState.getValue(erbs.getConnectionStatus()));
					return properties;
				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	/*
	 * Method To Get Properties of RBS from GUI
	 */
	public def viewPRBSProperties(){
		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<RNC> rncList;
			List<RBS> rbsList;
			Map<String, String> properties = new HashMap<String, String>();
			if (subNet != null) {
				rncList = subNet.getRncList();
			} else {
				return null;
			}
			for (RNC rnc : rncList) {

				rbsList=rnc.getRbsList();

				for( RBS rbs: rbsList){
					if(rbs.isPicoRbs())
					{
						String value = String.valueOf(rbs.getMirrorMIBsynchStatus());
						if(value.equals("UNSYNCHRONIZED")) {
							continue;
						}
						properties.put("FDN", rbs.toString());
						properties.put("userLabel", rbs.getUserLabel());
						properties.put("ipAddress", rbs.getIpAddress());
						properties.put("connectionStatus", ConnectionState.getValue(rbs.getConnectionStatus()));
						return properties;
					}
				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	/*
	 * Method To Get Properties of RBS from GUI
	 */
	public def viewPERBSProperties(){
		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<ERbs> erbsList;
			Map<String, String> properties = new HashMap<String, String>();
			if (subNet != null) {
				erbsList = topologyManager.getERBSList(subNet);
			} else {
				return null;
			}
			for( ERbs erbs: erbsList){
				if(erbs.isPicoRbs())
				{
					String value = String.valueOf(erbs.getMirrorMIBsynchStatus());
					if(value.equals("UNSYNCHRONIZED")) {
						continue;
					}
					properties.put("FDN", erbs.toString());
					properties.put("userLabel", erbs.getUserLabel());
					properties.put("ipAddress", erbs.getIpAddress());
					properties.put("connectionStatus", ConnectionState.getValue(erbs.getConnectionStatus()));
					return properties;
				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}



	/*
	 * Method to Retrieve RXI properties from GUI
	 */
	public def viewRXIProperties(){
		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<RXI> rxiList;
			List<RBS> rbsList;
			Map<String, String> properties = new HashMap<String, String>();
			if (subNet != null) {
				rxiList = subNet.getRxiList();
			} else {
				return null;
			}
			for (RXI rxi : rxiList) {


				properties.put("FDN", rxi.toString());
				properties.put("connectionStatus", rxi.getConnectionStatus());
				properties.put("synchronisationProgress", rxi.getSynchronisationProgress());
				properties.put("ipAddress", rxi.getIpAddress());
				properties.put("cppVersion",rxi.getCppVersion());
				return properties;

			}
		}
		catch(Exception e){
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

					String value = String.valueOf(rbs.getMirrorMIBsynchStatus());
					if(value.equals("UNSYNCHRONIZED")) {
						continue;
					}

					cellList = rbs.getUtranCellList();

					for (UtranCell cell : cellList) {
						MessageJobInfo<List> utranRelations = cellServiceManager.getInstance().getRelations(cell.getFdn().toString(),
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
		catch(Exception e){
			return e.getMessage().toString();
		}
		return null;
	}

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
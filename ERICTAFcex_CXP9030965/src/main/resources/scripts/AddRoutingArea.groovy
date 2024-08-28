import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ca.odell.glazedlists.EventList;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.domain.LocationArea;
import com.ericsson.oss.domain.RoutingArea;
import com.ericsson.oss.domain.Plmn;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.CEXMIMVersion;
import com.ericsson.oss.domain.modetails.RANOS_SUBNETWORK_MODEL.RoutingAreaImpl;
//
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.crud.ui.core.ServiceFacade;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.IJobListener;
//

public class AddRoutingArea {

	private boolean objectCreated;
	private IGenericItem routingAreaInstance;
	private CEXMIMVersion cexmimVersion;
	private String genericItemID;
	private String MODEL_NAME;
	private String MODEL_VER;
	private String racId;
	private String routingAreaFDN;
	private int randomNumber; // 0 .. 255
	private String status = "";
	private final String MO_TYPE = "RoutingArea";
	
	//
	
	public static final String SUCCESS = "Success";
	public static final String FAILED = "Failed";
	public static final String USER_LABEL = "userLabel";
	public static final String ATTRS = "attributes";
	public static final String DIRTY_ATTRS = "dirtyAttributes";
	//
	private int retryCount;
	protected Map<String,Object> attributesMap = new HashMap<String,Object>();
	protected Map<String,String> dirtyAttributesLogMap = new HashMap<String,String>();
	protected HashSet<Object> dirtyAttributesSet = new HashSet<Object>();
	protected TopologyManager topologyManager = TopologyManager.getInstance();
	protected CellServiceManager cellServiceManager = CellServiceManager.getInstance();
	protected CmManager cmManager = CmManager.getInstance();
	protected ServiceFacade serviceFacade = ServiceFacade.getInstance();
	protected String statusCreate = "FAILED";
	protected String statusDelete = "FAILED";
	public String userLabel;
	public String planName
	//
	
	public String createRoutingArea() {
		
		try{
		objectCreated = false;
		
		status = "";
		
		randomNumber = getUniqueID();
		racId = String.valueOf(randomNumber);
		
		cexmimVersion = topologyManager.getSubNetwork().getMoMimVersion();
		
		SubNetwork subNet = topologyManager.getSubNetwork();
	
		if(subNet.getPlmnList().size() > 0) {
			
			List<Plmn> plmnList = subNet.getPlmnList();
				
			List<LocationArea> areas = new ArrayList<LocationArea>();
			
			for (Plmn plmn : plmnList) {
				List<LocationArea> tempAreas = topologyManager.getLaList(plmn.getOid());
				if(tempAreas.size() > 0) {
					areas = tempAreas;
					break;
				}
			}
			
			if(areas.size() > 0) {
				genericItemID = areas.get(0).getFdn().toString();

			} else {
				return MO_TYPE + " Not Created : No Location Area available to handle a new " +MO_TYPE +" Object!";
			}
		} else {
			return MO_TYPE + " Not Created : No Plmn available to handle a new " +MO_TYPE +" Object!";
		}
			
		MODEL_NAME = cexmimVersion.getName();
		MODEL_VER = cexmimVersion.getVersion();
		
		try {
			
			routingAreaInstance = cmManager.createGenericItem(genericItemID, MODEL_NAME, MODEL_VER, MO_TYPE);
		
		} catch (Exception e) {
			
			return "Create Generic Item Fail - "+e;
		}
		
		updateRoutingAreaInstance();
		
		List<IGenericItem> genericItemList = new ArrayList<IGenericItem>();
		
		genericItemList.add(routingAreaInstance);
		
		final MessageJobInfo addMoJob = cmManager.createGenericItem(genericItemList, planName, null);
		
		checkMojobFinished(addMoJob);
		
		if(addMoJob.getState().equals(JobState.FAILED))
			status = MO_TYPE +" create failed - " + addMoJob.getAdditionalInfo().toString();
		
		waitForTopologyUpdate(7000);
		
		if(true) {
			status = "OK :"+ MO_TYPE +" CREATED";
			objectCreated = true;
		}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return status;
	}
	
	public String createLocationAreaWithNonUniRac() {
		
		try{
		objectCreated = false;
		
		status = "";
		
		randomNumber = getNonUniqueID();
		racId = String.valueOf(randomNumber);
		
		cexmimVersion = topologyManager.getSubNetwork().getMoMimVersion();
		
		SubNetwork subNet = topologyManager.getSubNetwork();
	
		if(subNet.getPlmnList().size() > 0) {
			
			List<Plmn> plmnList = subNet.getPlmnList();
				
			List<LocationArea> areas = new ArrayList<LocationArea>();
			
			for (Plmn plmn : plmnList) {
				List<LocationArea> tempAreas = topologyManager.getLaList(plmn.getOid());
				if(tempAreas.size() > 0) {
					areas = tempAreas;
					break;
				}
			}
			
			if(areas.size() > 0) {
				genericItemID = areas.get(0).getFdn().toString();

			} else {
				return MO_TYPE + " Not Created : No Location Area available to handle a new " +MO_TYPE +" Object!";
			}
		} else {
			return MO_TYPE + " Not Created : No Plmn available to handle a new " +MO_TYPE +" Object!";
		}
			
		MODEL_NAME = cexmimVersion.getName();
		MODEL_VER = cexmimVersion.getVersion();
		
		try {
			
			routingAreaInstance = cmManager.createGenericItem(genericItemID, MODEL_NAME, MODEL_VER, MO_TYPE);
		
		} catch (Exception e) {
			
			return "Create Generic Item Fail - "+e;
		}
		
		updateRoutingAreaInstance();
		
		List<IGenericItem> genericItemList = new ArrayList<IGenericItem>();
		
		genericItemList.add(routingAreaInstance);
		
		final MessageJobInfo addMoJob = cmManager.createGenericItem(genericItemList, planName, null);
		
		checkMojobFinished(addMoJob);
		
		if(addMoJob.getState().equals(JobState.FAILED))
			 return status = MO_TYPE +" create failed - " + addMoJob.getAdditionalInfo().toString();
		
		waitForTopologyUpdate(7000);
		
		if(true) {
			status =MO_TYPE +" CREATED";
			objectCreated = true;
		}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return status;
	}
	
	private boolean isRoutingAreaExists(int rac, String userL, String planName) {
		
		ContextProvider.getInstance().contextChanged(ContextProvider.PLANNED_CONFIGURATION_CONTEXT, planName);
		
		String[] MoType = {MO_TYPE};
		MessageJobInfo<List> retrieveChildrenMoJob = cellServiceManager.retreiveChildren(topologyManager.getSubNetwork(),MoType, planName);
		
		checkMojobFinished(retrieveChildrenMoJob);
		
		EventList<RoutingAreaImpl> routingAreas = retrieveChildrenMoJob.getList();
		
		if(retrieveChildrenMoJob.getState().equals(JobState.FINISHED)) {
			
			for(RoutingAreaImpl routingArea : routingAreas) {
				
				if(((routingArea.getUserLabel().equals(userL) && routingArea.getRac() == rac))) {
						routingAreaFDN = routingArea.getFdn().toString();
					return true;
				}
			}
		}
		return false;
	}
	
	private void updateRoutingAreaInstance() {
		
		attributesMap = routingAreaInstance.getPropertyValue(ATTRS);
		dirtyAttributesSet = routingAreaInstance.getPropertyValue(DIRTY_ATTRS);
		
		attributesMap.put(USER_LABEL, userLabel);
		attributesMap.put("rac", racId);
		
		dirtyAttributesSet.add(USER_LABEL);
		dirtyAttributesSet.add("rac");
		
		routingAreaInstance.setPropertyValue(ATTRS, attributesMap);
		routingAreaInstance.setPropertyValue(DIRTY_ATTRS, dirtyAttributesSet);
	}
	
	
	private int getUniqueID() {
		
		int range = 255;
		
		Random random = new Random();
		List<Integer> racIds = new ArrayList<Integer>();
		int randomNumber = random.nextInt(range);
		String[] MoType = {MO_TYPE};
		MessageJobInfo<List> retrieveChildrenMoJob = cellServiceManager.retreiveChildren(topologyManager.getSubNetwork(),MoType, planName);
		checkMojobFinished(retrieveChildrenMoJob);
		EventList<RoutingAreaImpl> routingAreas = retrieveChildrenMoJob.getList();
		for(RoutingAreaImpl area : routingAreas){
			racIds.add(area.getRac());
		}
		
		
		while(racIds.contains(randomNumber))
			randomNumber = random.nextInt(range);
			
		
		return randomNumber;
	}
	
	/*
	 * For Non Unique Id
	 */
	private int getNonUniqueID() {
		
		try{
		List<Integer> racIds = new ArrayList<Integer>();
		
		List<Plmn> plmnList = topologyManager.getSubNetwork().getPlmnList();
		
		if(plmnList.size() > 0) {
		
			for(Plmn plmn : plmnList) {
				
				List<LocationArea> areas = new ArrayList<LocationArea>();
				
				areas = plmn.getLaList();
				if(areas != null)
				if(areas.size() > 0) {
					
					List<LocationArea> routingAreas = new ArrayList<LocationArea>();
					for(LocationArea area : areas){
					routingAreas = area.getRaList();
					if(routingAreas != null)
					if(routingAreas.size() > 0) {
						for (RoutingArea ra : routingAreas)
						racIds.add(ra.getRac());
				}
					}
				
				
					
				
			}
		}
		}
		if(racIds.size()>0)
		return racIds.get(0);
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		
	}
	/*
	 * MO Fixteure
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
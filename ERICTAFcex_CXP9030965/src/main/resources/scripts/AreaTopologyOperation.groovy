import java.util.HashSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import ca.odell.glazedlists.EventList;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.CEXMIMVersion;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.LocationArea;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.domain.Plmn;
import com.ericsson.oss.domain.modetails.RANOS_SUBNETWORK_MODEL.LocationAreaImpl;

import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.crud.ui.core.ServiceFacade;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.IJobListener;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;

public class AreaTopologyOperation {
	
	private boolean objectCreated;
	private IGenericItem locationAreaInstance;
	private CEXMIMVersion cexmimVersion;
	private String genericItemID;
	private int randomNumber; // 0 .. 65533
	private String lacId;
	private Map<String,String> moAttributesMap = new HashMap<String,String>();
	private List<IGenericItem> genericItemList;
	private List<String> moDirtyAttributes = new ArrayList<String>();
	private String locationAreaFDN;
	private String status = "";
	private final String MO_TYPE = "LocationArea";
	
	/*
	 * 
	 */
	public static final String SUCCESS = "Success";
	public static final String FAILED = "Failed";
	public static final String USER_LABEL = "userLabel";
	public static final String ATTRS = "attributes";
	public static final String DIRTY_ATTRS = "dirtyAttributes"
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
    public String planName;
	/*
	 * 
	 */
	public String createLocationArea() {
		try{
		objectCreated = false;
	
		status = "";
		
		cexmimVersion = topologyManager.getSubNetwork().getMoMimVersion();
		
		if(topologyManager.getSubNetwork().getPlmnList().size() > 0)
		{
			genericItemID = topologyManager.getSubNetwork().getPlmnList().get(0).getFdn().toString();
		
		} else {
			
			return MO_TYPE + " Not Created : No Plmn list available to handle new " +MO_TYPE +" Object!";
		}
		
		String modelName = cexmimVersion.getName();
		String modelVer = cexmimVersion.getVersion();
		
		randomNumber = getUniqueID();
		lacId = String.valueOf(randomNumber);
		userLabel = "LA"+randomNumber.toString();
		genericItemList = new ArrayList<IGenericItem>();

		try {
			
			locationAreaInstance = cmManager.createGenericItem(genericItemID, modelName, modelVer, MO_TYPE);
		
		} catch (Exception e) {
			
			return "Failed to create generic item - " + e;
		}
		
		updateLocationAreaInstance();
	
		genericItemList.add(locationAreaInstance);
		
		final MessageJobInfo<Object> addMoJob = cmManager.createGenericItem(genericItemList, planName, null);
		checkMojobFinished(addMoJob);
		
		if(addMoJob.getState().equals(JobState.FAILED))
			status = MO_TYPE +" create failed - " + addMoJob.getAdditionalInfo().toString();
		
		waitForTopologyUpdate(7000);
//		if(isLocationAreaExists(randomNumber, userLabel, planName)) {
			 
			status = "OK :"+MO_TYPE +" CREATED";
			objectCreated = true;
//		}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return status;
	}
	
	public String createLocationAreaWithNonUniLac() {
		try{
		objectCreated = false;
	
		status = "";
		
		cexmimVersion = topologyManager.getSubNetwork().getMoMimVersion();
		
		if(topologyManager.getSubNetwork().getPlmnList().size() > 0)
		{
			genericItemID = topologyManager.getSubNetwork().getPlmnList().get(0).getFdn().toString();
		
		} else {
			
			return MO_TYPE + " Not Created : No Plmn list available to handle new " +MO_TYPE +" Object!";
		}
		
		String modelName = cexmimVersion.getName();
		String modelVer = cexmimVersion.getVersion();
		
		randomNumber = getNonUniqueID();
		lacId = String.valueOf(randomNumber);
		userLabel = "LA"+randomNumber.toString();
		genericItemList = new ArrayList<IGenericItem>();

		try {
			
			locationAreaInstance = cmManager.createGenericItem(genericItemID, modelName, modelVer, MO_TYPE);
		
		} catch (Exception e) {
			
			return "Failed to create generic item - " + e;
		}
		
		updateLocationAreaInstance();
	
		genericItemList.add(locationAreaInstance);
		
		final MessageJobInfo<Object> addMoJob = cmManager.createGenericItem(genericItemList, planName, null);
		checkMojobFinished(addMoJob);
		
		if(addMoJob.getState().equals(JobState.FAILED))
			return status = MO_TYPE +" create failed - " + addMoJob.getAdditionalInfo().toString();
		
		waitForTopologyUpdate(7000);
//		if(isLocationAreaExists(randomNumber, userLabel, planName)) {
			 
			status =  MO_TYPE +" CREATED";
			objectCreated = true;
//		}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return status;
	}
	
	private boolean isLocationAreaExists(int lac, String userL, String planName) {
		
//		return ContextProvider.getInstance().contextChanged(ContextProvider.PLANNED_CONFIGURATION_CONTEXT, planName);
		String[] MOtype= {MO_TYPE};
		MessageJobInfo<List> retrieveChildrenMoJob = cellServiceManager.retreiveChildren(topologyManager.getSubNetwork(),MOtype, planName);
		
		checkMojobFinished(retrieveChildrenMoJob);
		EventList<LocationAreaImpl> locationAreas = retrieveChildrenMoJob.getList();
		
		if(retrieveChildrenMoJob.getState().equals(JobState.FINISHED)) {
			
			for(LocationAreaImpl locationArea : locationAreas) {
				
				if(((locationArea.getUserLabel().equals(userL) && locationArea.getLac() == lac))) {
						locationAreaFDN = locationArea.getFdn().toString();
					return true;
				}
			}
		}
		return false;
	}
	
	// values specific to this mo
	private void updateLocationAreaInstance() {
		
		moAttributesMap.put(USER_LABEL, userLabel);
		moAttributesMap.put("lac", lacId);
		
		moDirtyAttributes.add(USER_LABEL);
		moDirtyAttributes.add("lac");
		
		// map the attributes to the new object
		locationAreaInstance.setPropertyValue(ATTRS, moAttributesMap);
		locationAreaInstance.setPropertyValue(DIRTY_ATTRS, moDirtyAttributes);
	}
	
	private int getUniqueID() {
		
		int range = 65533;
		
		Random random = new Random();
		List<Integer> lacIds = new ArrayList<Integer>();
		int randomNumber = random.nextInt(range);
	   
		SubNetwork subNet = topologyManager.getSubNetwork();
		
		List<Plmn> plmnList = subNet.getPlmnList();
		
		if(plmnList.size() > 0) {
		
			for(Plmn plmn : plmnList) {
				
				List<LocationArea> areas = new ArrayList<LocationArea>();
				
				areas = plmn.getLaList();
				
				if(areas != null)
				if(areas.size() > 0) {
				
				for(LocationArea area : areas)
					lacIds.add(area.getLac());
				}
			}
		}

		
		while(lacIds.contains(randomNumber))
			randomNumber = random.nextInt(range);
		
		return randomNumber;
	}
	
	/*
	 * Non Unique Lac Id
	 */
	private int getNonUniqueID() {
		
		
		List<Integer> lacIds = new ArrayList<Integer>();
		SubNetwork subNet = topologyManager.getSubNetwork();
		
		List<Plmn> plmnList = subNet.getPlmnList();
		
		if(plmnList.size() > 0) {
		
			for(Plmn plmn : plmnList) {
				
				List<LocationArea> areas = new ArrayList<LocationArea>();
				
				areas = plmn.getLaList();
				if(areas != null)
				if(areas.size() > 0) {
				
				for(LocationArea area : areas)
					lacIds.add(area.getLac());
					
				}
			}
		}
		if(lacIds.size()>0)
		return lacIds.get(0);
		
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
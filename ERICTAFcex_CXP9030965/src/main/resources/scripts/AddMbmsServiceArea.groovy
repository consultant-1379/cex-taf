import java.util.HashSet;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import ca.odell.glazedlists.EventList;

import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.CEXMIMVersion;
import com.ericsson.oss.domain.Plmn;
import com.ericsson.oss.domain.MBMSServiceArea;
import com.ericsson.oss.domain.modetails.ManagedObject;
import com.ericsson.oss.domain.modetails.RANOS_SUBNETWORK_MODEL.MbmsServiceAreaImpl;

//
import java.util.HashSet;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.crud.ui.core.ServiceFacade;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.IJobListener;
//

public class AddMbmsServiceArea{
	private boolean objectCreated = false;
	public String genericItemID;

	final String moType = "MbmsServiceArea";
	
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
	public String userLabel = "RAF_RA";
	public String planName
	//
	private IGenericItem mbmsServiceAreaInstance;
	private CEXMIMVersion cexMimVersion = topologyManager.getSubNetwork().getMoMimVersion();
	private String mbmsServiceAreaFDN;
	private List<IGenericItem> genericItemList = new ArrayList<IGenericItem>();
	private int randomNumber;
	final String modelName = cexMimVersion.getName();
	final String modelVersion = cexMimVersion.getVersion();
	
	
	public String createMbmsServiceArea() {
		
		try{
		randomNumber = getRandomSac();
		
		if(topologyManager.getSubNetwork().getPlmnList().size() > 0)
		{
		genericItemID = topologyManager.getSubNetwork().getPlmnList().get(0).getFdn().toString();
		}else
		{
			return moType + " Not Created : No Plmn list available to handle new " +moType +" Object!";
		}
		
		try {
			mbmsServiceAreaInstance = cmManager.createGenericItem(genericItemID, modelName, modelVersion, moType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	   
		populateAttributes();
		genericItemList.add(mbmsServiceAreaInstance);
		
		final MessageJobInfo addMoJob = CmManager.getInstance().createGenericItem(genericItemList, planName, null);
		
		checkMojobFinished(addMoJob);
		
		if (addMoJob.getState() == JobState.FAILED){
			statusCreate = addMoJob.getAdditionalInfo().toString();
		}
		
	   
		if(addMoJob.getState() == JobState.FINISHED){ 
			statusCreate = "OK :"+moType + " CREATED";
			objectCreated = true;
		}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		
		return "OK"+statusCreate;
		
	}
	
	public String createMbmsServiceAreaWithNonUniSac() {
		
		try{
		randomNumber = getNonUniqueSac();
//		return randomNumber;
		if(topologyManager.getSubNetwork().getPlmnList().size() > 0)
		{
		genericItemID = topologyManager.getSubNetwork().getPlmnList().get(0).getFdn().toString();
		}else
		{
			return moType + " Not Created : No Plmn list available to handle new " +moType +" Object!";
		}
		
		try {
			mbmsServiceAreaInstance = cmManager.createGenericItem(genericItemID, modelName, modelVersion, moType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	   
		populateAttributes();
		genericItemList.add(mbmsServiceAreaInstance);
		
		final MessageJobInfo addMoJob = CmManager.getInstance().createGenericItem(genericItemList, planName, null);
		
		checkMojobFinished(addMoJob);
		
		if (addMoJob.getState() == JobState.FAILED){
			statusCreate = addMoJob.getAdditionalInfo().toString();
		}
		
	   
		if(addMoJob.getState() == JobState.FINISHED){
			statusCreate = moType + " CREATED";
			objectCreated = true;
		}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		
		return statusCreate;
		
	}

	private void populateAttributes(){
					
		attributesMap.put("sac", randomNumber);
	
		dirtyAttributesSet.add("sac");
		
		mbmsServiceAreaInstance.setPropertyValue("attributes", attributesMap);
		mbmsServiceAreaInstance.setPropertyValue("dirtyAttributes", dirtyAttributesSet);
	}
	
	private boolean isMbmsServiceAreaExists(Integer sac, String planName){
		waitForTopologyUpdate(3000);
		
//		ContextProvider.getInstance().contextChanged(ContextProvider.PLANNED_CONFIGURATION_CONTEXT, planName);

		String[] MoType={moType};
		 MessageJobInfo<List> retrieveChildrenMoJob = cellServiceManager.retreiveChildren(topologyManager.getSubNetwork(),MoType, planName);
			checkMojobFinished(retrieveChildrenMoJob);
			if(retrieveChildrenMoJob.getState() == JobState.FINISHED){
				EventList<MbmsServiceAreaImpl> mbmsServiceAreaListAfterCreation = retrieveChildrenMoJob.getList();
				if(mbmsServiceAreaListAfterCreation.size() > 0){
					for(MbmsServiceAreaImpl mbmsServiceArea : mbmsServiceAreaListAfterCreation){
						if(mbmsServiceArea.getSac().equals(sac)){
							mbmsServiceAreaFDN = mbmsServiceArea.getFdn().toString();
							return true;
						}
						 
					}
				}
			}
			else{
				return ("Could not retrieve mos under vplanned configuration");
			}
			
			return false;
			
		
	}
	
	
	public int getNonUniqueSac(){
		try{
			List<Plmn> PlmnList = topologyManager.getSubNetwork().getPlmnList();
			List<ManagedObject> mbmsList;
			
			for (Plmn plmn : PlmnList) {

				mbmsList=plmn.getLaMsaList();
				if(mbmsList.size()<0)
				continue;
				for( ManagedObject mbms: mbmsList){
					if(mbms.toString().contains("LocationArea"))
					continue;
					int sacId =  mbms.getSac();
					if(null!=sacId){
						return sacId;
					}

				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
				
	}
	
	
	private int getRandomSac(){
		int range = 888;
		
		Random random = new Random();
		List<Integer> sacIds = new ArrayList<Integer>();
		int randomNumber = random.nextInt(range);
	   
		String[] MoType = {moType};
		MessageJobInfo<List> retrieveChildrenMoJob = cellServiceManager.retreiveChildren(topologyManager.getSubNetwork(),MoType, planName);
		
		checkMojobFinished(retrieveChildrenMoJob);
		EventList<MbmsServiceAreaImpl> mbmsServiceAreas = retrieveChildrenMoJob.getList();
		
		for(MbmsServiceAreaImpl area : mbmsServiceAreas){
			sacIds.add(area.getSac());
		}
		
		while(sacIds.contains(randomNumber))
			randomNumber = random.nextInt(range);
		
		return randomNumber;
		
				
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
	
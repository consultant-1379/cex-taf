import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ca.odell.glazedlists.EventList;

import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.CEXMIMVersion;
import com.ericsson.oss.domain.LocationArea;
import com.ericsson.oss.domain.RoutingArea;
import com.ericsson.oss.domain.Plmn;
import com.ericsson.oss.domain.ServiceArea;
import com.ericsson.oss.domain.modetails.RANOS_SUBNETWORK_MODEL.ServiceAreaImpl;
/*
 * */
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.crud.ui.core.ServiceFacade;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.IJobListener;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
/*
 *
 */

public class AddServiceArea {

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
	protected CmManager cmManager = CmManager.getInstance();
	protected ServiceFacade serviceFacade = ServiceFacade.getInstance();
	protected String statusCreate = "FAILED";
	protected String statusDelete = "FAILED";
	public String planName = null;

	private boolean objectCreated = false;
	private IGenericItem serviceAreaInstance;
	private CEXMIMVersion cexMimVersion = topologyManager.getSubNetwork().getMoMimVersion();
	private String serviceAreaFDN;
	private List<IGenericItem> genericItemList = new ArrayList<IGenericItem>();
	private int randomNumber;
	public String genericItemID;
	final String modelName = cexMimVersion.getName();
	final String modelVersion = cexMimVersion.getVersion();
	final String moType = "ServiceArea";
	private CellServiceManager cellServiceManager = CellServiceManager.getInstance();
	private String sac;
	private String userLabel;
	
	public String createServiceAreaWithNonUniSac(){
		try {
			randomNumber = getUniqueID();
	
			sac = String.valueOf(randomNumber);

			userLabel = "TestServiceArea" + randomNumber;

			List<LocationArea> areas = new ArrayList<LocationArea>();
			List<LocationArea> areaOid = new ArrayList<LocationArea>();

			outerloop:
			if(topologyManager.getSubNetwork().getPlmnList().size() >  0){
				List<Plmn> plmnList = topologyManager.getSubNetwork().getPlmnList();
				for(Plmn plmn : plmnList){
					if(plmn instanceof Plmn){
						List<LocationArea> locAreaList = topologyManager.getLaList(plmn.getOid());
						
						
						for(LocationArea locArea : locAreaList){
							genericItemID = locArea.getFdn().toString();
							break outerloop;
						}
					}
				}
			}
			else
				return "No Plmn exists";


			genericItemList = new ArrayList<IGenericItem>();

			try {

				serviceAreaInstance = cmManager.createGenericItem(genericItemID, modelName, modelVersion, moType);

			} catch (Exception e) {

				return "Create Generic Item Fail - "+e;
			}

			populateAttributes();
			genericItemList.add(serviceAreaInstance);
			
			final MessageJobInfo addMoJob = cmManager.createGenericItem(genericItemList, planName, null);
			checkMojobFinished(addMoJob);
			waitForTopologyUpdate(5000);

			if(addMoJob.getState().equals(JobState.FAILED))
				return statusCreate = moType +" create failed - " + addMoJob.getAdditionalInfo().toString();

			if(isServiceAreaExists(randomNumber, planName)) {
				statusCreate = "OK";
				objectCreated = true;
			}
			else {
				statusCreate = "FAILED";
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return statusCreate;

	}
	

	

	public void populateAttributes(){

		attributesMap.put("sac", sac);
		attributesMap.put("userLabel", userLabel);

		dirtyAttributesSet.add("sac");
		dirtyAttributesSet.add("userLabel");

		serviceAreaInstance.setPropertyValue("attributes", attributesMap);
		serviceAreaInstance.setPropertyValue("dirtyAttributes", dirtyAttributesSet);
	}

	public int getRandomSac(){
		int range = 65535;

		Random random = new Random();
		List<Integer> sacIds = new ArrayList<Integer>();
		int randomNumber = random.nextInt(range);

		List<Plmn> plmnList = topologyManager.getSubNetwork().getPlmnList();

		if(plmnList.size() > 0) {

			for(Plmn plmn : plmnList) {

				List<LocationArea> areas = new ArrayList<LocationArea>();

				areas = plmn.getLaList();
				if(areas != null)
					if(areas.size() > 0) {

						List<ServiceArea> serviceAreas = new ArrayList<ServiceArea>();

						for(LocationArea area : areas){
							serviceAreas = area.getSaList();
							if(serviceAreas != null)
								if(serviceAreas.size() > 0) {
									for (ServiceArea sa : serviceAreas)
										sacIds.add(sa.getSac());
								}
						}
					}
			}
		}

		while(sacIds.contains(randomNumber))
			randomNumber = random.nextInt(range);

		return randomNumber;


	}

	/*
	 * For Non Unique Id
	 */
	private int getNonUniqueSac(){
		try{
			List<Integer> sacIds = new ArrayList<Integer>();

			List<Plmn> plmnList = topologyManager.getSubNetwork().getPlmnList();
			sleep(1000);
			if(plmnList.size() > 0) {
				for(Plmn plmn : plmnList) {
					List<LocationArea> areas = new ArrayList<LocationArea>();
					areas = plmn.getLaList();
					if(areas != null)
						if(areas.size() > 0) {
							List<ServiceArea> serviceAreas = new ArrayList<ServiceArea>();
							for(LocationArea area : areas){
								serviceAreas = area.getSaList();
								if(serviceAreas != null)
									if(serviceAreas.size() > 0) {
										for (ServiceArea sa : serviceAreas)
											sacIds.add(sa.getSac());
									}
							}
						}
				}
			}
			if(sacIds.size()>0)
				return sacIds.get(0);
		}
		catch(Exception e){
			return e.getMessage().toString();
		}


	}
	
	private int getUniqueID() {
		int range = 65535;
		Random random = new Random();
		List<Integer> sacIds = new ArrayList<Integer>();
		int randomNumber = random.nextInt(range);
		String[] MoType = {moType};
		MessageJobInfo<List> retrieveChildrenMoJob = cellServiceManager.retreiveChildren(topologyManager.getSubNetwork(),MoType, planName);
		checkMojobFinished(retrieveChildrenMoJob);
		EventList<ServiceAreaImpl> serviceAreas = retrieveChildrenMoJob.getList();
		for(ServiceAreaImpl area : serviceAreas){
			sacIds.add(area.getSac());
		}
		
		
		while(sacIds.contains(randomNumber))
			randomNumber = random.nextInt(range);
			
		
		return randomNumber;
	}

	private boolean isServiceAreaExists(int randomNumber2, String planName){
		String[] MoType = {moType};
		MessageJobInfo<List> retrieveChildrenMoJob = cellServiceManager.retreiveChildren(topologyManager.getSubNetwork(),MoType, planName);
		checkMojobFinished(retrieveChildrenMoJob);
		waitForTopologyUpdate(3000);
		if(topologyManager.getSubNetwork().getPlmnList().size() >  0){
			List<Plmn> plmnList = topologyManager.getSubNetwork().getPlmnList();
			for(Plmn plmn : plmnList){
				if(plmn instanceof Plmn){
					List<LocationArea> locAreaList = topologyManager.getLaList(plmn.getOid());
					for(LocationArea locArea : locAreaList){
						List<ServiceArea> serviceAreaList = locArea.getSaList();
						if(serviceAreaList.size() > 0){
							for(ServiceArea serviceArea : serviceAreaList){
								waitForTopologyUpdate(1000);
								if(serviceArea.getSac().equals(randomNumber)){
									serviceAreaFDN = serviceArea.getFdn().toString();
									return true;
								}
							}
						}
					}

				}
			}
		}

		return false;



	}

	/*
	 *
	 * Mo Creud
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


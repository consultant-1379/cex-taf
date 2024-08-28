
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.ConnectionState;
import com.ericsson.oss.domain.LocationArea;
import com.ericsson.oss.domain.RNC;
import com.ericsson.oss.domain.ServiceArea;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.SyncState;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.domain.RBS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.ericsson.oss.cex.crud.ui.core.ServiceFacade;
import com.ericsson.oss.cp.core.OCP;
import com.ericsson.oss.domain.modetails.IManagedObject;
import org.eclipse.core.runtime.Platform;

class UtranCellCRUD  {

	private static final String RNCNODEMODEL = "RNC_NODE_MODEL";
	private  String MO_TYPE = "UtranCell";
	protected IGenericItem utranCellInstance;
	protected IGenericItem utranCellNegativeInstance;
	private RNC rncMo=null;
	String newCreatedCellfdn;
	String rncFunctionFdn = null;
	private boolean objectCreated = false;
	protected HashSet<Object> dirtyAttributesSet = new HashSet<Object>();
	protected Map<String,Object> attributesMap = new HashMap<String,Object>();
	String rncMimVersion = null;
	Random random = new Random();

	public def getRnc(String nodeType){

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		List<RNC> rncList = subNetwork.getRncList();

		switch(nodeType.toLowerCase()){
			case("rnc"):
				for(RNC rnc : rncList) {
					for(RBS rbs : rnc.getRbsList()) {
						if (rbs.isConnected() && !rbs.isPicoRbs() && !rbs.isSsrRbs()) {
							rncMimVersion = rnc.getNeMIMversion().toString();
							return rnc;
						}
					}
				}
				break;
			case("pico-rnc"):
				for(RNC rnc : rncList) {
					for(RBS rbs : rnc.getRbsList()) {
						if(rbs.isConnected() && rbs.isPicoRbs())
							rncMimVersion = rnc.getNeMIMversion().toString();
						return rnc;
					}
				}
		}
		return null;
	}
	public String getUtranCellInstance(String nodeType) {

		rncMo = getRnc(nodeType);

		rncFunctionFdn = rncMo.getFdn().toString() + ",ManagedElement=1,RncFunction=1";

		try
		{
			utranCellInstance = CmManager.getInstance().createGenericItem(rncFunctionFdn, RNCNODEMODEL, rncMimVersion, MO_TYPE);
			
			sleep(5000);
			setUtranCellInstance();
			
			return attributesMap.toString();
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
	}

	public def createUtranCell(){

		List<IManagedObject> mos = new ArrayList<IManagedObject>();
		IManagedObject newManagedObject = (IManagedObject)Platform.getAdapterManager().getAdapter(utranCellInstance, IManagedObject.class);

		mos.add(newManagedObject);

		final MessageJobInfo addMoJob = ServiceFacade.getInstance().createMo(mos, OCP.getUserId(), null);
		checkJobFinished(addMoJob);

		if (addMoJob.getState() == JobState.FAILED){
			return addMoJob.getAdditionalInfo().toString();
		}

		return "OK";
	}


	public String deleteUtranCell() {
		
		sleep(5000);
		String userLabel = attributesMap.get("userLabel");
		newCreatedCellfdn = null;
		List<UtranCell> newUtranCellList = rncMo.getUtranCellList();

		for (UtranCell uCell : newUtranCellList){
			if (uCell.getUserLabel().trim().toString().contains(userLabel.trim().toString())){
			newCreatedCellfdn = uCell.getFdn().toString();
			break;
			}
		}
		utranCellInstance.setId(newCreatedCellfdn);
		utranCellInstance.setName(newCreatedCellfdn);
		IGenericItem[] genericItemArray = new GenericItem[1];

		genericItemArray[0] = utranCellInstance;

		final MessageJobInfo addMoJob2 = CmManager.getInstance().deleteMo(genericItemArray, null);
		checkJobFinished(addMoJob2);

		if (addMoJob2.getState() == JobState.FAILED){
			return addMoJob2.getAdditionalInfo().toString();
		}

		return "OK";
	}

	private void setUtranCellInstance(){

		Integer tcell = 1;
		Integer la = 1;
		Integer sa = 1;
		String iubLinkId = null;
		ServiceArea sac = null;
		Integer localCellId = random.nextInt(268435455);
		Integer ucellId = random.nextInt(65535);

		if (rncMo != null) {
			iubLinkId = rncMo.getIuBcLinkId();

			List<UtranCell> utranCellList = rncMo.getUtranCellList();
			List<Integer> cIdList = new ArrayList<Integer>();
			List<Integer> localCellIdList = new ArrayList<Integer>();
			for (UtranCell uCell : utranCellList) {
				cIdList.add(uCell.getCId());
				localCellIdList.add(uCell.getLocalCellId());
			}

			boolean isUniqueCellIdFound = false;
			while (!isUniqueCellIdFound) {
				if (cIdList.contains(ucellId)) {
					ucellId = random.nextInt(65535);
				} else {
					isUniqueCellIdFound = true;
					break;
				}
			}
			boolean isUniqueLocalCellIdFound = false;
			while (!isUniqueLocalCellIdFound) {
				if (localCellIdList.contains(localCellId)) {
					localCellId = random.nextInt(268435455);
				} else {
					isUniqueLocalCellIdFound = true;
				}
			}

			List<LocationArea> laList = TopologyManager.getInstance().getLaList(rncMo.getPlmnOId());

			for (LocationArea lac :laList){
				if (lac != null){
					la = lac.getLac();
				}
				List<ServiceArea> saList = lac.getSaList();
				sleep(1000);
				if (saList.size() > 0) {
					sac = saList.get(0);
					if (sac != null){
						sa = sac.getSac();
						break;
					}
				}
			}
		}

		String iubLinkFdn = rncFunctionFdn + ",IubLink=" + iubLinkId;

		attributesMap.clear();
		dirtyAttributesSet.clear();

		attributesMap.put("uarfcnDl", "5");
		attributesMap.put("uarfcnUl", "5");
		attributesMap.put("sac", sa);
		attributesMap.put("tCell", tcell);
		attributesMap.put("userLabel", "TAF_CELL_"+ucellId);
		attributesMap.put("localCellId", localCellId);
		attributesMap.put("primaryScramblingCode", "5");
		attributesMap.put("utranCellIubLink", iubLinkFdn);
		attributesMap.put("sib1PlmnScopeValueTag", "5");
		attributesMap.put("cId", ucellId);
		attributesMap.put("lac", la);

		dirtyAttributesSet.add("userLabel");
		dirtyAttributesSet.add("uarfcnDl");
		dirtyAttributesSet.add("uarfcnUl");
		dirtyAttributesSet.add("UtranCellId");
		dirtyAttributesSet.add("sac");
		dirtyAttributesSet.add("tCell");
		dirtyAttributesSet.add("localCellId");
		dirtyAttributesSet.add("primaryScramblingCode");
		dirtyAttributesSet.add("utranCellIubLink");
		dirtyAttributesSet.add("sib1PlmnScopeValueTag");
		dirtyAttributesSet.add("cId");
		dirtyAttributesSet.add("lac");

		utranCellInstance.setPropertyValue("attributes", attributesMap);
		utranCellInstance.setPropertyValue("dirtyAttributes", dirtyAttributesSet);
	}

	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}

}
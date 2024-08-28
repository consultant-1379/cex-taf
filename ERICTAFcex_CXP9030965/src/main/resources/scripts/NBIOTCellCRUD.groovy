import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.Platform;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.FDN;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.domain.modetails.IManagedObject;
import com.ericsson.oss.cex.crud.ui.core.ServiceFacade;
import com.ericsson.oss.cp.core.OCP;
import com.ericsson.oss.domain.NbIoTCell;

class NBIOTCellCRUD {

	protected IGenericItem NbiotCellInstance;
	public List<IGenericItem> genericItemList  =  new ArrayList<IGenericItem>();
	private String MO_TYPE = null;
	String nodeModel="ERBS_NODE_MODEL";
	MessageJobInfo addMoJob;
	protected HashSet<Object> dirtyAttributesSet = new HashSet<Object>();
	protected Map<String,Object> dirtyAttributesLogMap = new HashMap<String,Object>();
	protected Map<String,Object> attributesMap = new HashMap<String,Object>();
	private ERbs erbsMo=null;
	private EUtranCell cell = null;
	String erbsFunctionFdn = null;
	String erbsMimVersion;
	String erbsfdn = null;
	private final String TDD_ADD_ON = ",ManagedElement=1,SectorEquipmentFunction=";
	private final String ADD_ON = ",ManagedElement=1,ENodeBFunction=1,SectorCarrier=1";
	Integer TAC = 0;
	Random random = new Random();
	List<String> filteredList;
	List<ERbs> erbsList;


	public def getNbiotCell(String cellType){

		filteredList  = new ArrayList<String>();
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
		}

		switch(cellType){

			case("MACRO"):

				for(ERbs erbs : erbsList){
					if(!erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.isConnected() && erbs.getNbIoTCellList().size() > 0){
						for(NbIoTCell cell : erbs.getNbIoTCellList()){

							filteredList.add(cell.getFdn().toString());
						}
					}
				}
				if(filteredList.size() > 0){
					return filteredList.get(random.nextInt(filteredList.size()));
				}else {
					return "No NBIOT Cell Present";
				}
				break;
			case("SSR") :
				for(ERbs erbs : erbsList){
					if(erbs.isSsrRbs() && erbs.isConnected() && erbs.getNbIoTCellList().size() > 0){
						for(NbIoTCell cell : erbs.getNbIoTCellList()){

							filteredList.add(cell.getFdn().toString());
						}
					}
				}
				if(filteredList.size() > 0){
					return filteredList.get(random.nextInt(filteredList.size()));
				}else {
					return "No NBIOT Cell Present";
				}
				break;
		}
		return null;
	}

	public def getErbs(String nodeType){

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		List<ERbs> erbsList;
		if (subNetwork != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subNetwork);
		} else {
			return "No Subnetwork with ERBS";
		}
		switch(nodeType){

			//Implement for the earfcndl(67136-67335) and earfcnul(0) within range of band 66 value
			case("NBIOTCell"):
				nodeType = "NbIotCell";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){

						for(EUtranCell Ecell : erbs.getCellList()){

							cell = Ecell;
							TAC = random.nextInt(65535);
							erbsMo = erbs;
							MO_TYPE = nodeType;
							return true;

						}
					}
				}
			case("SSR-NBIOTCell"):
				nodeType = "NbIotCell";

				for(ERbs erbs : erbsList){
					if(erbs.isConnected() && erbs.isSsrRbs() && (erbs.getMirrorRelease().compareTo("16B") >= 0)){

						for(EUtranCell Ecell : erbs.getCellList()){
							cell = Ecell;
							TAC = random.nextInt(65535);
							erbsMo = erbs;
							MO_TYPE = nodeType;
							return true;
						}
					}
				}
		}
	}
	public def getNbiotCellInstance(String nodeType){

		try {

			getErbs(nodeType);

			erbsfdn = erbsMo.getFdn().toString();

			erbsMimVersion = erbsMo.getMoMimVersion().getVersion();



			if(nodeType.equals("SSR-NBIOTCell")){
				erbsFunctionFdn = erbsfdn+",ManagedElement=" +erbsfdn.substring(erbsfdn.lastIndexOf('=')+1 )+ ",ENodeBFunction=1";
				nodeModel = "Lrat";
			}else {
				erbsFunctionFdn = erbsfdn+",ManagedElement=1,ENodeBFunction=1";
			}
			NbiotCellInstance = CmManager.getInstance().createGenericItem(erbsFunctionFdn,nodeModel,erbsMimVersion,MO_TYPE);

			sleep(5000);

			updateNbiotCellInstance(nodeType);

			return attributesMap.toString() + "\nMO_TYPE="+MO_TYPE+"\nErbsMO="+erbsfdn;

		} catch (Exception e) {
			return  e.getMessage().toString();
		}
	}

	public String createNbiotCell(){
return "OK";
		List<IManagedObject> mos = new ArrayList<IManagedObject>();
		IManagedObject newManagedObject = (IManagedObject)Platform.getAdapterManager().getAdapter(NbiotCellInstance, IManagedObject.class);

		mos.add(newManagedObject);

		final MessageJobInfo addMoJob = ServiceFacade.getInstance().createMo(mos, OCP.getUserId(), null);

		checkJobFinished(addMoJob);

		if (addMoJob.getState() == JobState.FAILED){
			return addMoJob.getAdditionalInfo().toString();
		}

		return "OK";
	}

	public def deleteNbiotCell() {

		sleep(6000);
		
		int count = 0;
		String nbiotCell = getNbiotCell("MACRO");

		NbiotCellInstance.setId(nbiotCell);
		NbiotCellInstance.setName(nbiotCell);

		IGenericItem[] genericItemArray = new GenericItem[1];

		genericItemArray[0] = NbiotCellInstance;

		final MessageJobInfo addMoJob = CmManager.getInstance().deleteMo(genericItemArray, null);
		checkJobFinished(addMoJob);
		
		count++;
		
		if (addMoJob.getState() == JobState.FAILED){
			return addMoJob.getAdditionalInfo().toString();
		}

	return "Deleted count="+count+"Deleted Cell = "+nbiotCell;
	
}

	//	public def cleanUpEUtranCell() {
	//
	//		List<EUtranCell> eUtranCellList;
	//		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
	//		String newCreatedCellfdn = null;
	//		int count = 0;
	//		eUtranCellList = TopologyManager.getInstance().getECellList(subNetwork);
	//		Set<EUtranCell> eUtranCellSet = new HashSet<EUtranCell>(eUtranCellList);
	//
	//		for (EUtranCell cell : eUtranCellSet) {
	//			if(cell.getUserLabel().toString().contains("TAF_CELL")){
	//
	//				newCreatedCellfdn = cell.getFdn().toString();
	//
	//				EUtranCellInstance.setId(newCreatedCellfdn);
	//				EUtranCellInstance.setName(newCreatedCellfdn);
	//
	//				IGenericItem[] genericItemArray = new GenericItem[1];
	//
	//				genericItemArray[0] = EUtranCellInstance;
	//
	//				final MessageJobInfo addMoJob = CmManager.getInstance().deleteMo(genericItemArray, null);
	//				checkJobFinished(addMoJob);
	//				count++;
	//				//				if (addMoJob.getState() == JobState.FAILED){
	//				//					return addMoJob.getAdditionalInfo().toString();
	//				//				}
	//			}
	//		}
	//		return "Deleted count="+count;
	//	}

	private void updateNbiotCellInstance(String nodeType){

		Integer phyLyrCellIdGroup = 1;
		Integer phyLyrCellId = 0;
		Integer earfcndl = 123;

		Integer cellId = random.nextInt(255);
		Integer nbIotCellType = random.nextInt(4);

		if (erbsMo != null) {
			List<EUtranCell> eutranCellList = erbsMo.getCellList();
			List<Integer> cIdList = new ArrayList<Integer>();
			for (EUtranCell euCell : eutranCellList) {
				cIdList.add(euCell.getCellId());
			}

			boolean isUniqueCellIdFound = false;
			while (!isUniqueCellIdFound) {
				if (cIdList.contains(cellId)) {
					cellId = random.nextInt(255);
				} else {
					isUniqueCellIdFound = true;
					break;
				}
			}
		}
		boolean type = false;

		while (!type) {
			if(nbIotCellType==0){
				nbIotCellType = random.nextInt(4);
			}else {
				type = true;
			}
		}


		attributesMap.clear();
		dirtyAttributesSet.clear();

		attributesMap.put("cellId", cellId);
		attributesMap.put("physicalLayerCellIdGroup", phyLyrCellIdGroup);
		attributesMap.put("physicalLayerCellId", phyLyrCellId);
		attributesMap.put("tac", TAC);
		attributesMap.put("earfcndl", earfcndl);
		attributesMap.put("nbIotCellType", nbIotCellType);
		attributesMap.put("eutranCellRef", cell.getFdn().toString());
		attributesMap.put("userLabel", "TAF_CELL_"+cellId);


		dirtyAttributesSet.add("userLabel");
		dirtyAttributesSet.add("cellId");
		dirtyAttributesSet.add("nbIotCellType");
		dirtyAttributesSet.add("physicalLayerCellId");
		dirtyAttributesSet.add("physicalLayerCellIdGroup");
		dirtyAttributesSet.add("tac");
		dirtyAttributesSet.add("earfcndl");
		dirtyAttributesSet.add("eutranCellRef");


		NbiotCellInstance.setPropertyValue("attributes", attributesMap);
		NbiotCellInstance.setPropertyValue("dirtyAttributes", dirtyAttributesSet);

	}
	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}
}

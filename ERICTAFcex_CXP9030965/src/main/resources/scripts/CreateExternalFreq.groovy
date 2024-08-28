
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean
import com.ericsson.oss.domain.modetails.ManagedObject;
import com.ericsson.oss.domain.ExternalPlmn;
import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.Direction;
import com.ericsson.oss.domain.CEXMIMVersion;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.ExternalGsmFreq;
import com.ericsson.oss.domain.ExternalUtranFreq;
import com.ericsson.oss.domain.ExternalEutranFrequency;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.domain.modetails.RANOS_SUBNETWORK_MODEL.ExternalUtranFreqImpl;
import com.ericsson.oss.domain.modetails.IManagedObject;
import com.ericsson.oss.cp.core.OCP;
import org.eclipse.core.runtime.Platform;
import com.ericsson.oss.cex.crud.ui.core.ServiceFacade;
import com.ericsson.oss.domain.EUtranFrequency;
import com.ericsson.oss.domain.ExternalCdma2000FreqBand;
import com.ericsson.oss.domain.ExternalCdma2000Freq;
import com.ericsson.oss.domain.HrpdBandClass;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;

class CreateExternalFreq {

	public TopologyManager topologyManager = TopologyManager.getInstance();
	public CmManager cmManager = CmManager.getInstance();
	public IGenericItem ExternalUtranCellInstance;
	private IGenericItem ExternalFreqInstance;
	String genericItemID;
	public List<IGenericItem> genericItemList  =  new ArrayList<IGenericItem>();
	protected HashSet<Object> dirtyAttributesSet = new HashSet<Object>();
	protected Map<String,Object> attributesMap = new HashMap<String,Object>();
	CEXMIMVersion cexMimVersion = topologyManager.getSubNetwork().getMoMimVersion();
	String modelName = cexMimVersion.getName();
	String modelVersion = cexMimVersion.getVersion();
	MessageJobInfo addMoJob;
	List<RBS> rbsList;
	List<ExternalUtranFreq> extUtranfreqList;
	List<ExternalEutranFrequency> extfreqList;
	private Integer DlValue = 0;
	private String userLabel;
	private String userLabelID = "";
	private String moType;  //Should match with the Managed Object
	List<Integer> linkValueList =new ArrayList<Integer>();
	Random random = new Random();
	Integer freqGroupId = 0;
	private ArrayList<ExternalCdma2000FreqBand> externalCdma2000FreqBandList;
	private Integer bandValue = 0;
	List<ExternalCdma2000Freq> extCdma2000freqList;

	public def createExtCell(){

		moType = "ExternalUtranCell";
		genericItemID = topologyManager.getSubNetwork().getFdn().toString();

		try {

			ExternalUtranCellInstance = cmManager.createGenericItem(genericItemID,modelName,modelVersion,moType);

		} catch (Exception e) {
			return  e.getMessage().toString();
		}

		genericItemList.add(ExternalUtranCellInstance);

		try{
			addMoJob = cmManager.createGenericItem(genericItemList, null, null);
			checkJobFinished(addMoJob);

			if (addMoJob.getState() == JobState.FAILED){
				return addMoJob.getAdditionalInfo().toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
		return "OK";
	}

	public def createExtFreq(String elementType){

		switch(elementType){
			case("ExternalUtranFreq"):
				moType = "ExternalUtranFreq";
				break;
			case("ExternalEutranFrequency"):
				moType = "ExternalEutranFrequency";
				break;
			case("ExternalGsmFreqGroup"):
				moType = "ExternalGsmFreqGroup";
				break;
			case("ExternalGsmFreq"):
				moType = "ExternalGsmFreq";
				break;
			case("EUtranFrequency"):
				moType = "EUtranFrequency";
				break;
			case("ExternalCdma2000FreqBand"):
				moType = "ExternalCdma2000FreqBand";
				getExternalCdma2000FreqBandList();

				List<Integer> bandValueList = new ArrayList<Integer>();

				if(externalCdma2000FreqBandList.size()<16){
					for(ExternalCdma2000FreqBand bandList : externalCdma2000FreqBandList){
						bandValueList.add(bandList.getHrpdBandClass());
					}
					bandValue = random.nextInt(17);
					while(bandValueList.contains(bandValue))
					{
						bandValue= random.nextInt(17);

					}
				}
				else {
					return "OK";   //If the list is more that the requirement [17] , No need to create ExternalCdma2000FreqBand
				}
				break;
			case("ExternalCdma2000Freq"):

				moType="ExternalCdma2000Freq";

				genericItemID = createGenericItemId();

				break;

		}
		if(!elementType.equals("ExternalCdma2000Freq")){
			genericItemID = topologyManager.getSubNetwork().getFdn().toString()+",FreqManagement=1";
		}

		try {

			ExternalFreqInstance = cmManager.createGenericItem(genericItemID,modelName,modelVersion,moType);

		} catch (Exception e) {
			return  e.getMessage().toString();
		}

		updateExternalFreqInstance(elementType);

		try{
			List<IManagedObject> mos = new ArrayList<IManagedObject>();

			IManagedObject newManagedObject = (IManagedObject)Platform.getAdapterManager().getAdapter(ExternalFreqInstance, IManagedObject.class);
			mos.add(newManagedObject);

			final MessageJobInfo addMoJob = ServiceFacade.getInstance().createMo(mos, OCP.getUserId(), null);

			checkJobFinished(addMoJob);

			if (addMoJob.getState() == JobState.FAILED){
				return addMoJob.getAdditionalInfo().toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
		return "OK";
	}

	private void updateExternalFreqInstance(String elementType) {

		switch(elementType){
			case("ExternalUtranFreq"):
				attributesMap.put("ExternalUtranFreqId", userLabelID);
				attributesMap.put("arfcnValueUtranDl", DlValue);

				dirtyAttributesSet.add("ExternalUtranFreqId");
				dirtyAttributesSet.add("arfcnValueUtranDl");
				break;
			case("ExternalEutranFrequency"):

				int mcc = random.nextInt(999);      // 0 to 999
				int mnc = random.nextInt(999);      // 0 to 999
				int mncLength = random.nextInt(3); // 2 to 3
				HashMap<String, Integer> plmnMap = new HashMap<String, Integer>();
				plmnMap.put("mcc", 1);
				plmnMap.put("mnc", 1);
				plmnMap.put("mncLength", 2);

				attributesMap.put("ExternalEutranFrequencyId", userLabelID);
				attributesMap.put("earfcnDl", DlValue);
				attributesMap.put("plmnIdentity", plmnMap);

				dirtyAttributesSet.add("ExternalEutranFrequencyId");
				dirtyAttributesSet.add("earfcnDl");
				dirtyAttributesSet.add("plmnIdentity");
				break;
			case("ExternalGsmFreqGroup"):

				freqGroupId = random.nextInt(1024)%100+50;	//Skipping to generate 0-49 as freqGroupId (already present on server)
				userLabel = "TAF_ExternalGsmFreqGroup-"+freqGroupId

				attributesMap.put("frequencyGroupId", freqGroupId);

				dirtyAttributesSet.add("frequencyGroupId");
				break;
			case("ExternalGsmFreq"):

				List<Integer> groupList = new ArrayList<Integer>();
				groupList.add(freqGroupId);   //Same as frequencyGroupId

				attributesMap.put("ExternalGsmFreqId", userLabelID);
				attributesMap.put("arfcnValueGeranDl", DlValue);
				attributesMap.put("externalGsmFreqGroupId", groupList);

				dirtyAttributesSet.add("ExternalGsmFreqId");
				dirtyAttributesSet.add("externalGsmFreqGroupId");
				dirtyAttributesSet.add("arfcnValueGeranDl");
				break;
			case("EUtranFrequency"):

				attributesMap.put("arfcnValueEUtranDl", DlValue);

				dirtyAttributesSet.add("arfcnValueEUtranDl");
				break;
			case("ExternalCdma2000FreqBand"):

				userLabel = "TAF_ExternalCdma2000FreqBand-"+freqGroupId

				attributesMap.put("cdmaBandClass", bandValue);

				dirtyAttributesSet.add("cdmaBandClass");

				break;
			case("ExternalCdma2000Freq"):

				attributesMap.put("freqCdma", String.valueOf(DlValue));

				userLabel = "TAF_ExternalCdma2000Freq-"+freqGroupId

				dirtyAttributesSet.add("freqCdma");

				break;


		}
		attributesMap.put("userLabel", userLabel);
		dirtyAttributesSet.add("userLabel");
		ExternalFreqInstance.setPropertyValue("attributes", attributesMap);
		ExternalFreqInstance.setPropertyValue("dirtyAttributes", dirtyAttributesSet);


	}

	public String setLinkValues(String elementType,String type,String dlValue) {

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();


		switch(elementType){
			case("ExternalUtranFreq"):
				linkValueList.clear();
				if (subN != null) {
					extUtranfreqList = TopologyManager.getInstance().getExternalUtranFreq(subN);
					if(extUtranfreqList == null || extUtranfreqList.isEmpty()) {
						return "No ExtUtranfreqList Exist";
					}
				} else{
					return "No SubNetwork Exist";
				}
				for(ExternalUtranFreq extfreq : extUtranfreqList){
					for(Integer value : extfreq.getArfcnValueUtranDl()){
						linkValueList.add(value);
					}
				}
				Integer randomValue = random.nextInt(16384);
				if(type.equals("UtranCell") || type.equals("ExternalUtranCellFDD")){
					randomValue = Integer.parseInt(dlValue);   //Implementation for create ExternalFreq
				}
				while(linkValueList.contains(randomValue))
				{
					randomValue= random.nextInt(16384);

				}
				DlValue = randomValue;
				userLabelID = userLabelID + randomValue;
				userLabel = "TAF_ExternalUtranFreq-"+randomValue;
				break;
			case("ExternalEutranFrequency"):
				linkValueList.clear();
				if (subN != null) {
					extfreqList = TopologyManager.getInstance().getExternalEutranFrequency(subN);
					if(extfreqList == null || extfreqList.isEmpty()) {
						return "No ExternalEutranFreq Exist";
					}
				} else{
					return "No SubNetwork Exist";
				}
				for(ExternalEutranFrequency extfreq : extfreqList){
					for(Integer value : extfreq.getEarfcnDl()){
						linkValueList.add(value);
					}
				}
				Integer randomValue = random.nextInt(65536);
				while(linkValueList.contains(randomValue))
				{
					randomValue= random.nextInt(65536);

				}
				DlValue = randomValue;
				userLabelID = userLabelID + randomValue;
				userLabel = "TAF_ExternalEutranFreq-"+randomValue;
				break;
			case("ExternalGsmFreq"):
				linkValueList.clear();
				if (subN != null) {
					extfreqList = TopologyManager.getInstance().getExternalGsmFreq(subN);
					if(extfreqList == null || extfreqList.isEmpty()) {
						return "No ExternalGsmFreq Exist";
					}
				} else{
					return "No SubNetwork Exist";
				}
				for(ExternalGsmFreq extfreq : extfreqList){
					for(Integer value : extfreq.getArfcnValueGeranDl()){
						linkValueList.add(value);
					}
				}
				Integer randomValue = random.nextInt(1023);
				while(linkValueList.contains(randomValue))
				{
					randomValue= random.nextInt(1023);

				}
				DlValue = randomValue;
				userLabelID = userLabelID + randomValue;
				userLabel = "TAF_ExternalGsmFreq-"+randomValue;
				break;
			case("EUtranFrequency"):
				linkValueList.clear();
				if (subN != null) {
					extfreqList = TopologyManager.getInstance().getEUtranFrequency(subN);
					if(extfreqList == null || extfreqList.isEmpty()) {
						return "No EUtranFrequency Exist";
					}
				} else{
					return "No SubNetwork Exist";
				}
				for(EUtranFrequency extfreq : extfreqList){
					for(Integer value : extfreq.getArfcnValueEUtranDl()){
						linkValueList.add(value);
					}
				}
				Integer randomValue = random.nextInt(17999);
				if(type.equals("EUtranCellTDD") || type.equals("EUtranCellFDD")){
					randomValue = Integer.parseInt(dlValue);   //Implementation for create ExternalFreq
				}
				while(linkValueList.contains(randomValue))
				{
					randomValue= random.nextInt(17999);

				}
				DlValue = randomValue;
				userLabelID = userLabelID + randomValue;
				userLabel = "TAF_EUtranFrequency-"+randomValue;
				break;
			case("ExternalCdma2000Freq"):
				linkValueList.clear();

				if (subN != null) {
					extCdma2000freqList = TopologyManager.getInstance().getExternalCdma2000Freq(subN);
					if(extCdma2000freqList == null || extCdma2000freqList.isEmpty()) {
						return "No ExternalCdma2000Frequency Exist";
					}
				} else{
					return "No SubNetwork Exist";
				}
				for(ExternalCdma2000Freq extfreq : extCdma2000freqList){
					for(Integer value : extfreq.getExternalCdma2000FreqId()){
						linkValueList.add(value);
					}
				}
				Integer randomValue = random.nextInt(2046);
				while(linkValueList.contains(randomValue))
				{
					randomValue= random.nextInt(2046);

				}
				DlValue = randomValue;
				userLabelID = userLabelID + randomValue;
				userLabel = "TAF_ExternalCdma2000freq-"+randomValue;

				break;

			default :

				return 0;
		}
		return DlValue;
	}
	private String createGenericItemId() {

		Random random = new Random();
		ExternalCdma2000FreqBand externalCdma2000FreqBand;

		getExternalCdma2000FreqBandList();
		int index = random.nextInt(externalCdma2000FreqBandList.size());

		if(externalCdma2000FreqBandList.size()>0){
			externalCdma2000FreqBand = externalCdma2000FreqBandList.get(index);
			return externalCdma2000FreqBand.getFdn().toString();
		}

		return null;
	}

	private void getExternalCdma2000FreqBandList() {

		externalCdma2000FreqBandList = new ArrayList<ExternalCdma2000FreqBand>();
		List<ExternalPlmn> externalPlmnList = topologyManager.getNotFilteredExternalPlmns(topologyManager.getSubNetwork());
		for(ManagedObject managedObject : externalPlmnList){
			if(managedObject instanceof ExternalCdma2000FreqBand){
				externalCdma2000FreqBandList.add((ExternalCdma2000FreqBand)managedObject);
			}

		}
	}

	public def deleteExternalCell(String cellFdn){

		GenericItem genericItem = new GenericItem(cellFdn);
		IGenericItem[] genericItemArray = new IGenericItem[1];
		genericItemArray[0] = genericItem;
		MessageJobInfo deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);
		checkJobFinished(deleteRelationJob);

		if (deleteRelationJob.getState() == JobState.FAILED){
			return "failed" ;
		}

		return "OK";

	}
	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}

}

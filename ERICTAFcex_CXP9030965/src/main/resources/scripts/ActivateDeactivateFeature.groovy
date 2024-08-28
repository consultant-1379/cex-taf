
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.common.son.management.service.ISONMgtService;
import com.ericsson.oss.common.son.management.service.domain.FeatureProperties;
import com.ericsson.oss.common.son.management.service.domain.SONFeature;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;

import com.ericsson.oss.utilities.templates.domain.Template;
import com.ericsson.oss.utilities.templates.service.ITemplatesService;
import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.domain.modetails.IManagedObject;
import com.ericsson.oss.cex.cellservice.service.ICellService;
import com.ericsson.oss.cex.cm.core.CmManager;
import com.ericsson.oss.common.son.management.service.ISONParameterMgtService;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cs.miminfo.MetaInfoManagerFactory;
import com.ericsson.oss.common.son.management.ui.service.messaging.MessageBindingService;
import com.ericsson.oss.domain.FDN;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.cex.mim.util.MoObjectFactory;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;

class ActivateDeactivateFeature {

	// Features
	private static final String ULMobility = "ULMobility"; // UL Mobility Feature
	private static final String NBIOT = "NBIOT"; //NarrowBand IoT Access
	private static final String MicroSleepTx = "MicroSleepTx";  // Micro Sleep Tx Feature

	private static CmManager cmManage = CmManager.getInstance();

	String planName = null;
	Collection<String> testNodeFdns=[];
	String[] features;
	ISONMgtService sonMgtService;
	String destination = "";
	String successResult = null;
	String SUCCESS = "OK";

	String moFdn, mcpcFdn;
	String mimName = null;
	String mimVersion = null;
	/*
	 *  Node Version Greater than "G"
	 */
	public def getRequiredFdn(String nodeType){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		List<ERbs> erbsList = new ArrayList<ERbs>();

		if(nodeType.equals("ERBS")) {
			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
				for(ERbs erbs : erbsList){
					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && (erbs.getNeMIMversion().toString().compareTo("vG.1") > 0)){
						return erbs.getFdn().toString();
					}
				}
			}
		}else if(nodeType.equals("SSR-ERBS")) {

			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
				for(ERbs erbs : erbsList){
					if(erbs.isConnected() && erbs.isSsrRbs() && (erbs.getMirrorRelease().toString().compareTo("16B") >= 0)){

						return erbs.getFdn().toString();
					}
				}
			}
		}
		return null;
	}


	public def activateMCPC(String requireFdn,String nodeType) {

		MessageJobInfo<IManagedObject> cmServicejob = null;
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		List<ERbs> erbsList = new ArrayList<ERbs>();



		List<IManagedObject> listofModifiedMo = new ArrayList<IManagedObject>();
		Map<String, Object> valueMap = new HashMap<String, Object>();

		valueMap.put("featureState", 1);

		MoObjectFactory objectFactory = MoObjectFactory.getInstance();
		objectFactory.setMetaDataManager(MetaInfoManagerFactory.getManager());

		if(nodeType.equals("ERBS")) {
			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
				for(ERbs erbs : erbsList){
					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && (erbs.getNeMIMversion().toString().compareTo("vG.1") > 0)){
						if(erbs.getFdn().toString().equals(requireFdn)){

							mcpcFdn = requireFdn + ",ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=MobCtrlAtPoorCov";

							getProperties(requireFdn,nodeType);
							IManagedObject mcpcObject = objectFactory.createCmMo(mcpcFdn,mimName ,mimVersion, valueMap);
							listofModifiedMo.add(mcpcObject);
							break;
						}
					}
				}
			}
		}else if(nodeType.equals("SSR-ERBS")) {

			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
				for(ERbs erbs : erbsList){
					if(erbs.isConnected() && erbs.isSsrRbs() && (erbs.getMirrorRelease().compareTo("16B") >= 0)){

						if(erbs.getFdn().toString().equals(requireFdn)){
							HashSet<String> dirtyAttrList = new HashSet<String>();
							dirtyAttrList.add("featureState");

							mcpcFdn = requireFdn + ",ManagedElement=" +requireFdn.substring(requireFdn.lastIndexOf('=')+1 )+ ",SystemFunctions=1,Lm=1,FeatureState=CXC4011345";

							getProperties(requireFdn,nodeType);
							IManagedObject mcpcObject = cmManage.findManagedObject(mcpcFdn.toString());
							mcpcObject.setAttributes(valueMap);
							mcpcObject.setDirtyAttributes(dirtyAttrList);
							listofModifiedMo.add(mcpcObject);
							break;
						}
					}
				}
			}
		}

		cmServicejob = cmManage.modify(listofModifiedMo, null);

		checkJobFinished(cmServicejob);

		if (cmServicejob.getState() == JobState.FAILED){

			return cmServicejob.getAdditionalInfo().toString();

		}

		return "OK";

	}

	public void getProperties(String requireFdn,String nodeType) {

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		List<ERbs> erbsList = new ArrayList<ERbs>();

		if(nodeType.equals("ERBS")) {
			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
				for(ERbs erbs : erbsList){
					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && (erbs.getNeMIMversion().toString().compareTo("vG.1") > 0)){
						if(erbs.getFdn().toString().equals(requireFdn)){
							mimName = erbs.getMoMimVersion().getName();
							mimVersion = erbs.getMoMimVersion().getVersion();
							break;
						}
					}
				}
			}
		}else if(nodeType.equals("SSR-ERBS")) {

			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
				for(ERbs erbs : erbsList){
					if(erbs.isConnected() && erbs.isSsrRbs() && (erbs.getMirrorRelease().compareTo("16B") >= 0)){
						if(erbs.getFdn().toString().equals(requireFdn)){
							mimName = erbs.getMoMimVersion().getName();
							mimVersion = erbs.getMoMimVersion().getVersion();
							break;
						}
					}
				}
			}
		}
	}

	public def activateDeactivateUlTrig(String operationType,String requireFdn,String nodeType,String featureType) {

		MessageJobInfo<IManagedObject> cmServicejob = null;


		List<IManagedObject> listofModifiedMo = new ArrayList<IManagedObject>();
		Map<String, Object> valueMap = new HashMap<String, Object>();

		if (operationType.equals("activate")) {
			valueMap.put("featureState", 1);
		} else {
			valueMap.put("featureState", 0);
		}

		switch(featureType){

			case (ULMobility) :

				if(nodeType.equals("ERBS")){

					moFdn = requireFdn + ",ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=UlTrigInterFreqMob";

				}else if(nodeType.equals("SSR-ERBS")){

					moFdn = requireFdn + ",ManagedElement=" +requireFdn.substring(requireFdn.lastIndexOf('=')+1 )+ ",SystemFunctions=1,Lm=1,FeatureState=CXC4011072";

				}

				break;
			case (NBIOT) :

				if(nodeType.equals("ERBS")){

					moFdn = requireFdn + ",ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=NarrowbandIoTAccess";
					getProperties(requireFdn,nodeType);

				}else if(nodeType.equals("SSR-ERBS")){

					moFdn = requireFdn + ",ManagedElement=" +requireFdn.substring(requireFdn.lastIndexOf('=')+1 )+ ",SystemFunctions=1,Lm=1,FeatureState=CXC4012081";

				}

				break;
				
			case (MicroSleepTx) : 
				if(nodeType.equals("ERBS")){
					moFdn = requireFdn + ",ManagedElement=1,SystemFunctions=1,Licensing=1,OptionalFeatureLicense=MicroSleepTx";
					getProperties(requireFdn, nodeType);
				}
				else if(nodeType.equals("SSR-ERBS")){
					moFdn = requireFdn + ",ManagedElement=" + requireFdn.substring(requireFdn.lastIndexOf('=')+1) + ",SystemFunctions=1,Lm=1,FeatureState=CXC4011803";
				}
				break;
		}

		if(nodeType.equals("ERBS")){

			MoObjectFactory objectFactory = MoObjectFactory.getInstance();
			objectFactory.setMetaDataManager(MetaInfoManagerFactory.getManager());

			IManagedObject moObject = objectFactory.createCmMo(moFdn,mimName ,mimVersion, valueMap);
			listofModifiedMo.add(moObject);

		}else if(nodeType.equals("SSR-ERBS")){

			HashSet<String> dirtyAttrList = new HashSet<String>();

			IManagedObject erbsObject = cmManage.findManagedObject(moFdn.toString());

			erbsObject.setAttributes(valueMap);
			dirtyAttrList.add("featureState");

			erbsObject.setDirtyAttributes(dirtyAttrList);
			listofModifiedMo.add(erbsObject);

		}

		cmServicejob = cmManage.modify(listofModifiedMo, null);

		checkJobFinished(cmServicejob);

		if (cmServicejob.getState() == JobState.FAILED){

			return cmServicejob.getAdditionalInfo().toString();

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

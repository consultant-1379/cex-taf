
import java.util.Collection;
import java.util.List;

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
import com.ericsson.oss.rps.service.IRBSPowerSaveService;
import com.ericsson.oss.common.son.management.service.ISONParameterMgtService;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.common.son.management.ui.service.messaging.MessageBindingService;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.cex.topology.core.TopologyManager;

class SonFeatures {

	// SON Features
	private static final String LTE_ANR_INTER = "LTE_ANR_INTER";
	private static final String LTE_ANR_INTRA = "LTE_ANR_INTRA";
	private static final String LTE_ANR_GSM = "LTE_ANR_GSM";
	private static final String LTE_ANR_UTRAN = "LTE_ANR_UTRAN";
	private static final String LTE_MRO_ULHOC = "LTE_MRO_ULHOC";
	private static final String LTE_MRO_HO_OPT_CNTRL = "LTE_MRO_HO_OPT_CNTRL";
	private static final String LTE_DRO_PREAMBLE_POWER = "LTE_DRO_PREAMBLE_POWER";
	private static final String LTE_DRO_ROOT_SEQ_ALLOC = "LTE_DRO_ROOT_SEQ_ALLOC";
	private static final String LTE_PCI_CONFLICT_DETECT = "LTE_PCI_CONFLICT_DETECT";
	private static final String LTE_LOAD_BALANCING_INTER_FREQ = "LTE_LOAD_BALANCING_INTER_FREQ";
	private static final String LTE_LOAD_BALANCING_INTRA_FREQ = "LTE_LOAD_BALANCING_INTRA_FREQ";
	private static final String IRAT_OFFLOAD_WCDMA = "IRAT_OFFLOAD_WCDMA";
	private static final String LTE_ADV_CELL_SUP = "LTE_ADV_CELL_SUP";
	private static final String WCDMA_ANR_INTRA = "WCDMA_ANR_INTRA";
	private static final String WCDMA_ANR_RELATION_CREATE = "WCDMA_ANR_RELATION_CREATE";
	private static final String SSLM = "SSLM";
	private static final String LTE_MLSTM = "LTE_MLSTM";
	private static final String LTE_ANR_PLMN_WHITE_LIST = "LTE_ANR_PLMN_WHITE_LIST";

	String planName = null;
	Collection<String> testNodeFdns=[];
	String[] features;
	ISONMgtService sonMgtService;
	String destination = "";
	String successResult = null;
	String SUCCESS = "OK";

	public def activateDeactivateSon(String operationType, String featureType,String requireFdn) {

		String result = null ;
		setPlan();
		testNodeFdns.add(requireFdn);
		getDestination();


		switch (featureType) {
			
			case(LTE_ANR_PLMN_WHITE_LIST):
				features=[SONFeature.getFeatureId(SONFeature.LTE_ANR_INTRA.getFeatureName()),SONFeature.getFeatureId(SONFeature.LTE_ANR_PLMN_WHITE_LIST.getFeatureName())]
				break;
			case (LTE_ANR_INTER):
				features=[SONFeature.getFeatureId(SONFeature.LTE_ANR_INTER.getFeatureName())]
				break;
			case(LTE_ANR_INTRA):
				features=[SONFeature.getFeatureId(SONFeature.LTE_ANR_INTRA.getFeatureName())]
				break;
			case(LTE_ANR_UTRAN):
				features=[SONFeature.getFeatureId(SONFeature.LTE_ANR_UTRAN.getFeatureName())]
				break;
			case(LTE_ANR_GSM):
				features=[SONFeature.getFeatureId(SONFeature.LTE_ANR_GSM.getFeatureName())]
				break;
			case(LTE_PCI_CONFLICT_DETECT):
				features=[SONFeature.getFeatureId(SONFeature.LTE_PCI_CONFLICT_DETECT.getFeatureName())]
				break;
			case(LTE_ADV_CELL_SUP):
				features=[SONFeature.getFeatureId(SONFeature.LTE_ADV_CELL_SUP.getFeatureName())]
				break;
			case(LTE_MRO_ULHOC):
				features=[SONFeature.getFeatureId(SONFeature.LTE_MRO_ULHOC.getFeatureName())]
				break;
			case(LTE_MRO_HO_OPT_CNTRL):
				features=[SONFeature.getFeatureId(SONFeature.LTE_MRO_HO_OPT_CNTRL.getFeatureName())]
				break;
			case(LTE_DRO_PREAMBLE_POWER):
				features=[SONFeature.getFeatureId(SONFeature.LTE_DRO_PREAMBLE_POWER.getFeatureName())]
				break;
			case(LTE_DRO_ROOT_SEQ_ALLOC):
				features=[SONFeature.getFeatureId(SONFeature.LTE_DRO_ROOT_SEQ_ALLOC.getFeatureName())]
				break;
			case(LTE_LOAD_BALANCING_INTER_FREQ):
				features=[SONFeature.getFeatureId(SONFeature.LTE_LOAD_BALANCING_INTER_FREQ.getFeatureName())]
				break;
			case(LTE_LOAD_BALANCING_INTRA_FREQ):
				features=[SONFeature.getFeatureId(SONFeature.LTE_LOAD_BALANCING_INTER_FREQ.getFeatureName())]
				break;
			case(IRAT_OFFLOAD_WCDMA):
				features=[SONFeature.getFeatureId(SONFeature.IRAT_OFFLOAD_WCDMA.getFeatureName())]
				break;
			case(LTE_MLSTM):
				features=[SONFeature.getFeatureId(SONFeature.LTE_MLSTM.getFeatureName())]
				break;
			case(SSLM):
				features=[SONFeature.getFeatureId(SONFeature.SSLM.getFeatureName())]
				break;
			case(WCDMA_ANR_INTRA):
				features=[SONFeature.getFeatureId(SONFeature.WCDMA_ANR_INTRA.getFeatureName())]
				break;
			case(WCDMA_ANR_RELATION_CREATE):
				features=[SONFeature.getFeatureId(SONFeature.WCDMA_ANR_RELATION_CREATE.getFeatureName())]
				break;

		}

		try{

			if(operationType.equals("activate")){
				result = getsonMgtService().activateFeature(destination,testNodeFdns,features, new FeatureProperties(false));
				waitForTopologyUpdate(3000);

			}
			else if (operationType.equals("deactivate")){
				result = getsonMgtService().deactivateFeature(destination, testNodeFdns, features);
				waitForTopologyUpdate(3000);
			}

			if(featureType.contains("ANR") && (result.contains("SON_SERVICE_activateANRFeature") || result.contains("SON_SERVICE_deactivateANRFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_PCI_CONFLICT_DETECT) && (result.contains("SON_SERVICE_activatePCIFeature") ||result.contains("SON_SERVICE_deactivatePCIFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_ADV_CELL_SUP) && (result.contains("SON_SERVICE_activateACSFeature") ||result.contains("SON_SERVICE_deactivateACSFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_MRO_ULHOC) && (result.contains("SON_SERVICE_activateMROFeature") ||result.contains("SON_SERVICE_deactivateMROFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_MRO_HO_OPT_CNTRL) && (result.contains("SON_SERVICE_activateMROFeature") ||result.contains("SON_SERVICE_deactivateMROFeature") )){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_DRO_PREAMBLE_POWER) && (result.contains("SON_SERVICE_activateDROFeature") ||result.contains("SON_SERVICE_deactivateDROFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_LOAD_BALANCING_INTER_FREQ) && (result.contains("SON_SERVICE_activateLoadBalancingFeature") ||result.contains("SON_SERVICE_deactivateLoadBalancingFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_LOAD_BALANCING_INTRA_FREQ) && (result.contains("SON_SERVICE_activateLoadBalancingFeature") ||result.contains("SON_SERVICE_deactivateLoadBalancingFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(IRAT_OFFLOAD_WCDMA) && (result.contains("SON_SERVICE_activateLoadBalancingFeature") ||result.contains("SON_SERVICE_deactivateLoadBalancingFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_DRO_ROOT_SEQ_ALLOC) && (result.contains("SON_SERVICE_activateDROFeature") || result.contains("SON_SERVICE_deactivateDROFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_MLSTM) && (result.contains("SON_SERVICE_activateMLSTMFeature") || result.contains("SON_SERVICE_deactivateMLSTMFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(SSLM) && (result.contains("SON_SERVICE_activateLoadBalancingFeature") || result.contains("SON_SERVICE_deactivateLoadBalancingFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(WCDMA_ANR_INTRA) && (result.contains("SON_SERVICE_activateANRFeature") || result.contains("SON_SERVICE_deactivateANRFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(WCDMA_ANR_RELATION_CREATE) && (result.contains("SON_SERVICE_activateANRFeature") ||result.contains("SON_SERVICE_deactivateANRFeature"))){
				successResult = SUCCESS;
			}else if (featureType.equals(LTE_ANR_PLMN_WHITE_LIST) && (result.contains("SON_SERVICE_activateANRFeature") ||result.contains("SON_SERVICE_deactivateANRFeature"))){
				successResult = SUCCESS;
			}

		}catch (Exception ex) {
			return ex.getMessage();
		}
		return successResult;
	}

	public def setPlan(){

		ContextProvider.getInstance().contextChanged(ContextProvider.PLANNED_CONFIGURATION_CONTEXT, planName);
		waitForTopologyUpdate(3000);
	}

	public def waitForTopologyUpdate(final int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public def getDestination() {
		if (destination == null) {
			try {
				destination = MessageBindingService.getInstance().getDestinationName();
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return destination;
	}
	/*
	 *  Node Version Less than "F"
	 */
	public def getErbs(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		List<ERbs> erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
			for(ERbs erbs : erbsList){
				if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && (erbs.getNeMIMversion().toString().compareTo("vF") < 0)){
					return erbs.getFdn().toString();
				}
			}
		}
		return null;
	}

	/*
	 *  Node Version Greater than "F"
	 */
	public def getFilteredErbs(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		List<ERbs> erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
			for(ERbs erbs : erbsList){
				if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && (erbs.getNeMIMversion().toString().compareTo("vF") > 0)){
					return erbs.getFdn().toString();
				}
			}
		}
		return null;
	}
	/*
	 * getRequiredFdn(String nodeType) is for getting FDN for LTE_ANR_PLMN_WHITE_LIST feature where we require only 16B nodes. 
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
	public def getsonMgtService(){

		final String SONMgtServiceUrl = "rmi://masterservice:50042/SONMgtService";
		sonMgtService= (ISONMgtService) getRmiService(SONMgtServiceUrl, ISONMgtService.class);
		return sonMgtService;
	}
	private static Object getRmiService(final String url, final Class clazz) {

		final RmiProxyFactoryBean plannedManagementRmiFactory = new RmiProxyFactoryBean();
		plannedManagementRmiFactory.setServiceInterface(clazz);
		plannedManagementRmiFactory.setServiceUrl(url);
		plannedManagementRmiFactory.setRefreshStubOnConnectFailure(true);
		plannedManagementRmiFactory.afterPropertiesSet();

		return ((FactoryBean) plannedManagementRmiFactory).getObject();
	}

}

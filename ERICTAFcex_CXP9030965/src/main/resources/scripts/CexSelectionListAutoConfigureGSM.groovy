import java.util.List;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean
import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.cp.core.progress.IJobListener;
import com.ericsson.oss.domain.Direction;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.domain.ConfigurationData;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.domain.FDN;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.modetails.UtranFreqRelation;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.ExternalEUtranPlmn;
import com.ericsson.oss.domain.ExternalENodeBFunction;
import com.ericsson.oss.domain.ExternalEUtranCell;
import com.ericsson.oss.cex.topology.service.impl.TopologyServiceFactory;
import com.ericsson.oss.domain.ExternalUtranFreq;
import com.ericsson.oss.domain.EUtranFrequency;

class CexSelectionListAutoConfigureGSM {

	private ICMService cmService;
	private final String PASSED = "OK";
	private final String FAILED = "FAILED";
	private String planName = null;
	protected CellServiceManager cellServiceManager = CellServiceManager.getInstance();
	String status;

	/*
	 * Geran Cell Relation
	 */

	public def GeranCellRelation(String eUtranCellfdn, String extGsmCellfdn){
		final MessageJobInfo job;

		try	{
			final List<String> CellFdns = new ArrayList<String>();
			final List<String> externalFdns = new ArrayList<String>();

			CellFdns.add(eUtranCellfdn);
			externalFdns.add(extGsmCellfdn);

			job = CellServiceManager.getInstance().createCellRelations(RelationType.GeranCellRelation, Direction.UNIDIRECTIONAL, CellFdns, externalFdns, null, null, null);
			checkJobFinished(job);

			CellFdns.clear();
			externalFdns.clear();
			if (job.getState() == JobState.FAILED){
				status = job.getAdditionalInfo().toString();
				return FAILED + status;
			}
			else {
				status = job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}


		String result = PASSED + " : Result = " + status;
		return result;
	}

	public def getExternalUtranFreq(){

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		List<ExternalUtranFreq> extutranfreqlist ;

		if (subNetwork != null) {
			extutranfreqlist = TopologyManager.getInstance().getExternalUtranFreq(subNetwork);
			if(extutranfreqlist == null || extutranfreqlist.isEmpty()) {
				return "No ExternalUtranFreq List present";
			}
		} else{
			return "No Subnetwork";
		}
		for(ExternalUtranFreq extfdn: extutranfreqlist){
			return extfdn.getFdn().toString();
		}
	}
	public def getEUtranFrequency(){

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		List<EUtranFrequency> utranfreqlist ;

		if (subNetwork != null) {
			utranfreqlist = TopologyManager.getInstance().getEUtranFrequency(subNetwork);
			if(utranfreqlist == null || utranfreqlist.isEmpty()) {
				return "No UtranFreq List present";
			}
		} else{
			return "No Subnetwork";
		}
		for(EUtranFrequency fdn: utranfreqlist){
			return fdn.getFdn().toString();
		}
	}

	public def getExternalENodeBFunction(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		List<ExternalEUtranPlmn> extEUtranPlmnList = null;
		List<ExternalENodeBFunction> ENodeFuncList = null;
		List<String> nodeList = new ArrayList<String>();

		if (subN != null) {
			extEUtranPlmnList =  TopologyManager.getInstance().getExternalEUtranPlmn(subN);
		} else {
			return "No Subnetwork with ERBS";
		}
		if (!extEUtranPlmnList.isEmpty()) {
			for(ExternalEUtranPlmn extEutranPlmn : extEUtranPlmnList){

				ENodeFuncList = TopologyManager.getInstance().getExternalENodeBFunction(extEutranPlmn);

				if(!ENodeFuncList.isEmpty()){

					for(ExternalENodeBFunction eNodeB : ENodeFuncList){
						nodeList.add(eNodeB.getFdn().toString());
					}
				}
				return nodeList;
			}
		}else {
			return "No ExtEutranENBFunctionList";
		}
		return null;
	}

	public def deleteExRelation(String relation){


		GenericItem genericItem = new GenericItem(relation);
		IGenericItem[] genericItemArray = new IGenericItem[1];
		genericItemArray[0] = genericItem;
		MessageJobInfo deleteRelationJob = CmManager.getInstance().deleteMo(genericItemArray, null);

		checkJobFinished(deleteRelationJob);
		if (deleteRelationJob.getState() == JobState.FAILED){
			return deleteRelationJob.getAdditionalInfo().toString();
		}

		return "OK"
	}

	/*
	 * Eutran Cell Relation for FDD
	 */
	public String eUtranCellR_ECellFDD(String eUtranCellfdn, String extCellfdn){
		final MessageJobInfo job;

		try	{
			final List<String> CellFdns = new ArrayList<String>();
			final List<String> externalFdns = new ArrayList<String>();

			CellFdns.add(eUtranCellfdn);
			externalFdns.add(extCellfdn);

			job = CellServiceManager.getInstance().createCellRelations(RelationType.EUtranCellRelation, Direction.UNIDIRECTIONAL, CellFdns, externalFdns, null, null, null);
			checkJobFinished(job);

			CellFdns.clear();
			externalFdns.clear();
			if (job.getState() == JobState.FAILED){
				status = job.getAdditionalInfo().toString();
				return FAILED + status;
			}
			else {
				status = job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}


		String result = PASSED + " : Result = " + status;
		return result;
	}

	/*
	 * Eutran Cell Relation for TDD
	 */
	public String eUtranCellR_ECellTDD(String eUtranCellfdn, String extCellfdn){
		final MessageJobInfo job;

		try	{

			final List<String> CellFdns = new ArrayList<String>();
			final List<String> externalFdns = new ArrayList<String>();

			CellFdns.add(eUtranCellfdn);
			externalFdns.add(extCellfdn);

			job = CellServiceManager.getInstance().createCellRelations(RelationType.EUtranCellRelation, Direction.UNIDIRECTIONAL, CellFdns, externalFdns, null, null, null);
			checkJobFinished(job);

			CellFdns.clear();
			externalFdns.clear();
			if (job.getState() == JobState.FAILED){
				status = job.getAdditionalInfo().toString();
				return FAILED + status;
			}
			else {
				status = job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}


		String result = PASSED + " : Result = " + status;
		return result;
	}

	/*
	 * utran Cell Relation for FDD
	 */
	public String utranCellR_ECellFDD(String eUtranCellfdn, String extCellfdn){
		final MessageJobInfo job;

		try	{
			final List<String> CellFdns = new ArrayList<String>();
			final List<String> externalFdns = new ArrayList<String>();

			CellFdns.add(eUtranCellfdn);
			externalFdns.add(extCellfdn);

			job = CellServiceManager.getInstance().createCellRelations(RelationType.UtranCellRelation, Direction.UNIDIRECTIONAL, CellFdns, externalFdns, null, null, null);
			checkJobFinished(job);

			CellFdns.clear();
			externalFdns.clear();
			if (job.getState() == JobState.FAILED){
				status = job.getAdditionalInfo().toString();
				return FAILED + status;
			}
			else {
				status = job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}


		return PASSED;
	}

	/*
	 * Utran Cell Relation for TDD
	 */
	public String utranCellR_ECellTDD(String eUtranCellfdn, String extCellfdn){
		final MessageJobInfo job;

		try	{
			final List<String> CellFdns = new ArrayList<String>();
			final List<String> externalFdns = new ArrayList<String>();

			CellFdns.add(eUtranCellfdn);
			externalFdns.add(extCellfdn);


			job = CellServiceManager.getInstance().createCellRelations(RelationType.UtranCellRelation, Direction.UNIDIRECTIONAL, CellFdns, externalFdns, null, null, null);
			checkJobFinished(job);

			CellFdns.clear();
			externalFdns.clear();
			if (job.getState() == JobState.FAILED){
				status = job.getAdditionalInfo().toString();
				return FAILED + status;
			}
			else {
				status = job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}


		return PASSED;
	}

	/*
	 * Utran Frequency Relation For FDD
	 */
	public String utranFreqR_ECellFDD(String eUtranCellfdn, String extCellfdn){
		final MessageJobInfo job;

		try	{

			final List<String> CellFdns = new ArrayList<String>();
			final List<String> externalFdns = new ArrayList<String>();

			CellFdns.add(eUtranCellfdn);
			externalFdns.add(extCellfdn);

			job = CellServiceManager.getInstance().createCellRelations(RelationType.UtranFreqRelation, Direction.UNIDIRECTIONAL, CellFdns, externalFdns, null, null, null);
			checkJobFinished(job);

			CellFdns.clear();
			externalFdns.clear();
			if (job.getState() == JobState.FAILED){
				status = job.getAdditionalInfo().toString();
				return FAILED + status;
			}
			else {
				status = job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}


		String result = PASSED + " : Result = " + status;
		return result;
	}

	/*
	 * Utran Frequency Relation for TDD
	 */
	public String utranFreqR_EcellTDD(String eUtranCellfdn, String extCellfdn){
		final MessageJobInfo job;

		try	{

			final List<String> CellFdns = new ArrayList<String>();
			final List<String> externalFdns = new ArrayList<String>();

			CellFdns.add(eUtranCellfdn);
			externalFdns.add(extCellfdn);

			job = CellServiceManager.getInstance().createCellRelations(RelationType.UtranFreqRelation, Direction.UNIDIRECTIONAL, CellFdns, externalFdns, null, null, null);
			checkJobFinished(job);

			CellFdns.clear();
			externalFdns.clear();
			if (job.getState() == JobState.FAILED){
				status = job.getAdditionalInfo().toString();
				return FAILED + status;
			}
			else {
				status = job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}


		String result = PASSED + " : Result = " + status;
		return result;
	}

	public String checkForExistingRelation(String eutranCellFDN){
		//todo
		try{
			final ConfigurationData configData = new ConfigurationData(planName,
					System.getProperty("user.name"))
			MessageJobInfo<List> utranFreqRelations = cellServiceManager.getInstance().getRelations(eutranCellFDN,
					RelationType.UtranFreqRelation.getName(), configData);

			checkJobFinished(utranFreqRelations);

			if (utranFreqRelations.getState().equals(JobState.FAILED)) {
				return null;
			} else {
				List<UtranFreqRelation> relations = utranFreqRelations.getList();
				if(null!= relations.toString()){

					return relations.toString();
				}
				else{
					return null;
				}

			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	/*
	 * Check Job Status
	 */

	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;

		while (!job.isCompleted() && retrycount < 10){
			println("retrying..."+retrycount);
			sleep(1000);
			retrycount ++;
		}
	}

	//=======================================================================================================================//
	/*
	 * Services
	 */

	public def getCmService(){
		final String cmServiceUrl = "rmi://masterservice:50042/CCMService";
		cmService= (ICMService) getRmiService(cmServiceUrl, ICMService.class);
		return cmService
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


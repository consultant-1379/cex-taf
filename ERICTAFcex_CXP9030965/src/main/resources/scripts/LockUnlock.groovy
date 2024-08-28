import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Map;

import ca.odell.glazedlists.EventList;

import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cex.cellservice.service.impl.UpdateMessageJobInfo;
import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.core.OCP;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.cm.core.CmManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.Cell;
import com.ericsson.oss.domain.Configuration;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.RNC;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.domain.modetails.ManagedObject;

import com.ericsson.oss.domain.modetails.IManagedObject;
import com.ericsson.oss.domain.Configuration;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

class LockUnlock{

	private static final RESULT = "OK";

	private static final String LOCKED = "LOCKED";
	private static final String SHUTTING_DOWN = "SHUTTING_DOWN";
	private static final String UNLOCKED = "UNLOCKED";

	private IManagedObject managedObject = null;

	public String lockUnlockPlan(String planName,String fdn,String resultAdminState) {

		UpdateMessageJobInfo job;
		Object cellAdminState = null;
		String[] cells = [fdn] as String []
		
		ContextProvider.getInstance().contextChanged(ContextProvider.PLANNED_CONFIGURATION_CONTEXT, planName);
		//needed to see new cell status in planned area
		waitForTopologyUpdate(4000);
		

		try{
			if(resultAdminState.equalsIgnoreCase(LOCKED)){
				
				job  = (UpdateMessageJobInfo) CellServiceManager.getInstance().lockCells(cells, planName, "nmsadm");
				
			}else if(resultAdminState.equalsIgnoreCase(UNLOCKED)){
			
				job  = (UpdateMessageJobInfo) CellServiceManager.getInstance().unLockCells(cells, planName, "nmsadm");
				
			}
			checkJobFinished(job);

			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}

		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}

		final MessageJobInfo<Map<Configuration, IManagedObject>> getMojob = CmManager.getInstance().getMoProperties(fdn, planName);
		checkJobFinished(getMojob);

		final EventList<Map<Configuration, IManagedObject>> list = getMojob.getList();

		Map<Configuration, IManagedObject> moMap = list.get(0);

		managedObject = moMap.get(Configuration.PLANNED);

		cellAdminState = managedObject.getAttributeValue("administrativeState");

		return cellAdminState.toString();

	}

	public def lockUnlockSsr(String lteSsrList, String wranSsrList, String operation)
	{
		MessageJobInfo job;

		List<String> list1 = new ArrayList<String>(Arrays.asList(wranSsrList.trim()));

		List<String> list2 = new ArrayList<String>(Arrays.asList(lteSsrList.trim()));

		Map<String,List<String>> moFdns = new HashedMap();

		moFdns.put("NodeB",list1);

		moFdns.put("EnodeB",list2);

		String[] cellFdns;

		try{
			if(operation.equalsIgnoreCase("lock")){

				job = CellServiceManager.getInstance().lockRBS_ERBSs(moFdns, cellFdns, null);

			}else  if(operation.equalsIgnoreCase("softlock")){
				job= CellServiceManager.getInstance().softLockRBS_ERBSs(moFdns, cellFdns, null);
			}else if(operation.equalsIgnoreCase("unlock")){

				job= CellServiceManager.getInstance().unLockRBS_ERBSs(moFdns, cellFdns, null);

			}

			waitForTopologyUpdate(2 * 10000);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
		return RESULT;
	}


	public def lockUnlockCells(String fdn, String resultAdminState){
		UpdateMessageJobInfo job;
		String[] cells = [fdn] as String []
		try{
			if(resultAdminState.equalsIgnoreCase(LOCKED)) {
				job = (UpdateMessageJobInfo) CellServiceManager.getInstance().lockCells(cells, null, "nmsadm");
			}
			else if (resultAdminState.equalsIgnoreCase(SHUTTING_DOWN)) {
				job = (UpdateMessageJobInfo) CellServiceManager.getInstance().softLockCells(cells, null, "nmsadm");
			}
			else {
				job = (UpdateMessageJobInfo) CellServiceManager.getInstance().unLockCells(cells, null, "nmsadm");
			}

			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}

		return RESULT;
	}


	public def lockUnlockRBS_ERBSs(String fdn, String cells, String resultAdminState){
		MessageJobInfo job;
		String[] rbsToLock = [fdn] as String []
		String [] cellsToLock = cells.trim().split("\n") as String []
		try{
			if(resultAdminState.equalsIgnoreCase(LOCKED)) {
				job= CellServiceManager.getInstance().lockRBS_ERBSs(rbsToLock, cellsToLock, null);
			}
			else if (resultAdminState.equalsIgnoreCase(SHUTTING_DOWN)) {
				job = CellServiceManager.getInstance().softLockRBS_ERBSs(rbsToLock, cellsToLock, null);
			}
			else {
				job= CellServiceManager.getInstance().unLockRBS_ERBSs(rbsToLock, cellsToLock, null);
			}

			checkJobFinished(job);
			if (job.getState() == JobState.FAILED || job.getState() == JobState.PARTIAL){
				return job.getAdditionalInfo().toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}

		return RESULT;
	}

	public def softLockRBS_ERBSs(String fdn, String cells){
		MessageJobInfo job;
		String[] cellToSoftLock = [fdn] as String []
		String [] mos = cells.trim().split("\n") as String []
		try{
			job= CellServiceManager.getInstance().softLockRBS_ERBSs(cellToSoftLock, mos, null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
	}

	public def performMixedRBS_ERBS(String completeFdn, String operation){

		MessageJobInfo job;
		String[] fdns = [completeFdn] as String []
		String [] mos =[] as String []
		try{
			if(operation.equalsIgnoreCase("lock")){
				job = CellServiceManager.getInstance().lockRBS_ERBSs(fdns, mos, null);
			}else  if(operation.equalsIgnoreCase("softlock")){
				job= CellServiceManager.getInstance().softLockRBS_ERBSs(fdns, mos, null);
			}else if(operation.equalsIgnoreCase("unlock")){

				job= CellServiceManager.getInstance().unLockRBS_ERBSs(fdns, mos, null);

			}
			waitForTopologyUpdate(2 * 10000);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
		return RESULT;
	}
	public def performMultinodelockunlock(String fdn1, String fdn2, String operation)
	{
		MessageJobInfo job;
		
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		Map<String,List<String>> moFdns = new HashedMap();
					 if (fdn1.contains("LTE") && fdn2.contains("LTE")) {
						list1.add(fdn1.trim());
						list1.add(fdn2.trim());
						moFdns.put("EnodeB",list1);
				}  else {
						list2.add(fdn1.trim());
						list2.add(fdn2.trim());
						moFdns.put("NodeB",list1);
					
				}
						
						

		String[] cellFdns;

		try{
			if(operation.equalsIgnoreCase("lock")){

				job = CellServiceManager.getInstance().lockRBS_ERBSs(moFdns, cellFdns, null);

			}else  if(operation.equalsIgnoreCase("softlock")){
				job= CellServiceManager.getInstance().softLockRBS_ERBSs(moFdns, cellFdns, null);
			}else if(operation.equalsIgnoreCase("unlock")){

				job= CellServiceManager.getInstance().unLockRBS_ERBSs(moFdns, cellFdns, null);

			}

			waitForTopologyUpdate(2 * 10000);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
		return RESULT;
	}
	
	private void waitForTopologyUpdate(final int milliseconds){
		try{
			sleep(milliseconds);
		}catch(Exception ex){
			ex.getMessage();
		}
	}

	public def getEUtranCellOId(){
		List<ERbs> erbsList;
		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
			} else {
				return null;
			}
			Set<EUtranCell> eUtranCellSet = new HashSet<EUtranCell>(erbsList);

			for(ERbs erbs : erbsList){
				if(!erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.isConnected() && erbs.getCellList().size() > 0){
					for(EUtranCell cell : erbs.getCellList()){
						return "" + cell.getOid();
					}
				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	public def getUtranCellOId(){
		List<RBS> rbsList;
		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
			if (subN != null) {
				rbsList = TopologyManager.getInstance().getRBSList(subN);
				if(rbsList == null || rbsList.isEmpty()) {
					return null;
				}
			} else {
				return null;
			}
			Set<UtranCell> utranCellSet = new HashSet<UtranCell>(rbsList);
			for(RBS rbs : rbsList){
				if(!rbs.isPicoRbs() && !rbs.isSsrRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0){
					for(UtranCell cell : rbs.getUtranCellList()){
						return "" + cell.getOid();
					}
				}
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	public def getCellFdn(String cellOId){
		int oid = Integer.parseInt(cellOId);
		ManagedObject cellMo = TopologyManager.getInstance().getMO(oid);
		if(cellMo != null){
			return cellMo.getFdn().toString();
		}else {
			return null;
		}
	}

	public def getRbsAndCellDetails(){
		StringBuffer rbsAndCellDetails = new StringBuffer();
		List<RBS> rbsList;
		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
			if (subN != null) {
				rbsList = TopologyManager.getInstance().getRBSList(subN);
				if(rbsList == null || rbsList.isEmpty()) {
					return null;
				}
			} else{
				return null;
			}
			Set<RBS> rbsSet = new HashSet<RBS>(rbsList);
			for (RBS rbs : rbsSet) {
				if(rbs.isConnected() && rbs.getUtranCellList().size() > 0 && !rbs.isPicoRbs() && !rbs.isSsrRbs()){
					rbsAndCellDetails.append(rbs.getFdn().toString() + "::");
					rbsAndCellDetails.append(getCellDetails(rbs.getUtranCellList()));
					return rbsAndCellDetails.toString();
				}
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
	}

	public def getErbsAndCellDetails(){
		StringBuffer erbsAndCellDetails = new StringBuffer();
		List<ERbs> erbsList;
		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
			} else{
				return null;
			}
			Set<ERbs> erbsSet = new HashSet<ERbs>(erbsList);
			for (ERbs erbs : erbsSet) {
				if(erbs.isConnected() && erbs.getCellList().size() > 0 && !erbs.isPicoRbs() && !erbs.isSsrRbs()){
					erbsAndCellDetails.append(erbs.getFdn().toString() + "::");
					erbsAndCellDetails.append(getCellDetails(erbs.getCellList()));
					return erbsAndCellDetails.toString();
				}
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
	}

	private def getCellDetails(List cells){
		StringBuffer cellDetails = new StringBuffer();
		for(Cell cell : cells) {
			cellDetails.append(cell.getOid() + "@" + cell.getFdn().toString() + ":")
		}
		return cellDetails.substring(0, cellDetails.length() - 1);
	}

	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}

	public def getAdminState(String cellOId) {
		int oid = Integer.parseInt(cellOId);
		ManagedObject cellMo = TopologyManager.getInstance().getMO(oid);
		if(cellMo instanceof Cell) {
			return ((Cell)cellMo).getState().getAdministrativeState().toString();
		}
		return null;
	}

}
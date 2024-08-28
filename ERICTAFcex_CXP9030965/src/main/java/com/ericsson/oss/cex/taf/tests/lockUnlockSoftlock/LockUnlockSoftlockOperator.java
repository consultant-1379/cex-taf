package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;

@Operator(context = Context.API)
public class LockUnlockSoftlockOperator implements ApiOperator, ILockUnlockSoftLockOperator {

	private final Logger log = Logger.getLogger(LockUnlockSoftlockOperator.class);

	@Inject
	private GroovyTestOperators groovyOperator;
	@Inject
	private TopologyManager topologyManager;

	private static final String POSITIVE_RESULT = "OK";

	private static final String FAILED_RESULT = "FAILED";

	private static final int WAIT_TIME = 10000;

	private static final String LOCK_UNLOCK = "LockUnlock";
	
	private CexCsHandler cexCsHandler;

	public static List<SiteDetails> siteDetailsList = null;

	private static LockUnlockSoftlockOperator instance = new LockUnlockSoftlockOperator();

	private static String lteSsr;

	private static String wranSsr;

	public LockUnlockSoftlockOperator(){
		cexCsHandler = new CexCsHandler();
	}

	public static LockUnlockSoftlockOperator getInstance(){
		return instance;
	}

	private static List<String> rbsList = null;
	private static List<String> erbsList = null;
	/**
	 * method for locking/unlocking cell
	 */


	public String lockUnlockSSR(String operation){

		lteSsr = topologyManager.getSsr_Erbs();
		wranSsr = topologyManager.getSsr_Rbs();

		return groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "lockUnlockSsr",lteSsr,wranSsr,operation);
	}

	public String lockUnlocklteSSR(String operation){
		List<String> ssrErbsList = topologyManager.getSsr_ErbsList();
		String ssrERBS1 = ssrErbsList.get(0);
		String ssrERBS2 = ssrErbsList.get(1);
		log.info("Selected lteSSR : " + ssrERBS1);
		log.info("Selected lteSSR : " + ssrERBS2);
		return groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "performMultinodelockunlock",ssrERBS1,ssrERBS2,operation);
	}
	
	
	public String lockUnlockrbsSSR(String operation){
		List<String> ssrrbsList = topologyManager.getSsr_RbsList();
		String ssrRBS1 = ssrrbsList.get(0);
		String ssrRBS2 = ssrrbsList.get(1);
		log.info("Selected wraneSSR : " + ssrRBS1);
		log.info("Selected wranSSR : " + ssrRBS2);

		return groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "performMultinodelockunlock",ssrRBS1,ssrRBS2,operation);
	}
	public boolean verifyInCS(String nodeType, String operation){

		String[] cellType = nodeType.split("-");

		List<String> eutranCell = topologyManager.getEUtranCellList(cellType[0],lteSsr);

		List<String> utranCell = topologyManager.getUtranCellList(cellType[0], wranSsr);
		int count = 0;

		if(!eutranCell.isEmpty()){

			for(String cellFdn: eutranCell){


				if(operation.equals("lock")){

					if(cexCsHandler.getAdministrativeState(cellFdn).equals(AdministrativeState.LOCKED)){
						count++;
					}else{
						log.info("Failed fdn : " + cellFdn);
					}
				}
				else if(operation.equals("unlock")){

					if(cexCsHandler.getAdministrativeState(cellFdn).equals(AdministrativeState.UNLOCKED)){
						count++;

					}else{
						log.info("Failed fdn : " + cellFdn);
					}
				}
				else if(operation.equals("softlock")){

					if(cexCsHandler.getAdministrativeState(cellFdn).equals(AdministrativeState.SHUTTING_DOWN)){
						count++;
					}else{
						log.info("Failed fdn : " + cellFdn);
					}
				}

			}
			if(count == eutranCell.size()){
				return true;
			}
		}
		if(!utranCell.isEmpty()){

			count = 0 ;

			for(String cellFdn: utranCell){

				if(operation.equals("lock")){

					if(cexCsHandler.getAdministrativeState(cellFdn).equals(AdministrativeState.LOCKED)){
						count++;
					}else{
						log.info("Failed fdn : " + cellFdn);
					}
				}
				else if(operation.equals("unlock")){

					if(cexCsHandler.getAdministrativeState(cellFdn).equals(AdministrativeState.UNLOCKED)){
						count++;
					}else{
						log.info("Failed fdn : " + cellFdn);
					}
				}
				else if(operation.equals("softlock")){

					if(cexCsHandler.getAdministrativeState(cellFdn).equals(AdministrativeState.SHUTTING_DOWN)){
						count++;
					}else{
						log.info("Failed fdn : " + cellFdn);
					}
				}
			}
			if(count == utranCell.size()){
				return true;
			}
		}
		return false;
	}
	
	public String createPlan(){
		
		String planName = cexCsHandler.createPlan();     // Create Plan
		
		return planName;
	}
	
	public void deletePlan(String planNAme){
		
		cexCsHandler.deletePlan(planNAme);     // Create Plan

	}

	public String lockUnlockInPlan(final String cellOId, final String fdn, final AdministrativeState resultAdminState,final String planName) {
		
		

		final String resultState = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "lockUnlockPlan", planName ,fdn, resultAdminState.name());
		
		if(resultState.equals(resultAdminState.name())){
			return POSITIVE_RESULT;
		}
		return FAILED_RESULT;
	}

	public String lockUnlockCell(final String cellOId, final String fdn, final AdministrativeState resultAdminState) {
		String result = "";
		if(fdn != null && !fdn.equals("")){
			final AdministrativeState administrativeState = cexCsHandler.getAdministrativeState(fdn); 

			if(administrativeState.equals(resultAdminState)) {
				final String actualResult;

				if(resultAdminState.equals(AdministrativeState.LOCKED)) {
					actualResult = lockUnlockCellOperation(fdn, cellOId, AdministrativeState.UNLOCKED);
				}
				else if(resultAdminState.equals(AdministrativeState.SHUTTING_DOWN)) {
					actualResult = lockUnlockCellOperation(fdn, cellOId, AdministrativeState.UNLOCKED);
				}
				else {
					actualResult = lockUnlockCellOperation(fdn, cellOId, AdministrativeState.LOCKED);
				}

				if(!POSITIVE_RESULT.equals(actualResult)){
					return result;
				}
			}

			result = lockUnlockCellOperation(fdn, cellOId, resultAdminState);
		}

		return result;
	}

	/**
	 * method for locking/unlocking of (E)RBS
	 * @param resultAdminState the result administrative state {@link AdministrativeState}
	 * @return true if lock/unlock operation on (E)RBS is successful
	 */
	public boolean lockUnlockRBS_ERBS(final AdministrativeState resultAdminState){
		boolean result = false;
		StringBuffer cellFdns = new StringBuffer();
		for(String cellFdn : siteDetailsList.get(0).getCellsDetailsMap().values()) {
			cellFdns.append(cellFdn + "\n");
		}

		final String response = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "lockUnlockRBS_ERBSs", siteDetailsList.get(0).getSiteFdn(), cellFdns.toString().trim(), resultAdminState.name());
		if(POSITIVE_RESULT.equals(response)) {
			waitForTopologyUpdate(WAIT_TIME);
			result = true;
		}

		return result;
	}

	/**
	 * method for verifying and modifying the state of cells if needed.
	 * @param resultAdminState the result administrative state {@link AdministrativeState}
	 * @return true if at least one cell in the (E)RBS is different from resultAdminState after verify and modify
	 */
	public boolean verifyAndModifySiteCellsState(final AdministrativeState resultAdminState) {
		boolean cellsStateDifferent = false;

		AdministrativeState administrativeState;
		for(String cellFdn : siteDetailsList.get(0).getCellsDetailsMap().values()) {
			administrativeState = cexCsHandler.getAdministrativeState(cellFdn);
			if(!administrativeState.equals(resultAdminState)) {
				log.info("At least one cell in the (E)RBS is not " + resultAdminState.name().toLowerCase() + ".");
				cellsStateDifferent = true;
				break;
			}
		}
		if(!cellsStateDifferent){
			cellsStateDifferent = modifySiteCellsState(resultAdminState);
		}

		return cellsStateDifferent;
	}

	/**
	 * method for verifying changes in CS
	 * @param resultAdminState the result administrative state {@link AdministrativeState}
	 * @return true if all the cells under the site are locked/unlocked in CS.
	 */
	public boolean verifyChangesInCS(final AdministrativeState resultAdminState) {
		boolean csResult = true;
		for(final String cellFdn : siteDetailsList.get(0).getCellsDetailsMap().values()) {
			if(!cexCsHandler.getAdministrativeState(cellFdn).equals(resultAdminState)) {
				csResult = false;
				log.error("Cell State not " + resultAdminState.name().toLowerCase() + " for " + cellFdn + " in CS.");
			}
		}

		return csResult;
	}

	/**
	 * method for verifying changes in Cex Domain
	 * @param resultAdminState the result administrative state {@link AdministrativeState}
	 * @return true if all the cells under the site are locked/unlocked in Cex Domain.
	 */
	public boolean verifyChangesInCexDomain(final AdministrativeState resultAdminState) {
		boolean cexDomainResult = true;
		for(final Map.Entry<String, String> entry : siteDetailsList.get(0).getCellsDetailsMap().entrySet()) {
			final String adminState = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "getAdminState", entry.getKey());
			if(!resultAdminState.name().equalsIgnoreCase(adminState)) {
				cexDomainResult = false;
				log.error("Cell State not " + resultAdminState.name().toLowerCase() + " for " + entry.getValue() + " in Cex Domain.");
			}
		}

		return cexDomainResult;
	}


	public String softLockRBS_ERBS(String fdn, String cells){	
		String [] mos = cells.trim().split("\n");
		List<String> unlockedcells=new ArrayList<String>();
		try {	
			for(String mo : mos){
				if(cexCsHandler.getAdministrativeState(mo).equals("UNLOCKED")){
					unlockedcells.add(mo);
				}
			}	
			groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "softLockRBS_ERBSs", fdn, cells);
			waitForTopologyUpdate(WAIT_TIME);
		} catch (Exception e) {
			log.error("Interrupted Exception");
		}

		for(String mo : unlockedcells){
			if(!cexCsHandler.getAdministrativeState(mo).equals("SHUTTING_DOWN")){
				return FAILED_RESULT;
			}
		}
		return POSITIVE_RESULT;
	}
	public String getEUtranCellOId() {
		String cellOId = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "getEUtranCellOId");
		return cellOId;
	}

	public String getUtranCellOId() {
		String cellOId = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "getUtranCellOId");
		return cellOId;
	}

	public String getCellFdn(String cellOId) {
		String fdn = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "getCellFdn", cellOId);
		return fdn;
	}

	public List<SiteDetails> getSiteAndCellDetails(final String elementType) {
		String methodName = "getRbsAndCellDetails";
		if(elementType.equalsIgnoreCase("erbs")) {
			methodName = "getErbsAndCellDetails";
		}
		final String siteAndCellDetails = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, methodName);
		if(siteAndCellDetails != null && !siteAndCellDetails.equals("")) {
			final String [] elementArrays =  siteAndCellDetails.split("::");
			siteDetailsList = new ArrayList<SiteDetails>();
			siteDetailsList.add(new SiteDetails(elementArrays[0], elementArrays[1]));
		}
		return siteDetailsList;
	}

	private String lockUnlockCellOperation(final String fdn, final String cellOId, final AdministrativeState resultAdminState){		
		try {
			final String response = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "lockUnlockCells", fdn, resultAdminState.name());
			if(!POSITIVE_RESULT.equals(response)){
				return FAILED_RESULT;
			}
			waitForTopologyUpdate(WAIT_TIME);
		} catch (Exception e) {
			log.error("Exception: " + e.getMessage());
		}

		if(!cexCsHandler.getAdministrativeState(fdn).equals(resultAdminState)){
			return FAILED_RESULT;
		}

		String adminState = groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "getAdminState", cellOId);
		if(!resultAdminState.name().equalsIgnoreCase(adminState)) {
			return FAILED_RESULT;
		}

		return POSITIVE_RESULT;
	}

	public void getRbs_ErbsList(){

		rbsList = topologyManager.getRbsList();
		erbsList = topologyManager.getErbsList();

	}
	public String performMixedRBS_ERBS(String nodeType, String operation) {

		if(nodeType.equals("RBS")){

			String rbs = rbsList.get(0);
			log.info("Selected RBS : " + rbs);
			return groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "performMixedRBS_ERBS", rbs , operation);

		}else if(nodeType.equals("ERBS")){

			String erbs = erbsList.get(0);
			log.info("Selected ERBS : " + erbs);
			return groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "performMixedRBS_ERBS", erbs , operation);
		}
		return null;
	}
	public String performMultinodeMixedRBS_ERBS(String nodeType, String operation) {

		if(nodeType.equals("RBS")){

			String rbs1 = rbsList.get(0);
			String rbs2 = rbsList.get(1);
			log.info("Selected RBS : " + rbs1);
			log.info("Selected RBS : " + rbs2);
			return groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "performMultinodelockunlock", rbs1,rbs2,operation);

		}else if(nodeType.equals("ERBS")){

			
				String erbs1 = erbsList.get(0);
				String erbs2 = erbsList.get(1);
				
			log.info("Selected ERBS : " + erbs1);
			log.info("Selected ERBS : " + erbs2);
			return groovyOperator.invokeGroovyMethodOnArgs(LOCK_UNLOCK, "performMultinodelockunlock", erbs1, erbs2 , operation);
			
		}
		return null;
	}
	private boolean modifySiteCellsState(final AdministrativeState resultAdminState) {
		boolean cellsStateDifferent = false;

		for(Map.Entry<String, String> entry : siteDetailsList.get(0).getCellsDetailsMap().entrySet()) {
			AdministrativeState adminState = AdministrativeState.UNLOCKED;
			if(adminState.equals(resultAdminState))  {
				adminState = AdministrativeState.LOCKED;
			}
			else if(adminState.equals(resultAdminState))  {
				adminState = AdministrativeState.SHUTTING_DOWN;
			}
			final String actualResult = lockUnlockCell(entry.getKey(), entry.getValue(), adminState);
			if(POSITIVE_RESULT.equals(actualResult)){
				cellsStateDifferent = true;
				break;
			}
		}

		return cellsStateDifferent;
	}

	private void waitForTopologyUpdate(final int milliseconds){
		try{
			Thread.sleep(milliseconds);
		}catch(Exception ex){
			log.info("Exception occured while Topology Update");
		}	
	}

}
package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;


@Operator(context = Context.API)
public class LockUnlockSoftlockMixedOperator implements ILockUnlockSoftlockMixedOperator{

	private final Logger log = Logger.getLogger(LockUnlockSoftlockMixedOperator.class);
	private static final String NewSupportedNodes_GROOVY = "LockUnlockSoftlockMixed";
	private List<String> filterederbslist = null;
	private List<String> lteSsrMSRBS_V2List ;
	private List<String> ltePicoErbsList ;
	private List<String> lteSsrMSRBS_V2_CS;
	private List<String> ltePicoErbsList_CS;


	@Inject
	private GroovyTestOperators groovyOperator;

	@Inject
	private LockUnlockSoftlockOperator siteOperator;

	public boolean getErbsAllNodes() {

		filterederbslist = new ArrayList<String>(Arrays.asList(groovyOperator.invokeGroovyMethodOnArgs(NewSupportedNodes_GROOVY, "getAllErbsList").split(", ")));
		if(filterederbslist== null | filterederbslist.isEmpty()){
			log.debug("There are no Erbslist present on the server");
			return false;
		}
		return true;

	}
	public boolean performSelectedOperation(String nodeType, String operation){

		if(operation.toLowerCase().contains("topology")){

			return getNewSupportedNodes(nodeType);
		}
		else{
			log.debug("yet to implement for other functionalities");
		}
		return false;
	}

	public boolean getNewSupportedNodes(String nodeType){

		lteSsrMSRBS_V2List = new ArrayList<String>();
		ltePicoErbsList = new ArrayList<String>();

		if (nodeType.toLowerCase().contains("pico")) {

			for(String allFilteredLteSsrMSRBS_V2 : filterederbslist){
				if(allFilteredLteSsrMSRBS_V2.contains("PRBS")){
					lteSsrMSRBS_V2List.add(allFilteredLteSsrMSRBS_V2);
				}
			}
			if(lteSsrMSRBS_V2List== null){
				log.debug("There are no LteSsrMSRBS_V2 are present on the server");
				return false;
			}
			log.info("MSRBS_V2 LIST : "+ lteSsrMSRBS_V2List);
		}
		else{

			for(String allFilteredPicoErbs : filterederbslist){
				if(allFilteredPicoErbs.contains("PRBS")){
					ltePicoErbsList.add(allFilteredPicoErbs);
				} 
			}
			if(ltePicoErbsList== null){
				log.debug("There are no LtePicoErbsList are present on the server");
				return false;
			}
			log.info("ltePicoErbsList LIST : "+ ltePicoErbsList);
		}
		return true;
	}

	public boolean verifyCountMatching(String nodeType){

		if (nodeType.toLowerCase().contains("pico")) {
			if(lteSsrMSRBS_V2_CS.size() == lteSsrMSRBS_V2List.size()){
				log.info("Total "+nodeType+" checked from CSTest:"+ lteSsrMSRBS_V2_CS.size() + 
						"\nTotal "+nodeType+" checked from Domain cache:"+ lteSsrMSRBS_V2List.size());

				return true;
			}
			log.info("Total "+nodeType+" checked from CSTest:"+ lteSsrMSRBS_V2_CS.size() + 
					"\nTotal "+nodeType+" checked from Domain cache:"+ lteSsrMSRBS_V2List.size() );
		}
		else{
			if(ltePicoErbsList_CS.size() == ltePicoErbsList.size()){
				log.info("Total "+nodeType+" checked from CSTest:"+ ltePicoErbsList.size() + 
						"\nTotal "+nodeType+" checked from Domain cache:"+ ltePicoErbsList_CS.size());

				return true;
			}
			log.info("Total "+nodeType+" checked from CSTest:"+ ltePicoErbsList.size() + 
					"\nTotal "+nodeType+" checked from Domain cache:"+ ltePicoErbsList_CS.size());
		}

		return false;
	}

	public List<SiteDetails> getSiteAndCellDetailsMSRBS_V2(final String nodeType){

		String methodName = "getRbsSsrAndCellDetails";
		if(nodeType.toLowerCase().contains("erbs-ssr")){
			methodName = "getErbsSsrAndCellDetails";
		}
		final String siteAndCellDetails = groovyOperator.invokeGroovyMethodOnArgs(NewSupportedNodes_GROOVY, methodName);
		if(siteAndCellDetails != null && !siteAndCellDetails.equals("")) {
			final String [] elementArrays =  siteAndCellDetails.split("::");
			siteOperator.siteDetailsList = new ArrayList<SiteDetails>();
			siteOperator.siteDetailsList.add(new SiteDetails(elementArrays[0], elementArrays[1]));
		}
		return siteOperator.siteDetailsList;
	}
	public List<SiteDetails> getSiteAndCellDetails(final String nodeType){

		String methodName = "getPicoRbsAndCellDetails";
		if(nodeType.toLowerCase().contains("erbs-pico")){
			methodName = "getPicoErbsAndCellDetails";
		}
		final String siteAndCellDetails = groovyOperator.invokeGroovyMethodOnArgs(NewSupportedNodes_GROOVY, methodName);
		if(siteAndCellDetails != null && !siteAndCellDetails.equals("")) {
			final String [] elementArrays =  siteAndCellDetails.split("::");
			siteOperator.siteDetailsList = new ArrayList<SiteDetails>();
			siteOperator.siteDetailsList.add(new SiteDetails(elementArrays[0], elementArrays[1]));
		}
		return siteOperator.siteDetailsList;
	}

	/*
	 * Getting Pico Cell ID based on @nodeType
	 * 
	 */
	public String getPicoCellOId(String nodeType) {

		String cellOId = groovyOperator.invokeGroovyMethodOnArgs(NewSupportedNodes_GROOVY, "getPicoCellOId" , nodeType);
		if(cellOId != null && !cellOId.equals("")){
			return cellOId;
		}
		log.info("There are no "+nodeType+" cell present on the server.");
		return null;
	} 

	/*
	 * Getting MSRBS_V2 Cell ID based on @nodeType
	 * 
	 */
	public String getMsrbs_v2CellOId(String nodeType) {

		String cellOId = groovyOperator.invokeGroovyMethodOnArgs(NewSupportedNodes_GROOVY, "getMsrbs_v2CellOId" , nodeType);
		if(cellOId != null && !cellOId.equals("")){
			return cellOId;
		}
		log.info("There are no "+nodeType+" cell present on the server.");
		return null;
	}
	
	/*
	 * Getting Cell Fdn basis on @ID
	 * 
	 */
	public String getCellFdn(String cellOId) {
		String fdn = groovyOperator.invokeGroovyMethodOnArgs(NewSupportedNodes_GROOVY, "getCellFdn", cellOId);
		return fdn;
	}
}

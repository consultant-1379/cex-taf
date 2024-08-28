package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;


@SuppressWarnings("deprecation")
@Operator(context = Context.API)
public class LockUnlockNBiotCellOperator implements  ApiOperator,ILockUnlockNBiotCellOperator {


	private final Logger log = Logger.getLogger(LockUnlockSoftlockOperator.class);

	@Inject
	private GroovyTestOperators groovyOperator;

	private CexCsHandler cexCsHandler;


	public LockUnlockNBiotCellOperator(){

		cexCsHandler = new CexCsHandler();
	}
	
	public String getNbiotCell(String cellType) {

		return groovyOperator.invokeGroovyMethodOnArgs("NBIOTCellCRUD", "getNbiotCell",cellType);

	}

	public String lockUnlockCell(String cellFdn, String operation) {

		String eutranCellRef = null;
		String adminState = null;

		if(cellFdn.contains("No NBIOT Cell Present")){
			return "OK";
		}

		if(operation.equals("lock")){
			adminState= "LOCKED";	
		}else {
			adminState= "UNLOCKED";
		}

		String nbIotCellType = cexCsHandler.getAttributeByFdn(cellFdn, "nbIotCellType");

		if(nbIotCellType.contains("1") || nbIotCellType.contains("4")){

			eutranCellRef = cexCsHandler.getAttributeByFdn(cellFdn, "eutranCellRef");

			log.info("Unlocking Refrence cell of NbiotCell"+eutranCellRef);
			groovyOperator.invokeGroovyMethodOnArgs("LockUnlock", "lockUnlockCells",eutranCellRef,"UNLOCKED");

		}				


		String result = groovyOperator.invokeGroovyMethodOnArgs("LockUnlock", "lockUnlockCells",cellFdn,adminState);

		return result;
	}

	public String verifyAdminState(String cellFdn, String operation) {

		if(cellFdn.contains("No NBIOT Cell Present")){
			return "OK";
		}
		if(operation.equals("lock")){

			if(cexCsHandler.getAdministrativeState(cellFdn).equals(AdministrativeState.LOCKED)){

				return "OK";
			}else{
				log.info("Failed fdn : " + cellFdn);
			}

		}else if(operation.equals("unlock")){

			if(cexCsHandler.getAdministrativeState(cellFdn).equals(AdministrativeState.UNLOCKED)){

				return "OK";
			}else{
				log.info("Failed fdn : " + cellFdn);
			}

		}

		return null;
	}



}

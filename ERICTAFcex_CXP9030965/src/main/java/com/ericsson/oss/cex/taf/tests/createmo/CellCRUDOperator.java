package com.ericsson.oss.cex.taf.tests.createmo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimCommandHandler;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimResult;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimSession;
import com.ericsson.cifwk.taf.handlers.netsim.commands.NetSimCommands;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NeGroup;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;
import com.ericsson.oss.cex.taf.generic.manager.NetsimCommand;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

@Operator(context = Context.API)
public class CellCRUDOperator implements ICellCRUDOperator{

	@Inject
	private GroovyTestOperators groovyOperator;
	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	private final Logger log = Logger.getLogger(CellCRUDOperator.class);

	public boolean createDeleteCell(String nodeType, String operation){

		if(nodeType.contains("RNC")){

			return createDeleteUtranCell(nodeType, operation);
		}else if(nodeType.contains("NBIOTCell")){

			return createDeleteNBIOTCell(nodeType, operation);
		}
		else{
			return createDeleteEUtranCell(nodeType, operation);
		}
	}

	public boolean createDeleteUtranCell(String nodeType, String operation){

		boolean successResponse = false;

		if(operation.contains("Create")){

			groovyOperator.invokeGroovyMethodOnArgs("UtranCellCRUD", "getUtranCellInstance", nodeType);

			String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranCellCRUD", "createUtranCell");

			if(respVal.contains("OK")){
				successResponse = true;
			}else{
				successResponse = false;;
			}
		}
		else{
			String respVal = groovyOperator.invokeGroovyMethodOnArgs("UtranCellCRUD", "deleteUtranCell");
			if(respVal.contains("OK"))
				successResponse = true;
			else
				successResponse = false;
		}
		return successResponse;
	}

	public boolean createDeleteEUtranCell(String nodeType, String operation){

		boolean successResponse = false;

		if(operation.contains("Create")){

			String eutranCellFdn = groovyOperator.invokeGroovyMethodOnArgs("EUtranCellCRUD", "getEUtranCellInstance", nodeType);

			if(nodeType.equals("EUtranCellTDD") || nodeType.equals("EUtranCellTDD-TAC") || nodeType.equals("EUtranCellFDD/TDD") || nodeType.equals("EUtranCellTDD/FDD")){

				CreateMoCommandNetsim(eutranCellFdn,"OptionalFeatureLicense","FddTddSameENodeB");
			}
			try{

				Thread.sleep(6000);
				String respVal = groovyOperator.invokeGroovyMethodOnArgs("EUtranCellCRUD", "createEUtranCell");



				if(respVal.contains("OK")){
					successResponse = true;
				}else{
					successResponse = false;
				}
			}catch(Exception e){
				log.info("Exception"+e);
			}
		}
		else{

			String respVal = groovyOperator.invokeGroovyMethodOnArgs("EUtranCellCRUD", "deleteEUtranCell");

			if(respVal.contains("Deleted count"))
				successResponse = true;
			else
				successResponse = false;
		}
		return successResponse;
	}

	public boolean createDeleteNBIOTCell(String nodeType, String operation){
		
		String type = null;

		NetsimCommand netsim = new NetsimCommand();
		boolean successResponse = false;

		if(operation.contains("Create")){

			String nbiotCellFdn = groovyOperator.invokeGroovyMethodOnArgs("NBIOTCellCRUD", "getNbiotCellInstance", nodeType);

			String erbsfdn[] = nbiotCellFdn.trim().split("ErbsMO=");

			if(nodeType.equals("SSR-NBIOTCell")){
				type = "SSR-ERBS";
				netsim.CreateMoCommandNetsim(erbsfdn[1],"FeatureState","CXC4012081");

			}else{
				type = "ERBS";
				netsim.CreateMoCommandNetsim(erbsfdn[1],"OptionalFeatureLicense","NarrowbandIoTAccess");
			}
			groovyOperator.invokeGroovyMethodOnArgs("ActivateDeactivateFeature", "activateDeactivateUlTrig","activate",erbsfdn[1],type,"NBIOT");

			try{

				Thread.sleep(6000);
				String respVal = groovyOperator.invokeGroovyMethodOnArgs("NBIOTCellCRUD", "createNbiotCell");



				if(respVal.contains("OK")){
					successResponse = true;
				}else{
					successResponse = false;
				}
			}catch(Exception e){
				log.info("Exception"+e);
			}
		}else{

			String respVal = groovyOperator.invokeGroovyMethodOnArgs("NBIOTCellCRUD", "deleteNbiotCell");

			if(respVal.contains("Deleted count"))
				successResponse = true;
			else
				successResponse = false;
		}
		return successResponse;
	}

	public void cleanUpEUtranCell(){

		groovyOperator.invokeGroovyMethodOnArgs("EUtranCellCRUD", "cleanUpEUtranCell");

	}
	/*
	 * Creating OptionalFeatureLicense feature for the FDD/TDD Implementation with the value @FddTddSameENodeB
	 *
	 */
	private void CreateMoCommandNetsim(String fdn, String childMotoBeCreated,String childMoName) {

		String command2 ="";
		List<String> fdnList5 = new ArrayList<String>();

		try {
			String[] properFdn = fdn.trim().split(",ManagedElement");
			fdn = properFdn[0];

			String nodeName = fdn.substring(fdn.lastIndexOf("=") + 1,fdn.length());
			command2 = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lm "+ fdn + " | grep -i " + childMotoBeCreated;
			String childrenMo = executor.simplExec(command2);
			fdnList5 = new ArrayList<String>(Arrays.asList(childrenMo.split("\n")));

			String keyFdn = fdnList5.get(0).substring(fdnList5.get(0).indexOf("ManagedElement"),fdnList5.get(0).lastIndexOf(","));
			Host Netsimhost = CexApiGetter.getHostNetsim();
			NeGroup allNEs = NetSimCommandHandler.getInstance(Netsimhost).getAllNEs();
			NetworkElement simulations = allNEs.get(nodeName);
			String simulationName = simulations.getSimulationName();
			NetSimSession session = NetSimCommandHandler.getSession(CexApiGetter.getHostNetsim());
			NetSimResult sessionResult = session.exec(NetSimCommands.echo("./inst/netsim_pipe"), NetSimCommands.open(simulationName), NetSimCommands.selectnocallback(nodeName), NetSimCommands.createmo(keyFdn, childMotoBeCreated, childMoName, 1));

			log.info(sessionResult);

		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}

}

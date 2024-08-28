package com.ericsson.oss.cex.taf.tests.properties;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.CellServiceManager;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.cex.taf.ui.validators.CexPreCheckApiOperator;


@Operator(context = {Context.API})
public class VerifyPropertiesFeatureOperator implements IVerifyPropertiesFeatureOperator{

	@Inject
	private CellServiceManager cellManager;
	@Inject
	private TopologyManager topologyManager;
	@Inject
	private GroovyTestOperators groovyOperator;

	private CexCsHandler cexCsHandler;

	private static String SEG_CS  = "ipAddress userLabel neMIMversion mirrorMIBsynchStatus";
	private static String ONRM_CS = "nodeVersion";

	public VerifyPropertiesFeatureOperator(){
		cexCsHandler = new CexCsHandler();
	}
	final CexPreCheckApiOperator cexPreCheckOperator = new CexPreCheckApiOperator();
	private static Logger log = Logger.getLogger(VerifyPropertiesFeatureOperator.class);


	public String getRequiredFdn(String cellType, String functionType, String moType){

		String cellFdn = null;

		if(null == functionType){
			functionType = " ";
		}
		switch(moType){

		case("EUtranCellFDD"):

			cellFdn = topologyManager.getEUtranCell(cellType,  moType);

		break;
		case("EUtranCellTDD"):

			cellFdn = topologyManager.getEUtranCell(cellType,  moType);

		break;
		case("Erbs"):

			cellFdn = topologyManager.getErbs();

		break;

		case("Rbs"):

			cellFdn = topologyManager.getRbs();

		break;

		case("Pico-Rbs"):

			cellFdn = topologyManager.getPico_Rbs();

		break;

		case("Pico-Erbs"):

			cellFdn = topologyManager.getPico_Erbs();

		break;
		
		case("SSR-Erbs"):

			cellFdn = topologyManager.getSsr_Erbs();

		break;
		case("SSR-Rbs"):

			cellFdn = topologyManager.getSsr_Rbs();

		break;
		case("RadioNode-Rbs"):

			cellFdn = topologyManager.getSsr_Rbs();

		break;

		case("RadioNode-Rnc"):

			cellFdn = topologyManager.getRnc();

		break;

		case("PicoNode-Rnc"):

			cellFdn = topologyManager.getRnc();

		break;


		}
		switch(functionType){

		case("CellSleepFunction"):
			// For Node Version Greater than or Equal to "G"
			String neMIMversion = "vG";
		cellFdn = topologyManager.getFilteredEUtranCell(cellType,  moType, neMIMversion);
		cellFdn = cellFdn+",CellSleepFunction=1";

		break;
		}

		return cellFdn;
	}

	public boolean getIMSNode(String nodeType){

		int count =0;
		String IM_ROOT = cexPreCheckOperator.getSystemProperties();

		final String nodeFdn = groovyOperator.invokeGroovyMethodOnArgs("IMSNodeProperties", "getIMSNode", nodeType);

		if(null!=nodeFdn && !nodeFdn.equals("")){

			String convertion = nodeFdn.substring(1,nodeFdn.length()-1);

			List<String> moList = new ArrayList<String>(Arrays.asList(convertion.trim().split(", ")));

			for(String moFdn : moList){

				String propertyValue = groovyOperator.invokeGroovyMethodOnArgs("IMSNodeProperties", "getIMSProperties", moFdn);

				if(null!=propertyValue && !propertyValue.equals("")){

					//Change TOPOLOGY = 1, ATTRIBUTE = 2, DISCOVERED = 5, UNSYNCHRONIZED = 4, SYNCHRONIZED = 3 to match the value from CS
					propertyValue =  propertyValue.replaceAll("TOPOLOGY","1");
					propertyValue =  propertyValue.replaceAll("ATTRIBUTE","2");
					propertyValue =  propertyValue.replaceAll("DISCOVERED","5");
					propertyValue =  propertyValue.replaceAll("UNSYNCHRONIZED","4");
					propertyValue =  propertyValue.replaceAll("SYNCHRONIZED","3");

					String property = propertyValue.substring(1,propertyValue.length()-1);

					List<String> gui_Properties = new ArrayList<String>(Arrays.asList(property.trim().split(", ")));

					Collections.sort(gui_Properties);

					List<Integer> finalResult = new ArrayList<Integer>();

					List<String> SEG_CS_Properties = cexCsHandler.getMoProperties(moFdn, SEG_CS);

					if(SEG_CS_Properties.size()!=0)
						for (String value : SEG_CS_Properties){
							finalResult.add(gui_Properties.contains(value) ? 1 : 0);
						}

					moFdn = moFdn.replaceAll("MeContext", "ManagedElement");
					moFdn = moFdn.replaceAll(IM_ROOT + "_R" , IM_ROOT);

					List<String> ONRM_CS_Properties = cexCsHandler.getONRMProperties(moFdn, ONRM_CS);

					if(ONRM_CS_Properties.size()!=0)
						for (String value : ONRM_CS_Properties){
							finalResult.add(gui_Properties.contains(value) ? 1 : 0);
						}
					if(finalResult.size()!=0 && !finalResult.contains(0)){
						count++;
					}else{
						log.info("Failed properties for : " + moFdn);
					}
				}
			}
			log.info(nodeType + " node count : " + count);
			if(count == moList.size()){
				return true;
			}

		}
		return false;
	}

	public boolean nodeMatcher(String requiredFdn, String moType){

		boolean result = false;

		if(moType.equals("RadioNode-Rbs") || moType.equals("Pico-Rbs")){

			String rncFdn = cellManager.getParentRnc(requiredFdn);

			String associatedRNC = cexCsHandler.getAttributeByFdn(requiredFdn, "associatedRnc");

			if(rncFdn.equals(associatedRNC)){
				result = true;
			}else {
				if("".equals(associatedRNC) || associatedRNC.equals("<UndefinedValue>")){
					log.info("No associatedRNC present for the "  + requiredFdn);
					result = true; // Handling in case of no associatedRNC attached by WMA team.
				}
			}
			return result;
		}else if(moType.equals("PicoNode-Rnc") ) {

			List<Integer> finalResult = new ArrayList<Integer>();

			List<String> gui_attachedRBS  = cellManager.getAttachedRBS(requiredFdn);

			List<String> CS_attachedRBS =  cexCsHandler.getMoProperties(requiredFdn, "associatedPicoNodes");

			log.info("\nValue From GUI : \n"+gui_attachedRBS + "\n" + "Value From CS : \n"+CS_attachedRBS);

			for (String value : CS_attachedRBS){
				finalResult.add(gui_attachedRBS.contains(value) ? 1 : 0);
			}

			if(finalResult.contains(0)){
				result = false;
			}

			if(CS_attachedRBS.contains(null)){
				result = true;
			}
			result = true;
		}else {

			List<Integer> finalResult = new ArrayList<Integer>();

			List<String> gui_attachedRBS  = cellManager.getAttachedRBS(requiredFdn);

			List<String> CS_attachedRBS =  cexCsHandler.getMoProperties(requiredFdn, "associatedRadioNodes");

			log.info("\nValue From GUI : \n"+gui_attachedRBS + "\n" + "Value From CS : \n"+CS_attachedRBS);

			for (String value : CS_attachedRBS){
				finalResult.add(gui_attachedRBS.contains(value) ? 1 : 0);
			}

			if(finalResult.contains(0)){
				result = false;
			}

			if(CS_attachedRBS.contains(null)){
				result = true;
			}
			result = true;
		}
		return result;
	}

	public boolean propertiesMatcher(String requiredFdn, String propertyName) {

		List<Integer> finalResult = new ArrayList<Integer>();

		List<String> gui_Properties = cellManager.getMoProperties(requiredFdn, propertyName, null);

		List<String> CS_Properties = cexCsHandler.getMoProperties(requiredFdn, propertyName);

		log.info("\nValue From GUI : \n"+gui_Properties + "\n" + "Value From CS : \n"+CS_Properties);

		for (String value : CS_Properties){
			finalResult.add(gui_Properties.contains(value) ? 1 : 0);
		}

		if(finalResult.contains(0)){
			return false;
		}

		return true;
	}

}

package com.ericsson.oss.cex.taf.generic.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;

/**
 * 
 * @author xharsja
 *
 */

@Operator(context = Context.API)
public class TopologyManager implements ITopologyManager{

	@Inject
	private GroovyTestOperators groovyOperator;

	private static String GROOVY_SCRIPT = "TopologyManagerFactory";

	private final Logger log = Logger.getLogger(TopologyManager.class);

	Random randomGenerator = new Random();

	public List<String> getRbsList() {

		String rbsList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getRbsList");

		rbsList = rbsList.substring(1,rbsList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(rbsList.trim().split(", ")));

		log.info("RBS List Size ==> " + list.size());

		return list;
	}
	public List<String> getRncList() {

		String rncList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getRncList");

		rncList = rncList.substring(1,rncList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(rncList.trim().split(", ")));

		log.info("RNC List Size ==> " + list.size());

		return list;
	}

	public List<String> getErbsList() {

		String erbsList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getErbsList");

		erbsList = erbsList.substring(1,erbsList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(erbsList.trim().split(", ")));

		log.info("ERBS List Size ==> " + list.size());

		return list;
	}

	public String getErbs() {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getErbs");

	}

	public String getFilteredEUtranCell(String cellType, String moType, String neMIMversion) {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getFilteredEUtranCell", cellType, moType, neMIMversion);

	}
	public String getPico_Rbs() {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getPico_Rbs");

	}

	public String getPico_Erbs() {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getPico_Erbs");

	}

	public String getRbs() {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getRbs");

	}

	public String getRnc() {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getRnc");

	}
	public String getSsr_Erbs() {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getSsr_Erbs");

	}

	public String getSsr_Rbs() {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getSsr_Rbs");

	}

	public List<String> getPico_ErbsList() {

		String pErbsList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getPico_ErbsList");

		pErbsList = pErbsList.substring(1,pErbsList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(pErbsList.trim().split(", ")));

		log.info("PERBS List Size ==> " + list.size());

		return list;
	}
	public List<String> getPico_RbsList() {

		String pRbsList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getPico_RbsList");

		pRbsList = pRbsList.substring(1,pRbsList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(pRbsList.trim().split(", ")));

		log.info("PRBS List Size ==> " + list.size());

		return list;
	}
	public List<String> getSsr_RbsList() {

		String wranSsrList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getSsr_RbsList");

		wranSsrList = wranSsrList.substring(1,wranSsrList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(wranSsrList.trim().split(", ")));

		log.info("WRAN SSR List Size ==> " + list.size());

		return list;
	}
	public List<String> getSsr_ErbsList() {

		String lteSsrList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getSsr_ErbsList");

		lteSsrList = lteSsrList.substring(1,lteSsrList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(lteSsrList.trim().split(", ")));

		log.info("LTE SSR List Size ==> " + list.size());

		return list;
	}
	public List<String> getMgwList() {

		String mgwList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getMGWList");

		mgwList = mgwList.substring(1,mgwList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(mgwList.trim().split(", ")));

		log.info("MGW List Size ==> " + list.size());

		return list;
	}

	/**
	 * @celltype { 
	 *  Enumtype.MACRO.getValue()
	 *  Enumtype.PICO.getValue()
	 *  Enumtype.SSR.getValue()
	 *  }
	 */
	public String getUtranCell(String cellType) {

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getUtranCell",cellType);

	}


	public List<String> getEUtranCellList(String cellType,String erbsFdn){
		
		String eutranCellList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT,"getEUtranCellList",cellType,erbsFdn);
		
		eutranCellList = eutranCellList.substring(1,eutranCellList.length()-1);

		List<String> eutranCell = new ArrayList<String>(Arrays.asList(eutranCellList.trim().split(", ")));
		
		return eutranCell;
	}
	
	
	public List<String> getUtranCellList(String cellType, String rbsFdn){
		
		String utranCellList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT,"getUtranCellList",cellType, rbsFdn);
		
		utranCellList = utranCellList.substring(1,utranCellList.length()-1);
		
		List<String> utranCell = new ArrayList<String>(Arrays.asList(utranCellList.trim().split(", ")));
		
		return utranCell;
	}
	
	/**
	 * @celltype {
	 *  Enumtype.MACRO.getValue(), Enumtype.EUtranCellFDD.getValue()
	 *  Enumtype.MACRO.getValue(), Enumtype.EUtranCellTDD.getValue()
	 *  Enumtype.PICO.getValue(), Enumtype.EUtranCellFDD.getValue()
	 *  Enumtype.SSR.getValue(), Enumtype.EUtranCellFDD.getValue()
	 *  }
	 */
	public String getEUtranCell(String cellType, String moType) {

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getEUtranCell", cellType, moType);

	}
	/**
	 * @externalPlmnType for List
	 *  
	 */
	public List<String> getExternalPlmnFreqList(String extPlmnType) {

		String extPlmnList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getExternalPlmnFreqList", extPlmnType);

		extPlmnList = extPlmnList.substring(1,extPlmnList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(extPlmnList.trim().split(", ")));

		log.info(extPlmnType + " List Size ==> " + list.size());

		return list;

	}
	/**
	 * @externalPlmnType For Single fdn
	 *  
	 */
	public String getExternalPlmnFreq(String extPlmnType) {

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getExternalPlmnFreq", extPlmnType);
	}
	/**
	 * @externalPlmnCellType for List
	 *  
	 */
	public List<String> getExternalPlmnCellList(String extPlmnType) {

		String extPlmnCellList = groovyOperator.invokeGroovyMethodForList(GROOVY_SCRIPT, "getExternalPlmnCellList", extPlmnType);

		extPlmnCellList = extPlmnCellList.substring(1,extPlmnCellList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(extPlmnCellList.trim().split(", ")));

		log.info(extPlmnType + " List Size ==> " + list.size());

		return list;

	}
	/**
	 * @externalPlmnCellType For Single fdn
	 *  
	 */
	public String getExternalPlmnCell(String extPlmnType) {

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getExternalPlmnCell", extPlmnType);

	}


}

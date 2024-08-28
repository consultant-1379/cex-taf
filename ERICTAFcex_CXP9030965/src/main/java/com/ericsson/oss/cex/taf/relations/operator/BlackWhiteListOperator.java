package com.ericsson.oss.cex.taf.relations.operator;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;

/**
 * 
 * @author xharsja
 *
 */
@Operator(context = Context.API)
public class BlackWhiteListOperator implements IBlackWhiteListOperator{

	@Inject
	private TopologyManager topologyManager;
	@Inject
	private GroovyTestOperators groovyOperator;

	private static String GROOVY_SCRIPT = "BlackWhiteList";

	private static final Logger log = Logger.getLogger(BlackWhiteListOperator.class);

	private static String baseErbs ;
	private static String operationalErbs;
	private static String basePerbs ;
	private static List<String> erbsList;
	private static List<String> perbsList;
	/**
	 * Adding x2 black list.
	 *
	 * @param x2ListName the x2 list name
	 * @param enodeBFunctionFdn the enode b function fdn
	 * @param planName the plan name
	 * @return the message job info
	 */
	public String AddERbsX2BlackList(){

		erbsList = topologyManager.getErbsList();

		baseErbs = erbsList.get(0);
		log.info("BasedErbs =>"+baseErbs);

		operationalErbs = erbsList.get(1);
		log.info("OperationalErbs =>"+operationalErbs);

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "AddERbsToX2BlackList",baseErbs,operationalErbs);
	}
	/**
	 * Modify x2 black list.
	 *
	 * @param x2ListName the x2 list name
	 * @param enodeBFunctionFdn the enode b function fdn
	 * @param planName the plan name
	 * @return the message job info
	 */
	public String RemoveERbsX2BlackList(){

		log.info("OperationalErbs =>"+operationalErbs);

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "RemoveERbsX2BlackList", operationalErbs);
	}

	/**
	 * Adding x2 White list.
	 *
	 * @param x2ListName the x2 list name
	 * @param enodeBFunctionFdn the enode b function fdn
	 * @param planName the plan name
	 * @return the message job info
	 */
	public String AddERbsX2WhiteList(){

		/*For a selected ERBS, if a Network Element is added to the Black
		list it cannot be added to the White list, the opposite is also true.
		 */
		baseErbs = erbsList.get(3);
		log.info("BasedErbs =>"+baseErbs);

		operationalErbs = erbsList.get(1);
		log.info("OperationalErbs =>"+operationalErbs);

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "AddERbsToX2WhiteList",baseErbs,operationalErbs);
	}
	/**
	 * Modify x2 White list.
	 *
	 * @param x2ListName the x2 list name
	 * @param enodeBFunctionFdn the enode b function fdn
	 * @param planName the plan name
	 * @return the message job info
	 */
	public String RemoveERbsX2WhiteList(){

		log.info("OperationalErbs =>"+operationalErbs);

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "RemoveERbsX2WhiteList", operationalErbs);
	}

	/**
	 * Adding x2 Black list.
	 *
	 * @param x2ListName the x2 list name
	 * @param enodeBFunctionFdn the enode b function fdn
	 * @param planName the plan name
	 * @return the message job info
	 */
	public String AddPERbsX2BlackList(){

		perbsList = topologyManager.getPico_ErbsList();

		basePerbs = perbsList.get(0);
		log.info("BasedErbs =>"+basePerbs);

		log.info("OperationalErbs =>"+operationalErbs);
		
		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "AddERbsToX2BlackList",basePerbs,operationalErbs);
	}

	/**
	 * Modify x2 black list.
	 *
	 * @param x2ListName the x2 list name
	 * @param enodeBFunctionFdn the enode b function fdn
	 * @param planName the plan name
	 * @return the message job info
	 */
	public String RemovePERbsX2BlackList(){

		log.info("OperationalErbs =>"+operationalErbs);

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "RemoveERbsX2BlackList", operationalErbs);
	}
}

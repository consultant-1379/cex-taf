package com.ericsson.oss.cex.taf.generic.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;

/**
 * 
 * @author xharsja
 *
 */

@Operator(context = Context.API)
public class CellServiceManager implements ICellServiceManager{

	@Inject
	private GroovyTestOperators groovyOperator;
	@Inject
	private TopologyManager topologyManager;

	private static String GROOVY_SCRIPT = "CellManager";

	public String getRelationFdn(String sourcecell, String relationType, String targetCell) {

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getRelationFdn", sourcecell, relationType, targetCell);

	}

	public String deleteAdjacentAtrribute(String adjacentRelation) {

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "deleteAdjacentAtrribute", adjacentRelation);

	}

	public List<String> getRelations(String sourcecell, String relationType) {

		String relationsList = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getRelations", sourcecell, relationType);

		relationsList = relationsList.substring(1,relationsList.length()-1);

		List<String> list = new ArrayList<String>(Arrays.asList(relationsList.trim().split(", ")));

		return list;
	}

	public String deleteRelations(String sourcecell, String relationType) {

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "deleteRelations",sourcecell, relationType);

	}

	public String createRelations(String sourcecell, String targetcell, String relationType){

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "createRelations",sourcecell, targetcell, relationType);
	}

	public String getParentRnc(String rbsFdn){

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getParentRNC",rbsFdn);
	}

	public List<String> getAttachedRBS(String rncFdn){

		String attachedRBS =  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getAttachedRBS",rncFdn);
		
		String convertion = attachedRBS.substring(1,attachedRBS.length()-1);

		List<String> moProperties = new ArrayList<String>(Arrays.asList(convertion.trim().split(", ")));

		Collections.sort(moProperties);

		return moProperties;
		
	}

	public List<String> getMoProperties(String  requiredFdn, String propertyName, String planName){

		String propertyValue = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "getMoProperties",  requiredFdn,  propertyName,  planName);

		//Change DEACTIVATED = 0, ACTIVATED = 1 to match the value from CS
		propertyValue =  propertyValue.replaceAll("DEACTIVATED","0");
		propertyValue =  propertyValue.replaceAll("ACTIVATED","1");
		
		//Change CONNECTED = 2, NEVER_CONNECTED = 1, DISCONNECTED = 3, NOT_APPLICABLE = 4 to match the value from CS for Connection Status
		propertyValue =  propertyValue.replaceAll("CONNECTED","2");
		propertyValue =  propertyValue.replaceAll("NEVER_CONNECTED","1");
		propertyValue =  propertyValue.replaceAll("DISCONNECTED","3");
		propertyValue =  propertyValue.replaceAll("NOT_APPLICABLE","4");

		String convertion = propertyValue.toString();

		convertion = convertion.substring(1,convertion.length()-1);

		List<String> moProperties = new ArrayList<String>(Arrays.asList(convertion.trim().split(", ")));

		Collections.sort(moProperties);

		return moProperties;
	}

	public boolean exportRelation() {

		String rbsfdn = topologyManager.getRbs();

		String result = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT,"exportRelation", rbsfdn);

		if (!result.equals("OK")) {
			return false;
		}
		
		return true;
	}
}

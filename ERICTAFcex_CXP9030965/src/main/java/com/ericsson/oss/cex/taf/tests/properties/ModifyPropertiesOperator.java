package com.ericsson.oss.cex.taf.tests.properties;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;

@Operator(context = Context.API)
public class ModifyPropertiesOperator implements IModifyPropertiesOperator{

	@Inject
	private GroovyTestOperators groovyOperator;
	@Inject
	private TopologyManager topologyManager;

	private static String GROOVY_SCRIPT = "ModifyProperties";

	private static final Logger log = Logger.getLogger(ModifyPropertiesOperator.class);

	public String UpdateUtranCellName(){

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "UpdateProperty", 
				topologyManager.getUtranCell("MACRO"));
	}
	/**
	 * @Negative Test case to change the property with different Type values
	 * @return Error message
	 */
	public String UpdateUtranCellNegative(){

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "UpdateNegativeProperty", 
				topologyManager.getUtranCell("MACRO"));
	}
	
	/**
	 * @Negative Test case to change the property with Out of range values
	 * @return Error message
	 */
	public String UpdateRangeNegative(){

		return  groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "UpdateNegativeProperty", 
				topologyManager.getUtranCell("MACRO"));
	}

	public String UpdateEUtranCellName(){

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "UpdateProperty", 
				topologyManager.getEUtranCell("MACRO", "EUtranCellFDD"));
	}
	public String UpdateMgwName(){

		String mgwFdn = topologyManager.getMgwList().get(0);

		log.info("Selected Mgw =>" + mgwFdn);

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "UpdateProperty", mgwFdn);
	}

	public String UpdateMgwBulkName(){

		List<String> mgwList = topologyManager.getMgwList();
		String result = null;

		for(String fdn : mgwList){
			result = groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "UpdateProperty", fdn);
		}
		return result;
	}
	
	public String getRbsFdn(){

		String rbs = topologyManager.getRbsList().get(0);
		
		log.info("Selected Rbs => " + rbs);

		return rbs;
	}
	
	public String UpdateRncName(){

		String rnc = topologyManager.getRncList().get(0);
		
		log.info("Selected Rnc => " + rnc);
		
		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "UpdateProperty", rnc);
	}
	
	public String UpdateRbsName(String rbsFdn){

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "UpdateProperty", rbsFdn);
	}
	
	public String ResetRbsName(String rbsFdn){

		return groovyOperator.invokeGroovyMethodOnArgs(GROOVY_SCRIPT, "ResetProperty", rbsFdn);
	}
}

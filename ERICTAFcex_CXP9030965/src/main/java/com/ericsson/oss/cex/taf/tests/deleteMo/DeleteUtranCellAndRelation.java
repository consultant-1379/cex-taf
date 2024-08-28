package com.ericsson.oss.cex.taf.tests.deleteMo;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.cex.taf.operators.*;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteUtranCellAndRelation extends TorTestCaseHelper implements TestCase {
	
	private static Logger log = Logger.getLogger(DeleteUtranCellAndRelation.class);
	private final String PASSED = "OK";
	
	
	/**
     * @DESCRIPTION To Verify the Deletion of Utran Cell that has Relation
     * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
     * @PRIORITY HIGH
     */
	@TestId(id="OSS-42911_Func_1", title = "Delete UtranCell Relation")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
    public void deleteUTRANCellWithRelation() {
    	final boolean expectedResult = true;
		
    	Assert.assertEquals(expectedResult, execute());
	}
	

	private boolean execute(){
		GroovyTestOperators operator = new GroovyTestOperators();
		String result = operator.invokeGroovyMethodOnArgs("DeleteUtranCell", "deleteUtranCellMo");
			log.info(result);
			
				
		if(result.contains(PASSED))	
			return true;
		
		else
			return false;
		
	}

}

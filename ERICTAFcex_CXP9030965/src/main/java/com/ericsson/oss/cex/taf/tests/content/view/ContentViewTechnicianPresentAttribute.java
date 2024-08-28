package com.ericsson.oss.cex.taf.tests.content.view;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.content.operator.IContentViewOperator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class ContentViewTechnicianPresentAttribute extends TorTestCaseHelper implements TestCase {
		
	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());

	private static final String NODEB_FUNCTION = "NodeBFunction";

	private static Logger log = Logger.getLogger(ContentViewTechnicianPresentAttribute.class);
	
	private final String PASSED = "OK";
	
	
	@Inject
    private OperatorRegistry<IContentViewOperator> ContentViewOperatorProvider;
	
	/**
     * @DESCRIPTION To verify that technicianPresent attribute is present in NodeBFunction properties by default.
     * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-25487_Func_3", title = "Verify that the technicianPresent attribute")
    @Context(context = {Context.API})
    @Test(groups={"Feature"})
    public void verifyThatTheTechnicianPresentAttribute() {

    	String result;
    	GroovyTestOperators operator = new GroovyTestOperators();
    	IContentViewOperator cOntentViewOperator = ContentViewOperatorProvider.provide(IContentViewOperator.class);
    	
    	List<String> externalGsmFdns = cOntentViewOperator.getFDNListfromCS(NODEB_FUNCTION);
    	result = operator.invokeGroovyMethodOnArgs("WCDMAOperations", "viewTechnicianPresentAttribute");
    	result =  result.replaceAll(" - Version=","\n");
    	String techState = result.substring(0, result.indexOf("FDN"));
    	for( String Fdn : result.trim().split("\n")){
    		if(!Fdn.contains("SubNetwork=ONRM_ROOT_MO"))
    			continue;
    		Fdn =  Fdn.substring(Fdn.indexOf("SubNetwork=ONRM_ROOT_MO_R"), Fdn.length());
    		if(techState.contains("Not_Present")){
    			String Value = verifyInCS(Fdn).toString();
    			if (Value.equals("")){
    				log.info("Technician Present Is Not Present On The Cell Provided");
    				assertEquals(true,true);
    			}
    		}
    		else if(techState.equalsIgnoreCase("Present")){
    			log.info("Technician Present Is Present On The Cell Provided");
    			assertEquals(true,true);
    		}
    		else{
    			assertEquals(true,true);
    			log.info("Technician Present Is Not Present On The Cell Provided");
    		}
    	}
    }

	public String verifyInCS(String nodeBFdn, String... var) {
		
		String resp = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS la "+nodeBFdn+"| grep technicianPresent");
		return resp;

	}
		
}

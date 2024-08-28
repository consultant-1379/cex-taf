package com.ericsson.oss.cex.taf.tests.properties;
import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class CheckSpecialAttrubute extends TorTestCaseHelper implements TestCase{

	@Inject
	private OperatorRegistry<IPropertyViewOperator> cexOperator;

	@TestId(id="oss-66888" ,title="check isDL attribute of UtranCell")
	@Context(context={Context.API})	
	@Test( groups= {"KGB","CDB"})	
	public void checkIsDLAttribute(){

		setTestStep("isDL property check started");
		final IPropertyViewOperator operator = cexOperator.provide(IPropertyViewOperator.class);		

		assertEquals("OK",operator.checkIsDL());		
	}


	@TestId(id="oss-" ,title="check prodDesignation attribute of RBS in property view")
	@Context(context={Context.API})	
	@Test(groups= {"KGB","CDB"})	
	public void checkRBSAttribute(){

		setTestStep("property check started");
		final IPropertyViewOperator operator = cexOperator.provide(IPropertyViewOperator.class);	

		assertEquals("OK",operator.viewRBSAttributes());		
	}

	/*@TestId(id="oss.. ", title="If ProductDesignation of RBS is DEFAULT/MICRO check the CellType of UtranCell should be DEFAULT")
	@Context(context={Context.API})
	@Test (enabled=false,groups={"KGB","CDB"})	
	public void checkUtranCellType(){

		setTestStep("property check started");
		final IPropertyViewOperator operator = cexOperator.provide(IPropertyViewOperator.class);	

		assertEquals("OK",operator.viewRBSAttributes());		

	}*/
}

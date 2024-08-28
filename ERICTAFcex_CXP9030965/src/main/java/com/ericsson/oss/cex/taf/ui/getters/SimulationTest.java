package com.ericsson.oss.cex.taf.ui.getters;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.oss.cex.taf.ui.validators.CexPreCheckApiOperator;

public class SimulationTest extends TorTestCaseHelper implements TestCase {

	private static final Logger log = Logger.getLogger(SimulationTest.class);
	final CexPreCheckApiOperator cexPreCheckOperator = new CexPreCheckApiOperator();
		
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	@DataDriven(name = "Online_Offline_Simulation")
	@Context(context = {Context.API})
	public void simulationTestStart(@TestId @Input("TestID") String testId,
			@Input("NE") String ne){

		setTestcase(testId, " Online Simulation for Network Element : " + ne);

		//cexPreCheckOperator.netsimRestart();
		
		final String startScript = "triggerStart.sh";
		
		assertTrue(cexPreCheckOperator.simulationOperator(ne, startScript));

	}

	@Test(groups = {"KGB", "GAT", "Feature"})
	@DataDriven(name = "Online_Offline_Simulation")
	@Context(context = {Context.API})
	public void simulationTestStop(@TestId @Input("TestID") String testId,
			@Input("NE") String ne){

		setTestcase(testId, " Ofline Simulation for Network Element : " + ne);

		final String stopScript = "triggerStop.sh";
		
		assertTrue(cexPreCheckOperator.simulationOperator(ne, stopScript));

	}
	
}

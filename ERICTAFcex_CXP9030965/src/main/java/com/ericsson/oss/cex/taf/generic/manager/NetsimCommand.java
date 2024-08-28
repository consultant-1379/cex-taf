package com.ericsson.oss.cex.taf.generic.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimCommandHandler;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimResult;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimSession;
import com.ericsson.cifwk.taf.handlers.netsim.commands.NetSimCommands;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NeGroup;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.taf.cshandler.model.Fdn;

public class NetsimCommand {

	private static List<String> fdnList;
	private CexCsHandler cexCsHandler;

	public NetsimCommand(){
		cexCsHandler = new CexCsHandler();
	} 

	private final Logger log = Logger.getLogger(NetsimCommand.class);

	public void CreateMoCommandNetsim(String fdn,String childMotoBeCreated ,String childMoName) {

		fdnList = new ArrayList<String>();
		try {
			String nodeName = fdn.substring(fdn.lastIndexOf("=") + 1,fdn.length());
			fdn = fdn+" | grep -i "+childMotoBeCreated;
			List<Fdn> fdnList1 = cexCsHandler.getChildMos(fdn);

			for(Fdn fdn1 : fdnList1){
				fdnList.add(fdn1.toString());
			}

			String keyFdn = fdnList.get(0).substring(fdnList.get(0).indexOf("ManagedElement"),fdnList.get(0).lastIndexOf(","));
			Host Netsimhost = CexApiGetter.getHostNetsim();

			NetSimCommandHandler netsim = NetSimCommandHandler.getInstance(Netsimhost);
			NeGroup allNEs = netsim.getAllNEs();

			//				NeGroup allNEs = NetSimCommandHandler.getInstance(Netsimhost).getAllNEs();

			NetworkElement simulations = allNEs.get(nodeName);
			String simulationName = simulations.getSimulationName();
			NetSimSession session = NetSimCommandHandler.getSession(CexApiGetter.getHostNetsim());

			NetSimResult sessionResult = session.exec(NetSimCommands.echo("./inst/netsim_pipe"), NetSimCommands.open(simulationName), NetSimCommands.selectnocallback(nodeName), NetSimCommands.createmo(keyFdn, childMotoBeCreated, childMoName, 1));

		}catch (Exception e) {
			log.info(e.getMessage());
		}

	}

}

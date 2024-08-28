
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean

import com.ericsson.oss.common.domain.IGenericItem;
import ca.odell.glazedlists.EventList;

import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.domain.modetails.IManagedObject;


import com.ericsson.oss.core.ims.topology.ui.core.internal.IMSTopologyManager;
import com.ericsson.oss.common.topology.services.TopologyContext;
import com.ericsson.oss.common.topology.services.ITopologyService;
import com.ericsson.oss.common.topology.ui.core.ITopologyManager;
import com.ericsson.oss.core.domain.ims.MTAS;
import com.ericsson.oss.core.domain.ims.CSCF;
import com.ericsson.oss.core.domain.ims.DUAS;
import com.ericsson.oss.core.domain.ims.WCG;
import com.ericsson.oss.core.domain.ims.IPWorks;
import com.ericsson.oss.core.domain.ims.PGM;
import com.ericsson.oss.core.domain.ims.SBG;
import com.ericsson.oss.core.domain.ims.CUDB;
import com.ericsson.oss.core.domain.ims.HSS_FE;

class IMSNodePropertiesPLM{

	TopologyContext imsTopologyContext = new TopologyContext("IMS");
	private ITopologyService cellService;
	List<String> filteredList;
	Map<String, String> properties = new HashMap<String, String>();
	String ipAddress = "ipAddress";
	String userLabel = "userLabel";
	String neMIMName = "neMIMName";
	String lostSynchronisation = "lostSynchronisation";
	String neMIMversion ="neMIMversion";
	String mirrorMIBsynchStatus = "mirrorMIBsynchStatus";


	public def getIMSNode(String nodeType, String version){

		filteredList = new ArrayList<String>();

		Collection<IGenericItem> imsTopology =  getCellService().findTopologyNodes("MeContext",imsTopologyContext);

		switch(nodeType){
			case("MTAS"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof MTAS && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
			case("CSCF"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof CSCF && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
			case("DUAS"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof DUAS && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
			case("WCG"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof WCG && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
			case("IPWorks"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof IPWorks && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
			case("PGM"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof PGM && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
			case("SBG"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof SBG && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
			case("CUDB"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof CUDB && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
			case("HSS_FE"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof HSS_FE && ims.getRelease().equals(version) ){
						return ims.getFdn().toString();

					}
				}
		}
	}

	public def getProperties(String nodeType, String version,String fdn){

		filteredList = new ArrayList<String>();

		Collection<IGenericItem> imsTopology =  getCellService().findTopologyNodes("MeContext",imsTopologyContext);

		switch(nodeType){
			case("MTAS"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof MTAS && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());

							return properties;
						}

					}
				}
			case("CSCF"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof CSCF && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());

							return properties;
						}

					}
				}
			case("DUAS"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof DUAS && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());

							return properties;
						}

					}
				}
			case("WCG"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof WCG && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());

							return properties;
						}

					}
				}
			case("IPWorks"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof IPWorks && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());

							return properties;
						}

					}
				}
			case("PGM"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof PGM && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());

							return properties;
						}

					}
				}
			case("SBG"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof SBG && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							//Change TOPOLOGY = 1, ATTRIBUTE = 2, DISCOVERED = 5, UNSYNCHRONIZED = 4, SYNCHRONIZED = 3 to match the value from CS
						    switch(ims.getMirrorMIBsynchStatus().toString()){
								
							
								case ("TOPOLOGY"):
									properties.put(mirrorMIBsynchStatus, "1");
									break;
								
								case ("ATTRIBUTE"):
									properties.put(mirrorMIBsynchStatus, "2");
									break;
								
								case ("DISCOVERED"):
									properties.put(mirrorMIBsynchStatus, "5");
									break;
								
								case ("UNSYNCHRONIZED"):
									properties.put(mirrorMIBsynchStatus, "4");
									break;
								
								case ("SYNCHRONIZED"):
									properties.put(mirrorMIBsynchStatus, "3");
									break;

						    }
							return properties;
						}

					}
				}
			case("CUDB"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof CUDB && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());

							return properties;
						}
					}
				}
			case("HSS_FE"):
				for(IGenericItem ims : imsTopology){
					if(ims instanceof HSS_FE && ims.getRelease().equals(version) ){

						if(ims.getFdn().toString().equals(fdn)){

							properties.put(ipAddress, ims.getIpAddress().toString());
							properties.put(userLabel, ims.getUserLabel().toString());
							properties.put(neMIMName, ims.getModelName().toString());
							properties.put(neMIMversion, ims.getModelVersion().toString());
//							properties.put(lostSynchronisation, ims.getLostSynchronisation().toString());
							properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());

							return properties;
						}

					}
				}
		}
	}
	
	public def getCellService(){
		final String cellServiceUrl = "rmi://masterservice:50042/commonTopologyService";
		cellService= (ITopologyService) getRmiService(cellServiceUrl, ITopologyService.class);
		return cellService;
	}

	private static Object getRmiService(final String url, final Class clazz) {

		final RmiProxyFactoryBean plannedManagementRmiFactory = new RmiProxyFactoryBean();
		plannedManagementRmiFactory.setServiceInterface(clazz);
		plannedManagementRmiFactory.setServiceUrl(url);
		plannedManagementRmiFactory.setRefreshStubOnConnectFailure(true);
		plannedManagementRmiFactory.afterPropertiesSet();
		return ((FactoryBean) plannedManagementRmiFactory).getObject();
	}

}
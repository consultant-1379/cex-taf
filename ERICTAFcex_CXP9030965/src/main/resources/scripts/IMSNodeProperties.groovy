
import java.util.Collection;
import java.util.Map;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.core.domain.ims.IMSNetworkElement;
import com.ericsson.oss.common.topology.services.TopologyContext;
import com.ericsson.oss.common.topology.services.ITopologyService;
import com.ericsson.oss.common.topology.ui.core.ITopologyManager;

class IMSNodeProperties{

	TopologyContext imsTopologyContext = new TopologyContext("IMS");
	Map<String, String> properties ;
	List<String> list;
	String sourceType = "sourceType";
	String ipAddress = "ipAddress";
	String nodeVersion = "nodeVersion";
	String userLabel = "userLabel";
	String neMIMversion ="neMIMversion";
	String mirrorMIBsynchStatus = "mirrorMIBsynchStatus";


	public def getIMSNode(String nodeType){


		Collection<IGenericItem> imsTopology =  getCellService().findTopologyNodes("MeContext",imsTopologyContext);

		list = new ArrayList<String>();
		
		for(IGenericItem ims : imsTopology){

			if(ims instanceof IMSNetworkElement && ims.getSubType().equals(nodeType)){
			
				list.add(ims.getFdn().toString());
			}
		}
		return list;
	}

	public def getIMSProperties(String moFdn){

		Collection<IGenericItem> imsTopology =  getCellService().findTopologyNodes("MeContext",imsTopologyContext);

		properties  = new HashMap<String, String>();
		
		for(IGenericItem ims : imsTopology){

			if(ims instanceof IMSNetworkElement && ims.getFdn().toString().equals(moFdn)){

				properties.put(sourceType, ims.getSourceType().toString());
				properties.put(nodeVersion, ims.getRelease().toString());
				properties.put(ipAddress, ims.getIpAddress().toString());
				properties.put(userLabel, ims.getUserLabel().toString());
				properties.put(neMIMversion, ims.getModelVersion().toString());
				properties.put(mirrorMIBsynchStatus, ims.getMirrorMIBsynchStatus().toString());
				
				return properties;
			}
		}
		return null;
	}
	
	public def getCellService(){
		final String cellServiceUrl = "rmi://masterservice:50042/commonTopologyService";
		ITopologyService cellService= (ITopologyService) getRmiService(cellServiceUrl, ITopologyService.class);
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
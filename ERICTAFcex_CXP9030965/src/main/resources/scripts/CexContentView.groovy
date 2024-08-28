import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean

import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.ExternalGsmFreqGroup;
import java.util.Collections;
import com.ericsson.oss.cex.topology.service.impl.TopologyServiceFactory;
class CexContentView {
	
	protected TopologyManager topologyManager = TopologyManager.getInstance();
	
	
	
	/*
	 * method to get EnodeB Page Of Content view for Lte Topology
	 */
	public String viewLteEnodeBPage(){
		
			SubNetwork subNet = topologyManager.getSubNetwork();
			
			try{
				return topologyManager.getERBSList(subNet).toString();
			}
			catch(Exception e){
				return e.getMessage().toString();
			}
			
	}
	
}

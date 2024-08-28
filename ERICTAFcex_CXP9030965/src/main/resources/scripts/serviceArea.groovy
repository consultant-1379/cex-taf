
import java.sql.ResultSet;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean

import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.Direction;
import com.ericsson.oss.domain.CEXMIMVersion;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.LocationArea;
import com.ericsson.oss.domain.Plmn;


class serviceArea {
	//MessageJobInfo MoJob;
	public TopologyManager topologyManager = TopologyManager.getInstance();
	public CmManager cmManager = CmManager.getInstance();
	
	public IGenericItem serviceAreainstance;
	String genericItemID;
	public List<IGenericItem> genericItemList  =  new ArrayList<IGenericItem>();
	String moType = "LocationArea";
	CEXMIMVersion cexMimVersion = topologyManager.getSubNetwork().getMoMimVersion();
	String modelName = cexMimVersion.getName();
	String modelVersion = cexMimVersion.getVersion();
	//String nodeModel="ERBS_NODE_MODEL";
	IGenericItem[] genericItemArray;

	MessageJobInfo Job;
	public def deleteServiceArea(String Areafdn){
		
		
		String fdn=Areafdn;
		
		loop:
		if(topologyManager.getSubNetwork().getPlmnList().size() >  0){
			List<Plmn> plmnList = topologyManager.getSubNetwork().getPlmnList();
			for(Plmn plmn : plmnList){
				if(plmn instanceof Plmn){
					
						List<LocationArea> locAreaList = topologyManager.getLaList(plmn.getOid());
						for(LocationArea locArea : locAreaList){
							genericItemID = locArea.getFdn().toString();
							break loop;
						}
				}
			}
		
		}
		else
			return "No Plmn exists";
try {

			serviceAreainstance = cmManager.createGenericItem(genericItemID,modelName,modelVersion,moType);
			serviceAreainstance.setId(fdn);
			deleteArea();
		

		} catch (Exception e) {
			return  e.getMessage().toString();
		}
	
		return "OK";
	}
	public def deleteArea(){
		try{
			serviceAreainstance = [genericItem] as IGenericItem[];
			IGenericItem[] genericItemArray;
			Job = cmManager.deleteMo(genericItemArray, null);
			
			
			checkJobFinished(Job);
			if (Job.getState() == JobState.FAILED){
				return Job.getAdditionalInfo().toString();
				//return genericItemList;
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
		
	}

	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}

}
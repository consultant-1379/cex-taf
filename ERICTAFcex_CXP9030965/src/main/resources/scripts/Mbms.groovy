
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


class Mbms{
	public TopologyManager topologyManager = TopologyManager.getInstance();
	public CmManager cmManager = CmManager.getInstance();
	
	
	public IGenericItem Mbmsinstance;
	String genericItemID;
	public List<IGenericItem> genericItemList  =  new ArrayList<IGenericItem>();
	String moType = "MbmsServiceArea";
	CEXMIMVersion cexMimVersion = topologyManager.getSubNetwork().getMoMimVersion();
	String modelName = cexMimVersion.getName();
	String modelVersion = cexMimVersion.getVersion();
	//String nodeModel="ERBS_NODE_MODEL";
	//IGenericItem[] genericItemArray;
	IGenericItem [] iGenericItem = new IGenericItem[1];

	MessageJobInfo MoJob;
	public def deleteMbms(String Areafdn){
		
		
		String fdn=Areafdn;


		if(topologyManager.getSubNetwork().getPlmnList().size() > 0)
		{
			genericItemID = topologyManager.getSubNetwork().getPlmnList().get(0).getFdn().toString();
		
		} else {
			
			return moType + " Not Created : No Plmn list available to handle new " +moType +" Object!";
		}


		try {

			Mbmsinstance = cmManager.createGenericItem(genericItemID,modelName,modelVersion,moType);
			Mbmsinstance.setId(fdn);
			//genericItemArray=[locationAreainstance];


		} catch (Exception e) {
			return  e.getMessage().toString();
		}
		//locationAreainstance.setId(fdn);
		//deleteArea();
		
		//checkjob();
		return "OK";
	}
	public def deleteArea(){
		try{
			//locationAreainstance = [genericItem] as IGenericItem[];
			//IGenericItem[] genericItemArray;
			//genericItemArray = [locationAreainstance];
			iGenericItem[0] = Mbmsinstance;
			MoJob = cmManager.deleteMo(iGenericItem, null);
			
			iGenericItem[0] = Mbmsinstance;
			checkJobFinished(MoJob);
			if (MoJob.getState() == JobState.FAILED){
				return MoJob.getAdditionalInfo().toString();
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
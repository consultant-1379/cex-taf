
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


class createExternalUtranCell{
	public TopologyManager topologyManager = TopologyManager.getInstance();
	public CmManager cmManager = CmManager.getInstance();
	public IGenericItem ExternalUtranCellInstance;
	String genericItemID;
	public List<IGenericItem> genericItemList  =  new ArrayList<IGenericItem>();
	String moType = "ExternalUtranCell";
	
	CEXMIMVersion cexMimVersion = topologyManager.getSubNetwork().getMoMimVersion();
	String modelName = cexMimVersion.getName();
	String modelVersion = cexMimVersion.getVersion();
	MessageJobInfo addMoJob;
	String successMessage = "OK";
	String failMessage = "FAIL";
	
	public def CreateExternalUtranCell(){
		genericItemID=topologyManager.getSubNetwork().getFdn().toString();

		try {
			ExternalUtranCellInstance = cmManager.createGenericItem(genericItemID,modelName,modelVersion,moType);
		} catch (Exception e) {
			return  e.getMessage().toString();
		}
		
		genericItemList.add(ExternalUtranCellInstance);
		String result = checkjob();
		if(successMessage.equals(result)) {
				return successMessage;
		}
		
		else {
			return result;
		}
	
	}
	
	public def checkjob(){
		try{
			addMoJob = cmManager.createGenericItem(genericItemList, null, null);
			checkJobFinished(addMoJob);
			if (addMoJob.getState() == JobState.FAILED){
				return addMoJob.getAdditionalInfo().toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
		
		return successMessage;
	}

	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}

}
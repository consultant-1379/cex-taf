import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.odell.glazedlists.EventList;

import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.domain.LocationArea;
import com.ericsson.oss.domain.Plmn;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.cp.core.contexts.ContextProvider;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.CEXMIMVersion;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;

class routingArea {

	public TopologyManager topologyManager = TopologyManager.getInstance();
	public CmManager cmManager = CmManager.getInstance();
	SubNetwork subNet = topologyManager.getSubNetwork();
	public IGenericItem routingAreainstance;
	String genericItemID;
	public List<IGenericItem> genericItemList  =  new ArrayList<IGenericItem>();
	String moType = "LocationArea";
	CEXMIMVersion cexmimVersion = topologyManager.getSubNetwork().getMoMimVersion();
	String modelName = cexmimVersion.getName(); ;
	String modelVersion = cexmimVersion.getVersion();
	//String nodeModel="ERBS_NODE_MODEL";
	IGenericItem[] genericItemArray;
	MessageJobInfo MoJob;
	List<Plmn> plmnList = subNet.getPlmnList();
	List<LocationArea> areas = new ArrayList<LocationArea>();
	//modelName = cexmimVersion.getName();
	//modelVersion = cexmimVersion.getVersion();



	public def deleteRoutingArea(String routingfdn){


		if(subNet.getPlmnList().size() > 0) {
			for (Plmn plmn : plmnList) {
				List<LocationArea> tempAreas = topologyManager.getLaList(plmn.getOid());
				if(tempAreas.size() > 0) {
					areas = tempAreas;
					break;
				}
			}
			if(areas.size() > 0) {
				genericItemID = areas.get(0).getFdn().toString();

			} else {
				return moType + " Not Created : No Location Area available to handle a new " +moType +" Object!";
			}

		}else {
			return moType + " Not Created : No Plmn available to handle a new " +moType +" Object!";
		}

		try {

			routingAreainstance = cmManager.createGenericItem(genericItemID, modelName, modelVersion, moType);
			routingAreainstance.setId(routingfdn);
			genericItemArray=[routingAreainstance] ;
			deleteArea();
			
			

		} catch (Exception e) {

			return "Create Generic Item Fail - "+e;
		}
		

		return "OK";
	}
	public def deleteArea(){
		try{
			//locationAreainstance = [genericItem] as IGenericItem[];
			//IGenericItem[] genericItemArray;
			//genericItemArray = [locationAreainstance];
			MoJob = cmManager.deleteMo(genericItemArray, null);
			
			
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
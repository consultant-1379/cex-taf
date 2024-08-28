import java.util.ArrayList;
import java.util.List;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.ConfigurationData;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.RNC;
import com.ericsson.oss.domain.FDN;
import com.ericsson.oss.domain.RXI;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.domain.UtranCellRelations;
import com.ericsson.oss.domain.NE;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.ericsson.oss.domain.TechnicianState;
import org.apache.commons.logging.Log;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cp.core.progress.IJobListener;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.domain.modetails.IManagedObject;


public class CheckSpecialAttribute{

	private ICMService cmService;
	protected TopologyManager topologyManager = TopologyManager.getInstance();
	protected CellServiceManager cellServiceManager = CellServiceManager.getInstance();
	protected CmManager cmManager = CmManager.getInstance();
	private List<UtranCell> ucellList;
	private List<UtranCell> filterList=new ArrayList<UtranCell>();
	String versionmsg="Version different than vV.4.*";
	String failuremsg="FAILED";
	String successmsg="SUCCESS";
	String result="";
	String DefaultIsDL="false";

	public String checkIsDLattr() {

		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<RNC> rncList;
			List<RBS> rbsList;
			List<UtranCell> utrancell;
			List<RNC> rncmimversion=new ArrayList<RNC>();
			boolean RESULT;
			Map<String, String> properties = new HashMap<String, String>();

			if(subNet != null) {
				rncList = subNet.getRncList();
				for (RNC rnc : rncList) {
					if(rnc.getNeMIMversion().toString().contains("V.4.")){
						rncmimversion.add(rnc);
					}
				}

				if(rncmimversion.size()>0) {
					for(RNC rncs:rncmimversion) {
						rbsList=rncs.getRbsList();
					}
					for(RBS rbs:rbsList) {

						utrancell=rbs.getUtranCellList();

						if(!utrancell.isEmpty()){
							for(UtranCell uc:utrancell) {
								boolean re=uc.isDLOnly();
								if(!re) {
									result=successmsg+":"+uc.getFdn().toString();
								}
								else if(re) {
									result=successmsg+":"+uc.getFdn().toString();
								}
								return result;
							}
						}
					}
				}
				else {
					rncList = subNet.getRncList();
					for(RNC rnc:rncList) {
						rbsList=rnc.getRbsList();
						for(RBS rbs:rbsList) {
							utrancell=rbs.getUtranCellList();
							if(!utrancell.isEmpty()){
								for(UtranCell uc:utrancell) {
									boolean re=uc.isDLOnly();

									if(!re) {
										result=DefaultIsDL;
									}
									else if(re) {
										result=DefaultIsDL;
									}
									return result;
								}
							}
						}
					}
				}
			}
			else {
				result=failuremsg;
			}
			return result;
		}

		catch(Exception ce) {
			return ce.message;
		}
	}

	public def viewRBSProperties() {

		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<RNC> rncList;
			List<RBS> rbsList;
			Map<String, String> properties = new HashMap<String, String>();
			if (subNet != null) {
				rncList = subNet.getRncList();
			} else {
				return null;
			}
			for (RNC rnc : rncList) {

				rbsList=rnc.getRbsList();

				for( RBS rbs: rbsList){
					if(rbs.isConnected() && !rbs.isPicoRbs() && !rbs.isSsrRbs()){
						
						String value = String.valueOf(rbs.getMirrorMIBsynchStatus());
						if(value.equals("UNSYNCHRONIZED")) {
							continue;
						}
						properties.put("FDN", rbs.toString());
						properties.put("prodDesignation",rbs.getProdDesignation());

					}
				}
			}
			return properties;
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}

	public String RBS6kCabinet() {


		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<RNC> rncList;
			List<RBS> rbsList;
			List<RBS> rbs6k=new ArrayList<RBS>();
			if(subNet != null) {
				rncList = subNet.getRncList();

				for(RNC rnc:rncList) {
					rbsList=rnc.getRbsList();

					for(RBS rbs:rbsList) {
						if(rbs.getMultiStandardRbs6k()) {
							rbs6k.add(rbs);
						}
						else{
							continue;
						}
						if(rbs6k.size()>0) {
							for(RBS rbssix:rbs6k) {
								result="CABID_"+rbssix.getCabinetIdentifier().toString()+":"+rbssix.getFdn().toString();
								break;
							}
						}
					}
				}
			}
			else {
				result=failuremsg;
			}
			return result;
		}

		catch(Exception ce) {
			return ce.message;
		}
	}



	public String CheckMicroSleepAttr(String fdn) {

		String MStxavailbility="";
		String MStxstatus="";

		try{
			SubNetwork subNet = topologyManager.getSubNetwork();
			List<ERbs> erbsList;
			if(subNet != null) {
				erbsList = subNet.getErbsList();
				for(ERbs erbs:erbsList) {
					MStxstatus=erbs.getMicroSleepFeatureState();
					MStxavailbility=erbs.getMicroSleepLicenseState();
					return 	MStxstatus+":"+MStxavailbility;
				}
			}
			else {
				result=failuremsg;
			}
			return result;
		}

		catch(Exception ce) {
			return ce.message;
		}
	}
}
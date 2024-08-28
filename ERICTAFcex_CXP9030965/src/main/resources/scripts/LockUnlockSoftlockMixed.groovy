
import java.util.List;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cex.cellservice.service.impl.UpdateMessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.domain.modetails.ManagedObject;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.domain.Cell;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;

class LockUnlockSoftlockMixed {

	SubNetwork subNetwork ;
	List<ERbs> erbsList;
	List<String> filterederbslist = new ArrayList<String>();
	private static final RESULT = "OK";

	public def getAllErbsList() {
		try {
			subNetwork = TopologyManager.getInstance().getSubNetwork();

			erbsList = subNetwork.getErbsList();
			for(ERbs erbs : erbsList){
				filterederbslist.add(erbs.getFdn());
			}

			if(filterederbslist == null || filterederbslist.isEmpty()){
				return null;
			}
			return filterederbslist.toString();
		}
		catch (Exception e){
			return e.getMessage().toString();
		}
	}

	public def getPicoRbsAndCellDetails(){
		StringBuffer rbsAndCellDetails = new StringBuffer();
		List<RBS> rbsList;
		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
			if (subN != null) {
				rbsList = TopologyManager.getInstance().getRBSList(subN);
				if(rbsList == null || rbsList.isEmpty()) {
					return null;
				}
			} else{
				return null;
			}
			Set<RBS> rbsSet = new HashSet<RBS>(rbsList);
			for (RBS rbs : rbsSet) {
				if(!(rbs.isPicoRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0)){
					continue;
				}
				rbsAndCellDetails.append(rbs.getFdn().toString() + "::");
				rbsAndCellDetails.append(getCellDetails(rbs.getUtranCellList()));
				return rbsAndCellDetails.toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
	}
	public def getRbsSsrAndCellDetails(){
		StringBuffer rbsAndCellDetails = new StringBuffer();
		List<RBS> rbsList;
		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
			if (subN != null) {
				rbsList = TopologyManager.getInstance().getRBSList(subN);
				if(rbsList == null || rbsList.isEmpty()) {
					return null;
				}
			} else{
				return null;
			}
			Set<RBS> rbsSet = new HashSet<RBS>(rbsList);
			for (RBS rbs : rbsSet) {
				if(!(rbs.isSsrRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0)){
					continue;
				}
				rbsAndCellDetails.append(rbs.getFdn().toString() + "::");
				rbsAndCellDetails.append(getCellDetails(rbs.getUtranCellList()));
				return rbsAndCellDetails.toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
	}
	public def getErbsSsrAndCellDetails(){
		StringBuffer erbsAndCellDetails = new StringBuffer();
		List<ERbs> erbsList;
		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
			} else{
				return null;
			}
			Set<ERbs> erbsSet = new HashSet<ERbs>(erbsList);
			for (ERbs erbs : erbsSet) {
				if(!(erbs.isSsrRbs() && erbs.isConnected() && erbs.getCellList().size() > 0)){
					continue;
				}
				erbsAndCellDetails.append(erbs.getFdn().toString() + "::");
				erbsAndCellDetails.append(getCellDetails(erbs.getCellList()));
				return erbsAndCellDetails.toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
	}
	public def getPicoErbsAndCellDetails(){
		StringBuffer erbsAndCellDetails = new StringBuffer();
		List<ERbs> erbsList;
		try{
			SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
			if (subN != null) {
				erbsList = TopologyManager.getInstance().getERBSList(subN);
				if(erbsList == null || erbsList.isEmpty()) {
					return null;
				}
			} else{
				return null;
			}
			Set<ERbs> erbsSet = new HashSet<ERbs>(erbsList);
			for (ERbs erbs : erbsSet) {
				if(!(erbs.isPicoRbs() && erbs.isConnected() && erbs.getCellList().size() > 0)){
					continue;
				}
				erbsAndCellDetails.append(erbs.getFdn().toString() + "::");
				erbsAndCellDetails.append(getCellDetails(erbs.getCellList()));
				return erbsAndCellDetails.toString();
			}
		}catch(Exception e){
			return e.getMessage().toString();
		}
	}

	private def getCellDetails(List cells){
		StringBuffer cellDetails = new StringBuffer();
		for(Cell cell : cells) {
			cellDetails.append(cell.getOid() + "@" + cell.getFdn().toString() + ":")
		}
		return cellDetails.substring(0, cellDetails.length() - 1);
	}
	public def getPicoCellOId(String elementType){

		List<EUtranCell> eUtranCellList;
		List<RBS> rbsList;
		List<UtranCell> UtranCellList
		switch(elementType.toLowerCase()){
			case("lte-pico"):
				try{
					SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
					if (subN != null) {
						erbsList = TopologyManager.getInstance().getERBSList(subN);
						if(erbsList == null || erbsList.isEmpty()) {
							return null;
						}
					} else{
						return null;
					}
					Set<ERbs> erbsSet = new HashSet<ERbs>(erbsList);
					for (ERbs erbs : erbsSet) {
						if(!(erbs.isPicoRbs() && erbs.isConnected() && erbs.getCellList().size() > 0)){
							continue;
						}
						return "" + erbs.getCellList().get(0).getOid();
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}

				break;

			case("wcdma-pico"):
				try{
					SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
					if (subN != null) {
						rbsList = TopologyManager.getInstance().getRBSList(subN);
						if(rbsList == null || rbsList.isEmpty()) {
							return null;
						}
					} else{
						return null;
					}
					for(RBS rbs : rbsList){
						if(!(rbs.isPicoRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0)){
							continue;
						}
						return "" + rbs.getUtranCellList().get(0).getOid();
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
		}
	}
	public def getMsrbs_v2CellOId(String elementType){

		List<EUtranCell> eUtranCellList;
		List<RBS> rbsList;
		List<UtranCell> UtranCellList
		switch(elementType.toLowerCase()){
			case("lte-ssr"):
				try{
					SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
					if (subN != null) {
						erbsList = TopologyManager.getInstance().getERBSList(subN);
						if(erbsList == null || erbsList.isEmpty()) {
							return null;
						}
					} else{
						return null;
					}
					Set<ERbs> erbsSet = new HashSet<ERbs>(erbsList);
					for (ERbs erbs : erbsSet) {
						if(!(erbs.isSsrRbs() && erbs.isConnected() && erbs.getCellList().size() > 0)){
							continue;
						}
						return "" + erbs.getCellList().get(0).getOid();
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}

				break;

			case("wcdma-ssr"):
				try{
					SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
					if (subN != null) {
						rbsList = TopologyManager.getInstance().getRBSList(subN);
						if(rbsList == null || rbsList.isEmpty()) {
							return null;
						}
					} else{
						return null;
					}
					for(RBS rbs : rbsList){
						if(!(rbs.isSsrRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0)){
							continue;
						}
						return "" + rbs.getUtranCellList().get(0).getOid();
					}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
		}
	}
	public def getCellFdn(String cellOId){
		int oid = Integer.parseInt(cellOId);
		ManagedObject cellMo = TopologyManager.getInstance().getMO(oid);
		if(cellMo != null){
			return cellMo.getFdn().toString();
		}else {
			return null;
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

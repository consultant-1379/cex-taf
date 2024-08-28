
import java.sql.ResultSet;
import java.util.List;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean

import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.domain.RelationType;
import com.ericsson.oss.domain.Direction;
import com.ericsson.oss.domain.modetails.IManagedObject;

class FreqBandRelation{
	final List<String> cellfdn = new ArrayList<String>();
	final List<String> cdma2000freqband = new ArrayList<String>();
	
	MessageJobInfo Job;

	public def createCdma2000FreqBandRelation(String sourceCell, String targetCell ){

		try
		{
			cellfdn.add(sourceCell);
			cdma2000freqband.add(targetCell);

			job = CellServiceManager.getInstance().createCellRelations(RelationType.Cdma2000FreqBandRelation, Direction.UNIDIRECTIONAL,  cellfdn, cdma2000freqband, null,null,null);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
				return job.getAdditionalInfo().toString();
			}
			return "OK";
		}
		catch(Exception e){
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
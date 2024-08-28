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

class AutoConfigureRelations{

	private ICMService cmService;
	
	final List<String> eutranCellFdnsForEUtranFreq = new ArrayList<String>();
	final List<String> EUtranFrequency = new ArrayList<String>();
	
	final List<String> eutranCellFdnsForCdma2000Cell = new ArrayList<String>();
	final List<String> externalCdma2000Cell = new ArrayList<String>();
	
	final List<String> eutranCellFdnsForCdma2000Freq = new ArrayList<String>();
	final List<String> externalCdma2000Freq = new ArrayList<String>();
	
	final List<String> utranCell1 = new ArrayList<String>();
	final List<String> utranCell2 = new ArrayList<String>();
	
	final List<String> eUtranCell1 = new ArrayList<String>();
	final List<String> eUtranCell2 = new ArrayList<String>();

	public def autoConfigureEUtranFreqRelation(String eUtranFrequencyfdn, String eUtranCellFDDfdn ){
		final MessageJobInfo job;
		try	{
			eutranCellFdnsForEUtranFreq.add(eUtranCellFDDfdn);
			EUtranFrequency.add(eUtranFrequencyfdn);
			job = CellServiceManager.getInstance().createCellRelations(RelationType.EUtranFreqRelation, Direction.UNIDIRECTIONAL, eutranCellFdnsForEUtranFreq, EUtranFrequency, null, null, null);
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
	
	public def autoConfigureCdma2000CellRelation(String cdma2000Cell, String eUtranCellFDDfdn ){
		final MessageJobInfo job;
		try	{
			eutranCellFdnsForCdma2000Cell.add(eUtranCellFDDfdn);
			externalCdma2000Cell.add(cdma2000Cell);
			job = CellServiceManager.getInstance().createCellRelations(RelationType.Cdma2000CellRelation, Direction.UNIDIRECTIONAL, eutranCellFdnsForCdma2000Cell , externalCdma2000Cell, null, null, null);
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
	
	public def autoConfigureCdma2000FreqRelation(String cdma2000Freq, String eUtranCellFDDfdn ){
		final MessageJobInfo job;
		try	{
			eutranCellFdnsForCdma2000Freq.add(eUtranCellFDDfdn);
			externalCdma2000Freq.add(cdma2000Freq);
			job = CellServiceManager.getInstance().createCellRelations(RelationType.Cdma2000FreqRelation, Direction.UNIDIRECTIONAL, eutranCellFdnsForCdma2000Freq, externalCdma2000Freq, null, null, null);
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
	
	public def autoConfigureUtranRelation(String firstUtranCell, String secondUtranCell ){
		final MessageJobInfo job;
		try	{
			utranCell1.add(firstUtranCell);
			utranCell2.add(secondUtranCell);
			job = CellServiceManager.getInstance().createCellRelations(RelationType.UtranRelation, Direction.BIDIRECTIONAL, utranCell1 , utranCell2, null, null, null);
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
	
	public def autoConfigureEUtranCellRelation(String firstEUtranCell, String secondEUtranCell ){
		final MessageJobInfo job;
		try          {
						eUtranCell1.add(firstEUtranCell);
						eUtranCell2.add(secondEUtranCell);
						job = CellServiceManager.getInstance().createCellRelations(RelationType.EUtranCellRelation, Direction.BIDIRECTIONAL, eUtranCell1 , eUtranCell2, null, null, null);
						sleep(1000);
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

	
	public def getCmService(){
		final String cmServiceUrl = "rmi://masterservice:50042/CCMService";
		cmService= (ICMService) getRmiService(cmServiceUrl, ICMService.class);
		return cmService
	}
	
	private static Object getRmiService(final String url, final Class clazz) {
		final RmiProxyFactoryBean plannedManagementRmiFactory = new RmiProxyFactoryBean();
		plannedManagementRmiFactory.setServiceInterface(clazz);
		plannedManagementRmiFactory.setServiceUrl(url);
		plannedManagementRmiFactory.setRefreshStubOnConnectFailure(true);
		plannedManagementRmiFactory.afterPropertiesSet();
		return ((FactoryBean) plannedManagementRmiFactory).getObject();
	}
	
	public def checkJobFinished(final MessageJobInfo job){	
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}
}

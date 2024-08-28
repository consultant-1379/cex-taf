import java.sql.ResultSet;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean
import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;

public class CexMoBrowserRequestAction {

	private ICMService cmService;
	
	public def requestAction(String Fdn){
		final String moFdn=Fdn;
		final String actionName="ping";
		final Map<String,Object> actionMap = new HashMap<String, Object>();
		Object value = null
		final String planName = null;
		value=(int)1;
	
		try	{
			actionMap.put("host",value);
			final Object requestActionJob = getCmService().callMoAction(moFdn, actionName, actionMap, planName);
			if(requestActionJob.contains("is alive"))
			return "OK";
		}
		catch(Exception e){
			if(e.toString().contains("processingFailure"))
			return "OK CS Error";
			else
			return e.getMessage().toString();
		}
	 return "OK";
	}
	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;

		while (!job.isCompleted() && retrycount < 10){
			println("retrying..."+retrycount);
			sleep(1000);
			retrycount ++;
		}
	}
	
	//=======================================================================================================================//
	/*
	 * Services
	 */
	
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
	
}

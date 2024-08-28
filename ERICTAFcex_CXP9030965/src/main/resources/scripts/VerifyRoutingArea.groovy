import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.domain.modetails.IManagedObject;
import com.ericsson.oss.domain.Configuration;
import java.util.Map;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import java.util.List;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.domain.UtranCell;

class VerifyRoutingArea {

	List<IManagedObject> list1;

	List<IManagedObject> list2;

	private Object obj;

	private Object obj2="taf_test";

	private Object obj3="";

	private IManagedObject mo = null;

	String value;

	String result1;

	String dummy="FO";

	MessageJobInfo modifyMoJob;

	MessageJobInfo resetMoJob;

	public def VerifyRA(String fdn) throws Exception{
		try{
			list1 = CmManager.getInstance().getMOProperties(fdn, null);
			if(list1.size()>0){
				mo = list1.get(0);
			}
			if(mo==null){
				throw new Exception("MO Retrieval Failed");
			}
			getUserLabel();
			getrac();
			
			//result1=setUserLabel();
			modifyMoJob = CmManager.getInstance().modify(mo, null);
			checkJobFinished(modifyMoJob);
			if (modifyMoJob.getState() == JobState.FAILED){
							return modifyMoJob.getAdditionalInfo().toString();
			}


		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
		
		return "userLabel--" +getUserLabel() + "\t" + "rac--" + getrac() ;
	}




	public def getUserLabel(){
		obj = mo.getAttributeValue("userLabel");
		return obj.toString();
	}
	
	public def getrac(){
		obj = mo.getAttributeValue("rac");
		return obj.toString();
	}

	
	

	public def setUserLabel(){
		mo.setAttribute("localCellId", dummy);
		mo.addDirtyAttribute("localCellId");
		value= mo.getAttributeValue("localCellId");
		return value.toString();
	}
	
	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
						sleep(1000);
						retrycount ++;
		}
}

}






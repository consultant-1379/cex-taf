import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import java.util.List;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.domain.modetails.IManagedObject;

class ModifyProperties {

	private IManagedObject mo = null;


	public def UpdateProperty(String fdn) throws Exception{

		try{

			List<IManagedObject>  list = CmManager.getInstance().getMOProperties(fdn, null);

			if(list.size()>0){
				mo = list.get(0);
			}
			if(mo == null){
				throw new Exception("MO Retrieval Failed");
			}

			getUserLabel();

			setUserLabel();

			MessageJobInfo modifyMoJob = CmManager.getInstance().modify(mo, null);

			checkJobFinished(modifyMoJob);

			if (modifyMoJob.getState() == JobState.FAILED){
				return modifyMoJob.getAdditionalInfo().toString();
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}

		return "OK";
	}
	public def UpdateNegativeProperty(String fdn) throws Exception{

		try{

			List<IManagedObject>  list = CmManager.getInstance().getMOProperties(fdn, null);

			if(list.size()>0){
				mo = list.get(0);
			}
			if(mo == null){
				throw new Exception("MO Retrieval Failed");
			}

			getPropertyValue();

			setPropertyValue();

			MessageJobInfo modifyMoJob = CmManager.getInstance().modify(mo, null);

			checkJobFinished(modifyMoJob);

			if (modifyMoJob.getState() == JobState.FAILED){
				return modifyMoJob.getAdditionalInfo().toString();
			}

		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}

		return "OK";
	}
	public def ResetProperty(String fdn) throws Exception{

		try{
			List<IManagedObject> list = CmManager.getInstance().getMOProperties(fdn, null);
			if(list.size()>0){
				mo = list.get(0);
			}
			if(mo==null){
				throw new Exception("MO Retrieval Failed");
			}

			String resetparam = resetUserLabel();

			MessageJobInfo resetMoJob = CmManager.getInstance().modify(mo, null);

			checkJobFinished(resetMoJob);

			if (resetMoJob.getState() == JobState.FAILED){
				return resetMoJob.getAdditionalInfo().toString();
			}
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
		return "OK";
	}
	public def getUserLabel(){

		return mo.getAttributeValue("userLabel").toString();

	}
	public def getPropertyValue(){

		return mo.getAttributeValue("localCellId").toString();

	}
	public def setPropertyValue(){

		Object obj = -1;

		mo.setAttribute("localCellId", obj);
		mo.addDirtyAttribute("localCellId");

		return mo.getAttributeValue("localCellId").toString();

	}
	public def setUserLabel(){

		Object obj = "taf_test";

		mo.setAttribute("userLabel", obj);
		mo.addDirtyAttribute("userLabel");

		return mo.getAttributeValue("userLabel").toString();

	}

	public def resetUserLabel(){

		Object obj = "";

		mo.setAttribute("userLabel", obj);
		mo.addDirtyAttribute("userLabel");

		return mo.getAttributeValue("userLabel").toString();
	}
	
	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}
}






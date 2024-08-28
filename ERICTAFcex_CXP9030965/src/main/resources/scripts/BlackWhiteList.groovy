import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.domain.modetails.X2ListDetails;


class BlackWhiteList {

	private String x2BlackList= "x2BlackList";
	private String x2WhiteList= "x2WhiteList";
	public String enodeBFunctionFdn;
	List<X2ListDetails> nullX2list ;
	MessageJobInfo modifyBlackListJob = null;
	MessageJobInfo modifyWhiteListJob = null;

	public def AddERbsToX2BlackList(String baseErbs,String operationalErbs){


		List<X2ListDetails> list = new ArrayList<X2ListDetails>(2);
		X2ListDetails x1 = new X2ListDetails();

		x1.setErbsFdn(baseErbs);

		list.add(x1);

		enodeBFunctionFdn = operationalErbs +",ManagedElement=1,ENodeBFunction=1";

		final MessageJobInfo job = CellServiceManager.getInstance().getX2List(x2BlackList, enodeBFunctionFdn, null);

		checkJobFinished(job);
		if (job.getState() == JobState.FAILED){
			return "Method getX2List Failed:" + job.getAdditionalInfo().toString();
		}

		if(job.getList().isEmpty()){

			final MessageJobInfo insertJob =  modifyBlackList(list);
			checkJobFinished(insertJob);

			if (insertJob.getState() == JobState.FAILED){
				return "x2BlackList Modification Failed for Empty List:" + insertJob.getAdditionalInfo().toString();
			}
		}else if(!job.getList().isEmpty()){

			final MessageJobInfo insertJob = modifyBlackList(list);
			checkJobFinished(insertJob);

			if (insertJob.getState() == JobState.FAILED){
				return "x2BlackList Modification Failed for Already Exiting List:" + insertJob.getAdditionalInfo().toString();
			}
		}

		return "OK";
	}

	public def AddERbsToX2WhiteList(String baseErbs,String operationalErbs){


		List<X2ListDetails> list = new ArrayList<X2ListDetails>(2);
		X2ListDetails x1 = new X2ListDetails();

		x1.setErbsFdn(baseErbs);

		list.add(x1);

		enodeBFunctionFdn = operationalErbs +",ManagedElement=1,ENodeBFunction=1";

		final MessageJobInfo job = CellServiceManager.getInstance().getX2List(x2WhiteList, enodeBFunctionFdn, null);

		checkJobFinished(job);
		if (job.getState() == JobState.FAILED){
			return "Method getX2List Failed:" + job.getAdditionalInfo().toString();
		}

		if(job.getList().isEmpty()){

			final MessageJobInfo insertJob =  modifyWhiteList(list);
			checkJobFinished(insertJob);

			if (insertJob.getState() == JobState.FAILED){
				return "x2WhiteList Modification Failed for Empty List:" + insertJob.getAdditionalInfo().toString();
			}
		}else if(!job.getList().isEmpty()){

			final MessageJobInfo insertJob = modifyWhiteList(list);
			checkJobFinished(insertJob);

			if (insertJob.getState() == JobState.FAILED){
				return "x2WhiteList Modification Failed for Already Exiting List:" + insertJob.getAdditionalInfo().toString();
			}
		}

		return "OK";
	}

	public MessageJobInfo modifyBlackList(List<X2ListDetails> list){

		try
		{
			modifyBlackListJob = CellServiceManager.getInstance().modifyX2List(x2BlackList, enodeBFunctionFdn, list, null);

		}catch(Exception e){
			return e.getMessage().toString();
		}
		return modifyBlackListJob;
	}
	public MessageJobInfo modifyWhiteList(List<X2ListDetails> list){

		try
		{
			modifyWhiteListJob = CellServiceManager.getInstance().modifyX2List(x2WhiteList, enodeBFunctionFdn, list, null);

		}catch(Exception e){
			return e.getMessage().toString();
		}
		return modifyWhiteListJob;
	}

	public String RemoveERbsX2BlackList(String operationalErbs){

		enodeBFunctionFdn = operationalErbs +",ManagedElement=1,ENodeBFunction=1";
		nullX2list = new ArrayList<X2ListDetails>(0);

		modifyBlackListJob = CellServiceManager.getInstance().modifyX2List(x2BlackList, enodeBFunctionFdn, nullX2list, null);

		checkJobFinished(modifyBlackListJob);

		if (modifyBlackListJob.getState() == JobState.FAILED){
			return "x2BlackList Deletion Failed for Already Exiting List:" + modifyBlackListJob.getAdditionalInfo().toString();
		}

		return "OK";
	}
	
	public String RemoveERbsX2WhiteList(String operationalErbs){

		enodeBFunctionFdn = operationalErbs +",ManagedElement=1,ENodeBFunction=1";
		nullX2list = new ArrayList<X2ListDetails>(0);

		modifyWhiteListJob = CellServiceManager.getInstance().modifyX2List(x2WhiteList, enodeBFunctionFdn, nullX2list, null);

		checkJobFinished(modifyWhiteListJob);

		if (modifyWhiteListJob.getState() == JobState.FAILED){
			return "x2WhiteList Deletion Failed for Already Exiting List:" + modifyWhiteListJob.getAdditionalInfo().toString();
		}

		return "OK";
	}
	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}

}

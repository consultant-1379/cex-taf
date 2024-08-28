package com.ericsson.oss.cex.taf.operators;


import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexRemoteCommandExecutor;

public class AutoConfOperator {
	
	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());

	public static boolean VerifyAutoConfigure(String moToVerify) {
			
		String response = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lm "+ moToVerify.substring(moToVerify.lastIndexOf("SubNetwork=ONRM_ROOT"),moToVerify.length()-1));
			
		if(response.contains("exception")){
			
			return false;
		}
	 return true;
	}

	public static void deleteMo(String fdn) {
		String response = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+fdn);
		
		if(response.contains("exception")){
			
			System.out.println("FDN : "+fdn+"either not exist or unalble to deleted had following exception : /n"+response);
		}
		else {
			System.out.println("Managed Object Successfully deleted with FDN :"+fdn);
		}
		
	}
	
	public static void cleanUp(String result) {
		String fdnToDelete = result;
		
		String fdn = fdnToDelete.substring(fdnToDelete.lastIndexOf("SubNetwork=ONRM_ROOT"),fdnToDelete.length()-1);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		AutoConfOperator.deleteMo(fdn);
		
	}
	
	public static void checkExistingsUtranFreqRelation(String eUtranCellfdn){
	    String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranFreqRelation");
	    String [] values = responseFromCS.trim().split("\n");
	    for(String value : values){
	      if(value!=null && value.contains(eUtranCellfdn)){
	        executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
	      }
	    } 
	  }
	
	public static void checkExistingsUtranCellRelation(String eUtranCellfdn){
	    String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranCellRelation");
	    String [] values = responseFromCS.trim().split("\n");
	    for(String value : values){
	      if(value!=null && value.contains(eUtranCellfdn)){
	        executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
	      }
	    } 
	  }
	
	public static void checkExistingsEUtranCellRelation(String eUtranCellfdn){
	    String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellRelation");
	    String [] values = responseFromCS.trim().split("\n");
	    for(String value : values){
	      if(value!=null && value.contains(eUtranCellfdn)){
	        executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
	      }
	    } 
	  }
	public static void checkExistingsGeranCellRelation(String eUtranCellfdn){
	    String responseFromCS =executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt GeranCellRelation");
	    String [] values = responseFromCS.trim().split("\n");
	    for(String value : values){
	      if(value!=null && value.contains(eUtranCellfdn)){
	    	  System.out.println("DELETINGG ::::: "+value);
	        executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm "+value);
	      }
	    } 
	  }
}

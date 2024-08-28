package com.ericsson.oss.cex.taf.ui.getters;

import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;

public class RetrieveValueFromDatabase {

	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	final static String cstestCommand ="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS";
	final static String listOption= " lt"; //List MOs by type.
	final static String listAttributes=" la"; // List mo attribute(s).

	private static RetrieveValueFromDatabase instance=new RetrieveValueFromDatabase();

	public static RetrieveValueFromDatabase getInstance(){
		return instance;
	}

	public String getERBS(){

		final String moToFetch= " MeContext";		
		final String command=cstestCommand+listOption+moToFetch+" -f neType==4";		
		final String value = executor.simplExec(command); 
		String [] mos = value.trim().split("\n");
		for(String mo : mos)
		{
			String listeUtranCells=cstestCommand+listOption+ " EUtranCellFDD | grep "+mo;
			String eutrancells = executor.simplExec(listeUtranCells);
			if(!eutrancells.equals("")){
				return mo;
			}
		}

		return "";
	}

	public String getRBS(){

		final String toFetch= " MeContext";
		final String command=cstestCommand+listOption+toFetch+" -f neType==2";		
		final String value = executor.simplExec(command); 	
		String [] mos = value.trim().split("\n");
		for(String mo : mos)
		{
			final String Utran=" UtranCell";

			String [] values = mo.trim().split(",");
			String RNCvalue= values[0]+","+values[1];

			String output=" ";

			final String fetchIubLinkForRBS=cstestCommand+listAttributes+" "+mo+" | grep fdnOfRncIubLink | awk '{ print $5}'";		
			final String iubLinkForRBS=executor.simplExec(fetchIubLinkForRBS);		
			final String fetchAllUtranCellsinRNC=cstestCommand+listOption+Utran+" | grep "+RNCvalue;	
			final String allUtranCellsinRNC=executor.simplExec(fetchAllUtranCellsinRNC);
			String [] utrancells = allUtranCellsinRNC.trim().split("\n");
			for(String utranfdn : utrancells){
				final String fetchIubLinkForUtranCell=cstestCommand+listAttributes+" "+utranfdn+" |grep utranCellIubLink | awk '{ print $7}'";
				final String iubLinkForUtranCell=executor.simplExec(fetchIubLinkForUtranCell);
				if(iubLinkForRBS.trim().equals(iubLinkForUtranCell.trim())){				
					String utranCell=utranfdn;
					output+=utranCell+"\n";
				}
			}		

			if(!output.trim().equals("")){
				return mo;
			}		
		}
		return "";
	}

	public String getEUtranFrequency(){

		final String moToFetch=" EUtranFrequency";
		final String command=cstestCommand+listOption+moToFetch;
		final String value=executor.simplExec(command);
		return value;
	}

	public String getCdma2000Frequency(){

		final String moToFetch=" ExternalCdma2000Freq";
		final String command=cstestCommand+listOption+moToFetch;
		final String value=executor.simplExec(command);
		return value;
	}

	public String getCdma2000Cell(){

		final String moToFetch=" ExternalCdma2000Cell";
		final String command=cstestCommand+listOption+moToFetch;
		final String value=executor.simplExec(command);
		return value;
	}

	public String getMo(String value){

		String mo=null;
		final String [] mos = value.trim().split("\n");
		mo=mos[0];
		return mo;
	}

	public String getConnectionState(String mo){

		//mo should be of type Erbs or rbs
		String connectionState=null;
		final String command= cstestCommand+listAttributes+" "+mo+" | grep connectionStatus";
		final String connectValue= executor.simplExec(command);
		if(connectValue.endsWith("1")){
			connectionState="NEVER CONNECTED";
		}else if(connectValue.endsWith("2")){
			connectionState="CONNECTED";
		}else if(connectValue.endsWith("3")){
			connectionState= "DISCONNECTED";
		}else if(connectValue.endsWith("4")){
			connectionState= "NOT APPLICABLE";
		}
		return connectionState; 
	}

	public String getAdministrativeState(String mo){
		String administrativeState=null;
		final String command=cstestCommand+listAttributes+ " "+mo+" | grep administrativeState";
		final String adminValue=executor.simplExec(command);
		if(adminValue.endsWith("0")){
			administrativeState="LOCKED";
		}else if(adminValue.endsWith("1")){
			administrativeState="UNLOCKED";
		}else if(adminValue.endsWith("2")){
			administrativeState="SHUTTING_DOWN";
		}    
		return administrativeState;
	}

	public String getChildEUtranCellFDDs(String ERBSfdn){

		final String moToFetch=" EUtranCellFDD";
		final String command=cstestCommand+listOption+moToFetch+" |grep "+ERBSfdn;		
		final String value = executor.simplExec(command); 
		return value;
	}

	public String getChildUtranCells(String RBSfdn){

		final String moToFetch=" UtranCell";

		String [] values = RBSfdn.trim().split(",");
		String RNCvalue= values[0]+","+values[1];

		String output=" ";

		final String fetchIubLinkForRBS=cstestCommand+listAttributes+" "+RBSfdn+" | grep fdnOfRncIubLink | awk '{ print $5}'";		
		final String iubLinkForRBS=executor.simplExec(fetchIubLinkForRBS);		
		final String fetchAllUtranCellsinRNC=cstestCommand+listOption+moToFetch+" | grep "+RNCvalue;	
		final String allUtranCellsinRNC=executor.simplExec(fetchAllUtranCellsinRNC);
		String [] utrancells = allUtranCellsinRNC.trim().split("\n");
		for(String utranfdn : utrancells){
			final String fetchIubLinkForUtranCell=cstestCommand+listAttributes+" "+utranfdn+" |grep utranCellIubLink | awk '{ print $7}'";
			final String iubLinkForUtranCell=executor.simplExec(fetchIubLinkForUtranCell);
			if(iubLinkForRBS.trim().equals(iubLinkForUtranCell.trim())){				
				String utranCell=utranfdn;
				output+=utranCell+"\n";
			}
		}		

		return output.trim();		
	}
	
	public boolean isMoExist(final String fdn){
		final String result = cstestCommand + listAttributes + " " + fdn +" | awk '{ print $2}'";
		if(result.contains("NotExisting")) {
			return false;
		}
		return true;
	}
	
}

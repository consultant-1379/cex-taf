package com.ericsson.oss.cex.taf.tests.content;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.generic.manager.TopologyManager;

@Operator(context = Context.API)
public class VerifyContentViewOperator  implements IVerifyContentViewOperator {

	@Inject
	private TopologyManager topologyManager;

	private static boolean result = false;

	private static List<String> externalPlmnCellList = new ArrayList<String>();
	private static List<String> externalPlmnFreqList = new ArrayList<String>();
	private static List<String> lteWcdmaList = new ArrayList<String>();

	public boolean verifyContentView(String elementType) {

		switch(elementType) {
		case("RNC"):
		case("RBS"):
		case("ERBS"):
		case("PRBS"):
		case("PERBS"):
		case("WRANSSR"):
		case("LTESSR"):

			lteWcdmaList.clear();

		if(elementType.toLowerCase().contains("rnc")){
			lteWcdmaList = topologyManager.getRncList();
		}else if(elementType.toLowerCase().equals("rbs")){
			lteWcdmaList = topologyManager.getRbsList();
		}else if(elementType.toLowerCase().equals("erbs")){
			lteWcdmaList = topologyManager.getErbsList();
		}else if(elementType.toLowerCase().equals("perbs")){
			lteWcdmaList = topologyManager.getPico_ErbsList();
		}else if(elementType.toLowerCase().equals("prbs")){
			lteWcdmaList = topologyManager.getPico_RbsList();
		}else if(elementType.toLowerCase().equals("wranssr")){
			lteWcdmaList = topologyManager.getSsr_RbsList();
		}else if(elementType.toLowerCase().equals("ltessr")){
			lteWcdmaList = topologyManager.getSsr_ErbsList();
		}

		if(!lteWcdmaList.isEmpty() || lteWcdmaList != null){
			result = true;
		}
		break;

		case("ExternalCdma2000Cell"):
		case("ExternalUtranCellTDD"):
		case("ExternalUtranCellFDD"):
		case("ExternalUtranRnc"):

			externalPlmnCellList.clear();
		externalPlmnCellList = topologyManager.getExternalPlmnCellList(elementType);
		if(!externalPlmnCellList.isEmpty() || externalPlmnCellList != null){
			result = true;
		}
		break;
		case("ExternalCdma2000Freq"):
		case("ExternalGsmFreq"):
		case("ExternalUtranFreq"):
		case("ExternalEutranFrequency"):
		case("EUtranFrequency"):

			externalPlmnFreqList.clear();
		externalPlmnFreqList = topologyManager.getExternalPlmnFreqList(elementType);

		if(!externalPlmnFreqList.isEmpty() || externalPlmnFreqList != null){
			result = true;
		}
		break;

		}
		return result;
	}

}

import java.util.List;
import java.util.Random;

import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.RBS;
import com.ericsson.oss.domain.MGW;
import com.ericsson.oss.domain.RNC;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.UtranCell;
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.domain.EUtranCellTDD;
import com.ericsson.oss.domain.EUtranCellFDD;
import com.ericsson.oss.domain.EUtranFrequency;
import com.ericsson.oss.domain.ExternalUtranCell;
import com.ericsson.oss.domain.ExternalUtranPlmn;
import com.ericsson.oss.domain.ExternalPlmn;
import com.ericsson.oss.domain.ExternalUtranRnc;
import com.ericsson.oss.domain.modetails.ManagedObject;
import com.ericsson.oss.cex.topology.service.impl.TopologyServiceFactory;
import com.ericsson.oss.domain.ExternalCdma2000Cell;
import com.ericsson.oss.domain.ExternalUtranCellTDD;
import com.ericsson.oss.domain.ExternalCdma2000AN;
import com.ericsson.oss.domain.ExternalCdma2000Plmn;
import com.ericsson.oss.domain.ExternalGsmPlmn;
import com.ericsson.oss.domain.ExternalGsmLA;
import com.ericsson.oss.domain.ExternalGsmCell;
import com.ericsson.oss.domain.ExternalCdma20001xRttCell;
import com.ericsson.oss.domain.ExternalCdma2000Freq;
import com.ericsson.oss.domain.ExternalGsmFreq;
import com.ericsson.oss.domain.ExternalUtranFreq;
import com.ericsson.oss.domain.ExternalEUtranPlmn;
import com.ericsson.oss.domain.ExternalENodeBFunction;
import com.ericsson.oss.domain.ExternalEUtranCell;
import com.ericsson.oss.domain.ExternalEUtranCellFDD;
import com.ericsson.oss.domain.ExternalEUtranCellTDD;
import com.ericsson.oss.domain.ExternalEutranFrequency;
import com.ericsson.oss.domain.EUtranFrequency;
import com.ericsson.oss.domain.ExternalGsmFreqGroup;

class TopologyManagerFactory {

	List<RBS> rbsList;
	List<MGW> mgwList;
	List<RNC> rncList;
	List<ERbs> erbsList;
	List<String> filteredList;
	Random randomGenerator = new Random();

	public def getRbsList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
			for(RBS rbs : rbsList){
				if(rbs.isConnected() && !rbs.isPicoRbs() && !rbs.isSsrRbs()){
					filteredList.add(rbs.getFdn().toString());
				}
			}
		}
		return filteredList;
	}

	public def getRbs(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
			for(RBS rbs : rbsList){
				if(rbs.isConnected() && !rbs.isPicoRbs() && !rbs.isSsrRbs() && rbs.getUtranCellList().size() > 0){

					filteredList.add(rbs.getFdn().toString());
				}
			}
		}
		if(!filteredList.isEmpty()){
			return filteredList.get(randomGenerator.nextInt(filteredList.size()));
		}

		return null;
	}
	public def getRnc(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		rncList = new ArrayList<RNC>();

		if (subN != null) {
			rncList = TopologyManager.getInstance().getRNCList(subN);
			if(rncList == null || rncList.isEmpty()) {
				return null;
			}
			for(RNC rnc : rncList){
				if(rnc.isConnected()){
					return rnc.getFdn().toString();
				}
			}
		}
		return null;
	}
	public def getPico_Rbs(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
			for(RBS rbs : rbsList){
				if(rbs.isConnected() && rbs.isPicoRbs()){
					return rbs.getFdn().toString();
				}
			}
		}
		return null;
	}

	public def getPico_RbsList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
			for(RBS rbs : rbsList){
				if(rbs.isConnected() && rbs.isPicoRbs()){
					filteredList.add(rbs.getFdn().toString());
				}
			}
		}
		return filteredList;
	}
	public def getPico_Erbs(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
			for(ERbs erbs : erbsList){
				if(erbs.isConnected() && erbs.isPicoRbs()){
					return erbs.getFdn().toString();
				}
			}
		}
		return null;
	}
	public def getSsr_RbsList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
			for(RBS rbs : rbsList){
				if(rbs.isConnected() && rbs.isSsrRbs()){
					filteredList.add(rbs.getFdn().toString());
				}
			}
		}
		return filteredList;
	}

	public def getSsr_Rbs(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
			for(RBS rbs : rbsList){
				if(rbs.isConnected() && rbs.isSsrRbs()){
					return rbs.getFdn().toString();
				}
			}
		}
		return filteredList;
	}
	public def getRncList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		rncList = new ArrayList<RNC>();

		if (subN != null) {
			rncList = TopologyManager.getInstance().getRNCList(subN);
			if(rncList == null || rncList.isEmpty()) {
				return null;
			}
			for(RNC rnc : rncList){
				if(rnc.isConnected()){
					filteredList.add(rnc.getFdn().toString());
				}
			}
		}
		return filteredList;
	}

	public def getErbsList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
			for(ERbs erbs : erbsList){
				if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs()){
					filteredList.add(erbs.getFdn().toString());
				}
			}
		}
		return filteredList;
	}
	public def getErbs(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
			for(ERbs erbs : erbsList){
				if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs()){
					return erbs.getFdn().toString();
				}
			}
		}
		return null;
	}
	public def getPico_ErbsList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
			for(ERbs erbs : erbsList){
				if(erbs.isConnected() && erbs.isPicoRbs()){
					filteredList.add(erbs.getFdn().toString());
				}
			}
		}
		return filteredList;
	}
	public def getSsr_ErbsList(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
			for(ERbs erbs : erbsList){
				if(erbs.isConnected() && erbs.isSsrRbs()){
					filteredList.add(erbs.getFdn().toString());
				}
			}
		}
		return filteredList;
	}
	public def getSsr_Erbs(){

		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		filteredList  = new ArrayList<String>();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
			for(ERbs erbs : erbsList){
				if(erbs.isConnected() && erbs.isSsrRbs()){
					return erbs.getFdn().toString();
				}
			}
		}
		return null;
	}

	public def getFilteredUtranCell(String Rbsfdn){

		filteredList  = new ArrayList<String>();
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
		}
		for(RBS rbs : rbsList){
			if((rbs.getFdn().toString()).equals(Rbsfdn))
			{

				for(UtranCell cell : rbs.getUtranCellList()){
					filteredList.add(cell.getFdn().toString());
				}
			}

		}
		return filteredList;
	}

	/*
	 * Node Version specific EUtrancell
	 */
	public def getFilteredEUtranCell(String cellType, String moType, String neMIMversion){

		filteredList  = new ArrayList<String>();
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
		}

		switch(cellType){

			case("MACRO"):

				for(ERbs erbs : erbsList){
					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && (erbs.getNeMIMversion().toString().compareTo(neMIMversion) > 0)){
						for(EUtranCell cell : erbs.getCellList()){
							if(moType.equals("EUtranCellFDD")){
								if(cell instanceof EUtranCellFDD){
									return cell.getFdn().toString();
								}
							}
							else if(moType.equals("EUtranCellTDD")){
								if(cell instanceof EUtranCellTDD){
									return cell.getFdn().toString();
								}
							}
						}
					}
				}
		}
		return null ;

	}

	public def getEUtranCellList(String cellType,String erbsFdn){

		filteredList  = new ArrayList<String>();
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
		}

		switch(cellType){
			case("SSR"):

				for(ERbs erbs : erbsList){

					if(erbs.isSsrRbs() && erbs.isConnected() && erbs.getCellList().size() > 0){

						if(erbs.getFdn().toString().equals(erbsFdn)){

							for(EUtranCell cell : erbs.getCellList()){
								filteredList.add(cell.getFdn().toString());
							}
						}
					}
				}
				return filteredList;
		}
		return null;
	}

	public def getUtranCellList(String cellType, String rbsFdn){

		filteredList  = new ArrayList<String>();
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
		}
		switch(cellType){

			case("SSR"):
				for(RBS rbs : rbsList){
					if(rbs.isSsrRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0){

						if(rbs.getFdn().toString().equals(rbsFdn)){
							for(UtranCell cell : rbs.getUtranCellList()){
								filteredList.add(cell.getFdn().toString());
							}
						}
					}
				}
				return filteredList;
		}
		return null;
	}

	public def getUtranCell(String cellType){

		filteredList  = new ArrayList<String>();
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		rbsList = new ArrayList<RBS>();

		if (subN != null) {
			rbsList = TopologyManager.getInstance().getRBSList(subN);
			if(rbsList == null || rbsList.isEmpty()) {
				return null;
			}
		}
		for(RBS rbs : rbsList){

			switch(cellType){

				case("MACRO"):
					if(!rbs.isPicoRbs() && !rbs.isSsrRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0){
						for(UtranCell cell : rbs.getUtranCellList()){
							filteredList.add(cell.getFdn().toString());
						}
						if(!filteredList.isEmpty()){
							return filteredList.get(randomGenerator.nextInt(filteredList.size()));
						}
						else{
							return null;
						}
					}
				case("PICO"):
					if(rbs.isPicoRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0){
						for(UtranCell cell : rbs.getUtranCellList()){
							filteredList.add(cell.getFdn().toString());
						}
						if(!filteredList.isEmpty()){
							return filteredList.get(randomGenerator.nextInt(filteredList.size()));
						}
						else{
							return null;
						}
					}
				case("SSR"):
					if(rbs.isSsrRbs() && rbs.isConnected() && rbs.getUtranCellList().size() > 0){
						for(UtranCell cell : rbs.getUtranCellList()){
							filteredList.add(cell.getFdn().toString());
						}
						if(!filteredList.isEmpty()){
							return filteredList.get(randomGenerator.nextInt(filteredList.size()));
						}
						else{
							return null;
						}
					}
			}
		}
		return filteredList;
	}

	public def getMGWList(){

		filteredList  = new ArrayList<String>();
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		mgwList = new ArrayList<MGW>();

		if (subN != null) {
			mgwList = TopologyManager.getInstance().getMGWList(subN);
			if(mgwList == null || mgwList.isEmpty()) {
				return null;
			}
			for(MGW mgw : mgwList){
				filteredList.add(mgw.getFdn().toString());
			}
		}
		return filteredList;
	}

	public def getEUtranCell(String cellType, String moType){

		filteredList  = new ArrayList<String>();
		SubNetwork subN = TopologyManager.getInstance().getSubNetwork();
		erbsList = new ArrayList<ERbs>();

		if (subN != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subN);
			if(erbsList == null || erbsList.isEmpty()) {
				return null;
			}
		}

		switch(cellType){

			case("MACRO"):

				for(ERbs erbs : erbsList){
					if(!erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.isConnected() && erbs.getCellList().size() > 0){
						for(EUtranCell cell : erbs.getCellList()){
							if(moType.equals("EUtranCellFDD")){
								if(cell instanceof EUtranCellFDD){
									filteredList.add(cell.getFdn().toString());
								}
							}
							else if(moType.equals("EUtranCellTDD")){
								if(cell instanceof EUtranCellTDD){
									filteredList.add(cell.getFdn().toString());
								}
							}
						}
					}
				}
				return filteredList.get(randomGenerator.nextInt(filteredList.size()));
			case("PICO"):
				for(ERbs erbs : erbsList){
					if(erbs.isPicoRbs() && erbs.isConnected() && erbs.getCellList().size() > 0){
						for(EUtranCell cell : erbs.getCellList()){
							if(moType.equals("EUtranCellFDD")){
								if(cell instanceof EUtranCellFDD){
									filteredList.add(cell.getFdn().toString());
								}
							}
							else if(moType.equals("EUtranCellTDD")){
								if(cell instanceof EUtranCellTDD){
									filteredList.add(cell.getFdn().toString());
								}
							}
						}
					}
				}
				return filteredList.get(randomGenerator.nextInt(filteredList.size()));
			case("SSR"):
				for(ERbs erbs : erbsList){
					if(erbs.isSsrRbs() && erbs.isConnected() && erbs.getCellList().size() > 0){
						for(EUtranCell cell : erbs.getCellList()){
							if(moType.equals("EUtranCellFDD")){
								if(cell instanceof EUtranCellFDD){
									filteredList.add(cell.getFdn().toString());
								}
							}
							else if(moType.equals("EUtranCellTDD")){
								if(cell instanceof EUtranCellTDD){
									filteredList.add(cell.getFdn().toString());
								}
							}
						}
					}
				}
				return filteredList.get(randomGenerator.nextInt(filteredList.size()));
		}
		return null;
	}
	public def getExternalPlmnFreq(String externalType){

		filteredList  = new ArrayList<String>();

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		List<EUtranFrequency> eutranfreqlist ;
		List<ExternalCdma2000Freq> extCdma2000FreqList ;
		List<ExternalGsmFreq> extGsmFreqList ;
		List<ExternalUtranFreq> extUtranFreqList ;
		List<ExternalEutranFrequency> extEutranFreqList;
		List<ExternalPlmn> externalPlmnList;
		//		List<ExternalGsmFreqGroup> extGsmFreqGroups = new ArrayList<ExternalGsmFreqGroup>();

		switch(externalType){

			case("EUtranFrequency"):
				if (subNetwork != null) {
					eutranfreqlist = TopologyManager.getInstance().getEUtranFrequency(subNetwork);
					if(eutranfreqlist == null || eutranfreqlist.isEmpty()) {
						return null;
					}
				} else{
					return null;
				}
				for(EUtranFrequency fdn: eutranfreqlist){
					return fdn.getFdn().toString();
				}
			case("ExternalEutranFrequency"):
				if(subNetwork != null){
					extEutranFreqList = TopologyManager.getInstance().getExternalEutranFrequency(subNetwork);
					if(extEutranFreqList == null || extEutranFreqList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(ExternalEutranFrequency fdn: extEutranFreqList){
					return fdn.getFdn().toString();
				}
				break;
			case("ExternalCdma2000Freq"):
				if(subNetwork != null){
					extCdma2000FreqList = TopologyManager.getInstance().getExternalCdma2000Freq(subNetwork);
					if(extCdma2000FreqList == null || extCdma2000FreqList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(ExternalCdma2000Freq fdn: extCdma2000FreqList){
					return fdn.getFdn().toString();
				}
			case("ExternalGsmFreq"):
				if(subNetwork != null){
					extGsmFreqList = TopologyManager.getInstance().getExternalGsmFreq(subNetwork);
					if(extGsmFreqList == null || extGsmFreqList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(ExternalGsmFreq fdn: extGsmFreqList){
					return fdn.getFdn().toString();
				}
			case("ExternalUtranFreq"):
				if(subNetwork != null){
					extUtranFreqList = TopologyManager.getInstance().getExternalUtranFreq(subNetwork);
					if(extUtranFreqList == null || extUtranFreqList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(ExternalUtranFreq fdn: extUtranFreqList){
					return fdn.getFdn().toString();
				}
			case("ExternalGSMFreqGroup"):
				if(subNetwork != null){

					externalPlmnList = TopologyManager.getInstance().getNotFilteredExternalPlmns(subNetwork);
					for(ManagedObject mo : externalPlmnList){
						if(mo instanceof ExternalGsmFreqGroup){
							return mo.getFdn().toString();
						}
					}

				}
		}
		return null;
	}
	public def getExternalPlmnCellList(String externalType){

		filteredList  = new ArrayList<String>();

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		ExternalUtranPlmn externalUtranPlmn = null;
		ExternalCdma2000Plmn extenalCdma2000Plmn = null;

		switch(externalType){

			case("ExternalUtranCell"):
			case("ExternalUtranCellFDD"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {
					if (managedObject instanceof ExternalUtranPlmn) {

						externalUtranPlmn = (ExternalUtranPlmn) managedObject;

						if (externalUtranPlmn != null) {

							List<ExternalUtranRnc> externalUtranRncList = externalUtranPlmn.getExternalRncList();

							if (!externalUtranRncList.isEmpty()) {

								for (ExternalUtranRnc extUtranRnc : externalUtranRncList) {

									List<ExternalUtranCell> extUtranCellList =extUtranRnc.getExternalUtranCellList();

									sleep(3000);

									for (ExternalUtranCell uc : extUtranCellList) {
										if(uc instanceof ExternalUtranCell){
											filteredList.add(uc.getFdn().toString());
										}
									}

								}

							}else{
								return null;
							}
						}else{
							return null;
						}
					}
				}
				break;
			case("ExternalUtranCellTDD"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {
					if (managedObject instanceof ExternalUtranPlmn) {

						externalUtranPlmn = (ExternalUtranPlmn) managedObject;

						if (externalUtranPlmn != null) {

							List<ExternalUtranRnc> externalUtranRncList = externalUtranPlmn.getExternalRncList();

							if (!externalUtranRncList.isEmpty()) {

								for (ExternalUtranRnc extUtranRnc : externalUtranRncList) {

									List<ExternalUtranCell> extUtranCellList =extUtranRnc.getExternalUtranCellList();

									sleep(3000);

									for (ExternalUtranCell uc : extUtranCellList) {

										if(uc instanceof ExternalUtranCellTDD){
											filteredList.add(uc.getFdn().toString());
										}
									}

								}

							}else{
								return null;
							}
						}else{
							return null;
						}
					}
				}
				break;
			case("ExternalCdma2000Cell"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {

					if (managedObject instanceof ExternalCdma2000Plmn) {

						extenalCdma2000Plmn = (ExternalCdma2000Plmn) managedObject;

						if (extenalCdma2000Plmn != null) {

							List<ExternalCdma2000AN> externalCdma2000ANList = extenalCdma2000Plmn.getExternalCdma2000ANList();
							sleep(3000);
							if(!externalCdma2000ANList.isEmpty()){

								for (ExternalCdma2000AN cdma2000AN : externalCdma2000ANList) {

									List<ExternalCdma2000Cell> cdma2000Cell=cdma2000AN.getExternalCdma2000CellList();
									sleep(3000);
									if(!cdma2000Cell.isEmpty()){
										for(ExternalCdma2000Cell extcdma2000Cell:cdma2000Cell){
											filteredList.add(extcdma2000Cell.getFdn().toString());
										}
									}
								}
							}
						}
					}
				}
				break;
			case("ExternalGsmCell"):
				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {

					if (managedObject instanceof ExternalGsmPlmn) {

						externalGsmPlmn = (ExternalGsmPlmn) managedObject;

						if (externalGsmPlmn != null) {

							List<ExternalGsmLA> externalGsmLAList = externalGsmPlmn.getExternalGsmLAList();
							sleep(3000);
							if(!externalGsmLAList.isEmpty()){

								for (ExternalGsmLA externalGsmLA : externalGsmLAList) {

									List<ExternalGsmCell> externalGsmCellList=externalGsmLA.getExternalGsmCellList();
									sleep(3000);
									if(!externalGsmCellList.isEmpty()){
										for(ExternalGsmCell externalGsmCell : externalGsmCellList){
											filteredList.add(externalGsmCell.getFdn().toString());
										}
									}
								}
							}
						}
					}
				}
				break;
			case("ExternalUtranRNC"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {
					if (managedObject instanceof ExternalUtranPlmn) {

						externalUtranPlmn = (ExternalUtranPlmn) managedObject;

						if (externalUtranPlmn != null) {

							List<ExternalUtranRnc> externalUtranRncList = externalUtranPlmn.getExternalRncList();

							if (!externalUtranRncList.isEmpty()) {
								sleep(3000);
								for (ExternalUtranRnc extUtranRnc : externalUtranRncList) {
									filteredList.add(extUtranRnc.getFdn().toString());
								}
							}else{
								return null;
							}
						}else{
							return null;
						}
					}
				}
				break;

		}
		return filteredList;
	}
	public def getExternalPlmnFreqList(String externalType){

		filteredList  = new ArrayList<String>();

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		List<EUtranFrequency> eutranfreqlist ;
		List<ExternalCdma2000Freq> extCdma2000FreqList ;
		List<ExternalGsmFreq> extGsmFreqList ;
		List<ExternalUtranFreq> extUtranFreqList ;
		List<ExternalEutranFrequency> extEutranFreqList;
		List<EUtranFrequency> eUtranFrequencyList;

		switch(externalType){

			case("EUtranFrequency"):
				if (subNetwork != null) {
					eutranfreqlist = TopologyManager.getInstance().getEUtranFrequency(subNetwork);
					if(eutranfreqlist == null || eutranfreqlist.isEmpty()) {
						return null;
					}
				} else{
					return null;
				}
				for(EUtranFrequency fdn: eutranfreqlist){
					filteredList.add(fdn.getFdn().toString());
				}
				break;
			case("ExternalCdma2000Freq"):

				if(subNetwork != null){
					extCdma2000FreqList = TopologyManager.getInstance().getExternalCdma2000Freq(subNetwork);
					if(extCdma2000FreqList == null || extCdma2000FreqList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(ExternalCdma2000Freq fdn: extCdma2000FreqList){
					filteredList.add(fdn.getFdn().toString());
				}
				break;
			case("ExternalGsmFreq"):
				if(subNetwork != null){
					extGsmFreqList = TopologyManager.getInstance().getExternalGsmFreq(subNetwork);
					if(extGsmFreqList == null || extGsmFreqList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(ExternalGsmFreq fdn: extGsmFreqList){
					filteredList.add(fdn.getFdn().toString());
				}
				break;
			case("ExternalUtranFreq"):
				if(subNetwork != null){
					extUtranFreqList = TopologyManager.getInstance().getExternalUtranFreq(subNetwork);
					if(extUtranFreqList == null || extUtranFreqList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(ExternalUtranFreq fdn: extUtranFreqList){
					filteredList.add(fdn.getFdn().toString());
				}
				break;
			case("ExternalEutranFrequency"):
				if(subNetwork != null){
					extEutranFreqList = TopologyManager.getInstance().getExternalEutranFrequency(subNetwork);
					if(extEutranFreqList == null || extEutranFreqList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(ExternalEutranFrequency fdn: extEutranFreqList){
					filteredList.add(fdn.getFdn().toString());
				}
				break;
			case("EUtranFrequency"):
				if(subNetwork != null){
					eUtranFrequencyList = TopologyManager.getInstance().getEUtranFrequency(subNetwork);
					if(eUtranFrequencyList == null || eUtranFrequencyList.isEmpty()) {
						return null;
					}
				}else{
					return null;
				}

				for(EUtranFrequency fdn: eUtranFrequencyList){
					filteredList.add(fdn.getFdn().toString());
				}
				break;
		}
		return filteredList;
	}
	public def getExternalPlmnCell(String externalType){

		filteredList  = new ArrayList<String>();

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		ExternalUtranPlmn externalUtranPlmn = null;
		ExternalCdma2000Plmn extenalCdma2000Plmn = null;
		ExternalEUtranPlmn externalEUtranPlmn = null;
		ExternalGsmPlmn externalGsmPlmn = null;

		switch(externalType){

			case("ExternalUtranCell"):
			case("ExternalUtranCellFDD"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {
					if (managedObject instanceof ExternalUtranPlmn) {

						externalUtranPlmn = (ExternalUtranPlmn) managedObject;

						if (externalUtranPlmn != null) {

							List<ExternalUtranRnc> externalUtranRncList = externalUtranPlmn.getExternalRncList();

							if (!externalUtranRncList.isEmpty()) {

								for (ExternalUtranRnc extUtranRnc : externalUtranRncList) {

									List<ExternalUtranCell> extUtranCellList =extUtranRnc.getExternalUtranCellList();

									sleep(3000);

									for (ExternalUtranCell uc : extUtranCellList) {
										if(uc instanceof ExternalUtranCell){
											if(uc.getFdn().toString().contains("ExternalUtranCellTDD")){
												continue;
											}
											return uc.getFdn().toString();
										}
									}

								}

							}else{
								return null;
							}
						}else{
							return null;
						}
					}
				}
				break;
			case("ExternalUtranCellTDD"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {
					if (managedObject instanceof ExternalUtranPlmn) {

						externalUtranPlmn = (ExternalUtranPlmn) managedObject;

						if (externalUtranPlmn != null) {

							List<ExternalUtranRnc> externalUtranRncList = externalUtranPlmn.getExternalRncList();

							if (!externalUtranRncList.isEmpty()) {

								for (ExternalUtranRnc extUtranRnc : externalUtranRncList) {

									List<ExternalUtranCell> extUtranCellList =extUtranRnc.getExternalUtranCellList();

									sleep(3000);

									for (ExternalUtranCell uc : extUtranCellList) {

										if(uc instanceof ExternalUtranCellTDD){
											return uc.getFdn().toString();
										}
									}

								}

							}else{
								return null;
							}
						}else{
							return null;
						}
					}
				}
				break;
			case("ExternalEUtranCellTDD"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {
					if (managedObject instanceof ExternalEUtranPlmn) {

						externalEUtranPlmn = (ExternalEUtranPlmn) managedObject;

						if (externalEUtranPlmn != null) {

							List<ExternalENodeBFunction> externalENodeBList = externalEUtranPlmn.getExtEutranENBFunctionList();

							if (!externalENodeBList.isEmpty()) {

								for (ExternalENodeBFunction extENodeB : externalENodeBList) {

									List<ExternalEUtranCell> extEUtranCellList = extENodeB.getExternalEUtranCells();

									sleep(3000);

									for (ExternalEUtranCell uc : extEUtranCellList) {

										if(uc instanceof ExternalEUtranCellTDD){
											return uc.getFdn().toString();
										}
									}

								}

							}else{
								return null;
							}
						}else{
							return null;
						}
					}
				}
				break;
			case("ExternalEUtranCellFDD"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {
					if (managedObject instanceof ExternalEUtranPlmn) {

						externalEUtranPlmn = (ExternalEUtranPlmn) managedObject;

						if (externalEUtranPlmn != null) {

							List<ExternalENodeBFunction> externalENodeBList = externalEUtranPlmn.getExtEutranENBFunctionList();

							if (!externalENodeBList.isEmpty()) {

								for (ExternalENodeBFunction extENodeB : externalENodeBList) {

									List<ExternalEUtranCell> extEUtranCellList = extENodeB.getExternalEUtranCells();

									sleep(3000);

									for (ExternalEUtranCell uc : extEUtranCellList) {

										if(uc instanceof ExternalEUtranCellFDD){
											return uc.getFdn().toString();
										}
									}

								}

							}else{
								return null;
							}
						}else{
							return null;
						}
					}
				}
			case("ExternalUtranRNC"):

				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {
					if (managedObject instanceof ExternalUtranPlmn) {

						externalUtranPlmn = (ExternalUtranPlmn) managedObject;

						if (externalUtranPlmn != null) {

							List<ExternalUtranRnc> externalUtranRncList = externalUtranPlmn.getExternalRncList();

							if (!externalUtranRncList.isEmpty()) {
								sleep(3000);
								for (ExternalUtranRnc extUtranRnc : externalUtranRncList) {
									return extUtranRnc.getFdn().toString();
								}
							}else{
								return null;
							}
						}else{
							return null;
						}
					}
				}
			case("ExternalGsmCell"):
				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {

					if (managedObject instanceof ExternalGsmPlmn) {

						externalGsmPlmn = (ExternalGsmPlmn) managedObject;

						if (externalGsmPlmn != null) {

							List<ExternalGsmLA> externalGsmLAList = externalGsmPlmn.getExternalGsmLAList();
							sleep(3000);
							if(!externalGsmLAList.isEmpty()){

								for (ExternalGsmLA externalGsmLA : externalGsmLAList) {

									List<ExternalGsmCell> externalGsmCellList=externalGsmLA.getExternalGsmCellList();
									sleep(3000);
									if(!externalGsmCellList.isEmpty()){
										for(ExternalGsmCell externalGsmCell : externalGsmCellList){
											return externalGsmCell.getFdn().toString();
										}
									}
								}
							}
						}
					}
				}
			case("ExternalCdma2000Cell"):
				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {

					if (managedObject instanceof ExternalCdma2000Plmn) {

						extenalCdma2000Plmn = (ExternalCdma2000Plmn) managedObject;

						if (extenalCdma2000Plmn != null) {

							List<ExternalCdma2000AN> externalCdma2000ANList = extenalCdma2000Plmn.getExternalCdma2000ANList();
							sleep(3000);
							if(!externalCdma2000ANList.isEmpty()){

								for (ExternalCdma2000AN cdma2000AN : externalCdma2000ANList) {

									List<ExternalCdma2000Cell> cdma2000Cell=cdma2000AN.getExternalCdma2000CellList();
									sleep(3000);
									if(!cdma2000Cell.isEmpty()){
										for(ExternalCdma2000Cell extcdma2000Cell:cdma2000Cell){
											return extcdma2000Cell.getFdn().toString();
										}
									}
								}
							}
						}
					}
				}
			case("ExternalCdma20001xRttCell"):
				List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

				for (ManagedObject managedObject : plmnlist) {

					if (managedObject instanceof ExternalCdma2000Plmn) {

						extenalCdma2000Plmn = (ExternalCdma2000Plmn) managedObject;

						if (extenalCdma2000Plmn != null) {

							List<ExternalCdma2000AN> externalCdma2000ANList = extenalCdma2000Plmn.getExternalCdma2000ANList();
							sleep(3000);
							if(!externalCdma2000ANList.isEmpty()){

								for (ExternalCdma2000AN cdma2000AN : externalCdma2000ANList) {

									List<ExternalCdma20001xRttCell> cdma20001xRttCell=cdma2000AN.getExternalCdma20001xRttCellList();
									sleep(3000);
									if(!cdma20001xRttCell.isEmpty()){
										for(ExternalCdma20001xRttCell extcdma20001xRttCell:cdma20001xRttCell){
											return extcdma20001xRttCell.getFdn().toString();
										}
									}
								}
							}
						}
					}
				}
		}
		return null;
	}

	public def getFilteredGSMCell(String frequencyvalue){

		filteredList  = new ArrayList<String>();

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		ExternalGsmPlmn externalGsmPlmn = null;

		List<ExternalPlmn> plmnlist = subNetwork.getExternalPlmnList();

		for (ManagedObject managedObject : plmnlist) {

			if (managedObject instanceof ExternalGsmPlmn) {

				externalGsmPlmn = (ExternalGsmPlmn) managedObject;

				if (externalGsmPlmn != null) {

					List<ExternalGsmLA> externalGsmLAList = externalGsmPlmn.getExternalGsmLAList();
					sleep(3000);
					if(!externalGsmLAList.isEmpty()){

						for (ExternalGsmLA externalGsmLA : externalGsmLAList) {

							List<ExternalGsmCell> externalGsmCellList=externalGsmLA.getExternalGsmCellList();
							sleep(3000);
							if(!externalGsmCellList.isEmpty()){
								for(ExternalGsmCell externalGsmCell : externalGsmCellList){
									
									if(externalGsmCell.getBcchFrequency().toString().equals(frequencyvalue)){
										
										return externalGsmCell.getFdn().toString();
									}

								}
							}
						}
					}
				}
			}
		}

	}

}

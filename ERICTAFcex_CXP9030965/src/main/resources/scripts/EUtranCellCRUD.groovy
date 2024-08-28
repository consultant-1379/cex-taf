import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Map;
import org.eclipse.core.runtime.Platform;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.common.domain.IGenericItem;
import com.ericsson.oss.common.domain.GenericItem;
import com.ericsson.oss.common.cm.ui.core.CmManager;
import com.ericsson.oss.cex.topology.core.TopologyManager;
import com.ericsson.oss.domain.ERbs;
import com.ericsson.oss.domain.FDN;
import com.ericsson.oss.domain.SubNetwork;
import com.ericsson.oss.domain.EUtranCell;
import com.ericsson.oss.cex.cellservice.core.CellServiceManager;
import com.ericsson.oss.domain.modetails.IManagedObject;
import com.ericsson.oss.cex.crud.ui.core.ServiceFacade;
import com.ericsson.oss.cp.core.OCP;

class EUtranCellCRUD {

	protected IGenericItem EUtranCellInstance;
	public List<IGenericItem> genericItemList  =  new ArrayList<IGenericItem>();
	private String MO_TYPE = null;
	String nodeModel="ERBS_NODE_MODEL";
	MessageJobInfo addMoJob;
	protected HashSet<Object> dirtyAttributesSet = new HashSet<Object>();
	protected Map<String,Object> dirtyAttributesLogMap = new HashMap<String,Object>();
	protected Map<String,Object> attributesMap = new HashMap<String,Object>();
	private ERbs erbsMo=null;
	private EUtranCell cell = null;
	String erbsFunctionFdn = null;
	String erbsMimVersion;
	String erbsfdn = null;
	private final String TDD_ADD_ON = ",ManagedElement=1,SectorEquipmentFunction=";
	private String ADD_ON = ",ManagedElement=1,ENodeBFunction=1,SectorCarrier=1";
	Integer TAC = 0;
	Integer MCC = 0;
	Integer MNC = 0;
	Integer MNCLENGTH = 0;
	Random random = new Random();

	public def getErbs(String nodeType){

		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		List<ERbs> erbsList;
		if (subNetwork != null) {
			erbsList = TopologyManager.getInstance().getERBSList(subNetwork);
		} else {
			return "No Subnetwork with ERBS";
		}
		switch(nodeType){
			case("EUtranCellFDD"):
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = cell.getTac();
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
			case("EUtranCellTDD"):
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && erbs.getNeMIMversion().toString().startsWith("vF.1")){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = cell.getTac();
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
				case("EUtranCellTDD-49"):
				nodeType = "EUtranCellTDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && erbs.getNeMIMversion().toString().startsWith("vF.1")){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = cell.getTac();
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
				case("EUtranCellTDD-50"):
				 nodeType = "EUtranCellTDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && erbs.getNeMIMversion().toString().startsWith("vF.1")){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = cell.getTac();
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
				case("EUtranCellTDD-51"):
				nodeType = "EUtranCellTDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && erbs.getNeMIMversion().toString().startsWith("vF.1")){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = cell.getTac();
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
			//Club both FDD/TDD with random TAC values
			case("EUtranCellFDD/TDD"):
				nodeType = "EUtranCellFDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && erbs.getNeMIMversion().toString().startsWith("vG.1")){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = random.nextInt(65535);
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
			//Club both TDD/FDD with random TAC values
			case("EUtranCellTDD/FDD"):
				nodeType = "EUtranCellTDD";

				for(EUtranCell Ecell : erbsMo.getCellList()){
					if(Ecell.getType().toString().equals(nodeType)){
						cell = Ecell;
						TAC = random.nextInt(65535);
						MO_TYPE = nodeType;
						return true;
					}
				}
			//Implement for the random TAC value
			case("EUtranCellFDD-TAC"):
				nodeType = "EUtranCellFDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = random.nextInt(65535);
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
			//Implement for the random TAC value
			case("EUtranCellTDD-TAC"):
				nodeType = "EUtranCellTDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = random.nextInt(65535);
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
			//Implement for the earfcndl(66436-67135) and earfcnul(131972-132671) within range of band 66A value
			case("EUtranCellFDD-66A"):
				nodeType = "EUtranCellFDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = random.nextInt(65535);
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
			//Implement for the earfcndl(67136-67335) and earfcnul(0) within range of band 66 value
			case("EUtranCellFDD-66"):
				nodeType = "EUtranCellFDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = random.nextInt(65535);
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
				//Implement for the earfcndl(67836-68335) and earfcnul(N/A) within range of band 69 value
				case("EUtranCellFDD-69"):
					nodeType = "EUtranCellFDD";
					for(ERbs erbs: erbsList){
	
						if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){
	
							for(EUtranCell Ecell : erbs.getCellList()){
								if(Ecell.getType().toString().equals(nodeType)){
									cell = Ecell;
									TAC = random.nextInt(65535);
									erbsMo = erbs;
									MO_TYPE = nodeType;
									return true;
								}
							}
						}
					}
					//Implement for the earfcndl(68336-68485) and earfcnul(132972-133121) within range of band 69 value
					case("EUtranCellFDD-70"):
						nodeType = "EUtranCellFDD";
						for(ERbs erbs: erbsList){
		
							if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){
		
								for(EUtranCell Ecell : erbs.getCellList()){
									if(Ecell.getType().toString().equals(nodeType)){
										cell = Ecell;
										TAC = random.nextInt(65535);
										erbsMo = erbs;
										MO_TYPE = nodeType;
										return true;
									}
								}
							}
						}
						//Implement for the earfcndl(54540-55239) and earfcnul(54540-55239) within range of band 47 value
						case("EUtranCellFDD-47"):
					       nodeType = "EUtranCellFDD";
						  for(ERbs erbs: erbsList){

							if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){

								for(EUtranCell Ecell : erbs.getCellList()){
												if(Ecell.getType().toString().equals(nodeType)){
													cell = Ecell;
													TAC = random.nextInt(65535);
													erbsMo = erbs;
													MO_TYPE = nodeType;
													return true;
							     	}
								}
					    	}
		                 }
										//Implement for the earfcndl(55240-56739) and earfcnul(55240-56739) within range of band 48 value
						case("EUtranCellFDD-48"):
						  nodeType = "EUtranCellFDD";
						   for(ERbs erbs: erbsList){

				 		if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){

							for(EUtranCell Ecell : erbs.getCellList()){
								if(Ecell.getType().toString().equals(nodeType)){
										cell = Ecell;
										TAC = random.nextInt(65535);
										erbsMo = erbs;
										MO_TYPE = nodeType;
										return true;
											}
										}
									}
								}
             	//Implement for the earfcndl(68586-68935) and earfcnul(133122-133471) within range of band 71 value
                                  case("EUtranCellFDD-71"):
                                     nodeType = "EUtranCellFDD";
                                          for(ERbs erbs: erbsList){
                
                                      if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){
                
                                                 for(EUtranCell Ecell : erbs.getCellList()){
                                                      if(Ecell.getType().toString().equals(nodeType)){
                                                                                                                                                cell = Ecell;
                                                                  TAC = random.nextInt(65535);
                                                                  erbsMo = erbs;
                                                                  MO_TYPE = nodeType;
                                                                  return true;
                                                               }
                                                            }
                                                         }
                                                      }
										  //Implement for the earfcndl(68936-68985) and earfcnul(133472-133521) within range of band 72 value
										  case("EUtranCellFDD-72"):
										  nodeType = "EUtranCellFDD";
										   for(ERbs erbs: erbsList){
				
										 if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){
				
											for(EUtranCell Ecell : erbs.getCellList()){
												if(Ecell.getType().toString().equals(nodeType)){
														cell = Ecell;
														TAC = random.nextInt(65535);
														erbsMo = erbs;
														MO_TYPE = nodeType;
														return true;
															}
														}
													}
												}
										   //Implement for the earfcndl(68986-69035) and earfcnul(1331522-133571) within range of band 73 value
										   case("EUtranCellFDD-73"):
										   nodeType = "EUtranCellFDD";
											for(ERbs erbs: erbsList){
				 
										  if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){
				 
											 for(EUtranCell Ecell : erbs.getCellList()){
												 if(Ecell.getType().toString().equals(nodeType)){
														 cell = Ecell;
														 TAC = random.nextInt(65535);
														 erbsMo = erbs;
														 MO_TYPE = nodeType;
														 return true;
															 }
														 }
													 }
												 }
											//Implement for the earfcndl(69036-69465) and earfcnul(1331572-134001) within range of band 74 value
											case("EUtranCellFDD-74"):
											nodeType = "EUtranCellFDD";
											 for(ERbs erbs: erbsList){
				  
										   if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){
				  
											  for(EUtranCell Ecell : erbs.getCellList()){
												  if(Ecell.getType().toString().equals(nodeType)){
														  cell = Ecell;
														  TAC = random.nextInt(65535);
														  erbsMo = erbs;
														  MO_TYPE = nodeType;
														  return true;
															  }
														  }
													  }
												  }
											 //Implement for the earfcndl(70366-70545) and earfcnul(134002-134181) within range of band 85 value
											 case("EUtranCellFDD-85"):
											 nodeType = "EUtranCellFDD";
											  for(ERbs erbs: erbsList){
				   
											if(erbs.isConnected() && !erbs.isPicoRbs() && !erbs.isSsrRbs() && erbs.getCellList().size() > 0 && ((erbs.getNeMIMversion().toString().compareTo("vG.1") > 0))){
				   
											   for(EUtranCell Ecell : erbs.getCellList()){
												   if(Ecell.getType().toString().equals(nodeType)){
														   cell = Ecell;
														   TAC = random.nextInt(65535);
														   erbsMo = erbs;
														   MO_TYPE = nodeType;
														   return true;
															   }
														   }
													   }
												   }
			case("SSR-Erbs"):
				nodeType = "EUtranCellFDD";
				for(ERbs erbs: erbsList){

					if(erbs.isConnected() && erbs.isSsrRbs() && erbs.getCellList().size() > 0 && (erbs.getMirrorRelease().toString().compareTo("16B") >= 0)){

						for(EUtranCell Ecell : erbs.getCellList()){
							if(Ecell.getType().toString().equals(nodeType)){
								cell = Ecell;
								TAC = random.nextInt(65535);
								erbsMo = erbs;
								MO_TYPE = nodeType;
								return true;
							}
						}
					}
				}
		}
	}
	public def getEUtranCellInstance(String nodeType){

		try {

			getErbs(nodeType);

			erbsfdn = erbsMo.getFdn().toString();
			erbsMimVersion = erbsMo.getNeMIMversion().toString();
			MCC = cell.getMcc();
			MNC = cell.getMnc();
			MNCLENGTH = cell.getMncLength();

			if(nodeType.equals("SSR-Erbs")){
				nodeModel = "Lrat";
				erbsFunctionFdn = erbsfdn+",ManagedElement=" +erbsfdn.substring(erbsfdn.lastIndexOf('=')+1 )+ ",ENodeBFunction=1";
				erbsMimVersion = erbsMo.getMoMimVersion().getVersion();
			}else{
				erbsFunctionFdn = erbsfdn+",ManagedElement=1,ENodeBFunction=1";
				nodeModel = "ERBS_NODE_MODEL";
			}

			EUtranCellInstance = CmManager.getInstance().createGenericItem(erbsFunctionFdn,nodeModel,erbsMimVersion,MO_TYPE);

			sleep(5000);
			updateEUtranCellInstance(nodeType);

			return attributesMap.toString() + "\nMO_TYPE="+MO_TYPE;

		} catch (Exception e) {
			return  e.getMessage().toString();
		}
	}

	public String createEUtranCell(){

		List<IManagedObject> mos = new ArrayList<IManagedObject>();
		IManagedObject newManagedObject = (IManagedObject)Platform.getAdapterManager().getAdapter(EUtranCellInstance, IManagedObject.class);

		mos.add(newManagedObject);

		final MessageJobInfo addMoJob = ServiceFacade.getInstance().createMo(mos, OCP.getUserId(), null);

		checkJobFinished(addMoJob);

		if (addMoJob.getState() == JobState.FAILED){
			return addMoJob.getAdditionalInfo().toString();
		}

		return "OK";
	}
	public def deleteEUtranCell() {

		sleep(6000);
		String newCreatedCellfdn = null;
		String userLabel = attributesMap.get("userLabel");

		List<EUtranCell> eUtranCellList = erbsMo.getCellList();
		int count = 0;

		for (EUtranCell eUtranCell : eUtranCellList) {

			if(eUtranCell.getUserLabel().toString().contains("TAF_CELL")) {

				newCreatedCellfdn = eUtranCell.getFdn().toString();

				EUtranCellInstance.setId(newCreatedCellfdn);
				EUtranCellInstance.setName(newCreatedCellfdn);

				IGenericItem[] genericItemArray = new GenericItem[1];

				genericItemArray[0] = EUtranCellInstance;

				final MessageJobInfo addMoJob = CmManager.getInstance().deleteMo(genericItemArray, null);
				checkJobFinished(addMoJob);
				count++;
				if (addMoJob.getState() == JobState.FAILED){
					return addMoJob.getAdditionalInfo().toString();
				}
			}
		}
		return "Deleted count="+count;
	}
	public def cleanUpEUtranCell() {

		List<EUtranCell> eUtranCellList;
		SubNetwork subNetwork = TopologyManager.getInstance().getSubNetwork();
		String newCreatedCellfdn = null;
		int count = 0;
		eUtranCellList = TopologyManager.getInstance().getECellList(subNetwork);
		Set<EUtranCell> eUtranCellSet = new HashSet<EUtranCell>(eUtranCellList);

		for (EUtranCell cell : eUtranCellSet) {
			if(cell.getUserLabel().toString().contains("TAF_CELL")){

				newCreatedCellfdn = cell.getFdn().toString();

				EUtranCellInstance.setId(newCreatedCellfdn);
				EUtranCellInstance.setName(newCreatedCellfdn);

				IGenericItem[] genericItemArray = new GenericItem[1];

				genericItemArray[0] = EUtranCellInstance;

				final MessageJobInfo addMoJob = CmManager.getInstance().deleteMo(genericItemArray, null);
				checkJobFinished(addMoJob);
				count++;
				//				if (addMoJob.getState() == JobState.FAILED){
				//					return addMoJob.getAdditionalInfo().toString();
				//				}
			}
		}
		return "Deleted count="+count;
	}

	private void updateEUtranCellInstance(String nodeType){

		String ADD_ON = null;
		Integer phyLyrCellIdGroup = 0;
		Integer phyLyrSubCellId = 0;
		Integer earfcndl = 123;
		Integer earfcnul = earfcndl + 18000;
		Integer earfcn = 36111; // 36000 - 44900
		Integer subframeAssignment = 1;
		Integer cellId = random.nextInt(255);

		if (erbsMo != null) {
			List<EUtranCell> eutranCellList = erbsMo.getCellList();
			List<Integer> cIdList = new ArrayList<Integer>();
			for (EUtranCell euCell : eutranCellList) {
				cIdList.add(euCell.getCellId());
			}

			boolean isUniqueCellIdFound = false;
			while (!isUniqueCellIdFound) {
				if (cIdList.contains(cellId)) {
					cellId = random.nextInt(255);
				} else {
					isUniqueCellIdFound = true;
					break;
				}
			}
		}

		Object[] adtPlmnResrvdList = new Object[5];
		Arrays.fill(adtPlmnResrvdList, Boolean.FALSE);
		List<Object> boolList = Arrays.asList(adtPlmnResrvdList);

		HashMap<String, Integer> plmnMap = new HashMap<String, Integer>();
		List<String> plmnList = new ArrayList<String>();
		plmnMap.put("mcc", MCC);
		plmnMap.put("mnc", MNC);
		plmnMap.put("mncLength", MNCLENGTH);
		plmnList.add(plmnMap);

		if(nodeType.equals("SSR-Erbs")){
			ADD_ON = ",ManagedElement=" +erbsfdn.substring(erbsfdn.lastIndexOf('=')+1 )+ ",ENodeBFunction=1,SectorCarrier=1";
		}else{
			ADD_ON = ",ManagedElement=1,ENodeBFunction=1,SectorCarrier=1";
		}
		
		List<String> sector = new ArrayList<String>();
		sector.add(erbsfdn+ADD_ON);

		attributesMap.clear();
		dirtyAttributesSet.clear();

		attributesMap.put("cellId", cellId);
		attributesMap.put("physicalLayerCellIdGroup", phyLyrCellIdGroup);
		attributesMap.put("physicalLayerSubCellId", phyLyrSubCellId);
		attributesMap.put("sectorCarrierRef", sector);
		attributesMap.put("tac", TAC);
		attributesMap.put("userLabel", "TAF_CELL_"+cellId);
		attributesMap.put("additionalPlmnReservedList", boolList);
		attributesMap.put("additionalPlmnList", plmnList);

		dirtyAttributesSet.add("userLabel");
		dirtyAttributesSet.add("cellId");
		dirtyAttributesSet.add("physicalLayerCellIdGroup");
		dirtyAttributesSet.add("physicalLayerSubCellId");
		dirtyAttributesSet.add("sectorCarrierRef");
		dirtyAttributesSet.add("tac");
		dirtyAttributesSet.add("additionalPlmnReservedList");
		dirtyAttributesSet.add("additionalPlmnList");

		if(MO_TYPE.equals("EUtranCellFDD")) {

			attributesMap.put("earfcndl", earfcndl);
			attributesMap.put("earfcnul", earfcnul);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");
		}else if(MO_TYPE.equals("EUtranCellTDD")){
			//handle custom TDD attributes
			attributesMap.put("earfcn", earfcn);
			attributesMap.put("subframeAssignment", subframeAssignment);
			dirtyAttributesSet.add("earfcn");
			dirtyAttributesSet.add("subframeAssignment");
		}

		if(nodeType.equals("EUtranCellFDD-66A")){
			// Handle for earfcndl(66436-67135) and earfcnul(131972-132671) value for vH version erbs node

			attributesMap.put("earfcndl", 66436);
			attributesMap.put("earfcnul", 131972);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-66")){
			//Handle for the earfcndl(67136-67335) and earfcnul(0) within range of band 66 value for vH version erbs node

			attributesMap.put("earfcndl", 67136);
			attributesMap.put("earfcnul", 0);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-68")){
			//Handle for the earfcndl(67536-67835) and earfcnul(132672-132971) within range of band 68 value for vH version erbs node

			attributesMap.put("earfcndl", 67536);
			attributesMap.put("earfcnul", 132672);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-69")){
			//Handle for the earfcndl(67836-68335) and earfcnul(N/A) within range of band 69 value for vH version erbs node

			attributesMap.put("earfcndl", 67836);
			attributesMap.put("earfcnul", 0);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-70")){
			//Handle for the earfcndl(68336-68485) and earfcnul(132972-133121) within range of band 70 value for vH version erbs node

			attributesMap.put("earfcndl", 68336);
			attributesMap.put("earfcnul", 132972);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-47")){
			//Handle for the earfcndl(54540-55239) and earfcnul(54540-55239) within range of band 69 value for vH version erbs node

			attributesMap.put("earfcndl", 54540);
			attributesMap.put("earfcnul", 54540);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

           }
            if(nodeType.equals("EUtranCellFDD-48")){
			//Handle for the earfcndl(55240-56739) and earfcnul(55240-56739) within range of band 70 value for vH version erbs node

			attributesMap.put("earfcndl", 55240);
			attributesMap.put("earfcnul", 55240);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

           }
			
			if(nodeType.equals("EUtranCellFDD-49")){
				//Handle for the earfcndl(56740-58239)  within range of band 49 value for vH version erbs node
	
				attributesMap.put("earfcn", 56740);
				attributesMap.put("subframeAssignment", subframeAssignment);
				dirtyAttributesSet.add("earfcn");
				dirtyAttributesSet.add("subframeAssignment");
	
			   }
			if(nodeType.equals("EUtranCellFDD-50")){
				//Handle for the earfcndl(58240-59089)  within range of band 50 value for vH version erbs node
	
				attributesMap.put("earfcn", 58240);
				attributesMap.put("subframeAssignment", subframeAssignment);
				dirtyAttributesSet.add("earfcn");
				dirtyAttributesSet.add("subframeAssignment");
	
			   }
			if(nodeType.equals("EUtranCellFDD-51")){
				//Handle for the earfcndl(59090-59139)  within range of band 51 value for vH version erbs node
	
				attributesMap.put("earfcn", 59090);
				attributesMap.put("subframeAssignment", subframeAssignment);
				dirtyAttributesSet.add("earfcn");
				dirtyAttributesSet.add("subframeAssignment");
	
			   }
		if(nodeType.equals("EUtranCellFDD-71")){
			//Handle for the earfcndl(68586-68935) and earfcnul(133122-133471) within range of band 71 value for vH version erbs node

			attributesMap.put("earfcndl", 68586);
			attributesMap.put("earfcnul", 133122);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

        }
		if(nodeType.equals("EUtranCellFDD-72")){
			//Handle for the earfcndl(68936-68985) and earfcnul(1331472-133521) within range of band 72 value for vH version erbs node

			attributesMap.put("earfcndl", 68936);
			attributesMap.put("earfcnul", 133472);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		
		if(nodeType.equals("EUtranCellFDD-73")){
			//Handle for the earfcndl(68986-69035) and earfcnul(133522-133571) within range of band 73 value for vH version erbs node

			attributesMap.put("earfcndl", 68986);
			attributesMap.put("earfcnul", 133522);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-74")){
			//Handle for the earfcndl(69036-69465) and earfcnul(133572-134001) within range of band 74 value for vH version erbs node

			attributesMap.put("earfcndl", 69036);
			attributesMap.put("earfcnul", 133572);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-75")){
			//Handle for the earfcndl(69466-70315) and earfcnul(0) within range of band 75 value for vH version erbs node

			attributesMap.put("earfcndl", 69466);
			attributesMap.put("earfcnul", 0);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-76")){
			//Handle for the earfcndl(70316-70365) and earfcnul(0) within range of band 76 value for vH version erbs node

			attributesMap.put("earfcndl", 70316);
			attributesMap.put("earfcnul", 0);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		if(nodeType.equals("EUtranCellFDD-85")){
			//Handle for the earfcndl(70366-70545) and earfcnul(134002-134181) within range of band 71 value for vH version erbs node

			attributesMap.put("earfcndl", 70366);
			attributesMap.put("earfcnul", 134002);
			dirtyAttributesSet.add("earfcnul");
			dirtyAttributesSet.add("earfcndl");

		}
		EUtranCellInstance.setPropertyValue("attributes", attributesMap);
		EUtranCellInstance.setPropertyValue("dirtyAttributes", dirtyAttributesSet);

	}
	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}
}
package com.ericsson.oss.cex.taf.ui.getters;



public class FdnGetter {

	private static CexRemoteCommandExecutor executor = CexApiGetter.getRemoteCommandExecutor(CexApiGetter.getHostMaster());
	private static String cellFdn;
	private static String externalFdn;
	private static String utrancellfdn;

	public static boolean createFileForLte() {

		String ltenodefdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt MeContext -f synchronisationProgress==\"100\" |grep MeContext=LTE |head -1 ");

		if (ltenodefdn != null && ltenodefdn.contains("ERBS")) {
			cellFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lm "+ltenodefdn+" | grep EUtranCell | cut -f1-5 -d\",\" | tail -1");
			if (cellFdn != null && cellFdn.contains("LTE")) {
				return true;
			}
		}
		return false;

	}

	public static String getFDN(){
		createFileForLte();
		return cellFdn;

	}
	public static String getUtranCell() {
		utrancellfdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt UtranCell | grep UtranCell=RNC | head -1");
		return utrancellfdn;
	}

	public static String getExternalGsmFDN() {
		externalFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt ExternalGsmCell | grep -v MeContext | head -1");
		return externalFdn;
	}

	public static String getEutranCellFDDfdn() {
		externalFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellFDD | tail -1");
		if(externalFdn.contains("NETSim") || externalFdn.contains("PRBS") || externalFdn.contains("dg2"))
			externalFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellFDD | head -1");
		return externalFdn;
	}

	public static String getEutranCellTDDfdn() {
		externalFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellTDD | tail -1");
		if(externalFdn.contains("NETSim") || externalFdn.contains("PRBS") || externalFdn.contains("dg2"))
			externalFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt EUtranCellTDD | head -1");
		return externalFdn;
	}

	public static String getExternalUtranCellfdn() {
		externalFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt ExternalUtranCell | head -1");
		return externalFdn;
	}
	public static String getExternalUtranFreqfdn() {
		externalFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt ExternalUtranFreq | head -1");
		return externalFdn;
	}
	public static String getExternalEUtranCellTddFdn() {
		externalFdn = executor.simplExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt ExternalEUtranCellTDD | head -1");
		return externalFdn;
	}

	public static String getEutranCellFDN() {
		createFileForLte();
		return cellFdn;
	}
}

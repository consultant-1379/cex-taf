package com.ericsson.oss.cex.taf.ui.getters;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.oss.taf.hostconfigurator.OssHost;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;

public class CexRemoteCommandExecutor {
	//	 boolean useCLI = false;
	//	Host host;

	CLICommandHelper sshExecutor;

	CLI cliExecutor;

	public CexRemoteCommandExecutor(final Host host) {

		//		setHost(host);
		sshExecutor = new CLICommandHelper(host);
		cliExecutor = new CLI(host);
	}

	public CexRemoteCommandExecutor(final OssHost host , boolean user) {

		//		setHost(host);
		sshExecutor = new CLICommandHelper(host);
		cliExecutor = new CLI(host,host.getRootUser());
	}

	//	public void setHost(final Host host) {
	//
	//		this.host = host;
	//	}

	//	public Host getHost() {
	//
	//		return host;
	//	}

	public String simplExec(final String cmdWithArgs) {

		return  sshExecutor.simpleExec(cmdWithArgs).trim();
	}

	public String simplExec(final String cmdWithArgs, final boolean sendOnly) {

		//	        if (useCLI) {
		//return executeCommandUseCLIAPI(cmdWithArgs, sendOnly);
		//	        }
		//	        else {
		return executeCommandUseSshAPI(cmdWithArgs, sendOnly);
		//	        }
	}

	private String executeCommandUseSshAPI(final String cmdWithArgs, final boolean sendOnly) {

//		return sshExecutor.simplExec(cmdWithArgs, sendOnly);
		return sshExecutor.simpleExec(cmdWithArgs).trim();
		// sshExecutor.disconnect();
	}

	//	    private String executeCommandUseCLIAPI(final String cmdWithArgs, final boolean sendOnly) {
	//
	//	        Shell shell = cliExecutor.openShell(Terminal.VT100);
	//	        shell = cliExecutor.executeCommand(Terminal.VT100, cmdWithArgs);
	//
	//	        final String returnVal = "";
	//	        if (!sendOnly) {
	//	            return shell.read();
	//	        }
	//
	//	        shell.disconnect();
	//	        return returnVal;
	//	    }

}

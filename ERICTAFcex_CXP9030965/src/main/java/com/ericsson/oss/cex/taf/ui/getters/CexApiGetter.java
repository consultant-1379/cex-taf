
package com.ericsson.oss.cex.taf.ui.getters;

import java.util.List;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.handlers.netsim.implementation.SshNetsimHandler;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;
import com.ericsson.oss.taf.hostconfigurator.OssHost;
import com.ericsson.cifwk.taf.tools.cli.handlers.impl.RemoteObjectHandler;

public class CexApiGetter {

    private static final OssHost masterHost = HostGroup.getOssmaster();//DataHandler.getHostByName("ossmaster");

    private static final Host masterRootHost = DataHandler.getHostByName("ossmasterRoot");
    
    private static final List<Host> netsimHost = HostGroup.getAllNetsims();// DataHandler.get.getHostByName("netsim");

    //Not in use for Cex
    private static final Host infraServerHost = DataHandler.getHostByName("infraServer");
    private static final Host gateway = HostGroup.getGateway();//DataHandler.getHostByName("gateway");

    public static Host getRootHostMaster() {

        return masterRootHost;
    }
    
    public static OssHost getHostMaster() {

        return masterHost;
    }
   
    public static Host getHostNetsim() {

        return netsimHost.get(0);
    }

    public static Host getHostInfraServer() {

        return infraServerHost;
    }

    public static Host getGateway() {

        return gateway;
    }

    public static CexRemoteCommandExecutor getRemoteCommandExecutor(final Host host) {

        return new CexRemoteCommandExecutor(host);
    }
    
//    public static CexRemoteCommandExecutor getRootRemoteCommandExecutor(final Host host) {
//
//        return new CexRemoteCommandExecutor(host, true);
//    }

    public static RemoteObjectHandler getMasterHostFileHandler() {

        return getRemoteObjectHandler(masterHost);
    }

    public static RemoteObjectHandler getMasterRootFileHandler() {

        return getRemoteObjectHandler(masterHost);
    }

    public static RemoteObjectHandler getInfrServerFileHandler() {

        return getRemoteObjectHandler(infraServerHost);
    }

    public static RemoteObjectHandler getNetsimRemoteObjectHandler() {

        return getRemoteObjectHandler(getHostNetsim());
    }

    public static RemoteObjectHandler getRemoteObjectHandler(final Host host) {

        return new RemoteObjectHandler(host);
    }

    public static SshNetsimHandler getSshNetsimHandler() {

        return getSshNetsimHandler(getHostNetsim());
    }

    public static SshNetsimHandler getSshNetsimHandler(final Host netsimHost) {

        return new SshNetsimHandler(netsimHost);
    }

}
package com.ericsson.oss.cex.taf.ui.validators;

import java.io.IOException;

import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;

public interface OsgiClientOperator {
	
	ApiClient getClient();

	void startCex() throws ContainerNotReadyException, IOException;

	void close();

}

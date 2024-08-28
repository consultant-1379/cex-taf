package com.ericsson.oss.cex.taf.ui.getters;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.oss.cex.operator.CEXOperator;


/**
 * Wrapper on OSS CEXOpeartor.
 * @author xananan
 *
 */
public class CexOperators extends CEXOperator {

	
	private static ApiClient apiClient;

	public CexOperators(final Host rcHost) {
		super(rcHost);
	}

	/**
	 * Get OSGI Client
	 * 
	 * @return osgi client
	 */
	public static ApiClient getClient() {
		return apiClient;
	}

	/**
	 * set client to be used in GroovyTestOperators.
	 * @param client
	 */
	public void setClient(final ApiClient client) {
		apiClient = client;
	}
}

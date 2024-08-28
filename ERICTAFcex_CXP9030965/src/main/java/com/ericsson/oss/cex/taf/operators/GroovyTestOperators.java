package com.ericsson.oss.cex.taf.operators;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.oss.cex.taf.ui.getters.CexOperators;
import com.ericsson.oss.cex.taf.ui.getters.IGroovyTestOperators;

@Operator(context = Context.API)
public class GroovyTestOperators implements IGroovyTestOperators{

	private final Logger log = Logger.getLogger(GroovyTestOperators.class);

	private static ApiClient client = CexOperators.getClient();

	/**
	 * Generic method to invoke groovy method with arguments
	 * 
	 * @param className
	 *            the name of groovy class
	 * @param method
	 *            the name of groovy method
	 * @param args
	 *            the arguments of the method
	 * @return - a string that represents the response of the invocation
	 */
	@Override
	public String invokeGroovyMethodForList(final String className, final String method, final String... args) {

		String respVal = null;
		respVal = client.invoke(className, method, args).getValue();
		return respVal;
	}

	/**
	 * Generic method to invoke groovy method with arguments
	 * 
	 * @param className
	 *            the name of groovy class
	 * @param method
	 *            the name of groovy method
	 * @param args
	 *            the arguments of the method
	 * @return - a string that represents the response of the invocation
	 */
	@Override
	public String invokeGroovyMethodOnArgs(final String className, final String method, final String... args) {

		String respVal = null;
		respVal = client.invoke(className, method, args).getValue();
		log.info(String.format("Invoking %1$s: %2$s", method, respVal));
		return respVal;
	}

}

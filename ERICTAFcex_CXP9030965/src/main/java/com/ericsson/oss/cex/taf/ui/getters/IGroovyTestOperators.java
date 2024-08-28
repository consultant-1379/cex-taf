package com.ericsson.oss.cex.taf.ui.getters;

public interface IGroovyTestOperators {

	String invokeGroovyMethodOnArgs(final String className, final String method, final String... args);

	String invokeGroovyMethodForList(String className, String method,
			String[] args);

}

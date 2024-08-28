package com.ericsson.oss.cex.taf.operators;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.oss.cex.taf.operator.interfaces.ICDBOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler;
import com.ericsson.oss.taf.cshandler.model.Fdn;

@Operator(context = Context.API)
public class CDBOperator implements ICDBOperators {

	private CexCsHandler cexCsHandler;

	public CDBOperator(){
		cexCsHandler = new CexCsHandler();
	}
	private static Logger log = Logger.getLogger(CDBOperator.class);

	@Inject
	private GroovyTestOperators groovyOperator;


	public String CexMoBrowserRequestAction(){

		List<Fdn> myList = cexCsHandler.getListByType("IpOam");
		Fdn fdn = myList.get(myList.size()-1);

		if(fdn.equals("")){
			log.info("No FDN Found please check");
		}
		String result = groovyOperator.invokeGroovyMethodOnArgs("CexMoBrowserRequestAction", "requestAction", fdn.toString());

		return result;
	}

	

	
}


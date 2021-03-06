/**
 * Copyright (C) 2015 BITPlan GmbH
 *
 * Pater-Delp-Str. 1
 * D-47877 Willich-Schiefbahn
 *
 * http://www.bitplan.com
 * 
 * This source is part of
 * https://github.com/WolfgangFahl/Mediawiki-Japi
 * and the license for Mediawiki-Japi applies
 * 
 */
package com.bitplan.mediawiki.japi.jaxb;

import javax.xml.bind.JAXBException;

/**
 * generic conversion of <T> instance to and from xml/json
 * @author wf
 *
 * @param <T>
 */
public interface JaxbFactoryApi<T> {
  /**
   * create an xml representation for the given <T> instance
   * @param instance - the instance to convert to xml
   * @return a xml representation of the given <T> instance
   * @throws JAXBException
   */
  public String asXML(T instance) throws JAXBException;
  
	/**
	 * get a <T> instance for the given xml
	 * @param xml - the xml representation of a <T> instance
	 * @return a new <T> instance
	 * @throws JAXBException - if there is an issue with the xml
	 * @throws Exception 
	 */
	public T fromXML(final String xml) throws JAXBException, Exception;
	
	 /**
   * create a Json representation for the given <T> instance 
   * @param instance - the instance to convert to json
   * @return a Json representation of the given <T>
   * @throws JAXBException
   */
  public String asJson(T instance) throws JAXBException;
  
	/**
	 * get a <T> instance for the given json
	 * @param json - the json representation of a <T> instance
	 * @return a new <T> instance
	 * @throws Exception - if there is an issue with the json
	 */
	public T fromJson(final String json) throws Exception;
	
}

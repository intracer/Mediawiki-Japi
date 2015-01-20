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
package com.bitplan.mediawiki.guice;

import com.bitplan.mediawiki.japi.MediawikiApi;
import com.google.inject.AbstractModule;

/**
 * Guice Module to use the wrapper {@link org.wikipedia.Mediawiki} for the one class 
 * solution {@link org.wikipedia.Wiki}
 * as the implementation for the {@link com.bitplan.mediawiki.japi.MediawikiApi} interface 
 * @author wf
 *
 */
public class OrgWikiModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MediawikiApi.class).to(org.wikipedia.Mediawiki.class); 
  }

}

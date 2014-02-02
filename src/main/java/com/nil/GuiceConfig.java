package com.nil;


import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.guice.ShiroModule;
import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.text.IniRealm;

import java.util.Map;

public class GuiceConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {

        Injector injector = Guice.createInjector(
                new NilServletModule(),
                new ShiroGuiceModule(),
                new ShiroAopModule()
        );

        SecurityManager securityManager = injector.getInstance(SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

        return injector;
    }

    private static class NilServletModule extends JerseyServletModule {

        @Override
        protected void configureServlets() {
            filter("/*").through(GuiceContainer.class, getInitParams());
            // serve() blocks everything
            //serve("/*").with(GuiceContainer.class, getInitParams());
        }

        private Map<String, String> getInitParams() {

            Map<String, String> params = Maps.newHashMap();

            // "component scan"
            params.put("com.sun.jersey.config.property.packages","com.nil.web");
            // json producing
            params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
            // static resources
            params.put(ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX, "/assets/.*");
            // templates
            params.put("com.sun.jersey.config.property.JSPTemplatesBasePath", "/WEB-INF/jsp");

            return params;
        }
    }

    static class ShiroGuiceModule extends ShiroModule {

        protected void configureShiro() {
            try {
                bindRealm().toConstructor(IniRealm.class.getConstructor(Ini.class));
            } catch (NoSuchMethodException e) {
                addError(e);
            }
        }

        @Provides
        Ini loadShiroIni() {
            return Ini.fromResourcePath("classpath:shiro.ini");
        }
    }
}

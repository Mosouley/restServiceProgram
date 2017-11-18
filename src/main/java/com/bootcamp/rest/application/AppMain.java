
package com.bootcamp.rest.application;

import com.bootcamp.rest.controllers.ProgrammeRessource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import java.net.URISyntaxException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author soul
 */
public class AppMain {
    private static final int SERVER_PORT = 9090;
    public static void main(String[] args) {
         try {
            // Workaround for resources from JAR files
            Resource.setDefaultUseCaches( false );

            // Build the Swagger Bean.
            buildSwagger();

            // Holds handlers
            final HandlerList handlers = new HandlerList();

            // Handler for Swagger UI, static handler.
            handlers.addHandler( buildSwaggerUI() );

            // Handler for Entity Browser and Swagger
            handlers.addHandler( buildContext() );

            // Start server
            Server server = new Server( SERVER_PORT );
            server.setHandler( handlers );
            server.start();
            server.join();
        } catch ( Exception e ) {
             System.out.println( "There was an error starting up the WS Programme "+ e );
        }
    }

    private static void buildSwagger() {
  // This configures Swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setResourcePackage(ProgrammeRessource.class.getPackage().getName());        //"com.bootcamp.rest.controllers"
        beanConfig.setScan(true);
        beanConfig.setBasePath("/rest");
        beanConfig.setDescription("Programmes Rest API to access all the programmes ressources");
        beanConfig.setTitle("Programmes");
    }

    private static Handler buildSwaggerUI() throws URISyntaxException {
  //to configure swagger UI

         final ResourceHandler swaggerUIResourceHandler = new ResourceHandler();
        swaggerUIResourceHandler.setResourceBase( AppMain.class.getClassLoader().getResource("webapp" ).toURI().toString() );
        final ContextHandler swaggerUIContext = new ContextHandler();
        swaggerUIContext.setContextPath( "/docs/" );
        swaggerUIContext.setHandler( swaggerUIResourceHandler );

        return swaggerUIContext;
    }

    private static Handler buildContext() {
         ResourceConfig resourceConfig = new ResourceConfig();
        // io.swagger.jaxrs.listing loads up Swagger resources
        resourceConfig.packages( ProgrammeRessource.class.getPackage().getName(), ApiListingResource.class.getPackage().getName() );
        ServletContainer servletContainer = new ServletContainer( resourceConfig );
        ServletHolder holder = new ServletHolder( servletContainer );
        ServletContextHandler restContext = new ServletContextHandler( ServletContextHandler.SESSIONS );
        restContext.setContextPath( "/" );
        restContext.addServlet( holder, "/*" );

        return restContext;
    }
}

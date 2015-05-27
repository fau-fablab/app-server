package de.fau.cs.mad.fablab.rest.server;

import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import de.fau.cs.mad.fablab.rest.server.drupal.ICalClient;
import de.fau.cs.mad.fablab.rest.server.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.resources.NewsResource;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.core.*;
import de.fau.cs.mad.fablab.rest.server.health.DatabaseHealthCheck;
import de.fau.cs.mad.fablab.rest.server.health.HelloFablabHealthCheck;
import de.fau.cs.mad.fablab.rest.server.resources.*;
import de.fau.cs.mad.fablab.rest.server.security.AdminConstraintSecurityHandler;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

/**
 * The Core of our rest server
 */
class ServerApplication extends Application<ServerConfiguration> {

    /**
     * used to configure aspects of the application required before the application is run
     *
     * @param bootstrap
     */
    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {

        bootstrap.addBundle(hibernate);
        //enables the use of environment variables in yaml config file
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor()
        ));
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {

        // Create our basic health check
        final HelloFablabHealthCheck helloFablabHealthCheck =
                new HelloFablabHealthCheck(configuration.getTemplate());

        // add health check and resource to our jersey environment
        environment.healthChecks().register("Hello Fablab template", helloFablabHealthCheck);
        environment.healthChecks().register("DBHealthCheck", new DatabaseHealthCheck(hibernate));

        // configure OpenERP client
        OpenErpClient.setConfiguration(configuration.getOpenErpConfiguration());

        // configure ICalClient
        ICalClient.setConfiguration(configuration.getICalConfiguration());

        // create an instance of our HelloFablabResource
        final HelloFablabResource helloFablabResource = new HelloFablabResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(helloFablabResource);

        // create and register instance of SpaceApiResource
        SpaceApiConfiguration spaceApiConfiguration = configuration.getSpaceApiConfiguration();
        final SpaceAPIResource spaceAPIResource = new SpaceAPIResource(
                configuration.getSpaceApiConfiguration()
        );
        environment.jersey().register(spaceAPIResource);

        // create some resources
        environment.jersey().register(new NewsResource(new NewsFacade(new NewsDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new ICalResource(new ICalFacade(new ICalDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new ProductResource(new ProductFacade(new ProductDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new CartResource(new CartFacade(new CartDAO(hibernate.getSessionFactory()))));

        //set the security handler for admin resources
        environment.admin().setSecurityHandler(new AdminConstraintSecurityHandler(configuration.getAdminConfiguration()));

        // create dummy data
        dummyData.createDummyData(hibernate);
    }

    public static void main(String[] args) {
        try {
            new ServerApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final HibernateBundle<ServerConfiguration> hibernate = new HibernateBundle<ServerConfiguration>(News.class, ICal.class, Product.class, Cart.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ServerConfiguration configuration) {
            return configuration.getDatabase();
        }
    };
}


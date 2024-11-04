package app.config;

import app.exceptions.ApiException;
import app.controller.ExceptionController;
import app.exceptions.JPAException;
import app.routes.Routes;
import app.security.controller.AccessController;
import app.security.routes.SecurityRoutes;
import app.util.ApiProps;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AppConfig {
    private static Javalin app;
    private static Routes routes;
    private static AccessController accessController = new AccessController();
    private static final ExceptionController exceptionController = new ExceptionController();



    private static void configuration (JavalinConfig config){
        //Server
        config.router.contextPath = ApiProps.API_CONTEXT;

        //Server respond - Json
        config.http.defaultContentType = "application/json";


        //Plugin
        config.bundledPlugins.enableRouteOverview("/routes");
        config.bundledPlugins.enableDevLogging();

        //Routes
        config.router.apiBuilder(routes.getRoutes());

        //Security
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    //Exceptions
    private static void exceptionContext(Javalin app) {
        app.exception(ApiException.class, exceptionController::apiExceptionHandler);
        app.exception(JPAException.class, exceptionController::jpaExceptionHandler);

        app.exception(Exception.class, exceptionController::exceptionHandler);
    }

    //start server
    public static Javalin startServer(EntityManagerFactory emf) {
        routes = new Routes(emf);
        app = Javalin.create(AppConfig::configuration);
        app.beforeMatched(accessController::accessHandler);
        app.start(ApiProps.PORT);
        exceptionContext(app);
        return app;
    }
    //stop server
    public static void stopServer(Javalin app) {
        app.stop();
    }
}

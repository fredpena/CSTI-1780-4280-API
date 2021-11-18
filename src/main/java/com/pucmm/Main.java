package com.pucmm;

import com.pucmm.database.JdbcUtil;
import com.pucmm.user.UserController;
import io.javalin.Javalin;
import io.javalin.core.util.Header;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {


        JdbcUtil.initialize();

        Javalin.create(config -> {
            config.enableCorsForAllOrigins();
            config.enableDevLogging();
            config.registerPlugin(getConfiguredOpenApiPlugin());
            config.defaultContentType = "application/json";
        }).routes(() -> {
            path("users", () -> {
                get(UserController::getAll);
                post(UserController::create);
                put(UserController::update);
                path("{userId}", () -> {
                    get(UserController::getOne);
                    delete(UserController::delete);
                });
                path("/change", () -> {
                    put(UserController::changePassword);
                });
                path("/login", () -> {
                    post(UserController::login);
                });
            });
        }).start(7002);

        LOG.debug("Check out ReDoc docs at http://localhost:7002/redoc");
        LOG.debug("Check out Swagger UI docs at http://localhost:7002/swagger-ui");
    }

    private static OpenApiPlugin getConfiguredOpenApiPlugin() {
        Info info = new Info().version("1.0").title("User API").description("Demo API");
        OpenApiOptions options = new OpenApiOptions(info)
                .activateAnnotationScanningFor("com.pucmm")
                .path("/swagger-docs") // endpoint for OpenAPI json
                .swagger(new SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
                .reDoc(new ReDocOptions("/redoc")) // endpoint for redoc
                .defaultDocumentation(doc -> {
                    doc.json("500", ErrorResponse.class);
                    doc.json("503", ErrorResponse.class);
                });
        return new OpenApiPlugin(options);
    }
}

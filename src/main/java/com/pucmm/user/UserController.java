package com.pucmm.user;

import com.pucmm.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.plugin.openapi.annotations.*;
import io.swagger.util.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.concurrent.atomic.AtomicInteger;

// This is a controller, it should contain logic related to client/server IO
public class UserController {

    private static final Logger LOG = LogManager.getLogger(UserController.class.getName());

    private static User getByEmail(String email) throws SQLException {
        return UserService.getInstance().queryForEmail(email);
    }

    @OpenApi(
            summary = "Create user",
            operationId = "createUser",
            path = "/users",
            method = HttpMethod.POST,
            tags = {"User"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = User.class)}),
            responses = {
                    @OpenApiResponse(status = "201", content = {@OpenApiContent(from = User.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "503", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void create(Context ctx) {
        final User user = ctx.bodyAsClass(User.class);

        try {
            if (getByEmail(user.getEmail()) != null) {
                ctx.json(Json.pretty("This email is registered"));
                ctx.status(409);
            } else {
                user.setPassword(convertMD5(user.getPassword()));
                UserService.getInstance().create(user);
                ctx.json(user);
                ctx.status(201);
            }

        } catch (SQLException e) {
            LOG.error(e);
            ctx.json(Json.pretty(e.getLocalizedMessage()));
            ctx.status(500);
        }
    }

    @OpenApi(
            summary = "Get all users",
            operationId = "getAllUsers",
            path = "/users",
            method = HttpMethod.GET,
            tags = {"User"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = User[].class)}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "503", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAll(Context ctx) {
        try {
            ctx.json(UserService.getInstance().queryForAll());
        } catch (SQLException e) {
            LOG.error(e);
            ctx.status(500);
        }
    }

    @OpenApi(
            summary = "Get user by ID",
            operationId = "getUserById",
            path = "/users/:userId",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "userId", type = Integer.class, description = "The user ID")},
            tags = {"User"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = User.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "503", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getOne(Context ctx) {
        try {
            final User user = UserService.getInstance().queryForId(validPathParamUserId(ctx));
            if (user == null) {
                throw new NotFoundResponse("User not found");
            } else {
                ctx.json(user);
                ctx.status(200);
            }
        } catch (SQLException e) {
            LOG.error(e);
            ctx.status(500);
        }

    }

    @OpenApi(
            summary = "Update user",
            operationId = "updateUser",
            path = "/users",
            method = HttpMethod.PUT,
            tags = {"User"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = User.class)}),
            responses = {
                    @OpenApiResponse(status = "204", content = {@OpenApiContent(from = User.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "503", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void update(Context ctx) {
        final User user = ctx.bodyAsClass(User.class);

        try {
                final User exist = UserService.getInstance().queryForId(user.getUid());
                if (exist == null) {
                    throw new NotFoundResponse("Not found!");

                } else {
                    user.setPassword(exist.getPassword());
                    UserService.getInstance().update(user);
                    ctx.json(user);
                    ctx.status(204);
                }

        } catch (SQLException e) {
            AtomicInteger errorCode = new AtomicInteger(500);
            e.iterator().forEachRemaining(err -> {
                if(err instanceof SQLIntegrityConstraintViolationException){
                    ctx.json(Json.pretty("This email is registered"));
                    errorCode.set(409);
                }
            });
            LOG.error(e);
            ctx.status(errorCode.get());
        }
    }

    @OpenApi(
            summary = "Change Password",
            operationId = "changePassword",
            path = "/users/change",
            method = HttpMethod.PUT,
            tags = {"User"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = AuthUser.class)}),
            responses = {
                    @OpenApiResponse(status = "204"),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "503", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void changePassword(Context ctx) {
        final AuthUser auth = ctx.bodyAsClass(AuthUser.class);

        try {
            final User user = UserService.getInstance().queryForEmail(auth.email);
            if (user == null) {
                throw new NotFoundResponse("Not found!");
            } else {
                user.setPassword(convertMD5(auth.password));
                UserService.getInstance().update(user);
                ctx.status(204);
            }
        } catch (SQLException e) {
            LOG.error(e);
            ctx.status(500);
        }
    }

    @OpenApi(
            summary = "Login",
            operationId = "login",
            path = "/users/login",
            method = HttpMethod.POST,
            tags = {"User"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = AuthUser.class)}),
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = User.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "503", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void login(Context ctx) {
        final AuthUser auth = ctx.bodyAsClass(AuthUser.class);

        try {
            final User user = UserService.getInstance().login(auth.email, convertMD5(auth.password));
            if (user == null) {
                throw new NotFoundResponse("Not found!");
            } else {
                ctx.json(user);
                ctx.status(200);
            }
        } catch (SQLException e) {
            LOG.error(e);
            ctx.status(500);
        }
    }

    @OpenApi(
            summary = "Delete user by ID",
            operationId = "deleteUserById",
            path = "/users/:userId",
            method = HttpMethod.DELETE,
            pathParams = {@OpenApiParam(name = "userId", type = Integer.class, description = "The user ID")},
            tags = {"User"},
            responses = {
                    @OpenApiResponse(status = "204"),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "503", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void delete(Context ctx) {
        try {
            final User user = UserService.getInstance().queryForId(validPathParamUserId(ctx));
            if (user == null) {
                throw new NotFoundResponse("User not found");
            } else {
                UserService.getInstance().delete(user);
                ctx.status(204);
            }
        } catch (SQLException e) {
            LOG.error(e);
            ctx.status(500);
        }
    }

    // Prevent duplicate validation of userId
    private static int validPathParamUserId(Context ctx) {
        return ctx.pathParamAsClass("userId", Integer.class).check(id -> id > 0, "ID must be greater than 0").get();
    }

    public static String convertMD5(String s) {

        String md5 = null;
        try {
            if (s != null) {
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(s.getBytes());
                byte bytes[] = algorithm.digest();
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < bytes.length; i++) {
                    String hex = Integer.toHexString(0xff & bytes[i]);
                    if (hex.length() == 1) {
                        sb.append('0');
                    }
                    sb.append(hex);
                }
                md5 = sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
}

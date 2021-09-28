package net.ausiasmarch.miriase;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class session extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, x-requested-with, Content-Type");

        Gson oGson = new Gson();

        try ( PrintWriter out = response.getWriter()) {
            String op = request.getParameter("op");
            if (op != null) {
                HttpSession oSession = request.getSession();
                switch (op) {
                    case "login":
                        String login = request.getParameter("login");
                        String password = request.getParameter("password");
                        if (login != null && password != null) {
                            if (login.equalsIgnoreCase("admin") && password.equalsIgnoreCase("8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918")) { //admin
                                oSession.setAttribute("usuario", login);
                                response.setStatus(HttpServletResponse.SC_OK);
                                out.print(oGson.toJson("Welcome"));
                            } else {
                                if (login.equalsIgnoreCase("user") && password.equalsIgnoreCase("04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb")) { //user
                                    response.setStatus(HttpServletResponse.SC_OK);
                                    out.print(oGson.toJson("Welcome"));
                                } else {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    out.print(oGson.toJson("Auth Error"));
                                }
                            }
                        } else {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            out.print(oGson.toJson("Auth Error"));
                        }
                        break;
                    case "logout":
                        oSession.invalidate();
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.print(oGson.toJson("Session closed"));
                        break;
                    case "check":
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.print(oGson.toJson((String) oSession.getAttribute("usuario")));
                        break;
                    case "get":
                        String name = (String) oSession.getAttribute("usuario");
                        if (name != null) {
                            if (name == "admin") {
                                response.setStatus(HttpServletResponse.SC_OK);
                                out.print(oGson.toJson("QWERTY"));
                            } else {
                                response.setStatus(HttpServletResponse.SC_OK);
                                out.print(oGson.toJson("ASDFG"));
                            }
                        } else {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            out.print(oGson.toJson("Unauthorized"));
                        }
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        out.print(oGson.toJson("Method Not Allowed"));
                        break;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                out.print(oGson.toJson("Method Not Allowed"));
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }




}

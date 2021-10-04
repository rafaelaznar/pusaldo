package net.ausiasmarch.pusaldo;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class session extends HttpServlet {

    private void doCORS(HttpServletRequest oRequest, HttpServletResponse oResponse) {   
        System.out.println("Request: "+oRequest.getRequestURI());
        System.out.println("FROM IP: "+oRequest.getHeader("x-forwarded-for"));
        oResponse.setContentType("application/json;charset=UTF-8");        
        if (!(oRequest.getMethod().equalsIgnoreCase("OPTIONS"))) {        
            oResponse.setHeader("Cache-control", "no-cache, no-store");
            oResponse.setHeader("Pragma", "no-cache");
            oResponse.setHeader("Expires", "-1");        
            oResponse.setHeader("Access-Control-Allow-Origin", oRequest.getHeader("origin"));
            oResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH");
            oResponse.setHeader("Access-Control-Max-Age", "86400");
            oResponse.setHeader("Access-Control-Allow-Credentials", "true");
            oResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            oResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");            
            oResponse.setHeader("Access-Control-Allow-Headers", "Origin");            
            oResponse.setHeader("Access-Control-Allow-Headers", "Accept"); 
            oResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            oResponse.addHeader("Access-Control-Expose-Headers", "responseType");
            oResponse.addHeader("Access-Control-Expose-Headers", "observe");
         } else {
            //https://stackoverflow.com/questions/56479150/access-blocked-by-cors-policy-response-to-preflight-request-doesnt-pass-access
            System.out.println("Pre-flight");
            oResponse.setHeader("Access-Control-Allow-Origin", oRequest.getHeader("origin"));
            oResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH");
            oResponse.setHeader("Access-Control-Max-Age", "3600");
            oResponse.setHeader("Access-Control-Allow-Credentials", "true");
            oResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Expose-Headers");
            oResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Request-Headers");
            oResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Request-Method");            
            oResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            oResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");
            oResponse.setHeader("Access-Control-Allow-Headers", "Origin");            
            oResponse.setHeader("Access-Control-Allow-Headers", "Accept"); 
            oResponse.setHeader("Access-Control-Allow-Headers", "Authorization");                       
            oResponse.setHeader("Access-Control-Allow-Headers", "ResponseType");
            oResponse.setHeader("Access-Control-Allow-Headers", "Observe");
            oResponse.setStatus(HttpServletResponse.SC_OK);
        }
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doCORS(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         System.out.println("goGET Request: "+request.getRequestURI());
        doCORS(request,response);
        Gson oGson = new Gson();
        try ( PrintWriter out = response.getWriter()) {
            String op = request.getParameter("op");
            if (op != null) {
                HttpSession oSession = request.getSession();
                switch (op) {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doCORS(request,response);
        Gson oGson = new Gson();
        try ( PrintWriter out = response.getWriter()) {
            HttpSession oSession = request.getSession();
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            if (login != null && password != null) {
                if (login.equalsIgnoreCase("admin") && password.equalsIgnoreCase("8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918")) { //admin
                    oSession.setAttribute("usuario", login);
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(oGson.toJson("Welcome"));
                } else {
                    if (login.equalsIgnoreCase("user") && password.equalsIgnoreCase("04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb")) { //user
                        oSession.setAttribute("usuario", login);                        
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
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doCORS(request,response);
        Gson oGson = new Gson();
        try ( PrintWriter out = response.getWriter()) {
            HttpSession oSession = request.getSession();
            oSession.invalidate();
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(oGson.toJson("Session closed"));
        }
    }

}

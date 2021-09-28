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

        try ( PrintWriter out = response.getWriter()) {
            String op = request.getParameter("op");            
            HttpSession oSession = request.getSession();
            Gson oGson = new Gson();
            switch (op) {
                case "login":
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
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
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

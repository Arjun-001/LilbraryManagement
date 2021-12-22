/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Servlet.Test;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arjun
 */
@WebServlet(name = "signup", urlPatterns = {"/signup"})
public class signup extends HttpServlet {

    private Connection conUsers;
    private Map users;
    private Statement stmtUsers;
    private ResultSet rsUsers;
    private PrintWriter out;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        /* TODO output your page here. You may use following sample code. */
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        String user = "";
        String password = "";
        String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
        Class.forName(driver);
        users = new HashMap();
        conUsers = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);

        stmtUsers = conUsers.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rsUsers = stmtUsers.executeQuery("Select * from Users");
        while (rsUsers.next()) {
            users.put(rsUsers.getString(1), rsUsers.getString(2));
        }
        out.println("<title>Servlet signup</title>");
        
        out.println(
                "<link rel='stylesheet' href='" + request.getContextPath() + "/styles.css' TYPE=\"text/css\">"
                + "<meta charset=\"UTF-8\">\n"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("</head>");
        out.println("<body background = 'abc.jpg' align = 'Center'>");
        out.println("<h1>Servlet signup at " + request.getContextPath() + "</h1>");
        if (request.getParameter("Sign up") != "" && request.getParameter("Sign up") != null) {
            if (users.containsKey(request.getParameter("Username")) != true) {
                PreparedStatement pStmt = conUsers.prepareStatement("INSERT INTO Users (Username, Password, Address, Email, Phone) VALUES (?,?,?,?,?)");
                pStmt.setString(1, request.getParameter("Username"));
                pStmt.setString(2, request.getParameter("Password"));
                pStmt.setString(3, request.getParameter("Address"));
                pStmt.setString(4, request.getParameter("Email"));
                pStmt.setString(5, request.getParameter("Phone"));
                pStmt.executeUpdate();
                out.println("<h1 align = 'center'User is added"
                        + "<br><a href='index.html'>Click here to go to login page</a></h1>");
            } else {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Please enter a different username!');");
                out.println("</script>");
                create();
            }
        } else {
            create();
        }
        out.println("</body>");
        out.println("</html>");

    }

    private void create() {

        out.println(" <form action=\"\" method=\"post\"><strong><em>"
                + "<div class='box'>Username: <input type=\"text\" name=\"Username\" required value=\"Arjun\"/><br><br>"
                + "Password: <input type=\"password\" name=\"Password\" required value=\"Password\"/><br><br>"
                + "Address: <input type=\"text\" name=\"Address\" required value=\"456 def road\"/><br><br>"
                + "Phone number: <input type=\"tel\" name=\"Phone\" required value=\"6477795405\"/><br><br>"
                + "Email : <input type=\"email\" name=\"Email\" required value=\"arjun@arjun.com\"/><br><br>"
                + "<input type=\"submit\" value=\"Sign up\" name= \"Sign up\" class='button'/>"
                + "</strong></em></div></form>");
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(signup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(signup.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(signup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(signup.class.getName()).log(Level.SEVERE, null, ex);
        }
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

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author arjun
 */
@WebServlet(name = "Users", urlPatterns = {"/Users"})
public class Users extends HttpServlet {

    PrintWriter out;
    String username;
    Map users;
    Connection conUsers, conBooks;
    ResultSet rsUsers, rsBooks;
    Statement stmtUsers, stmtBooks;
    HttpSession session;

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
            throws ServletException, IOException, SQLException, ParseException {
        username = request.getParameter("Username");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        /* TODO output your page here. You may use following sample code. */
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Users</title>");
        out.println(
                "<link rel='stylesheet' href='" + request.getContextPath() + "/styles.css' TYPE=\"text/css\">"
                + "<meta charset=\"UTF-8\">\n"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("</head>");
        out.println("<body background = 'abc.jpg'>");
        out.println("<h1>Servlet Users at " + request.getContextPath() + "</h1>");
        session = request.getSession(false);

        session.setAttribute("type", "Users");
        if (session != null) {
            session.setAttribute("type", "Users");
            session.setAttribute("name", session.getAttribute("Username"));
            out.println("<h1 align = 'center'>Welcome, " + session.getAttribute("Username") + "</h1>");
            selection(request, response);
            out.println("</body>");
            out.println("</html>");
        } else {

            out.println("<h1 align = 'center'>Please log in.</h1>"
                    + "<br><br><h2 align ='center'>"
                    + "<a href ='index.html'>click here to go to log in page</a></h2> ");
        }

    }

    void selection(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ParseException, ServletException {

        String value = request.getParameter("value");
        if (value != null) {

            if (value.equalsIgnoreCase("All")) {
                response.sendRedirect("AllBooks");
            } else if (value.equalsIgnoreCase("All avialable Books")) {
                response.sendRedirect("AvailableBooks");
            } else if (value.equalsIgnoreCase("BookTitle")) {
                response.sendRedirect("BookSearch");
            } else if (value.equalsIgnoreCase("BookCode")) {
                response.sendRedirect("BookCode");
            } else if (value.equalsIgnoreCase("BookGenre")) {
                response.sendRedirect("BookGenre");
            } else if (value.equalsIgnoreCase("Logout")) {
                response.sendRedirect("Logout");
            } else if (value.equalsIgnoreCase("Homepage")) {
                response.sendRedirect("Users");
            } else if (value.equalsIgnoreCase("See your info")) {
//                     String genre = request.getParameter("fieldname");
                out.println("<form action='' method = 'post' align = 'right' >"
                        + "<input type = 'submit' value='Logout' class='logout' >"
                        + "<input type = 'submit' value='Homepage' class='logout' ></form>");
                PreparedStatement pstmtUsers = conUsers.prepareStatement("Select * from Users where Username = ?");
                pstmtUsers.setString(1, (String) session.getAttribute("Username"));
                rsUsers = pstmtUsers.executeQuery();
                out.println("<form action='' method = 'post'><table style='width:100%'>"
                        + "<tr><th>Username</th>"
                        + "<th>Address</th>"
                        + "<th>Phone</th>"
                        + "<th>Email</th>"
                        + "<th>Issued Book</th>"
                        + "<th>Book issue date</th>"
                        + "<th>Book due date</th>"
                        + "<th>Fine</th>");
                while (rsUsers.next()) {
                    out.println("<tr>");

                    out.println("<td>" + rsUsers.getString(1) + "</td><td>" + rsUsers.getString(3) + "</td><td>"
                            + rsUsers.getString(4) + "</td><td>" + rsUsers.getString(5) + "</td><td>" + rsUsers.getString(6) + "</td><td>"
                            + rsUsers.getString(7) + "</td><td>" + rsUsers.getString(8) + "</td><td>$" + rsUsers.getString(9) + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else if (value.equalsIgnoreCase("Logout")) {
                response.sendRedirect("Logout");
            }
        } else {
//                out.println("Search :");
            out.println("Search criteria: ");
            out.println("<form action='' method = 'post'>"
                    + "<input type = 'submit' value='All avialable Books' class='button' name='value'>"
                    + "<input type = 'submit' value='BookTitle' name='value' class='button'>"
                    + "<input type = 'submit' value='BookGenre' name='value' class='button'>"
                    + "<input type = 'submit' value='BookCode' name='value' class='button'>"
                    + "<input type = 'submit' value='See your info' name='value' class='button'>"
                    + "<input type = 'submit' value='Logout' name='value' class='button'> ");
        }
    }

    public void init() {
        try {
            String user = "";
            String password = "";
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);

            conUsers = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);
            stmtUsers = conUsers.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            conBooks = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);
            stmtBooks = conBooks.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {

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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
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

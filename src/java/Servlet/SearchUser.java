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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author arjun
 */
@WebServlet(name = "SearchUser", urlPatterns = {"/SearchUser"})
public class SearchUser extends HttpServlet {

    private ResultSet rsUsers;
    private Connection conUsers;
    private Statement stmtUsers;
    PrintWriter out;
    String search;
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
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        /* TODO output your page here. You may use following sample code. */
        rsUsers = stmtUsers.executeQuery("Select * from Users");
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
        session = request.getSession(false);
        if (session != null) {
            
             out.println("<h1 align = 'center'>Welcome, " + session.getAttribute("Username") + "</h1>");
            out.println("<form action='Logout'  method = 'post' align = 'right'>"
                    + "<input type = 'submit' value='Logout' class='logout'></form>");
            
            String types = (String) session.getAttribute("type");
                out.println("<form action='"+types+"' method = 'post' align = 'right'>"
                        + "<input type = 'submit' value='Homepage' class='logout'></form>");
            search = request.getParameter("SearchType");
            if (search == null || search.equals("")) {
                form();
            } else {
                PreparedStatement pstmtUsers = conUsers.prepareStatement("Select"
                        + "* from Users where " + search + " = ?");
                String input = request.getParameter("input");
                pstmtUsers.setString(1, input);
                rsUsers = pstmtUsers.executeQuery();

                out.println("<form action='Action' method = 'post'><table style='width:100%'>"
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

                    out.println("<td><input type='radio' name='user' checked value=" + rsUsers.getString(1)
                            + ">" + rsUsers.getString(1) + "</td><td>" + rsUsers.getString(3) + "</td><td>"
                            + rsUsers.getString(4) + "</td><td>" + rsUsers.getString(5) + "</td><td>" + rsUsers.getString(6) + "</td><td>"
                            + rsUsers.getString(7) + "</td><td>" + rsUsers.getString(8) + "</td><td>$" + rsUsers.getString(9) + "</td>");
                    out.println("</tr>");
                }
                out.println("<h1>Select action: ");

                out.println("<select name=\"ActionType\" required>\n"
                        + "<option value=\"DeleteUser\">DeleteUser</option>\n"
                        + "<option value=\"IssueBook\">IssueBookByBookCode</option>\n"
                        + "<option value=\"ReturnBook\">ReturnBook</option>"
                        + "<option value=\"IssueFine\">IssueFine</option>"
                        + "<option value=\"CollectFine\">CollectFine</option>");
                out.println("<br>Input value: <input type='text' name='input' required>");
                out.println("<br><br><input type='submit' value='Action' name='Action' class='button'></h1></form>");
            }
        } else {
            out.println("<h1 align = 'center'>Please log in.</h1>"
                    + "<br><br><h2 align ='center'>"
                    + "<a href ='index.html'>click here to go to log in page</a></h2> ");
        }
        out.println("</body>");
        out.println("</html>");
    }

    void form() {
        out.println("<form action ='' method = 'post' align = 'center'>"
                + "<select name=\"SearchType\" required>\n"
                + "<option value=\"Username\">Username</option>\n"
                + "<option value=\"Address\">Address</option>\n"
                + "<option value=\"Phone\">Phone</option>"
                + "<option value=\"Email\">Email</option>"
                + "<option value=\"IssuedBook\">IssuedBook</option>");
        out.println("Input value: <input type='text' name='input' required>"
                + "<input type='submit' value='Search' name='find'></form>");
    }

    void table() throws SQLException {

        out.println("<h1 align = 'center'>Welcome, " + session.getAttribute("Username") + "</h1>");
        out.println("<form action='Logout' method = 'post' align = 'right'>"
                + "<input type = 'submit' value='Logout' class='logout'></form>");
        String type = (String) session.getAttribute("type");
        out.println("<form action='" + type + "' method = 'post'  align = 'right'>"
                + "<input type = 'submit' value='Homepage' class='logout'></form>");
        PreparedStatement pstmtUsers = conUsers.prepareStatement("Select * from Users where ");
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
            Logger.getLogger(AllBooks.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AllBooks.class.getName()).log(Level.SEVERE, null, ex);
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

    public void init() {
        try {
            String user = "";
            String password = "";
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);

            conUsers = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);
            stmtUsers = conUsers.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {

        }
    }
}

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
@WebServlet(name = "BookGenre", urlPatterns = {"/BookGenre"})
public class BookGenre extends HttpServlet {

    Connection conBooks;
    ResultSet rsBooks;
    Statement stmtBooks;

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

 /* TODO output your page here. You may use following sample code. */
//            rsBooks = stmtBooks.executeQuery("Select * from Books");
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
            HttpSession session = request.getSession(false);
            if (session != null) {

                if (request.getParameter("Issue Book") != null) {

                    session.setAttribute("book", request.getParameter("book"));
                    response.sendRedirect("IssueBook");
                }

                out.println("<h1 align = 'center'>Welcome, " + session.getAttribute("Username") + "</h1>");
                out.println("<form action='Logout' method = 'post' align = 'right'>"
                        + "<input type = 'submit' value='Logout' class='logout'></form>");

                String types = (String) session.getAttribute("type");
                out.println("<form action='" + types + "' method = 'post'  align = 'right'>"
                        + "<input type = 'submit' value='Homepage' class='logout'></form>");

                if (request.getParameter("type") != null && request.getParameter("type") != "") {
                    String type = request.getParameter("type");

                    if (request.getParameter("fieldname") != null && request.getParameter("fieldname") != "") {
                        String set = request.getParameter("fieldname");
                        PreparedStatement pstmtBooks = conBooks.prepareStatement("Select * from Books where BookGenre = ?");
                        pstmtBooks.setString(1, set);
//                    pstmtBooks.setString(2, set);
                        rsBooks = pstmtBooks.executeQuery();
                    out.println("<form action='' method = 'post'><table style='width:100%'>"
                            + "<tr><th>BookCode</th>"
                            + "<th>BookTitle</th>"
                            + "<th>BookPrice</th>"
                            + "<th>Book Availability</th>"
                            + "<th>BookGenre</th>"
                            + "<th>BookIssuedDate</th>"
                            + "<th>BookExpectedReturn</th>");
                    while (rsBooks.next()) {
                        out.println("<tr>");

                        out.println("<td><input type='radio' name='book' checked value=" + rsBooks.getString(1) + ">" + rsBooks.getString(1) + "</td><td>" + rsBooks.getString(2) + "</td><td>$"
                                + rsBooks.getString(3) + "</td><td>" + rsBooks.getString(4) + "</td><td>" + rsBooks.getString(5) + "</td>"
                                + "</td><td>" + rsBooks.getString(6) + "</td>" + "</td><td>" + rsBooks.getString(7) + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                    if (types.equalsIgnoreCase("Users")) {
                        out.println("<br><br><input type='submit' value='Issue Book' name='Issue Book' class='button'></form>");
                    } else {
                        out.println("<br><br><input type='submit' value='Remove Book' name='Remove' class='button'></form>");
                    }
                    }
                } else if (request.getParameter("Remove") != null && request.getParameter("Remove") != "") {

                    String book = request.getParameter("book");
                    PreparedStatement pstmtBooks = conBooks.prepareStatement("Select * from Books where BookCode=?");
                    pstmtBooks.setString(1, book);
                    rsBooks = pstmtBooks.executeQuery();
                    String a = "";
                    while (rsBooks.next()) {
                        a = rsBooks.getString(6);
                    }
                    if (a != null && a != "") {
                        out.println("<script type=\"text/javascript\">");
                        out.println("alert('Book is issued and can not be removed');");
                        out.println("</script>");
                        response.setHeader("Refresh", "1;url=" + types);
                    } else {
                        out.println(book);
                        pstmtBooks = conBooks.prepareStatement("Delete from Books where BookCode=?");
                        pstmtBooks.setString(1, book);
                        pstmtBooks.executeUpdate();
                        out.println("<h1>Book removed</h1>");
                    }
                } else {
                    String value = request.getParameter("value");
                    out.println("<form action ='' method = 'post' class= 'search'>"
                            + "Please enter the value: <input type='hidden' value='BookGenre' name='type'>");

                    out.println("<br><input type='text' required name='fieldname' placeholder='BookGenre'><br><br>");
                    out.println("<input type='submit' value='Search' class='button'></form>");
                }
            } else {

                out.println("<h1 align = 'center'>Please log in.</h1>"
                        + "<br><br><h2 align ='center'>"
                        + "<a href ='index.html'>click here to go to log in page</a></h2> ");
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    public void init() {
        try {
            String user = "";
            String password = "";
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);

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
            Logger.getLogger(BookGenre.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BookGenre.class.getName()).log(Level.SEVERE, null, ex);
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

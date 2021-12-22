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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
@WebServlet(name = "IssueBook", urlPatterns = {"/IssueBook"})
public class IssueBook extends HttpServlet {

    Connection conUsers, conBooks;
    ResultSet rsUsers, rsBooks;
    Statement stmtUsers, stmtBooks;

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

                out.println("<h1 align = 'center'>Welcome, " + session.getAttribute("Username") + "</h1>");
                out.println("<form action='Logout' method = 'post'  align = 'right'>"
                        + "<input type = 'submit' value='Logout' class='logout'></form>");

                String types = (String) session.getAttribute("type");
                out.println("<form action='" + types + "' method = 'post'  align = 'right'>"
                        + "<input type = 'submit' value='Homepage' class='logout'></form>");

                PreparedStatement pstmtusers = conUsers.prepareStatement("Select * from Users where Username = ?");
                pstmtusers.setString(1, (String) session.getAttribute("name"));
                out.println(session.getAttribute("name"));
                rsUsers = pstmtusers.executeQuery();
                String issue = "";
                while (rsUsers.next()) {
                    issue = rsUsers.getString(6);
                }
                out.println(issue);
                if (issue == null || issue == "") {
                    String radio = (String) session.getAttribute("book");
                    PreparedStatement pstmtBooks = conBooks.prepareStatement("Update "
                            + "Books set Availability= ?, BookIssuedDate=?, BookExpectedReturn=? where BookCode = ?");
                    pstmtBooks.setBoolean(1, false);

                    Calendar date2 = Calendar.getInstance();

                    SimpleDateFormat format11 = new SimpleDateFormat("dd-MM-yyyy");
                    String formatted1 = format11.format(date2.getTime());

                    pstmtBooks.setString(2, formatted1);

                    date2.add(Calendar.DATE, +7);
                    formatted1 = format11.format(date2.getTime());
                    pstmtBooks.setString(3, formatted1);
                    pstmtBooks.setString(4, radio);
                    pstmtBooks.executeUpdate();
                    pstmtBooks = conBooks.prepareStatement("Select * from Books where BookCode = ?");
                    pstmtBooks.setString(1, radio);
                    rsBooks = pstmtBooks.executeQuery();
                    String booktitle = "";
                    while (rsBooks.next()) {
                        booktitle = rsBooks.getString(2);
                    }

                    Calendar date = Calendar.getInstance();

                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                    String formatted = format1.format(date.getTime());
//            out.println(formatted);
                    PreparedStatement pstmtUsers = conUsers.prepareStatement("Update Users set IssuedBook = ? , BookIssuedDate = ? , BookDueDate=? where Username = ?");
                    pstmtUsers.setString(1, booktitle);
                    pstmtUsers.setString(2, formatted);

                    date.add(Calendar.DATE, +7);
                    formatted = format1.format(date.getTime());
//            out.println("<br><br>"+formatted);
                    pstmtUsers.setString(3, formatted);
                    pstmtUsers.setString(4, (String) session.getAttribute("name"));
                    pstmtUsers.executeUpdate();
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Please collect book from library');");
                    out.println("</script>");
                    response.setHeader("Refresh", "1;url="+types);
                    out.println("<h1 align = 'center'>Welcome, " + session.getAttribute("Username") + "<br>"
                            + "Sending you back to your home page</h1>");
                } else {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Please return issued book before issuing new book');");
                    out.println("</script>");
                    response.setHeader("Refresh", "1;url="+types);
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
            Logger.getLogger(IssueBook.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(IssueBook.class.getName()).log(Level.SEVERE, null, ex);
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

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

/**
 *
 * @author arjun
 */
@WebServlet(name = "AddBook", urlPatterns = {"/AddBook"})
public class AddBook extends HttpServlet {

    Connection conBooks;
    ResultSet rsBooks;
    Statement stmtBooks;
    Map Books;
    PrintWriter out;
    PreparedStatement pstmtBooks;
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
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet AddBook</title>");
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
            out.println("<form action='" + types + "' method = 'post'"
                    + ""
                    + " align = 'right'>"
                    + "<input type = 'submit' value='Homepage' class='logout'></form>");
            session.setAttribute("type", "Librarians");
            if (request.getParameter("AddBook") != null && request.getParameter("AddBook") != "") {
                if (Books.containsKey(request.getParameter("BookCode")) != true){
                pstmtBooks = conBooks.prepareStatement("Insert into Books (BookCode ,BookTitle,"
                        + "BookPrice , BookGenre) VALUES (?,?,?,?)");
                pstmtBooks.setString(1, request.getParameter("BookCode"));
                pstmtBooks.setString(2, request.getParameter("BookTitle"));
                pstmtBooks.setString(3, request.getParameter("BookPrice"));
                pstmtBooks.setString(4, request.getParameter("BookGenre"));
                if (isNumber(request.getParameter("BookPrice"))) {
                    pstmtBooks.executeUpdate();
                    out.println("Records update");
                } else {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Please enter numeric value for price');");
                    out.println("</script>");
                    response.setHeader("Refresh", "1;url=SearchUser");
                }
            } else{
                    
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Please enter a different BookCode!');");
                out.println("</script>");
                create();
                }
            }
            else {
                create();
            }
            out.println("</body>");
            out.println("</html>");
        } else {

            out.println("<h1 align = 'center'>Please log in.</h1>"
                    + "<br><br><h2 align ='center'>"
                    + "<a href ='index.html'>click here to go to log in page</a></h2> ");
        }
        out.println("</html>");

    }

    private static boolean isNumber(String str) {
//        method to check if the text is number or not
//        returns true if the text is valid number and vice-versa
        try {
            double v = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    public void init() {
        try {
            String user = "";
            String password = "";
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
            Books = new HashMap();

            conBooks = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);
            stmtBooks = conBooks.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsBooks = stmtBooks.executeQuery("Select * from Books");
            while (rsBooks.next()) {
                Books.put(rsBooks.getString(1), rsBooks.getString(2));
            }
        } catch (Exception e) {

        }
    }

    void create() {

        out.println(" <form action=\"\" method = 'post' align = 'center'><strong><em>"
                + "<div class='box'>BookCode: <input type=\"text\" name=\"BookCode\" required ><br><br>"
                + "BookTitle: <input type=\"text\" name=\"BookTitle\" required ><br><br>"
                + "BookPrice: <input type=\"text\" name=\"BookPrice\" required ><br><br>"
                + "BookGenre: <input type=\"tel\" name=\"BookGenre\" required ><br><br>"
                + "<input type=\"submit\" value=\"Add Book\" name= \"AddBook\" class='button'/>"
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
            Logger.getLogger(AddBook.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddBook.class.getName()).log(Level.SEVERE, null, ex);
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

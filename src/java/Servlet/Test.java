package Servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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
@WebServlet(urlPatterns = {"/Test"})
public class Test extends HttpServlet {

    private Connection conUsers, conBooks, conLibrarians;
    Map users, librarians;
//    Connection object
    private Statement stmtUsers, stmtBooks, stmtLibrarians;
//    Statement object
    private ResultSet rsUsers, rsBooks, rsLibrarians;
//    ResultSet object
    private String s;
    private PrintWriter out;
    private String logintype, username, password;
//    private String s

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
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        /* TODO output Your page here. You may use following sample code. */
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Test</title>");
        out.println("</head>");
        out.println("<body background='abc.jpg' align = 'center'>");
        out.println("<h1>Servlet Test at " + request.getContextPath() + "</h1>");
        logintype = request.getParameter("logintype");
        username = request.getParameter("Username");
        password = request.getParameter("Password");
        init();
        check(request, response);
        out.println("</body>");

        out.println("<h1>The login information was invalid.</h1>");
        out.println("<h1 align = 'center'><a href = 'index.html'> Go to login page</a></h1>");
//            response.sendRedirect("./index.html?aa=aa");
        out.println("</html>");
//        method to create connection to the database     

    }

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Override
    public void init() {
        try {
            String user = "";
            String password = "";
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
//            admins = new HashMap();
            users = new HashMap();
            librarians = new HashMap();
            conUsers = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);
            stmtUsers = conUsers.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsUsers = stmtUsers.executeQuery("Select * from Users");

            conBooks = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);
            stmtBooks = conBooks.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsBooks = stmtBooks.executeQuery("Select * from Books ORDER BY BookCode ASC");

            conLibrarians = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);
            stmtLibrarians = conLibrarians.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsLibrarians = stmtLibrarians.executeQuery("Select * from Librarian");

            while (rsBooks.next()) {
            }
            while (rsUsers.next()) {
                users.put(rsUsers.getString(1), rsUsers.getString(2));
            }
            while (rsLibrarians.next()) {
                librarians.put(rsLibrarians.getString(1), rsLibrarians.getString(2));
            }
        } catch (Exception e) {

        }
    }

    void check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (logintype.equalsIgnoreCase("User")) {
            if (users.containsKey(username)) {
                if (users.containsValue(password)) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("Username", username);
                    response.sendRedirect("Users");
                }
            }

        } else if (librarians.containsKey(username)) {
            if (librarians.containsValue(password)) {

                HttpSession session = request.getSession(true);
                session.setAttribute("Username", username);
                response.sendRedirect("Librarians");
            }
        } else {
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
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

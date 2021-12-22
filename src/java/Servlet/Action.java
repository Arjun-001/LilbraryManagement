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
@WebServlet(name = "Action", urlPatterns = {"/Action"})
public class Action extends HttpServlet {

    private ResultSet rsUsers, rsBooks;
    private Connection conUsers, conBooks;
    private Statement stmtUsers, stmtBooks;
    PrintWriter out;
    String search;
    HttpSession session;
    PreparedStatement pstmtUsers, pstmtBooks;

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
            out.println("<form action='Logout'  align = 'right'>"
                    + "<input type = 'submit' value='Logout' class='logout'></form>");
            
            String types = (String) session.getAttribute("type");
                out.println("<form action='"+types+"' method='post' align = 'right'>"
                        + "<input type = 'submit' value='Homepage' class='logout'></form>");
            String actiontype = request.getParameter("ActionType");
            String input = request.getParameter("input");
            String uname = request.getParameter("user");
            
            if (actiontype.equalsIgnoreCase("DeleteUser")) {
                pstmtUsers = conUsers.prepareStatement("Delete from Users where Username = ?");
                pstmtUsers.setString(1,uname);
                pstmtUsers.executeUpdate();
                out.println("<h1>User "+uname+" was deleted</h1>");
            } else if (actiontype.equalsIgnoreCase("IssueBook")) {
                session.setAttribute("name",uname);
                session.setAttribute("book", input);
                response.sendRedirect("IssueBook");
            } else if (actiontype.equalsIgnoreCase("ReturnBook")) {

                pstmtUsers = conUsers.prepareStatement("Select * from Users where Username = ?");
                pstmtUsers.setString(1, uname);
                rsUsers = pstmtUsers.executeQuery();
                String bname="";
                while(rsUsers.next()){
                    bname = rsUsers.getString(6);
                }
                pstmtUsers = conUsers.prepareStatement("Update Users set IssuedBook = ? , BookIssuedDate = ? , BookDueDate=?"
                        + " where Username = ?");
                pstmtUsers.setString(1,null);
                pstmtUsers.setString(2,null);
                pstmtUsers.setString(3, null);
                pstmtUsers.setString(4, uname);
                pstmtUsers.executeUpdate();
                pstmtBooks = conBooks.prepareStatement("Update Books set Availability = ?, BookIssuedDate=?,"
                        + " BookExpectedReturn = ? where BookTitle = ?");
                pstmtBooks.setBoolean(1, true);
                pstmtBooks.setString(2, null);
                pstmtBooks.setString(3, null);
                pstmtBooks.setString(4,bname);
                pstmtBooks.executeUpdate();
                out.println("<h2>Book returned</h2>");
            } else if (actiontype.equalsIgnoreCase("IssueFine")) {
                if(isNumber(input)){
                pstmtUsers = conUsers.prepareStatement("Select * from Users where Username = ?");
                pstmtUsers.setString(1,uname);
                rsUsers = pstmtUsers.executeQuery();
                double d = 0;
                while(rsUsers.next()){
                    d = Double.parseDouble(rsUsers.getString(9));
                }
                double a = Double.parseDouble(request.getParameter("input"));
                d = d+a;
                pstmtUsers = conUsers.prepareStatement("Update Users set Fine = ? where Username = ?");
                pstmtUsers.setString(1,Double.toString(d));
                pstmtUsers.setString(2, uname);
                pstmtUsers.executeUpdate();
                out.println("<h1>Fine of amount $"+request.getParameter("input")+" collected from the user "+uname+"</h1>");
                }
                else{
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Please enter numeric value for fine');");
                    out.println("</script>");
                    response.setHeader("Refresh", "1;url=SearchUser");
                }
            }else if (actiontype.equalsIgnoreCase("CollectFine")) {
                if(isNumber(input)){
                pstmtUsers = conUsers.prepareStatement("Select * from Users where Username = ?");
                pstmtUsers.setString(1,uname);
                rsUsers = pstmtUsers.executeQuery();
                double d = 0;
                while(rsUsers.next()){
                    d = Double.parseDouble(rsUsers.getString(9));
                }
                double a = Double.parseDouble(request.getParameter("input"));
                d = d-a;
                pstmtUsers = conUsers.prepareStatement("Update Users set Fine = ? where Username = ?");
                pstmtUsers.setString(1,Double.toString(d));
                pstmtUsers.setString(2, uname);
                pstmtUsers.executeUpdate();
                out.println("<h1>Fine of amount $"+request.getParameter("input")+" collected from the user "+uname+"</h1>");
            }
            }else{
                
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Please enter numeric value for fine');");
                    out.println("</script>");
                    
                    response.setHeader("Refresh", "1;url=SearchUser");
            }
//            pstmtUsers = conUsers.prepareStatement("Update Users set IssuedBook = null , BookIssuedDate = null , BookDueDate=?null"
//                    + " where Username = ?");
//            pstmtUsers.setString(1, uname);
//            rsUsers = pstmtUsers.executeQuery();
        } else {

            out.println("<h1 align = 'center'>Please log in.</h1>"
                    + "<br><br><h2 align ='center'>"
                    + "<a href ='index.html'>click here to go to log in page</a></h2> ");
        }
        out.println("</body>");
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
    @Override
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
            Logger.getLogger(BookSearch.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BookSearch.class.getName()).log(Level.SEVERE, null, ex);
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

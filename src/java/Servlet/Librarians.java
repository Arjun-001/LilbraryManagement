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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
@WebServlet(urlPatterns = {"/Librarians"})
public class Librarians extends HttpServlet {

    PrintWriter out;
    String username;
    private ResultSet rsUsers, rsBooks, rsLibrarians;

    private Connection conUsers, conBooks, conLibrarians;
    private Statement stmtUsers, stmtBooks, stmtLibrarians;
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
        out.println("<title>Servlet Librarians</title>");
        out.println(
                "<link rel='stylesheet' href='" + request.getContextPath() + "/styles.css' TYPE=\"text/css\">"
                + "<meta charset=\"UTF-8\">\n"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("</head>");
        out.println("<body background = 'abc.jpg'>");
        out.println("<h1>Servlet Users at " + request.getContextPath() + "</h1>");
        session = request.getSession(false);
        if (session != null) {
            session.setAttribute("type", "Librarians");
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
            } else if (value.equalsIgnoreCase("See your info")) {
//                     String genre = request.getParameter("fieldname");
//             out.println("<h1 align = 'center'>Welcome, " + session.getAttribute("Username") + "</h1>");
                out.println("<form action='Logout' method = 'post' align = 'right'>"
                        + "<input type = 'submit' value='Logout' class='logout'></form>");

                String types = (String) session.getAttribute("type");
                out.println("<form action='" + types + "' method = 'post' align = 'right'>"
                        + "<input type = 'submit' value='Homepage' class='logout'></form>");
                PreparedStatement pstmtLibrarians = conLibrarians.prepareStatement("Select * from Librarian where LibrarianName = ?");
                pstmtLibrarians.setString(1, (String) session.getAttribute("Username"));
                rsLibrarians = pstmtLibrarians.executeQuery();
                out.println("<form action='' method = 'post'><table style='width:100%'>"
                        + "<tr><th>Username</th>"
                        + "<th>Shift timings</th>"
                        + "<th>Address</th>"
                        + "<th>Phone</th>"
                        + "<th>Email</th>");
                while (rsLibrarians.next()) {
                    out.println("<tr>");

                    out.println("<td>" + rsLibrarians.getString(1) + "</td><td>" + rsLibrarians.getString(3) + "</td><td>"
                            + rsLibrarians.getString(4) + "</td><td>" + rsLibrarians.getString(5) + "</td>"
                            + "</td><td>" + rsLibrarians.getString(6) + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else if (value.equalsIgnoreCase("Logout")) {
                response.sendRedirect("Logout");
            } else if (value.equals("All Users")) {
                response.sendRedirect("AllUsers");
            } else if (value.equalsIgnoreCase("Search for User")) {
                response.sendRedirect("SearchUser");
            } else if (value.equalsIgnoreCase("Add User")) {
                response.sendRedirect("signup");
            } else if (value.equalsIgnoreCase("Unavialable Books")) {
                response.sendRedirect("UnavialableBooks");
            } else if (value.equalsIgnoreCase("Add Book")) {
                response.sendRedirect("AddBook");
            } else if (value.equalsIgnoreCase("Remove Book")) {
                response.sendRedirect("AllBooks");
            }
        } else {
//                out.println("Search :");
            out.println("Search criteria: ");
            out.println("<form action='' method = 'post'>"
                    + "<input type = 'submit' value='All' name='value' class='button'>"
                    + "<input type = 'submit' value='All avialable Books' class='button' name='value'>"
                    + "<input type = 'submit' value='BookTitle' name='value' class='button'>"
                    + "<input type = 'submit' value='BookGenre' name='value' class='button'>"
                    + "<input type = 'submit' value='BookCode' name='value' class='button'>"
                    + "<input type = 'submit' value='See your info' name='value' class='button'>"
                    + "<input type = 'submit' value='Logout' name='value' class='button'> <br>"
                    + "<input type = 'submit' value='All Users' name='value' class='button'>"
                    + "<input type = 'submit' value='Search for User' name='value' class='button'>"
                    + "<input type = 'submit' value='Add User' name='value' class='button'>"
                    + "<input type = 'submit' value='Unavialable Books' name='value' class='button'>"
                    + "<input type = 'submit' value='Add Book' name='value' class='button'>"
                    + "<input type = 'submit' value='Remove Book' name='value' class='button'>");
        }
    }

    public void init() {
        try {
            String user = "";
            String password = "";
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);

            conLibrarians = DriverManager.getConnection("jdbc:ucanaccess://" + "C:\\Users\\arjun\\Bookse.mdb", user, password);
            stmtLibrarians = conLibrarians.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

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

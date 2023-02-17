package Logining;

// Local libs

import DB.Admin;
import DB.DbFunctions;
import DB.User;

// External libs
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainMenu {
    // Possible options for login
    private static final String ADMIN_OPTION = "1";
    private static final String USER_OPTION = "2";
    private static final String EXIT_OPTION = "3";

    // Checking the login data type
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Main menu of the whole program
    public void menu(DbFunctions db, Connection conn) {
        String option;
        String loginType;
        Scanner sc = new Scanner(System.in);

        // Greeting window
        System.out.println("1. Admin");
        System.out.println("2. User");
        System.out.print("Please select an option (1 or 2) or type 'exit' to quit: ");
        option = sc.nextLine();

        // Login option for the Admin
        if (option.equals(ADMIN_OPTION)) {
            try {
                System.out.println("Welcome, Admin!");
                System.out.print("Email or IIN: ");
                loginType = sc.nextLine();
                if (isNumeric(loginType)) { // If Admin entered his IIN
                    int idLogin = Integer.parseInt(loginType);
                    Admin AdminById = new Admin(conn, idLogin);
                    if (AdminById.loginByID(idLogin)) {
                        AdminById.AdminOptions();
                    }
                } else {
                    Admin AdminByEmail = new Admin(conn, loginType);
                    if (AdminByEmail.loginByEmail(loginType)) {
                        AdminByEmail.AdminOptions();
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("No such Admin account");
            }
        }
        // Login option for the User
        else if (option.equals(USER_OPTION)) {
            try {
                System.out.println("Welcome, User!");
                System.out.print("Email or IIN: ");
                loginType = sc.nextLine();
                if (isNumeric(loginType)) { // if User entered his IIN
                    int IdLogin = Integer.parseInt(loginType); // data type substitution for loginType
                    User UserById = new User(conn, IdLogin);    // creation User instance by ID
                    UserById.loginByID(IdLogin);                // Main login method
                } else {
                    String emailLogin = loginType; // if User entered his Email
                    User UserByEmail = new User(conn, emailLogin);  // Creating User instance by Email
                    if (UserByEmail.loginByEmail(emailLogin)) {
                        UserByEmail.deleteUser(UserByEmail.getId_num());
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("No such User account");
            }
        }

        // Exit option
        else if (option.equals(EXIT_OPTION))
            System.out.println("Bye");
        else {
            System.out.println("invalid option!");
        }
    }
}

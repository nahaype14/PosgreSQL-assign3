package DB;

import Logining.Password;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;
import java.util.SplittableRandom;

import static Logining.Password.PasswordValidation;
import static Logining.Password.doHashing;
import static java.lang.Integer.parseInt;


public class Admin implements Person {

    private int id_num;
    private String name;
    private String email;
    private String password;


    public Admin(Connection conn, int id_num) {
        Statement statement;
        ResultSet rs = null;
        try {
            String sql = String.format("select * from users where id_num = %s and isadmin = true", id_num);
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                this.id_num = rs.getInt("id_num");
                this.name = rs.getString("name");
                this.email = rs.getString("email");
                this.password = rs.getString("password");
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Admin(Connection conn, String email) {
        Statement statement;
        ResultSet rs = null;
        try {
            String sql = String.format("select * from users where email = '%s' and isadmin = true", email);
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                this.id_num = rs.getInt("id_num");
                this.name = rs.getString("name");
                this.email = rs.getString("email");
                this.password = rs.getString("password");
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean loginByEmail(String loginType) {
        Scanner sc = new Scanner(System.in);
        String passIn;

        for (int i = 3; i > 0; i--) {
            System.out.print("Password: ");
            passIn = doHashing(sc.nextLine());

            if ((this.password.equals(passIn) && this.email.equals(loginType))) {
                System.out.println("Success login!");
                return true;
            } else {
                System.out.println("Invalid Email/ID or Password");
                System.out.println("You have " + (i - 1) + " try(-ies)");
            }

            if (i == 1) {
                System.out.print("Forget password? [Y/N]:");
                String confirmation = sc.nextLine().toUpperCase();
                if (confirmation.equals("Y")) {
                    Password.resetPassword(this.password, this.email);
                    return false;
                }
                if (confirmation.equals("N")) {
                    registration();
                }
            }
        }
        return false;
    }

    @Override
    public boolean loginByID(int loginType) {
        Scanner sc = new Scanner(System.in);
        String passIn;

        for (int i = 3; i > 0; i--) {
            System.out.print("Password: ");
            passIn = doHashing(sc.nextLine());

            if ((this.password.equals(passIn) && this.id_num == loginType)) {
                System.out.println("Success login!");
                return true;
            } else {
                System.out.println("Invalid Email/ID or Password");
                System.out.println("You have " + (i - 1) + " try(-ies)");
            }

            if (i == 1) {
                System.out.print("Forget password? [Y/N]:");
                String confirmation = sc.nextLine().toUpperCase();
                if (confirmation.equals("Y")) {
                    Password.resetPassword(this.password, this.email);
                    return true;
                } else if (confirmation.equals("N")) {
                    System.out.println("INVALID ADMIN");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void registration() {
        // Connection to the DB
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("Users", "postgres", "1423");
        // Global variables
        Boolean isAdmin = true;
        String name, email, password, id_num;
        Scanner scan = new Scanner(System.in);

        // Clarification of user details
        System.out.println("New User Registration");
        System.out.println("Enter User details:");
        System.out.print("ID: ");
        id_num = scan.nextLine();
        System.out.print("Name: ");
        name = scan.nextLine();
        System.out.print("Email: ");
        email = scan.nextLine();

        password = null;
        boolean validation = false;
        while (!validation) { // Password validation by criteria
            System.out.print("Password: ");
            password = scan.nextLine();
            validation = PasswordValidation(password);
        }
        // Adding a user to the database
        db.insert_row(conn, "Users", name, email, doHashing(password), parseInt(id_num), isAdmin);
        System.out.println(name + " is registered!");
    }

    @Override
    public void deleteUser(int id) {
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("Users", "postgres", "1423");
        db.delete_row_by_id(conn, "users", id);
        System.out.println("User has been deleted!");
    }

    public void AdminOptions() {
        String option = null;
        Scanner sc = new Scanner(System.in);
        while (!Objects.equals(option, "e")) {
            System.out.println("As admin you can register and delete users");
            System.out.println("[reg] Add new user");
            System.out.println("[del] Delete user");
            System.out.print(">> ");
            option = sc.nextLine();
            if (option.equals("reg")) {
                this.registration();
            } else if (option.equals("del")) {
                System.out.print("Enter User's ID: ");
                int id = sc.nextInt();
                this.deleteUser(id);
            }
        }
    }

}

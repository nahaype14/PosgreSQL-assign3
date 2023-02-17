package DB;

// Local libs
import Logining.Password;

// External libs
import java.sql.*;
import java.util.Scanner;

//
import static Logining.Password.*;
import static java.lang.Integer.parseInt;

public class User implements Person {

    public int getId_num() {
        return id_num;
    }

    // User's fields
    private int id_num;
    private String name;
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    // User's constructor for loginByID
    public User(Connection conn, int id_num) {
        Statement statement;
        ResultSet rs = null;
        try {
            String sql = String.format("select * from users where id_num = %s and isadmin = false", id_num);
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

    // User's constructor for loginByEmail
    public User(Connection conn, String email) {
        Statement statement;
        ResultSet rs = null;
        try {
            String sql = String.format("select * from users where email = '%s and isadmin = false'", email);
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


    // Overrided login by email for User
    @Override
    public boolean loginByEmail(String loginType) {
        Scanner sc = new Scanner(System.in);
        String passIn;

        for (int i = 3; i > 0; i--) {
            System.out.print("Password: ");
            passIn = doHashing(sc.nextLine());

            if ((this.password.equals(passIn) && this.email.equals(loginType))){
                System.out.println("Success login!");
                return true;
            }
            else {
                System.out.println("Invalid Email/ID or Password");
                System.out.println("You have " + (i-1) + " try(-ies)");
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

    // Overrided login by ID for User
    @Override
    public boolean loginByID(int loginType) {
        Scanner sc = new Scanner(System.in);
        String passIn;

        // 3 tries to log in
        for (int i = 3; i > 0; i--) {
            System.out.print("Password: ");
            passIn = doHashing(sc.nextLine());

            if (this.password.equals(passIn) && this.id_num == loginType) {
                System.out.println("Success login!");
                return true;
            } else {
                System.out.println("Invalid Email/ID or Password");
                System.out.println("You have " + (i - 1) + " try(-ies)");
            }

            if (i == 1) {
                // Registration of new User or password reset
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

    // Registration for User
    @Override
    public void registration() {
        // Connection to the DB
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("Users", "postgres", "1423");
        // Global variables
        Boolean isAdmin = false;
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

    // Override method for deleting own account
    // (UNFINISHED)
    @Override
    public void deleteUser(int id) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Now you can only delete your Account");
        System.out.println("Do you want to do it? [Y/N]: ");
        String option = sc.nextLine();
        if (option.equals("Y")){
            DbFunctions db = new DbFunctions();
            Connection conn = db.connect_to_db("Users", "postgres", "1423");
            db.delete_row_by_id(conn, "users", this.id_num);
        } else if (option.equals("N")) {
            System.out.println("Bye!");
        }
    }

    // Name getter (May ever need)
    public String getName() {
        return name;
    }
}

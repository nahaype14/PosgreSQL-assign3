import DB.DbFunctions;
import DB.User;
import Logining.MainMenu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static Logining.MainMenu.isNumeric;


public class Main {
    public static void main(String[] args) throws SQLException {
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("Users", "postgres", "12345678");
        MainMenu menu = new MainMenu();
        menu.menu(db, conn);

    }
}

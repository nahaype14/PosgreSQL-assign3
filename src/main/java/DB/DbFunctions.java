package DB;

import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbFunctions {
    public Connection connect_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }

    public void createTable(Connection conn, String table_name) {
        Statement statement;
        try {
            String query = "create table " + table_name + "(empid SERIAL,name varchar(200),address varchar(200),primary key(empid));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void insert_row(Connection conn, String table_name, String name, String email, String password, int id, boolean isAdmin) {
        Statement statement;
        try {
            String query = String.format("insert into %s(id_num,name,email,password,isAdmin) values('%s','%s','%s','%s', %s);", table_name, id, name, email, password, isAdmin);
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void read_data(Connection conn, String table_name) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("select * from %s", table_name);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getString("id_num") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.println(rs.getString("email") + " ");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void update_password(Connection conn, String table_name, String old_pass, String email, String new_pass) {
        Statement statement;
        try {
            String query = String.format("update %s set password='%s' where email = '%s' and password='%s'", table_name, new_pass, email, old_pass);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Password was Updated!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void search_by_email(Connection conn, String table_name, String email) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("select * from %s where name= '%s'", table_name, email);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getString("id") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.println(rs.getString("email"));

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void search_by_id(Connection conn, String table_name, int id) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("select * from %s where empid= %s", table_name, id);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getString("empid") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.println(rs.getString("address"));

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void delete_row_by_email(Connection conn, String table_name, String email) {
        Statement statement;
        try {
            String query = String.format("delete from %s where name='%s'", table_name, email);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Deleted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void delete_row_by_id(Connection conn, String table_name, int id) {
        Statement statement;
        try {
            String query = String.format("delete from %s where id_num= %s", table_name, id);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Deleted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void delete_table(Connection conn, String table_name) {
        Statement statement;
        try {
            String query = String.format("drop table %s", table_name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Deleted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
package Logining;

import DB.DbFunctions;
import DB.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.postgresql.jdbc.PgConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Password {

    public static boolean PasswordValidation(String password) {
        int passwordLength = 6, upChars = 0, lowChars = 0;
        int special = 0, digits = 0;
        char ch;
        Scanner s = new Scanner(System.in);
        int total = password.length();
        if (total < passwordLength) {
            System.out.println("The Password is invalid!");
            return false;
        } else {
            for (int i = 0; i < total; i++) {
                ch = password.charAt(i);
                if (Character.isUpperCase(ch))
                    upChars = 1;
                else if (Character.isLowerCase(ch))
                    lowChars = 1;
                else if (Character.isDigit(ch))
                    digits = 1;
                else
                    special = 1;
            }
        }
        if (upChars == 1 && lowChars == 1 && digits == 1 && special == 1) {
            System.out.println("Password is Strong");
            return true;
        } else {
            System.out.println("Password is Weak");
            return false;
        }
    }

    private static String codeGenerator() {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        String characters = "0123456789";
        String code = RandomStringUtils.random(6, characters);
        return code;
    }

    public String getCode() {
        return codeGenerator();
    }

    public static String doHashing(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(password.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : resultByteArray) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static boolean emailVerification(String email) {
        Scanner sc = new Scanner(System.in);
        String confirmationCode = codeGenerator();
        String codeFromPrompt;
        SendMail mail = new SendMail(("Confirmation code: " + confirmationCode), email);

        do {
            System.out.print("Code: ");
            codeFromPrompt = sc.nextLine();
        } while (!codeFromPrompt.equals(confirmationCode));
        return true;
    }

    public static void resetPassword(String password, String email) {
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("Users", "postgres", "1423");
        if (emailVerification(email)) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Password must have:");
            System.out.println("- At least 6 symbols");
            System.out.println("- 1 digit:");
            System.out.println("- 1 extra symbol:");
            System.out.println("- Uppercase and lowercase letters");
            System.out.println();
            String new_pass = null;
            boolean validation = false;
            while (!validation) {
                System.out.print("New password: ");
                new_pass = sc.nextLine();
                validation = PasswordValidation(new_pass);
            }
            db.update_password(conn, "Users", password, email, doHashing(new_pass));

            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

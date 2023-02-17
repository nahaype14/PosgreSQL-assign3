package DB;

public interface Person {
    boolean loginByEmail(String loginType);
    boolean loginByID(int loginType) ;
    void registration();
    void deleteUser(int id);
}

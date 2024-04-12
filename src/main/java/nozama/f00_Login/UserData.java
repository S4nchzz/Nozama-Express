package nozama.f00_Login;

public class UserData {
    private final int user_id;
    private final String username;
    private final boolean login_status;
    private final String salt;
    private final String pass;
    private final boolean isadmin;
    private final String name;
    private final String telf;
    private final String gender;
    private final boolean banned;
    private final int warns;

    public UserData(int user_id, String username, boolean login_status, String salt, String pass, boolean isAdmin, String name, String telf, String gender, boolean banned, int warns) {
        this.user_id = user_id;
        this.username = username;
        this.login_status = login_status;
        this.salt = salt;
        this.pass = pass;
        this.isadmin = isAdmin;
        this.name = name;
        this.telf = telf;
        this.gender = gender;
        this.banned = banned;
        this.warns = warns;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLogin_status() {
        return login_status;
    }

    public String getSalt() {
        return salt;
    }

    public String getPass() {
        return pass;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public String getName() {
        return name;
    }

    public String getTelf() {
        return telf;
    }

    public String getGender() {
        return gender;
    }

    public boolean isBanned() {
        return banned;
    }

    public int getWarns() {
        return warns;
    }
}

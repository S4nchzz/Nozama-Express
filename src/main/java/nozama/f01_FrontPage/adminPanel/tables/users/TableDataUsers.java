package nozama.f01_FrontPage.adminPanel.tables.users;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableDataUsers {
    private final SimpleIntegerProperty user_id;
    private final SimpleStringProperty username;
    private final SimpleBooleanProperty loginStatus;
    private final SimpleStringProperty salt;
    private final SimpleStringProperty pass;
    private final SimpleBooleanProperty isAdmin;
    private final SimpleStringProperty name;
    private final SimpleStringProperty telf;
    private final SimpleStringProperty gender;
    private final SimpleBooleanProperty banned;
    private final SimpleIntegerProperty warnings;

    public TableDataUsers(int user_id, String username, boolean login_stauts, String salt, String pass, boolean isAdmin, String name, String telf,
            String gender, boolean banned, int warnings) {
        this.user_id = new SimpleIntegerProperty(user_id);
        this.username = new SimpleStringProperty(username);
        this.loginStatus = new SimpleBooleanProperty(login_stauts);
        this.salt = new SimpleStringProperty(salt);
        this.pass = new SimpleStringProperty(pass);
        this.isAdmin = new SimpleBooleanProperty(isAdmin);
        this.name = new SimpleStringProperty(name);
        this.telf = new SimpleStringProperty(telf);
        this.gender = new SimpleStringProperty(gender);
        this.banned = new SimpleBooleanProperty(banned);
        this.warnings = new SimpleIntegerProperty(warnings);
    }

    public int getUserID() {
        return user_id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public boolean getLoginStatus() {
        return loginStatus.get();
    }

    public String getSalt() {
        return salt.get();
    }

    public String getPass() {
        return pass.get();
    }

    public boolean getAdmin() {
        return isAdmin.get();
    }

    public String getName() {
        return name.get();
    }

    public String getTelf() {
        return telf.get();
    }

    public String getGender() {
        return gender.get();
    }

    public boolean getBanned () {
        return banned.get();
    }

    public int getWarnings () {
        return warnings.get();
    }
}
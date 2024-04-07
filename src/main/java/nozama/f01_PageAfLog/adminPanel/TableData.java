package nozama.f01_PageAfLog.adminPanel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableData {
    private final SimpleStringProperty username;
    private final SimpleStringProperty salt;
    private final SimpleStringProperty pass;
    private final SimpleBooleanProperty isAdmin;
    private final SimpleStringProperty name;
    private final SimpleStringProperty telf;
    private final SimpleStringProperty gender;

    public TableData(String username, String salt, String pass, boolean isAdmin, String name, String telf,
            String gender) {
        this.username = new SimpleStringProperty(username);
        this.salt = new SimpleStringProperty(salt);
        this.pass = new SimpleStringProperty(pass);
        this.isAdmin = new SimpleBooleanProperty(isAdmin);
        this.name = new SimpleStringProperty(name);
        this.telf = new SimpleStringProperty(telf);
        this.gender = new SimpleStringProperty(gender);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getSalt() {
        return salt.get();
    }

    public SimpleStringProperty saltProperty() {
        return salt;
    }

    public String getPass() {
        return pass.get();
    }

    public SimpleStringProperty passProperty() {
        return pass;
    }

    public boolean isAdmin() {
        return isAdmin.get();
    }

    public SimpleBooleanProperty isAdminProperty() {
        return isAdmin;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getTelf() {
        return telf.get();
    }

    public SimpleStringProperty telfProperty() {
        return telf;
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }
}
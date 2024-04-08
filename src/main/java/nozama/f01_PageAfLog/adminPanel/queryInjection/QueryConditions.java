package nozama.f01_PageAfLog.adminPanel.queryInjection;

import javax.swing.JOptionPane;

public class QueryConditions {
    private final String query;

    public QueryConditions (String query) {
        this.query = query;
    }

    public boolean conditions () {
        if (deleteFromCondition() && alterTableCondition() && dropDatabaseCondition() && dropTableCondition() && createProcedure() && select()) {
            return true;
        }

        return false;
    }

    private boolean select () {
        if (this.query.contains("SELECT USERNAME") || !this.query.contains("SELECT SALT") || !this.query
                .contains("SELECT PASS") || !this.query.contains("SELECT ISADMIN") || !this.query
                        .contains("SELECT NAME") || !this.query.contains("SELECT TELF") ||
                        !this.query.contains("SELECT GENDER")) {
            JOptionPane.showMessageDialog(null, "Funcion de seleccion de campo unico aun no implementada");
            return false;
        }

        return true;
    }

    private boolean createProcedure () {
        if (this.query.contains("CREATE PROCEDURE") || this.query.contains("CREATE FUNCTION")) {
            return false;
        }

        return true;
    }

    private boolean dropTableCondition () {
        if (this.query.contains("DROP TABLE")) {
            return false;
        }

        return true;
    }

    private boolean dropDatabaseCondition () {
        if (this.query.contains("DROP DATABASE")) {
            return false;
        }

        return true;
    }

    private boolean alterTableCondition () {
        if (this.query.contains("ALTER TABLE")) {
            return false;
        }

        return true;
    }

    private boolean deleteFromCondition () {
        if (this.query.contains("DELETE FROM")) {
            return false;
        }

        return true;
    }
}

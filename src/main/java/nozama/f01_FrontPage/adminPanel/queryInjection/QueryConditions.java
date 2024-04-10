package nozama.f01_FrontPage.adminPanel.queryInjection;

public class QueryConditions {
    private final String query;

    public QueryConditions (String query) {
        this.query = query;
    }

    public boolean conditions () {
        if (alterTableCondition() && dropDatabaseCondition() && updateTableCondition() && dropTableCondition() && createProcedure()) {
            return true;
        }

        return false;
    }

    private boolean createProcedure () {
        if (this.query.contains("CREATE PROCEDURE") || this.query.contains("CREATE FUNCTION") || this.query
                .contains("CREATE OR REPLACE PROCEDURE") || this.query.contains("CREATE OR REPLACE FUNCTION")) {
            return false;
        }

        return true;
    }

    private boolean updateTableCondition() {
        if (this.query.contains("UPDATE")) {
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
}

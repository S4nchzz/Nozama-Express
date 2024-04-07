package nozama.f01_PageAfLog.adminPanel;

public class QueryConditions {
    private final String query;

    public QueryConditions (String query) {
        this.query = query;
    }

    protected boolean conditions () {
        if (deleteFromCondition() && alterTableCondition() && dropDatabaseCondition()) {
            return true;
        }

        return false;
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

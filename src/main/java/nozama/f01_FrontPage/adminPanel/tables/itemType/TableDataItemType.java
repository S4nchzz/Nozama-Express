package nozama.f01_FrontPage.adminPanel.tables.itemType;

import javafx.beans.property.SimpleStringProperty;

public class TableDataItemType {
    private SimpleStringProperty type;
    private SimpleStringProperty description;

    public TableDataItemType (String type, String description) {
        this.type = new SimpleStringProperty(type);
        this.description = new SimpleStringProperty(description);
    }

    public String getType() {
        return type.get();
    }

    public String getDescription() {
        return description.get();
    }
}
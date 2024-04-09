package nozama.f01_PageAfLog.adminPanel.tables.itemType;

import javafx.beans.property.SimpleStringProperty;

public class TableDataItemType {
    private SimpleStringProperty item_type;
    private SimpleStringProperty description;

    public TableDataItemType (String item_type, String description) {
        this.item_type = new SimpleStringProperty(item_type);
        this.description = new SimpleStringProperty(description);
    }

    public String getItem_type() {
        return item_type.get();
    }

    public String getDescription() {
        return description.get();
    }
}

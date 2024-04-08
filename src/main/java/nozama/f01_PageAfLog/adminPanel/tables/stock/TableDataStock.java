package nozama.f01_PageAfLog.adminPanel.tables.stock;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableDataStock {
    private SimpleIntegerProperty stock_id;
    private SimpleStringProperty product;
    private SimpleIntegerProperty stock_amount;
    private SimpleDoubleProperty item_price;
    private SimpleIntegerProperty discount;

    public TableDataStock (int stock_id, String product, int stock_amount, double item_price, int discount) {
        this.stock_id = new SimpleIntegerProperty(stock_id);
        this.product = new SimpleStringProperty(product);
        this.stock_amount = new SimpleIntegerProperty(stock_amount);
        this.item_price = new SimpleDoubleProperty(item_price);
    }
    
    public int getStock_id() {
        return stock_id.get();
    }

    public String getProduct() {
        return product.get();
    }

    public int getStock_amount() {
        return stock_amount.get();
    }

    public double getItem_price() {
        return item_price.get();
    }
    
    public int getDiscount() {
        return discount.get();
    }
}
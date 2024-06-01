package nozama.f01_FrontPage.storeData;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ProductStoreTemplate {
    private Pane productPane;
    private final StoreProductData productData;
    
    // Product elements
    @FXML
    private ImageView fxid_itemPicture;
    @FXML
    private Text fxid_itemName;
    @FXML
    private Text fxid_itemDescription;
    @FXML
    private Text fxid_itemPrice;
    @FXML
    private Button fxid_addToCart;
    
    public ProductStoreTemplate (StoreProductData product) {
        FXMLLoader template = new FXMLLoader();
        template.setLocation(getClass().getResource("/nozama/frontPage/templateElements/storeProductTemplate.fxml"));
        template.setController(this);
        try {productPane = template.load();} catch (IOException e) {}
        this.productData = product;
    }
    
    public Pane getProductPane () {
        ByteArrayInputStream b = new ByteArrayInputStream(DatabaseRequestManagment.getProductImage(productData.getStock_ID()));
        this.fxid_itemPicture.setImage(new Image(b));
        this.fxid_itemName.setText(productData.getProduct());
        this.fxid_itemDescription.setText(productData.getDescription());
        this.fxid_itemPrice.setText(productData.getItemPrice() + "€");
        return this.productPane;
    }
}
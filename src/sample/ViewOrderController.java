package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewOrderController implements Initializable {
    @FXML
    private Label txtID;
    @FXML
    private Label txtOrderTime;
    @FXML
    private Label txtOrderDate;
    @FXML
    private Label txtTotal;
    @FXML
    private Label txtDiscount;
    @FXML
    private Label txtOrderTotal;

    @FXML
    private TableView<ProductInCart> tbProduct;
    @FXML
    private TableColumn<ProductInCart, String> colName;
    @FXML
    private TableColumn<ProductInCart, String> colQuantity;
    @FXML
    private TableColumn<ProductInCart, String> colPrice;
    @FXML
    private TableColumn<ProductInCart, String> colTotal;

    ObservableList<ProductInCart> observableList = FXCollections.observableArrayList();

    public void buttonBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("OrderDetail.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Order Detail");

        stage.show();
    }

    public void setOrder(Order order) {
        for (ProductInCart p: order.getList()) observableList.add(p);

        txtID.setText(order.getId());
        txtOrderTime.setText(order.getOrderTime());
        txtOrderDate.setText(order.getOrderDate());
        txtDiscount.setText(String.valueOf(order.getDiscount()));
        txtTotal.setText(String.valueOf(order.getTotal()));
        txtOrderTotal.setText(String.valueOf(order.getOrderTotal()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        tbProduct.setItems(observableList);
    }
}

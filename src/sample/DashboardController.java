package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    Button btnEmployee;
    @FXML
    Button btnOrder;
    @FXML
    Button btnOrderDetail;
    @FXML
    Button btnProduct;
    @FXML
    Button btnCategory;
    @FXML
    Button btnLogout;

    public void buttonEmployee(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Employee.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Employee");

        stage.show();
    }

    public void buttonOrder(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Order.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Order");

        stage.show();
    }

    public void buttonProduct(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Product.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Product");

        stage.show();
    }

    public void buttonCategory(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Category.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Category");

        stage.show();
    }

    public void buttonLogOut(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Log in");

        stage.show();
    }

    public void buttonListOrder(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("OrderDetail.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Order Detail");

        stage.show();
    }
}

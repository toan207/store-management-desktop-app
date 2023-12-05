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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtQuantity;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtCategory;
    @FXML
    private TableView<Product> tbProduct;
    @FXML
    private TableColumn<Product, String> colID;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, String> colQuantity;
    @FXML
    private TableColumn<Product, String> colPrice;
    @FXML
    private TableColumn<Product, String> colCategory;
    @FXML
    private Label txtNoti;

    private ObservableList<Product> observableList = FXCollections.observableArrayList();
    private ArrayList<Category> categories = new ArrayList<Category>();

    public void buttonBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FileInputStream fin = new FileInputStream("category.txt");
            int i = 0;
            int line = 1;
            String tmp = "";
            ArrayList<String> tmpArr = new ArrayList<String>();

            while ((i = fin.read()) != -1) {
                if ((char) i != '\n') {
                    tmp += (char) i;
                }
                else {
                    tmpArr.add(tmp);
                    tmp = "";
                    line++;
                }

                if (line % 3 == 0) {
                    line = 1;
                    Category tmpCate = new Category(tmpArr.get(0),tmpArr.get(1));
                    categories.add(tmpCate);
                    tmpArr.removeAll(tmpArr);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fin = new FileInputStream("product.txt");
            int i = 0;
            int line = 1;
            String tmp = "";
            ArrayList<String> tmpArr = new ArrayList<String>();

            while ((i = fin.read()) != -1) {
                if ((char) i != '\n') {
                    tmp += (char) i;
                }
                else {
                    tmpArr.add(tmp);
                    tmp = "";
                    line++;
                }

                if (line % 6 == 0) {
                    line = 1;
                    Product tmpProduct = new Product(tmpArr.get(0),tmpArr.get(1), tmpArr.get(4), Double.parseDouble(tmpArr.get(2)), Double.parseDouble(tmpArr.get(3)));
                    observableList.add(tmpProduct);
                    tmpArr.removeAll(tmpArr);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        tbProduct.setItems(observableList);
    }

    public void saveFile() {
        try {
            FileOutputStream fout = new FileOutputStream("product.txt");
            for (int i = 0; i < observableList.size(); ++i) {
                String myendl = "\n";
                fout.write(observableList.get(i).getId().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getName().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getQuantity().toString().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getPrice().toString().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getCategory().getBytes());
                fout.write(myendl.getBytes());
            }
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonSave(ActionEvent actionEvent) {
        int pos = -1;
        for (int i = 0; i < observableList.size(); ++i) {
            if (observableList.get(i).getId().equals(txtID.getText())) {
                pos = i;
                break;
            }
        }

        int f = 0;
        for (int i = 0; i < categories.size(); ++i) {
            if (categories.get(i).getId().equals(txtCategory.getText())) {
                f = 1;
                break;
            }
        }

        if (pos == -1) {
            if (f == 0) txtNoti.setText("Invalid category ID!");
            else {
                Product product = new Product(txtID.getText(), txtName.getText(), txtCategory.getText(), Double.parseDouble(txtQuantity.getText()), Double.parseDouble(txtPrice.getText()));
                observableList.add(product);
                txtNoti.setText("");
            }
        }
        else {
            observableList.get(pos).setId(txtID.getText());
            observableList.get(pos).setName(txtName.getText());
            observableList.get(pos).setQuantity(Double.parseDouble(txtQuantity.getText()));
            observableList.get(pos).setPrice(Double.parseDouble(txtPrice.getText()));
            observableList.get(pos).setCategory(txtCategory.getText());
            tbProduct.refresh();
        }

        txtID.setText("");
        txtName.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
        txtCategory.setText("");
        saveFile();
    }

    public void buttonDelete(ActionEvent actionEvent) {
        Product selected = tbProduct.getSelectionModel().getSelectedItem();
        observableList.remove(selected);
        saveFile();
    }

    public void buttonUpdate(ActionEvent actionEvent) {
        Product selected = tbProduct.getSelectionModel().getSelectedItem();
        txtID.setText(selected.getId());
        txtName.setText(selected.getName());
        txtQuantity.setText(selected.getQuantity().toString());
        txtPrice.setText(selected.getPrice().toString());
        txtCategory.setText(selected.getCategory());
    }
}

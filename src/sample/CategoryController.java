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
import javafx.scene.control.Button;
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

public class CategoryController implements Initializable {
    @FXML
    private TableView<Category> tbCategory;
    @FXML
    private TableColumn<Category, String> colID;
    @FXML
    private TableColumn<Category, String> colCategory;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;

    public void buttonBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");

        stage.show();
    }
    private ObservableList<Category> observableList = FXCollections.observableArrayList();

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
                    observableList.add(tmpCate);
                    tmpArr.removeAll(tmpArr);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbCategory.setItems(observableList);
    }

    public void saveFile() {
        try {
            FileOutputStream fout = new FileOutputStream("category.txt");
            for (int i = 0; i < observableList.size(); ++i) {
                String myendl = "\n";
                fout.write(observableList.get(i).getId().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getName().getBytes());
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
        if (pos == -1) {
            Category category = new Category(txtID.getText(), txtName.getText());
            observableList.add(category);
        }
        else {
            observableList.get(pos).setId(txtID.getText());
            observableList.get(pos).setName(txtName.getText());
            tbCategory.refresh();
        }

        txtID.setText("");
        txtName.setText("");
        saveFile();
    }

    public void buttonDelete(ActionEvent actionEvent) {
        Category selected = tbCategory.getSelectionModel().getSelectedItem();
        observableList.remove(selected);
        saveFile();
    }

    public void buttonUpdate(ActionEvent actionEvent) {
        Category selected = tbCategory.getSelectionModel().getSelectedItem();
        txtID.setText(selected.getId());
        txtName.setText(selected.getName());
    }
}

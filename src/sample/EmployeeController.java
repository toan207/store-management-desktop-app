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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


public class EmployeeController implements Initializable {
    @FXML
    private TableView<Employee> tbEmployee;
    @FXML
    private TableColumn<Employee, String> colUsername;
    @FXML
    private TableColumn<Employee, String> colPwd;
    @FXML
    private TableColumn<Employee, String> colName;
    @FXML
    private TableColumn<Employee, String> colGender;
    @FXML
    private TableColumn<Employee, String> colDOB;
    @FXML
    private TableColumn<Employee, String> colEmail;
    @FXML
    private TableColumn<Employee, String> colPhone;
    @FXML
    private TableColumn<Employee, String> colRole;
    @FXML
    private TableColumn<Employee, Double> colSalary;

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPwd;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtGender;
    @FXML
    private TextField txtDOB;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtRole;
    @FXML
    private TextField txtSalary;

    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    public Button btnSave;

    private ObservableList<Employee> observableList = FXCollections.observableArrayList();

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
            FileInputStream fin = new FileInputStream("employee.txt");
            int i = 0;
            int line = 1;
            String tmp = "";
            ArrayList<String> tmpArr = new ArrayList<String>();

            while ((i = fin.read()) != -1) {
                if ((char) i != '\n') {
                    tmp += (char) i;
                }
                else {
//                    System.out.println(tmp);
                    tmpArr.add(tmp);
                    tmp = "";
                    line++;
                }

                if (line % 10 == 0) {
                    line = 1;
                    Employee tmpEmp = new Employee(tmpArr.get(0),tmpArr.get(1),tmpArr.get(2),tmpArr.get(3),tmpArr.get(4),tmpArr.get(5),tmpArr.get(6),tmpArr.get(7),Double.parseDouble(tmpArr.get(8)));
                    observableList.add(tmpEmp);
                    tmpArr.removeAll(tmpArr);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPwd.setCellValueFactory(new PropertyValueFactory<>("password"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("DOB"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        tbEmployee.setItems(observableList);
    }

    public void saveFile() {
        try {
            FileOutputStream fout = new FileOutputStream("employee.txt");
            for (int i = 0; i < observableList.size(); ++i) {
//                System.out.println(observableList.get(i).getUsername());
                String myendl = "\n";
                fout.write(observableList.get(i).getUsername().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getPassword().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getRole().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getName().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getDOB().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getGender().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getEmail().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getPhone().getBytes());
                fout.write(myendl.getBytes());
                fout.write(observableList.get(i).getSalary().toString().getBytes());
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
        try {
            int pos = -1;
            for (int i = 0; i < observableList.size(); ++i) {
                if (observableList.get(i).getUsername().equals(txtUsername.getText())) {
                    pos = i;
                    break;
                }
            }
            if (pos == -1) {
                Employee employee = new Employee(txtUsername.getText(), txtPwd.getText(), txtRole.getText(), txtName.getText(), txtDOB.getText(), txtGender.getText(), txtEmail.getText(), txtPhone.getText(), Double.parseDouble(txtSalary.getText()));
                observableList.add(employee);
            }
            else {
                observableList.get(pos).setUsername(txtUsername.getText());
                observableList.get(pos).setPassword(txtPwd.getText());
                observableList.get(pos).setRole(txtRole.getText());
                observableList.get(pos).setName(txtName.getText());
                observableList.get(pos).setDOB(txtDOB.getText());
                observableList.get(pos).setGender(txtGender.getText());
                observableList.get(pos).setEmail(txtEmail.getText());
                observableList.get(pos).setPhone(txtPhone.getText());
                observableList.get(pos).setSalary(Double.parseDouble(txtSalary.getText()));
                tbEmployee.refresh();
            }

            txtUsername.setText("");
            txtPwd.setText("");
            txtName.setText("");
            txtDOB.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
            txtGender.setText("");
            txtRole.setText("");
            txtSalary.setText("");

            saveFile();
        } catch (Exception e) {

        }
    }

    public void buttonUpdate(javafx.event.ActionEvent actionEvent) {
        Employee selected = tbEmployee.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtUsername.setText(selected.getUsername());
            txtPwd.setText(selected.getPassword());
            txtName.setText(selected.getName());
            txtDOB.setText(selected.getDOB());
            txtEmail.setText(selected.getEmail());
            txtPhone.setText(selected.getPhone());
            txtGender.setText(selected.getGender());
            txtRole.setText(selected.getRole());
            txtSalary.setText(selected.getSalary().toString());
        }
    }

    public void buttonDelete(javafx.event.ActionEvent actionEvent) {
        Employee selected = tbEmployee.getSelectionModel().getSelectedItem();
        observableList.remove(selected);
        saveFile();
    }
}

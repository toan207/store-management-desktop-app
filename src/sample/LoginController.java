package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import  javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lbLoginMessage;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

//    Stage stage = new Stage();
    ArrayList<Employee> accountData = new ArrayList<Employee>();
    public void loginButtonOnAction(ActionEvent actionEvent) throws IOException {
        if (txtUsername.getText().isEmpty() == false && txtPassword.getText().isEmpty() == false) {
            int f = 0;
            for (int i = 0; i < accountData.size(); ++i) {
                if (accountData.get(i).getUsername().equals(txtUsername.getText()) && accountData.get(i).getPassword().equals(txtPassword.getText())) {
                    f = 1;
                    break;
                }
            }
            if (f == 1) {
//                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//                FXMLLoader loader = new FXMLLoader();
//                URL tmp = getClass().getResource("Dashboard.fxml");
//                loader.setLocation(tmp);
//                System.out.println(tmp);
////                AnchorPane dashboardParent = (AnchorPane) loader.load();
//                Parent dashboardParent = loader.load();
//                Scene scene = new Scene(dashboardParent);
//                DashboardController controller = loader.getController();
//                stage.setScene(scene);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");

                stage.show();
            }
            else lbLoginMessage.setText("Username or password invalid, please try again!");
        }
        else {
            lbLoginMessage.setText("Please enter username and password");
        }
    }

    public void cancelButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
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
                    accountData.add(tmpEmp);
                    tmpArr.removeAll(tmpArr);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File brandingFile = new File("Images/img.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("Images/login.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }
}

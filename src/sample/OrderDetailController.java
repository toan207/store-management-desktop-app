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
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderDetailController implements Initializable {
    @FXML
    private TableView<Order> tbOrder;
    @FXML
    private TableColumn<Order, String> colID;
    @FXML
    private TableColumn<Order, String> colDate;
    @FXML
    private TableColumn<Order, String> colTime;
    @FXML
    private TableColumn<Order, String> colDiscount;
    @FXML
    private TableColumn<Order, String> colTotal;

    public void buttonBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");

        stage.show();
    }

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    public void buttonViewDetail(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewOrder.fxml"));
        Parent root = loader.load();

        Order selected = tbOrder.getSelectionModel().getSelectedItem();
        ViewOrderController controller = loader.getController();
        controller.setOrder(selected);

        stage.setScene(new Scene(root));
        stage.setTitle("Order Info");

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
        tbOrder.setItems(orders);
    }

    public void buttonDelete(ActionEvent actionEvent) {
        Order selected = tbOrder.getSelectionModel().getSelectedItem();
        orders.remove(selected);

        for (Order order: orders) saveFile(order);
    }

    public void saveFile(Order order) {
        try {
            FileOutputStream fout = new FileOutputStream("order.txt");
            String myendl = "\n";
            fout.write(order.getOrderDate().getBytes());
            fout.write(myendl.getBytes());
            fout.write(order.getOrderTime().getBytes());
            fout.write(myendl.getBytes());
            fout.write(order.getId().getBytes());
            fout.write(myendl.getBytes());
            fout.write(String.valueOf(order.getTotal()).getBytes());
            fout.write(myendl.getBytes());
            fout.write(String.valueOf(order.getDiscount()).getBytes());
            fout.write(myendl.getBytes());
            fout.write(String.valueOf(order.getOrderTotal()).getBytes());
            fout.write(myendl.getBytes());
            for (int i = 0; i < order.getList().size(); ++i) {
                fout.write(order.getList().get(i).getName().getBytes());
                fout.write(myendl.getBytes());
                fout.write(String.valueOf(order.getList().get(i).getPrice()).getBytes());
                fout.write(myendl.getBytes());
                fout.write(String.valueOf(order.getList().get(i).getQuantity()).getBytes());
                fout.write(myendl.getBytes());
                fout.write(String.valueOf(order.getList().get(i).getSubTotal()).getBytes());
                fout.write(myendl.getBytes());
            }
            fout.write("#####".getBytes());
            fout.write(myendl.getBytes());
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try {
            FileInputStream fin = new FileInputStream("order.txt");
            int i = 0;
            int f = 0;
            String tmp = "";
            ArrayList<String> tmpArr = new ArrayList<String>();

            while ((i = fin.read()) != -1) {
                if ((char) i != '\n') {
                    tmp += (char) i;
                }
                else if (!tmp.equals("") && !tmp.equals("#####")){
                    tmpArr.add(tmp);
                    tmp = "";
                }

                if (tmp.equals("#####")) {
                    ArrayList<ProductInCart> products = new ArrayList<ProductInCart>();
                    for (int x = 6; x < tmpArr.size(); x += 4) {
                        System.out.println(tmpArr.get(x));
                        System.out.println(tmpArr.get(x+1));
                        System.out.println(tmpArr.get(x+2));
                        System.out.println(tmpArr.get(x+3));
                        System.out.println();
                        ProductInCart pic = new ProductInCart(tmpArr.get(x), Double.parseDouble(tmpArr.get(x+1)), Double.parseDouble(tmpArr.get(x+2)), Double.parseDouble(tmpArr.get(x+3)));
                        products.add(pic);
                    }
                    Order tmpOrder = new Order(tmpArr.get(0),tmpArr.get(1),tmpArr.get(2),Double.parseDouble(tmpArr.get(3)),Double.parseDouble(tmpArr.get(4)),Double.parseDouble(tmpArr.get(5)),products);
                    orders.add(tmpOrder);
                    tmpArr.removeAll(tmpArr);
                    tmp = "";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonAdd(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Order.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Order");

        stage.show();
    }
}

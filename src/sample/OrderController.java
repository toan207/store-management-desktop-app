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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class OrderController implements Initializable {
    @FXML
    private ComboBox<String> cbboxCategory;
    @FXML
    private ComboBox<String> cbboxProduct;
    @FXML
    private ImageView orderImageView;

    @FXML
    private TableView<ProductInCart> tbOrder;
    @FXML
    private TableColumn<ProductInCart, String> colName;
    @FXML
    private TableColumn<ProductInCart, String> colQuantity;
    @FXML
    private TableColumn<ProductInCart, String> colPrice;
    @FXML
    private TableColumn<ProductInCart, String> colTotal;

    @FXML
    private TextField txtOrderDate;
    @FXML
    private TextField txtOrderTime;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Label txtNoti;
    @FXML
    private Label txtNoti1;
    @FXML
    private TextField txtDiscount;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtOrderTotal;

    ObservableList<String> listCategory = FXCollections.observableArrayList();
    ObservableList<String> listProduct = FXCollections.observableArrayList();
    ObservableList<ProductInCart> observableList = FXCollections.observableArrayList();
    private ArrayList<Category> categories = new ArrayList<Category>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Order> orders = new ArrayList<Order>();
    private HashMap<String, String> categoryMp = new HashMap<String, String>();
    private HashMap<String, ArrayList<Product>> productMp = new HashMap<String, ArrayList<Product>>();

    public void buttonPayment(ActionEvent actionEvent) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        ZonedDateTime now = ZonedDateTime.now();
        now.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));
        String dateTmp = dtf.format(now).toString();
        String[] datetime = dateTmp.split(" ");
        String date = datetime[0];
        String time = datetime[1];
        String id = date.replaceAll("/","") + time.replaceAll(":", "");
        if (!txtDiscount.getText().equals("")) {
            double total = Double.parseDouble(txtTotal.getText());
            double discound = Double.parseDouble(txtDiscount.getText());
            double orderTotal = total - (total * (discound/100));
            ArrayList<ProductInCart> p = new ArrayList<>();
            Order order = new Order(date, time, id, total, discound, orderTotal, p);
            saveFile(order);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");

            stage.show();
        }
        else {
            txtNoti1.setText("Please enter discount first!");
        }
    }

    public void saveFile(Order order) {
        try {
            FileOutputStream fout = new FileOutputStream("order.txt", true);
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
            for (int i = 0; i < observableList.size(); ++i) {
                fout.write(observableList.get(i).getName().getBytes());
                fout.write(myendl.getBytes());
                fout.write(String.valueOf(observableList.get(i).getPrice()).getBytes());
                fout.write(myendl.getBytes());
                fout.write(String.valueOf(observableList.get(i).getQuantity()).getBytes());
                fout.write(myendl.getBytes());
                fout.write(String.valueOf(observableList.get(i).getSubTotal()).getBytes());
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

    public void discountTextChange(KeyEvent keyEvent) {
//        System.out.println(txtDiscount.getText());
        txtNoti1.setText("");
        try {
            double dcTmp = Double.parseDouble(txtDiscount.getText());
            if (txtTotal.getText().equals("")) txtNoti1.setText("Please add product first!");
            else if (dcTmp > 100) txtNoti1.setText("Please enter under 100!");
            else {
                double total = Double.parseDouble(txtTotal.getText());
                total -= total * (dcTmp/100);
                txtOrderTotal.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            txtNoti1.setText("Please enter valid number!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        getProdutListByCategory();
        cbboxCategory.setItems(listCategory);
        cbboxProduct.setItems(listProduct);

        File orderFile = new File("Images/order.jpg");
        Image orderImage = new Image(orderFile.toURI().toString());
        orderImageView.setImage(orderImage);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        tbOrder.setItems(observableList);
    }

    public void buttonAddToCart(ActionEvent actionEvent) {
        txtNoti.setText("");
        ProductInCart productInCart;
        double quantity = Double.parseDouble(txtQuantity.getText());

        for (Product p: products) {
            if (p.getName().equals(cbboxProduct.getValue())) {
                if (p.getQuantity() < quantity) {
                    txtNoti.setText("Not enough in storage!");
                    break;
                }
                int pos = -1;
                for (int i = 0; i < observableList.size(); ++i) {
                    if (observableList.get(i).getName().equals(p.getName())) {
                        pos = i;
                        break;
                    }
                }
                if (pos != -1) {
                    if (observableList.get(pos).getQuantity() + quantity > p.getQuantity()) {
                        txtNoti.setText("Not enough in storage!");
                        break;
                    }
                    observableList.get(pos).setQuantity(observableList.get(pos).getQuantity() + quantity);
                    observableList.get(pos).setSubTotal(p.getPrice() * observableList.get(pos).getQuantity());
                    tbOrder.refresh();
                    break;
                }
                productInCart = new ProductInCart(p.getName(), p.getPrice(), quantity, quantity * p.getPrice());
                observableList.add(productInCart);
                break;
            }
        }

        double priceTotal = 0;
        for (ProductInCart p: observableList) {
            priceTotal += p.getSubTotal();
        }
        txtTotal.setText(String.valueOf(priceTotal));

        cbboxProduct.setValue("");
        cbboxCategory.setValue("");
        txtQuantity.setText("");
    }

    public void getProductByCategory(ActionEvent actionEvent) {
//        System.out.println(cbboxCategory.getValue());
        listProduct.clear();
        if (!cbboxCategory.getValue().equals("")) {
            for (Product i : productMp.get(cbboxCategory.getValue())) {
                listProduct.add(i.getName());
            }
        }
    }

    private void loadData() {
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
                    products.add(tmpProduct);
                    tmpArr.removeAll(tmpArr);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getProdutListByCategory() {
        for (Category category : categories) {
            categoryMp.put(category.getId(), category.getName());
            listCategory.add(category.getName());
        }

        for (Map.Entry<String, String> entry : categoryMp.entrySet()) {
            ArrayList<Product> tmp = new ArrayList<Product>();
//            System.out.println(entry.getValue());
            for (Product product : products) {
                if (product.getCategory().equals(entry.getKey())) {
//                    System.out.print(product.getName() + " ");
                    tmp.add(product);
                }
            }
//            System.out.println();
            productMp.put(entry.getValue(), tmp);
        }
    }

    public void buttonBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");

        stage.show();
    }

    public void buttonRemove(ActionEvent actionEvent) {
        ProductInCart selected = tbOrder.getSelectionModel().getSelectedItem();
        observableList.remove(selected);

        double priceTotal = 0;
        for (ProductInCart p: observableList) {
            priceTotal += p.getSubTotal();
        }
        txtTotal.setText(String.valueOf(priceTotal));
    }
}

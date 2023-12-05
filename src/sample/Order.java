package sample;

import java.util.ArrayList;

public class Order {
    private String orderDate, orderTime, id;
    private double total, discount, orderTotal;
    private ArrayList<ProductInCart> list;

    public Order(String orderDate, String orderTime, String id, double total, double discount, double orderTotal, ArrayList<ProductInCart> list) {
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.id = id;
        this.total = total;
        this.discount = discount;
        this.orderTotal = orderTotal;
        this.list = list;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public ArrayList<ProductInCart> getList() {
        return list;
    }

    public void setList(ArrayList<ProductInCart> list) {
        this.list = list;
    }
}

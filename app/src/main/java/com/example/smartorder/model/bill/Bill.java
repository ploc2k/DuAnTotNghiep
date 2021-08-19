package com.example.smartorder.model.bill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bill {
    @SerializedName("dateBill")
    @Expose
    private String dateBill;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("billCode")
    @Expose
    private String billCode;
    @SerializedName("nameCashier")
    @Expose
    private String nameCashier;
    @SerializedName("tableCode")
    @Expose
    private Integer tableCode;
    @SerializedName("totalPrice")
    @Expose
    private Integer totalPrice;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("nameOrder")
    @Expose
    private String nameOrder;
    @SerializedName("discount")
    @Expose
    private int discount;


    public String getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(String nameOrder) {
        this.nameOrder = nameOrder;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDateBill() {
        return dateBill;
    }

    public void setDateBill(String dateBill) {
        this.dateBill = dateBill;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getNameCashier() {
        return nameCashier;
    }

    public void setNameCashier(String nameCashier) {
        this.nameCashier = nameCashier;
    }

    public Integer getTableCode() {
        return tableCode;
    }

    public void setTableCode(Integer tableCode) {
        this.tableCode = tableCode;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public Bill(String dateBill, String id, String billCode, String nameCashier, Integer tableCode, Integer totalPrice, String status) {
//        this.dateBill = dateBill;
//        this.id = id;
//        this.billCode = billCode;
//        this.nameCashier = nameCashier;
//        this.tableCode = tableCode;
//        this.totalPrice = totalPrice;
//        this.status = status;
//    }

    public Bill(String dateBill, String id, String billCode, String nameCashier, Integer tableCode, Integer totalPrice, String status, String nameOrder, int discount) {
        this.dateBill = dateBill;
        this.id = id;
        this.billCode = billCode;
        this.nameCashier = nameCashier;
        this.tableCode = tableCode;
        this.totalPrice = totalPrice;
        this.status = status;
        this.nameOrder = nameOrder;
        this.discount = discount;
    }
}

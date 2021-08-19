package com.example.smartorder.model.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListMenuOrder {
    @SerializedName("nameOrder")
    @Expose
    private String nameOrder;
    @SerializedName("tableCode")
    @Expose
    private Integer tableCodes;
    @SerializedName("list_menu")
    @Expose
    private List<MenuOrder> menuOrders;

    public Integer getTableCodes() {
        return tableCodes;
    }

    public void setTableCodes(Integer tableCodes) {
        this.tableCodes = tableCodes;
    }

    public List<MenuOrder> getMenuOrders() {
        return menuOrders;
    }

    public void setMenuOrders(List<MenuOrder> menuOrders) {
        this.menuOrders = menuOrders;
    }

    public String getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(String nameOrder) {
        this.nameOrder = nameOrder;
    }
}

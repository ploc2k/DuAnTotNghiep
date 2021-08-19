package com.example.smartorder.api;

import com.example.smartorder.model.bill.Bill;
import com.example.smartorder.model.bill.BillOne;
import com.example.smartorder.model.login.Auth;
import com.example.smartorder.model.menu.ListBillUpdate;
import com.example.smartorder.model.menu.ListMenuOrder;
import com.example.smartorder.model.menu.Menu;
import com.example.smartorder.model.menu.MenuOrder;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.model.table.Table;
import com.example.smartorder.model.user.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitAPI {

    //All
    @FormUrlEncoded
    @PUT("user/update/changePass/{id}")
    Call<ServerResponse> changePass(@Path("id") String id,
                                    @Field("currentpass") String currentPass,
                                    @Field("newpass") String newPass);

    @Multipart
    @PUT("user/update/info/{id}")
    Call<ServerResponse> updateInfoNoImage(@Path("id") String id,
                                           @Part("fullName") String fullName,
                                           @Part("phone") String phone,
                                           @Part("age") Integer age,
                                           @Part("address") String address);

    @Multipart
    @PUT("user/update/info/{id}")
    Call<ServerResponse> updateInfo(@Path("id") String id,
                                    @Part("fullName") String fullName,
                                    @Part("phone") String phone,
                                    @Part("age") Integer age,
                                    @Part("address") String address,
                                    @Part MultipartBody.Part file);

    //Login
    @FormUrlEncoded
    @POST("login")
    Call<Auth> checkLogin(@Field("phone") String phone,
                          @Field("password") String passWord);


    //Menu
    @GET("menus")
    Call<List<Menu>> getAllMenu();

    @Multipart
    @POST("menu/create")
    Call<ServerResponse> createFood(
            @Part("name") String name,
            @Part("price") Integer price,
            @Part("type") String type,
            @Part MultipartBody.Part file
    );

    @Multipart
    @PUT("menu/update/{id}")
    Call<ServerResponse> updateDrink(
            @Path("id") String id,
            @Part("name") String name,
            @Part("status") boolean status,
            @Part("price") Integer price,
            @Part MultipartBody.Part file);

    @Multipart
    @PUT("menu/update/{id}")
    Call<ServerResponse> updateDrinkNoImage(
            @Path("id") String id,
            @Part("name") String name,
            @Part("status") boolean status,
            @Part("price") Integer price);

    @Multipart
    @PUT("menu/update/{id}")
    Call<ServerResponse> updateFood(
            @Path("id") String id,
            @Part("name") String name,
            @Part("price") Integer price,
            @Part MultipartBody.Part file);

    @Multipart
    @PUT("menu/update/{id}")
    Call<ServerResponse> updateFoodNoImage(
            @Path("id") String id,
            @Part("name") String name,
            @Part("price") Integer price
    );

    @Multipart
    @PUT("menu/update/{id}")
    Call<ServerResponse> updateOther(
            @Path("id") String id,
            @Part("name") String name,
            @Part("price") Integer price,
            @Part("status") boolean status,
            @Part MultipartBody.Part file);

    @Multipart
    @PUT("menu/update/{id}")
    Call<ServerResponse> updateOtherNoImage(
            @Path("id") String id,
            @Part("name") String name,
            @Part("status") boolean status,
            @Part("price") Integer price
    );

    @DELETE("menu/delete/{id}")
    Call<ServerResponse> deleteDrink(@Path("id") String id);

    @DELETE("menu/delete/{id}")
    Call<ServerResponse> deleteOther(@Path("id") String id);


    //User
    @GET("user")
    Call<List<User>> getAllUser();

    @GET("user/{id}")
    Call<User> getInfoUser(@Path("id") String id);

    @Multipart
    @POST("user/create")
    Call<ServerResponse> createUser(
            @Part("fullName") String fullName,
            @Part("phone") String phone,
            @Part("indentityCardNumber") Integer indentityCardNumber,
            @Part("age") Integer age,
            @Part("address") String address,
            @Part("role") String role,
            @Part MultipartBody.Part file
    );

    @Multipart
    @PUT("user/update/{id}")
    Call<ServerResponse> updateUser(
            @Path("id") String id,
            @Part("fullName") String fullName,
            @Part("phone") String phone,
            @Part("indentityCardNumber") Integer indentityCardNumber,
            @Part("age") Integer age,
            @Part("address") String address,
            @Part("role") String role,
            @Part MultipartBody.Part file
    );

    @Multipart
    @PUT("user/update/{id}")
    Call<ServerResponse> updateUserNoImage(
            @Path("id") String id,
            @Part("fullName") String fullName,
            @Part("phone") String phone,
            @Part("indentityCardNumber") Integer indentityCardNumber,
            @Part("age") Integer age,
            @Part("address") String address,
            @Part("role") String role
    );


    @DELETE("user/delete/{id}")
    Call<ServerResponse> deleteUser(@Path("id") String id);


    //Table
    @GET("table")
    Call<List<Table>> getAllTable();

    @FormUrlEncoded
    @POST("table/create")
    Call<ServerResponse> createTable(@Field("tableSeats") int tableSeats);

    @PUT("table/update/{id}")
    Call<ServerResponse> updateTable(@Path("id") String id,
                                     @Body Table table);

    @DELETE("table/delete/{id}")
    Call<ServerResponse> deleteTable(@Path("id") String id);


    //Bill
    @POST("bill/return")
    Call<ServerResponse> returnItems(@Body ListBillUpdate listBillUpdate);

    @GET("bill/listPaid")
    Call<List<Bill>> getListPaid();

    @GET("bill/billOne/{billCode}")
    Call<List<BillOne>> getDetailBill(@Path("billCode") String billCode);

    @FormUrlEncoded
    @POST("bill/paid/{billCode}")
    Call<ServerResponse> payBill(@Path("billCode") String billCode,
                                 @Field("nameCashier") String name,
                                 @Field("discount") Integer dis,
                                 @Field("totalMoney") Integer total);

    //Staff
    @GET("menus")
    Call<List<MenuOrder>> getAllMenuOrder();

    @POST("bill/create")
    Call<ServerResponse> createBill(@Body ListMenuOrder listMenuOrder);

    @GET("bill/billPreview/{tableCode}")
    Call<List<BillOne>> getBillPreview(@Path("tableCode") Integer tableCode);


    //Cashier
    @GET("bill/listUnpaid")
    Call<List<Bill>> getListUnpaid();

}

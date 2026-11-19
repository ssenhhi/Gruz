package com.example.gruz.network;

import com.example.gruz.models.CargoCreateRequest;
import com.example.gruz.models.CargoListResponse;
import com.example.gruz.models.CargoTypesListResponse;
import com.example.gruz.models.CitiesListResponse;
import com.example.gruz.models.LoginRequest;
import com.example.gruz.models.NotificationsListResponse;
import com.example.gruz.models.OrderCreateRequest;
import com.example.gruz.models.OrderResponse;
import com.example.gruz.models.OrdersListResponse;
import com.example.gruz.models.PaymentCreateRequest;
import com.example.gruz.models.PaymentListResponse;
import com.example.gruz.models.RegisterRequest;
import com.example.gruz.models.StatusHistoryListResponse;
import com.example.gruz.models.TokenResponse;
import com.example.gruz.models.UserResponse;
import com.example.gruz.models.UserUpdateRequest;
import com.example.gruz.models.UsersListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/collections/payments/records")
    Call<PaymentListResponse> getPaymentByOrder(@Query("filter") String filter);
    @GET("api/collections/users/records/{id}")
    Call<UserResponse> getUserById(@Path("id") String userId);
    @GET("api/collections/users/records")
    Call<UsersListResponse> getUserByEmail(@Query("filter") String filter);
    @POST("api/collections/users/records")
    Call<UserResponse> register(@Body RegisterRequest request);
    @POST("api/collections/users/auth-with-password")
    Call<TokenResponse> login(@Body LoginRequest request);
    @PATCH("api/collections/users/records/{id}")
    Call<UserResponse> updateUser(@Path("id") String userId, @Body UserUpdateRequest request);
    @GET("api/collections/users/records")
    Call<UsersListResponse> getAllUsers();
    @GET("api/collections/orders/records")
    Call<OrdersListResponse> getAllOrders();
    @POST("api/collections/cargos/records")
    Call<CargoCreateRequest> createCargo(@Body CargoCreateRequest request);
    @GET("api/collections/cargo_types/records")
    Call<CargoTypesListResponse> getCargoTypes();
    @POST("api/collections/orders/records")
    Call<OrderResponse> createOrder(@Body OrderCreateRequest request);
    @POST("api/collections/payments/records")
    Call<PaymentCreateRequest> createPayment(@Body PaymentCreateRequest request);
    @GET("api/collections/notifications/records")
    Call<NotificationsListResponse> getAllNotifications();
    @GET("api/collections/cargos/records")
    Call<CargoListResponse> getCargoByOrder(@Query("filter") String filter);
    @GET("api/collections/cities/records")
    Call<CitiesListResponse> getCities();
    @GET("api/collections/status_history/records")
    Call<StatusHistoryListResponse> getHistoryByOrder(@Query("filter") String filter);
}

package com.example.gruz;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/collections/users/records")
    Call<UserResponse> register(@Body RegisterRequest request);

    // Вход
    @POST("api/collections/users/auth-with-password")
    Call<TokenResponse> login(@Body LoginRequest request);

    @GET("api/collections/users/records/{id}")
    Call<UserResponse> getUser(@Path("id") String userId);
    @GET("api/collections/users/records")
    Call<UsersListResponse> getUserByEmail(@Query("filter") String filter);
    // Обновить данные пользователя
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
}

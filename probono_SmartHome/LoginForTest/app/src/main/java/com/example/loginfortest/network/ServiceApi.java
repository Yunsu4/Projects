package com.example.loginfortest.network;



import com.example.loginfortest.data.JoinData;
import com.example.loginfortest.data.JoinResponse;
import com.example.loginfortest.data.LoginData;
import com.example.loginfortest.data.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<JoinResponse> userJoin(@Body JoinData data);
}

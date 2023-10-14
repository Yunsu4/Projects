package com.example.total_test.network


import com.example.total_test.data.BluetoothData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface MyApiService {
    @FormUrlEncoded
    @POST("/receiveData") // 노드.js 서버의 POST 엔드포인트 URL
    fun sendDataToServer(@Field("bluetooth_data") data: BluetoothData): Call<Void>
}

package com.sourav.drinkitnewversion.Api;

import com.sourav.drinkitnewversion.Model.DataMessage;
import com.sourav.drinkitnewversion.Model.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {

    @Headers({

            "Content-Type:application/json",
            "Authorization:key=AAAA0lwkZVs:APA91bHfZHvgEwgolicmhtlGUjbuaz_5z11owSIdefv82q4OVUIJ-OIFuwiAIsxZq-otTToiuouRMw_ShtIDID9FAbPy4-dbt_CZLWWGyqvpJfTo6zotEq5P4pFMoyBiHkrRsn_9pU-5"

    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);
}
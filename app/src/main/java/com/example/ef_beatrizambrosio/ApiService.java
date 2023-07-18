package com.example.ef_beatrizambrosio;


import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("PublicHolidays/{year}/{country}")
    Call<List<Holiday>> getHolidays(@Path("year") int year, @Path("country") String country);
}

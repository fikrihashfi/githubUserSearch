package com.fikrihashfi.githubusersearch.api;

import com.fikrihashfi.githubusersearch.model.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("search/users")
    Call <Data> getUsers (@Query("q") String keyword, @Query("page") int page, @Query("per_page") int per_page);

}

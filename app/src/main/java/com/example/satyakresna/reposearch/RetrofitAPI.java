package com.example.satyakresna.reposearch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by satyakresna on 02-Jul-17.
 */

public interface RetrofitAPI {
    @GET("/repositories")
    Call<ArrayList<Repository>> retrieveRepositories();
}

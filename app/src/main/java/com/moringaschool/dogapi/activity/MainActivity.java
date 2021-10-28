package com.moringaschool.dogapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.moringaschool.dogapi.R;
import com.moringaschool.dogapi.adapter.MoviesAdapter;
import com.moringaschool.dogapi.model.Movie;
import com.moringaschool.dogapi.model.MovieApiService;
import com.moringaschool.dogapi.model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static  final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit ;
    private RecyclerView recyclerView = null;

    //the movie api key
    private final static String API_KEY = "236c0612a9da20170b62603163bb93f9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        connectAndGetApiData();
    }

    private void connectAndGetApiData() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<MovieResponse> call = movieApiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
                Log.d(TAG,"Number of Movies Received:" + movies.size());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
//                Log.e(TAG, throwable.toString());
            }
        });
    }
}
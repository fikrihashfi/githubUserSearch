package com.fikrihashfi.githubusersearch.ui.user;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.fikrihashfi.githubusersearch.R;
import com.fikrihashfi.githubusersearch.api.ApiClient;
import com.fikrihashfi.githubusersearch.api.ApiInterface;
import com.fikrihashfi.githubusersearch.model.Data;
import com.fikrihashfi.githubusersearch.model.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private ArrayList<Users> listUsers = new ArrayList<>();
    private boolean rateLimit = false;

    public boolean query(final Context context, final UserAdapter userAdapter, final String command, final String keyword, final int page, final int per_page) {
        if (command == "new") {
            listUsers.clear();
        }
        if (rateLimit == false) {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Data> call = apiInterface.getUsers(keyword, page, per_page);
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                    int statusCode = response.code();
                    if (statusCode == 200) {
                        if(response.body().getTotalCount()==listUsers.size()){
                            Toast.makeText(context, R.string.last_result, Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (response.body().getTotalCount() > 0) {
                                Data listUser = response.body();
//                Log.d("user", listUser.getItems().get(0).getLogin());
                                for (int i = 0; i < listUser.getSize(); i++) {
                                    Users user = new Users();
                                    user.setLogin(listUser.getItems().get(i).getLogin());
                                    user.setAvatarUrl(listUser.getItems().get(i).getAvatarUrl());
                                    listUsers.add(user);
                                }
                            } else {
                                Toast.makeText(context, R.string.empty, Toast.LENGTH_LONG).show();
                            }
                            userAdapter.setListUsers(getUsers());
                        }
                    } else {
                        rateLimit = true;
                        Log.d("onFailure", response.message());
                        Toast.makeText(context, response.message() + ", wait for 15 seconds and try again.", Toast.LENGTH_LONG).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rateLimit = false;
                            }
                        }, 15000);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Data> call, @NonNull Throwable error) {
                    Log.d("onFailure", error.getMessage());
                    Toast.makeText(context, R.string.cek_koneksi, Toast.LENGTH_LONG).show();
                }
            });
        }
        return true;
    }

    public ArrayList<Users> getUsers() {
        return listUsers;
    }

}

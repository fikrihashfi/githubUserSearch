package com.fikrihashfi.githubusersearch;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fikrihashfi.githubusersearch.model.Users;
import com.fikrihashfi.githubusersearch.ui.user.UserAdapter;
import com.fikrihashfi.githubusersearch.ui.user.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private UserViewModel userViewModel;
    private UserAdapter userAdapter;
    private ArrayList<Users> users = new ArrayList<>();
    private RecyclerView rvUsers;
    private int page = 1;
    private int per_page = 10;
    private String query = null;
    private boolean loading = false;
    private final String STATE_LIST = "state_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvUsers = findViewById(R.id.rv_users);
        progressBar = findViewById(R.id.progressBar);
        userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
        if (savedInstanceState == null) {
            showRecyclerList(savedInstanceState);
        } else {
            ArrayList<Users> stateList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            if (stateList != null) {
                users.addAll(stateList);
            }
            showRecyclerList(savedInstanceState);
        }
        scrollListener();
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (androidx.appcompat.widget.SearchView) (menu.findItem(R.id.menu_search)).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                page=1;
                setQuery(query);
                showLoading(true);
                userViewModel.query(getApplicationContext(), userAdapter, "new", query, page, per_page);
                showLoading(false);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.clearFocus();
        return true;
    }


    private void showRecyclerList(Bundle savedInstanceState){
        if(savedInstanceState==null) userAdapter = new UserAdapter();
        else userAdapter = new UserAdapter(users);
        rvUsers.setAdapter(userAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);
        userAdapter.setOnItemClickCallback(new UserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Users data) {
                showSelectedUsers(data);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_LIST, userAdapter.getList());
    }

    private void showSelectedUsers(Users user) {
        Toast.makeText(getApplicationContext(), "Kamu memilih " + user.getLogin(), Toast.LENGTH_SHORT).show();
    }

    public void showLoading(Boolean state) {
        if (state) {
            loading = true;
            progressBar.setVisibility(View.VISIBLE);
        } else {
            loading = false;
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setQuery(String q) {
        this.query = q;
    }

    private void scrollListener() {
        final MainActivity that = this;
        rvUsers.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = Objects.requireNonNull(recyclerView.getLayoutManager()).getItemCount();
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int visibleThreshold = 0;
                if(totalItemCount > visibleItemCount) {
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                                page++;
                                userViewModel.query(getApplicationContext(), userAdapter, "load", query, page, per_page);
                            }
                }
            }
        });

    }


}
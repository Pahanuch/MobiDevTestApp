package ua.tykhonovp.mobidev.testapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.tykhonovp.mobidev.testapp.adapter.BooksAdapter;
import ua.tykhonovp.mobidev.testapp.model.BookList;
import ua.tykhonovp.mobidev.testapp.rest.IServiceEndPoint;
import ua.tykhonovp.mobidev.testapp.utils.EndlessRecyclerViewScrollListener;

public class MainActivity extends AppCompatActivity implements Callback<BookList> {

    private int COLUMN_PORTRAIT = 2;
    private int COLUMN_LANDSCAPE = 3;

    private int MAX_REZULTS = 20;

    private String mSearchString;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private BookList bookList;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private BooksAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList = new BookList();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                // Clear the array of data
                bookList.items.clear();
                scrollListener.resetState();
                doSearch(0);
            }
        });

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(COLUMN_PORTRAIT, StaggeredGridLayoutManager.VERTICAL);
        } else{
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(COLUMN_LANDSCAPE, StaggeredGridLayoutManager.VERTICAL);
        }

        scrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                doSearch(totalItemsCount);

            }
        };

        mAdapter = new BooksAdapter(this, bookList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            staggeredGridLayoutManager.setSpanCount(COLUMN_LANDSCAPE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            staggeredGridLayoutManager.setSpanCount(COLUMN_PORTRAIT);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction()) ) {

            mSearchString = intent.getStringExtra(SearchManager.QUERY);
            // Clear the array of data
            bookList.items.clear();
            // Reset endless scroll listener when performing a new search
            scrollListener.resetState();

            doSearch(0);
        }
    }


    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        mAdapter.notifyDataSetChanged();
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void doSearch(int offset) {

        // Create a REST adapter which points the Google Books API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our Google Books API interface.
        IServiceEndPoint googleBooks = retrofit.create(IServiceEndPoint.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<BookList> call = googleBooks.contributors(mSearchString, offset, MAX_REZULTS);
        //asynchronous call
        call.enqueue(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        if (mSearchString != null && !mSearchString.isEmpty()) {
            searchMenuItem.expandActionView();
            searchView.setQuery(mSearchString, true);
            searchView.clearFocus();
        }
        return true;
    }

    @Override
    public void onResponse(Call<BookList> call, Response<BookList> response) {

        bookList.items.addAll(response.body().items);
        onItemsLoadComplete();
    }

    @Override
    public void onFailure(Call<BookList> call, Throwable t) {
        showAlert(t.getLocalizedMessage());
        // Stop refresh animation
        onItemsLoadComplete();
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Error!")
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

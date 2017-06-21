package com.awoisoak.giphyviewer;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;

import com.awoisoak.giphyviewer.source.Gif;
import com.awoisoak.giphyviewer.source.remote.GiphyListener;
import com.awoisoak.giphyviewer.source.remote.GiphyManager;
import com.awoisoak.giphyviewer.source.remote.responses.ErrorResponse;
import com.awoisoak.giphyviewer.source.remote.responses.ListsGifsResponse;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //TODO look for Post,awoisoak,WP strings


    @BindView(R.id.imageview1) ImageView imageView1;
    @BindView(R.id.imageview2) ImageView imageView2;
    @BindView(R.id.toolbar) Toolbar toolbar;

    Context mContext = this;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_36dp);
        setSupportActionBar(toolbar);
        //        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);




        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                GiphyManager.getInstance().trending(new GiphyListener<ListsGifsResponse>() {
                    @Override
                    public void onResponse(ListsGifsResponse response) {
                        final List<Gif> list = response.getList();
                        System.out.println("awoooooo trending response gif.url =" + list.get(0).getUrl());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(mContext)
                                        .load(list.get(0).getUrl())
                                        .into(imageView2);
                            }
                        });

                    }

                    @Override
                    public void onError(ErrorResponse error) {
                        System.out.println("awoooooo trending onError =" + error.getMessage());

                    }
                });
            }
        });
        t2.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView)  menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        //        searchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GiphyManager.getInstance().search(query, 0, new GiphyListener<ListsGifsResponse>() {
                    @Override
                    public void onResponse(ListsGifsResponse response) {
                        final List<Gif> list = response.getList();
                        System.out.println("awoooooo search response gif.url =" + list.get(0).getUrl());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(mContext)
                                        .load(list.get(0).getUrl())
                                        .into(imageView1);
                            }
                        });


                    }

                    @Override
                    public void onError(ErrorResponse error) {
                        System.out.println("awoooooo search onError =" + error.getMessage());

                    }
                });
            }
        });
        t.start();

        mSearchView.setQuery("", false);
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

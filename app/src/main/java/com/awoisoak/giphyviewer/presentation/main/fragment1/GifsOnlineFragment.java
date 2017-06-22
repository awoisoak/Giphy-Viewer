package com.awoisoak.giphyviewer.presentation.main.fragment1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.GiphyListener;
import com.awoisoak.giphyviewer.data.remote.GiphyManager;
import com.awoisoak.giphyviewer.data.remote.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.responses.ListsGifsResponse;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;
import com.awoisoak.giphyviewer.presentation.main.fragment1.dagger.DaggerGifsOnlineComponent;
import com.awoisoak.giphyviewer.presentation.main.fragment1.dagger.GifsOnlineModule;

import java.util.List;

import javax.inject.Inject;


/**
 * A Fragment to display online gifs
 *
 * Req:
 * - Contains a search bar at the top.
 * - Contains a recycler view that displays searched gifs.
 * - Loading indicator while searching.
 * - The default items in the recycler view should be the trending gifs.
 */
public class GifsOnlineFragment extends Fragment implements GifsOnlineView,SearchView.OnQueryTextListener{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private SearchView mSearchView;

    Snackbar mSnackbar;
    @Inject
    GifsOnlinePresenter mPresenter;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static GifsOnlineFragment newInstance(int sectionNumber) {
        GifsOnlineFragment fragment = new GifsOnlineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        DaggerGifsOnlineComponent.builder()
                .giphyApiComponent(((GiphyViewerApplication) getActivity().getApplication()).getWPAPIComponent())
                .gifsOnlineModule(new GifsOnlineModule(this))
                .build().inject(this);

        mPresenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intro1, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        System.out.println("awooooooo onQueryTextSubmit = "+query);
        //TODO call the corresponding interactor
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GiphyManager.getInstance().search(query, 0, new GiphyListener<ListsGifsResponse>() {
                    @Override
                    public void onResponse(ListsGifsResponse response) {
                        final List<Gif> list = response.getList();
                        System.out.println("awoooooo search response gif.url =" + list.get(0).getUrl());

                        //                        runOnUiThread(new Runnable() {
                        //                            @Override
                        //                            public void run() {
                        //                                Glide.with(mContext)
                        //                                        .load(list.get(0).getUrl())
                        //                                        .into(imageView1);
                        //                            }
                        //                        });


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

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void bindPostsList(List<Gif> gifs) {

    }

    @Override
    public void updatePostsList(List<Gif> gifs) {

    }

    @Override
    public void showLoadingSnackbar() {

    }

    @Override
    public void showErrorSnackbar() {

    }

    @Override
    public void hideSnackbar() {

    }

    @Override
    public void setGifAsFavourite(boolean favourite) {

    }
}
package com.awoisoak.giphyviewer.presentation.main.onlinefragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsPresenter;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsView;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.dagger.DaggerOnlineGifsComponent;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.dagger.OnlineGifsModule;
import com.awoisoak.giphyviewer.utils.threading.ThreadPool;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A Fragment to display online gifs
 * <p>
 * Req:
 * - Contains a search bar at the top.
 * - Contains a recycler view that displays searched gifs.
 * - Loading indicator while searching.
 * - The default items in the recycler view should be the trending gifs.
 */

public class OnlineGifsFragment extends Fragment
        implements OnlineGifsView, SearchView.OnQueryTextListener, OnlineGifsAdapter.GifItemClickListener {
    public static String TAG = OnlineGifsFragment.class.getSimpleName();

    private static final String ARG_SECTION_NUMBER = "section_number";

    @BindView(R.id.online_gifs_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.online_gifs_recycler) RecyclerView mRecyclerView;
    @BindView(R.id.online_gifs_text_view) TextView mLoadingText;
    private Snackbar mSnackbar;
    private SearchView mSearchView;
    private LinearLayoutManager mLayoutManager;
    OnlineGifsAdapter mAdapter;
    @Inject
    OnlineGifsPresenter mPresenter;
    private String mQuery;


    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static OnlineGifsFragment newInstance(int sectionNumber) {
        OnlineGifsFragment fragment = new OnlineGifsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.online_gifs_fragment, container, false);
        ButterKnife.bind(this, rootView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        addOnScrollListener();
        initDagger();
        mPresenter.onCreate();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    /**
     * Method to detect when the RecyclerView bottom is reached
     */
    private void addOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if (!mRecyclerView.canScrollVertically(1)) {
                        mPresenter.onBottomReached();
                    }
                }
            }
        });
    }

    private void initDagger() {
        DaggerOnlineGifsComponent.builder()
                .giphyApiComponent(((GiphyViewerApplication) getActivity().getApplication()).getWPAPIComponent())
                .onlineGifsModule(new OnlineGifsModule(this))
                .build().inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("awooooooo onQueryTextSubmit = " + query);
        mQuery = query;
        mPresenter.onSearchSubmitted(query);
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
        mProgressBar.setVisibility(View.GONE);
        mLoadingText.setVisibility(View.GONE);
    }

    @Override
    public void bindGifsList(List<Gif> gifs) {
        Log.d(TAG, "awoooooo | OnlineGifsFragment | bindPostList");
        mAdapter = new OnlineGifsAdapter(gifs, this, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateGifsList(List<Gif> gifs) {
        Log.d(TAG, "awoooooo | OnlineGifsFragment | updatePostList");

        if (mAdapter != null) {
            /**
             * We execute like this because of the next bug
             * http://stackoverflow.com/questions/39445330/cannot-call-notifyiteminserted-method-in-a-scroll-callback-recyclerview-v724-2
             */
            mRecyclerView.post(new Runnable() {
                public void run() {
                    /**
                     * We don't use notifyItemRangeInserted because we keep replicating this known Android bug
                     * https://issuetracker.google.com/issues/37007605
                     */
                    //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - posts.size(), posts.size());
                    mAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Log.d(TAG, "awoooooo | OnlineGifsFragment | updatePostGallery | mAdapter is null!");
        }
    }

    @Override
    public void showLoadingSnackbar() {
        mSnackbar = Snackbar
                .make(mRecyclerView, getResources().getString(R.string.loading_gifs), Snackbar.LENGTH_INDEFINITE);
        mSnackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
        mSnackbar.show();
    }

    @Override
    public void showErrorSnackbar() {
        mSnackbar = Snackbar.make(mRecyclerView, getResources().getString(R.string.error_network_connection),
                                  Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackbar.dismiss();
                showLoadingSnackbar();
                mPresenter.onRetrySearchGifsRequest();
            }
        });
        mSnackbar.show();
    }

    @Override
    public void hideSnackbar() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }


    @Override
    public String getSearchText() {
        return mQuery;
    }

    @Override
    public void showtoast(final String message) {
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFavouriteGifItemClick(Gif gif) {
        mPresenter.onGifSetAsFavourite(gif);
    }
}
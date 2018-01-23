package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsPresenter;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsView;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger.DaggerOfflineGifsComponent;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger.OfflineGifsModule;
import com.awoisoak.giphyviewer.utils.threading.ThreadPool;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder for Second Fragment.
 * Req:
 * - Contains a recycler view that displays a grid of favourite gifs stored locally.
 */
public class OfflineGifsFragment extends Fragment implements OfflineGifsView, OfflineGifsAdapter.GifItemClickListener {

    @BindView(R.id.offline_gifs_recycler) RecyclerView mRecyclerView;
    public static final String TAG = OfflineGifsFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int GRID_COLUMNS = 2;
    private GridLayoutManager mLayoutManager;
    private OfflineGifsAdapter mAdapter;
    @Inject OfflineGifsPresenter mPresenter;

    public OfflineGifsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static OfflineGifsFragment newInstance(int sectionNumber) {
        OfflineGifsFragment fragment = new OfflineGifsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDagger();
        mPresenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.offline_gifs_fragment, container, false);
        ButterKnife.bind(this, rootView);
        mLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMNS, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addOnScrollListener();
        mPresenter.onCreateView();
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
        DaggerOfflineGifsComponent.builder()
                .repositoryComponent(((GiphyViewerApplication) getActivity().getApplication()).getRepositoryComponent())
                .offlineGifsModule(new OfflineGifsModule(this))
                .build().inject(this);
    }


    @Override
    public void bindGifsList(final List<Gif> gifs) {
        final OfflineGifsFragment f = this;
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = new OfflineGifsAdapter(gifs, f, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }
        });

    }

    @Override
    public void updateGifsList(List<Gif> gifs) {

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
                    mAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Log.e(TAG, "updateGifList | mAdapter is null!");
        }
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
        mPresenter.onUnsetGifAsFavourite(gif);
    }
}

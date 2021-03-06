package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineViewModel;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineViewModelFactory;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger.DaggerOfflineGifsComponent;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger.OfflineGifsModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder for Second Fragment.
 * Req:
 * - Contains a recycler view that displays a grid of favourite gifs stored locally.
 */
public class OfflineGifsFragment extends Fragment implements
        OfflineGifsPagedListAdapter.GifItemClickListener {

    @BindView(R.id.offline_gifs_recycler)
    RecyclerView mRecyclerView;
    public static final String TAG = "awoooo" + OfflineGifsFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int GRID_COLUMNS = 2;
    private GridLayoutManager mLayoutManager;
    private OfflineGifsPagedListAdapter mAdapter;
    OfflineViewModel mOfflineViewModel;
    @Inject
    LocalRepository mLocalRepository;

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
        initViewModel();
        observeData();
    }

    private void initViewModel() {
        mOfflineViewModel = ViewModelProviders.of(this,
                new OfflineViewModelFactory(mLocalRepository)).get(OfflineViewModel.class);
    }

    private void observeData() {
        mAdapter = new OfflineGifsPagedListAdapter(this, this.getActivity());
        mOfflineViewModel.gifList.observe(this, new Observer<PagedList<Gif>>() {
            @Override
            public void onChanged(@Nullable PagedList<Gif> gifs) {
                mAdapter.setList(gifs);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.offline_gifs_fragment, container, false);
        ButterKnife.bind(this, rootView);
        mLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMNS,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

//        addOnScrollListener();
        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initDagger() {
        DaggerOfflineGifsComponent.builder()
                .localRepositoryComponent(
                        ((GiphyViewerApplication) getActivity().getApplication())
                                .getLocalRepositoryComponent())
                .offlineGifsModule(new OfflineGifsModule(this))
                .build().inject(this);
    }


    @Override
    public void onFavouriteGifItemClick(Gif gif) {
        mOfflineViewModel.onUnsetGifAsFavourite(gif);
    }
}

package com.awoisoak.giphyviewer.presentation.main.onlinefragment.impl;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.Resource;
import com.awoisoak.giphyviewer.data.Status;
import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineViewModel;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineViewModelFactory;
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
        implements SearchView.OnQueryTextListener,
        OnlineGifsAdapter.GifItemClickListener, FavouriteListener {

    @BindView(R.id.online_gifs_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.online_gifs_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.online_gifs_text_view)
    TextView mLoadingText;
    private static final String TAG = "awoooo" + OnlineGifsFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final Object LOCK = new Object();
    private Snackbar mSnackbar;
    private SearchView mSearchView;
    private LinearLayoutManager mLayoutManager;
    private OnlineGifsAdapter mAdapter;
    private boolean isFirstSearchRequest = true;


    OnlineViewModel mOnlineViewModel;
    @Inject
    LocalRepository mLocalRepository;
    @Inject
    GiphyApi mRemoteRepository;
    private List<Gif> mFavouriteGifs;


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
        initDagger();
        initViewModel();
        observeData();

    }

    private void initViewModel() {
        mOnlineViewModel = ViewModelProviders.of(this,
                new OnlineViewModelFactory(mLocalRepository, mRemoteRepository)).get(
                OnlineViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.online_gifs_fragment, container, false);
        ButterKnife.bind(this, rootView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        addOnScrollListener();
        return rootView;
    }


    private void observeData() {
        mOnlineViewModel.getSearchedGifs().observe(this, new Observer<Resource<List<Gif>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Gif>> resource) {
                final List<Gif> gifs = resource.data;
                Log.d(TAG, "getSearchedGifs | onChanged" + gifs.size() +
                        "| status =" + resource.status);

                if (gifs.size() == 0 && !resource.status.equals(Status.LOADING)) {
                    showToast(getResources().getString(R.string.no_gifs_found));
                    return;
                }

                switch (resource.status) {
                    case LOADING:
                        Log.d(TAG, "Searched | onChanged | LOADING data");
                        showLoadingSnackbar();
                        break;
                    case SUCCESS:
                        Log.d(TAG, "Searched | onChanged | SUCCESS size = " + gifs.size());
                        ThreadPool.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFirstSearchRequest) {
                                    bindGifsList(gifs);
                                    isFirstSearchRequest = false;
                                } else {
                                    updateGifsList(gifs);
                                }
                                hideSnackbar();
                                hideProgressBar();
                            }
                        });
                        break;

                    case ERROR:
                        Log.d(TAG, "Searched | onChanged | ERROR size = " + gifs.size());
                        onErrorResponse();
                        break;
                }
            }
        });


        mOnlineViewModel.getTrendingGifs().observe(this, new Observer<Resource<List<Gif>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Gif>> resource) {
                final List<Gif> gifs = resource.data;
                Log.d(TAG, "getTrendingGifs | onChanged" + gifs.size());

                if (gifs.size() == 0) {
                    showToast(getResources().getString(R.string.no_gifs_found));
                    return;
                }

                switch (resource.status) {
                    case LOADING:
                        Log.d(TAG, "Trending | onChanged | LOADING data ");
                        showLoadingSnackbar();
                        break;
                    case SUCCESS:
                        Log.d(TAG, "Trending | onChanged | SUCCESS size = " + gifs.size());
                        ThreadPool.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bindGifsList(gifs);
                                hideSnackbar();
                                hideProgressBar();
                            }
                        });
                        break;
                    case ERROR:
                        Log.d(TAG, "Trending | onChanged | ERROR size = " + gifs.size());
                        onErrorResponse();
                        break;
                }
            }
        });

        mOnlineViewModel.getGifsSavedInDB().observe(this, new Observer<List<Gif>>() {
            @Override
            public void onChanged(@Nullable List<Gif> gifs) {
                Log.d(TAG, "getGifsSavedInDB  = " + gifs.size());
                mFavouriteGifs = gifs;

            }
        });
    }

    private void onErrorResponse() {
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
                showErrorSnackbar();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                        mOnlineViewModel.onBottomReached();
                    }
                }
            }
        });
    }

    private void initDagger() {
        DaggerOnlineGifsComponent.builder()
                .localRepositoryComponent(
                        ((GiphyViewerApplication) getActivity().getApplication())
                                .getLocalRepositoryComponent())
                .giphyApiComponent(
                        ((GiphyViewerApplication) getActivity().getApplication())
                                .getGiphyApiComponent())
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
        mOnlineViewModel.onSearchSubmitted(query);
        mSearchView.setQuery("", false);
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mLoadingText.setVisibility(View.GONE);
    }

    public void bindGifsList(List<Gif> gifs) {
        mAdapter = new OnlineGifsAdapter(gifs, this, this, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateGifsList(List<Gif> gifs) {
        mAdapter.updateGifList(gifs);
    }


    public void showLoadingSnackbar() {
        mSnackbar = Snackbar.make(mRecyclerView, getResources().getString(R.string.loading_gifs),
                Snackbar.LENGTH_INDEFINITE);
        mSnackbar.getView().setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.black));
        mSnackbar.show();
    }

    public void showErrorSnackbar() {
        mSnackbar = Snackbar.make(mRecyclerView,
                getResources().getString(R.string.error_network_connection),
                Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackbar.dismiss();
                showLoadingSnackbar();
                mOnlineViewModel.onRetryGifsRequest();
            }
        });
        mSnackbar.show();
    }

    public void hideSnackbar() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }



    public void showToast(final String message) {
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onFavouriteGifItemClick(View v, final Gif gif, final int position) {
        synchronized (LOCK) {
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    if (!isAlreadyFavourite(gif)) {
                        mLocalRepository.addGif(gif);
                    } else {
                        mLocalRepository.removeGif(gif.getServerId());
                    }
                    chooseFavouriteIcon(gif, position);
                }
            });
        }

    }


    public boolean isAlreadyFavourite(Gif gif) {
        boolean wasAlreadyFavourite = false;

        synchronized (LOCK) {
            for (Gif favGif : mFavouriteGifs) {
                if (favGif.getServerId().equals(gif.getServerId())) {
                    wasAlreadyFavourite = true;
                }
            }
        }
        return wasAlreadyFavourite;
    }

    private void chooseFavouriteIcon(final Gif gif, final int position) {
        /**
         * We delay the task in order to the favourite icon to be updated correctly. At this
         * stage might be many tasks running on the UI thread (the adapter and the list might not be correctly synchronized yet) what was making
         * the icon to not be changed directly.
         */
        ThreadPool.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable regIcon = getResources().getDrawable(R.drawable.ic_grade_white_48dp);
                Drawable favIcon = getResources().getDrawable(
                        R.drawable.rate_star_big_on_holo_dark);

                if (isAlreadyFavourite(gif)) {
                    ((ImageView) mRecyclerView.findViewById(
                            R.id.item_online_gifs_favourite_button)).setImageDrawable(favIcon);
                } else {
                    ((ImageView) mRecyclerView.findViewById(
                            R.id.item_online_gifs_favourite_button)).setImageDrawable(regIcon);
                }
                mAdapter.notifyDataSetChanged();

            }
        }, 200);

    }

    @Override
    public boolean isFavourite(Gif gif) {
        return isAlreadyFavourite(gif);
    }


}
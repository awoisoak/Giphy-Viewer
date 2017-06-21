package com.awoisoak.giphyviewer.presentation.main.fragment1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.GiphyListener;
import com.awoisoak.giphyviewer.data.remote.GiphyManager;
import com.awoisoak.giphyviewer.data.remote.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.responses.ListsGifsResponse;

import java.util.List;

import butterknife.BindView;

/**
 * A placeholder for First Fragment.
 *
 * Req:
 * - Contains a search bar at the top.
 * - Contains a recycler view that displays searched gifs.
 * - Loading indicator while searching.
 * - The default items in the recycler view should be the trending gifs.
 */
public class PlaceholderFragment1 extends Fragment implements SearchView.OnQueryTextListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SearchView mSearchView;

    //        @BindView(R.id.toolbar) Toolbar mToolbar;

    public PlaceholderFragment1() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment1 newInstance(int sectionNumber) {
        PlaceholderFragment1 fragment = new PlaceholderFragment1();
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
        //TODO move it to fragment1?
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

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        System.out.println("awooooo Fragment1 | onHiddenChanged to = "+hidden);
//        super.onHiddenChanged(hidden);
//        if (!hidden){
//            TextView tvTitle =(TextView)getActivity().findViewById(R.id.toolbar_title);
//            tvTitle.setText("PlaceHolder1");
//        }
//    }

}
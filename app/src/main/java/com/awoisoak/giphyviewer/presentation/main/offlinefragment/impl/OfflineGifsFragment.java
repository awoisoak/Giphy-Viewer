package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsView;

import java.util.List;

/**
 * A placeholder for Second Fragment.
 * Req:
 * - Contains a recycler view that displays a grid of favourite gifs stored locally.
 */
public  class OfflineGifsFragment extends Fragment implements OfflineGifsView{

    private static final String ARG_SECTION_NUMBER = "section_number";

    public OfflineGifsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OfflineGifsFragment newInstance(int sectionNumber) {
        OfflineGifsFragment fragment = new OfflineGifsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.offline_gifs_fragment, container, false);
        return rootView;
    }


    @Override
    public void hideProgressBar() {

    }

    @Override
    public void bindGifsList(List<Gif> gifs) {

    }

    @Override
    public void updateGifsList(List<Gif> gifs) {

    }

    @Override
    public void showtoast(String message) {

    }
}

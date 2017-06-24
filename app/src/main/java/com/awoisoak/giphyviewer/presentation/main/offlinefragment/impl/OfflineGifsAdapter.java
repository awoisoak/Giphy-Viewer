package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfflineGifsAdapter extends RecyclerView.Adapter<OfflineGifsAdapter.GifViewHolder> {

    private List<Gif> mGifs;
    private GifItemClickListener mListener;
    private Context mContext;

    /**
     * Listener to detect when the user click on a Gif item
     */
    public interface GifItemClickListener {
        void onFavouriteGifItemClick(Gif Gif);
    }

    public OfflineGifsAdapter(List<Gif> gifs, GifItemClickListener listener, Context context) {
        mGifs = gifs;
        mListener = listener;
        mContext = context;
    }

    @Override
    public GifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_offline_gifs_list, parent, false);
        return new GifViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(GifViewHolder holder, int position) {
        holder.bindGif(mGifs.get(position));
    }

    @Override
    public int getItemCount() {
        return mGifs.size();
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }


    public class GifViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_offline_gif_image) ImageView ivGif;
        @BindView(R.id.item_offline_gifs_favourite_button) ImageView favouriteButton;

        public GifViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            favouriteButton.setOnClickListener(this);
        }

        public void bindGif(Gif gif) {
            if (gif.getUrl().equals("") || gif.getUrl() == null) {
                ivGif.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_black_48dp));
            }
            Glide.with(mContext).load(gif.getUrl()).into(ivGif);
        }

        @Override
        public void onClick(View v) {
            mListener.onFavouriteGifItemClick(mGifs.get(getAdapterPosition()));
        }
    }
}

package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
//TODO Use the DiffUtil as we did in the OnlineGifsAdapter
public class OfflineGifsAdapter extends RecyclerView.Adapter<OfflineGifsAdapter.GifViewHolder> {
    public static String TAG = "awoooo"+OfflineGifsAdapter.class.getSimpleName();

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
        Log.d(TAG,"mGifs is changed!! | OfflineGifsAdapter constructor | size = "+mGifs.size());

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

    public void updateGifList(final List<Gif> gifs) {
        if (mGifs == null) {
            mGifs = gifs;
            Log.d(TAG,"mGifs is changed!! | mGifs was null | size = "+mGifs.size());
            notifyItemRangeInserted(0, gifs.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mGifs.size();
                }

                @Override
                public int getNewListSize() {
                    return gifs.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    /**
                     * localId is only generated when Room create the objects so it is 0 when the
                     * gifs are requested to the server. Because of that we compare them with the
                     * server ID
                     */
                    return mGifs.get(oldItemPosition).getServerId() ==
                            gifs.get(newItemPosition).getServerId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Gif newGif = gifs.get(newItemPosition);
                    Gif oldGif = mGifs.get(oldItemPosition);
                    return Objects.equals(newGif.getServerId(), oldGif.getServerId())
                            && Objects.equals(newGif.getUrl(), oldGif.getUrl());
                }
            }, true);//if tru items can be moved when we remove them
            mGifs = gifs;
            Log.d(TAG,"mGifs updated where it is supposed to be | size = "+mGifs.size());
            result.dispatchUpdatesTo(this);
            /*
             For some reason in this case the dispatchUpdatesTo was not enough to update the list
             */
            notifyDataSetChanged();
        }
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

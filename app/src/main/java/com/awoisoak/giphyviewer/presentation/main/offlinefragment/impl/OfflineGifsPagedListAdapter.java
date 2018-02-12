package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapterHelper;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfflineGifsPagedListAdapter extends
        RecyclerView.Adapter<OfflineGifsPagedListAdapter.GifViewHolder> {
    public static String TAG = "awoooo" + OfflineGifsPagedListAdapter.class.getSimpleName();
    private GifItemClickListener mListener;
    private Context mContext;
    private final PagedListAdapterHelper<Gif> mHelper
            = new PagedListAdapterHelper(this, DIFF_CALLBACK);

    /**
     * Listener to detect when the user click on a Gif item
     */
    public interface GifItemClickListener {
        void onFavouriteGifItemClick(Gif Gif);
    }

    public OfflineGifsPagedListAdapter(GifItemClickListener listener, Context context) {
        mListener = listener;
        mContext = context;
        /**
         * To avoid blinking the RV using unique ids. In order to work getItemId needs to be
         * override to return an unique id
         * https://stackoverflow.com/questions/48438944/how-to-stop-blinking-on-recycler-view
         * -with-architecture-components-paging-librar
         */
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        try {
            return mHelper.getItem(position).getLocal_id();
        } catch (NullPointerException e) {
            //ugly workaround to generate a random number as sometimes the helper must not be
            // synchronized and it was throwing a NPE
            return UUID.randomUUID().getMostSignificantBits();
        }
    }

    @Override
    public GifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_offline_gifs_list, parent, false);
        return new GifViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(GifViewHolder holder, int position) {
        Gif gif = mHelper.getItem(position);
        if (gif != null) {
            holder.bindGif(gif);
        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            //TODO might be it related to the issue with the getItemId in the adapter??
            //TODO maybe we should do some changes here to avoid the issue?
            holder.clear();
        }
    }

    @Override
    public int getItemCount() {
        return mHelper.getItemCount();
    }

    public void setList(PagedList<Gif> pagedList) {
        mHelper.setList(pagedList);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    public static final DiffCallback<Gif> DIFF_CALLBACK = new DiffCallback<Gif>() {

        @Override
        public boolean areItemsTheSame(@NonNull Gif oldItem, @NonNull Gif newItem) {
            /**
             * localId is only generated when Room create the objects so it is 0 when the
             * gifs are requested to the server. Because of that we compare them with the
             * server ID
             */
            return oldItem.getServerId() == newItem.getServerId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Gif oldItem, @NonNull Gif newItem) {
            return Objects.equals(oldItem.getServerId(), newItem.getServerId())
                    && Objects.equals(oldItem.getUrl(), newItem.getUrl());
        }

    };


    public class GifViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_offline_gif_image)
        ImageView ivGif;
        @BindView(R.id.item_offline_gifs_favourite_button)
        ImageView favouriteButton;

        public GifViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            favouriteButton.setOnClickListener(this);
        }

        public void bindGif(Gif gif) {
            if (gif.getUrl().equals("") || gif.getUrl() == null) {
                ivGif.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_error_black_48dp));
            }
            Glide.with(mContext).load(gif.getUrl()).into(ivGif);
        }

        @Override
        public void onClick(View v) {
            mListener.onFavouriteGifItemClick(mHelper.getItem(getAdapterPosition()));
        }

        public void clear() {
            //TODO what exactly should we do here?
            Toast.makeText(mContext, "GifViewHolder.clear() was called", Toast.LENGTH_SHORT).show();
        }
    }
}

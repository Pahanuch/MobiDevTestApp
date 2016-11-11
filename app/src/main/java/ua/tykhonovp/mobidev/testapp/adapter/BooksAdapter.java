package ua.tykhonovp.mobidev.testapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ua.tykhonovp.mobidev.testapp.R;
import ua.tykhonovp.mobidev.testapp.model.Book;
import ua.tykhonovp.mobidev.testapp.model.BookList;

/**
 * Created by Tikho on 28.10.2016.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private Context mContext;
    private BookList bookList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;//, year, genre;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public BooksAdapter(Context context, BookList bookList) {
        this.mContext = context;
        this.bookList = bookList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Book book = bookList.items.get(position);
        holder.title.setText(book.getVolumeInfo().getTitle());

        try {

            String thumbImagePath = book.getVolumeInfo().getImageLinks().getSmallThumbnail();

            Glide
                    .with(mContext)
                    .load(thumbImagePath)
                    .fitCenter()
                    .error(android.R.drawable.stat_notify_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.empty)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.e("IMAGE_EXCEPTION", "Exception " + e.toString());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
            })
            .into(holder.image);

        }
        catch (Exception e) {
            Log.e("LOADING_IMAGE", "Exception " + e.toString());
        }

        final String infoLink = book.getVolumeInfo().getInfoLink();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoLink));
                mContext.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.items.size();
    }

}



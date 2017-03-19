package bipin.me.dailymotivation.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import bipin.me.dailymotivation.R;
import bipin.me.dailymotivation.activities.AuthorsActivity;
import bipin.me.dailymotivation.activities.GridActivity;
import bipin.me.dailymotivation.activities.QuoteListActivity;
import bipin.me.dailymotivation.utils.Constants;
import bipin.me.dailymotivation.utils.ImageUrl;

/**
 * Created by BipinSutar on 15/03/17.
 */

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.MyViewHolder> {

    private ArrayList<String> authorList = new ArrayList<>();
    private Context context;
    private int color, darkColor;
    private ArrayList<String> authorQuotes = new ArrayList<>();
    private static final String TAG = "AuthorAdapter";


    public AuthorAdapter(Context context, ArrayList<String> authorList) {
        this.context = context;
        this.authorList = authorList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_element, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.TV_authorName.setText(authorList.get(position));

        Glide.with(context)
                .load(ImageUrl.getAuthorImage(authorList.get(position)))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_error)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                        Palette palette = Palette.generate(bitmap);
                        int defaultColor = 0xFF333333;
                        color = palette.getMutedColor(defaultColor);
                        holder.TV_authorName.setBackgroundColor(color);
                        return false;
                    }
                })
                .into(holder.IV_authorImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQuotesFromFirebase(authorList.get(holder.getAdapterPosition()), holder);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://en.wikipedia.org/wiki/"+authorList.get(holder.getAdapterPosition())));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
                return true;
            }
        });

    }


    private void getQuotesFromFirebase(final String authorName, final MyViewHolder holder) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("authors").child(authorName);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (authorQuotes != null)
                    authorQuotes.clear();
                for (DataSnapshot child : dataSnapshot.getChildren())
                    authorQuotes.add(child.child("Quote").getValue().toString());

                Intent sendAuthorsList = new Intent(context, QuoteListActivity.class);
                sendAuthorsList.putExtra(Constants.SENDER_KEY, "Author");
                sendAuthorsList.putStringArrayListExtra(Constants.QUOTES_KEY, authorQuotes);
                sendAuthorsList.putExtra(Constants.AUTHOR_NAME_KEY, authorName);
                sendAuthorsList.putExtra(Constants.MUTED_COLOR, color);     //TODO: color not changing for every author
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (!QuoteListActivity.mTwoPane) {
                        ActivityOptionsCompat options;
                        try {
                            options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((GridActivity) context, holder.IV_authorImage, context.getString(R.string.authorimage_transition));
                        }catch (Exception e){
                            e.printStackTrace();
                            options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((AuthorsActivity) context, holder.IV_authorImage, context.getString(R.string.authorimage_transition));
                        }
                        context.startActivity(sendAuthorsList, options.toBundle());
                    } else
                        context.startActivity(sendAuthorsList);
                } else
                    context.startActivity(sendAuthorsList);
                Log.i(TAG, "onDataChange: Got author quotes, Started QuoteListActivity");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }


    @Override
    public int getItemCount() {
        return authorList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.grid_element_image_view)
        ImageView IV_authorImage;
        @BindView(R.id.grid_element_text_view)
        TextView TV_authorName;
        @BindView(R.id.author_card_view)
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

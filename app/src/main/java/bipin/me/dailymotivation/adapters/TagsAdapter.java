package bipin.me.dailymotivation.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
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
import bipin.me.dailymotivation.activities.QuoteListActivity;
import bipin.me.dailymotivation.utils.Constants;
import bipin.me.dailymotivation.utils.ImageUrl;

/**
 * Created by BipinSutar on 15/03/17.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> tagsList;
    private int color;
    private static final String TAG = "TagsAdapter";
    private ArrayList<String> tagQuotes = new ArrayList<>();


    public TagsAdapter(Context context, ArrayList<String> tagsList) {
        this.context = context;
        this.tagsList = tagsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_element, parent, false);
        return new TagsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String tagName = tagsList.get(position).substring(0,1).toUpperCase() + tagsList.get(position).substring(1);
        holder.TV_TagName.setText(tagName); //TODO: remove this and add images
        Glide.with(context)
                .load(ImageUrl.getTagImage(tagsList.get(position)))
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
                        holder.TV_TagName.setBackgroundColor(color);
                        return false;
                    }
                })
                .into(holder.IV_TagImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTagsFromFirebase(tagsList.get(holder.getAdapterPosition()), holder);
            }
        });
    }

    private void getTagsFromFirebase(final String tagName, final MyViewHolder holder) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("tags").child(tagName);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (tagQuotes != null)
                    tagQuotes.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    tagQuotes.add(child.getValue().toString());
                    Log.i(TAG, "onDataChange: " + child.getValue().toString());
                }
                Intent sendTagsList = new Intent(context, QuoteListActivity.class);
                sendTagsList.putExtra(Constants.SENDER_KEY, "Tags");
                sendTagsList.putExtra(Constants.TAGS_QUOTES, tagQuotes);
                sendTagsList.putExtra(Constants.TAG_CATEGORY, tagName);
                sendTagsList.putExtra(Constants.MUTED_COLOR, color);
                context.startActivity(sendTagsList);
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
        return tagsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.grid_element_image_view)
        ImageView IV_TagImage;
        @BindView(R.id.grid_element_text_view)
        TextView TV_TagName;
        @BindView(R.id.author_card_view)
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

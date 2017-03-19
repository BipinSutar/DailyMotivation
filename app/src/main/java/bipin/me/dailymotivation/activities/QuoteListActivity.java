package bipin.me.dailymotivation.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import bipin.me.dailymotivation.R;
import bipin.me.dailymotivation.fragments.QuoteFragment;
import bipin.me.dailymotivation.fragments.QuoteListFragment;
import bipin.me.dailymotivation.utils.Constants;
import bipin.me.dailymotivation.utils.ImageUrl;

public class QuoteListActivity extends AppCompatActivity implements QuoteListFragment.Callback {

    CollapsingToolbarLayout collapsingToolbarLayout;
    CircleImageView authorImage;
    Toolbar toolbar;
    int toolbarColor;

    public static boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO:NPE because of this
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String sender = getIntent().getStringExtra(Constants.SENDER_KEY);

        if (sender.equals("Author")) {
            setContentView(R.layout.activity_quote_list);
            ButterKnife.bind(this);
        } else if (sender.equals("Tags")) {
            setContentView(R.layout.activity_tags_list);
            ButterKnife.bind(this);
        }

        mTwoPane = findViewById(R.id.two_pane_view) != null;
        final String authorName = getIntent().getStringExtra(Constants.AUTHOR_NAME_KEY);
        String tag = getIntent().getStringExtra(Constants.TAG_CATEGORY);


        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (!mTwoPane && sender.equals("Author")) {
            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
            authorImage = (CircleImageView) findViewById(R.id.detail_image_view);
            collapsingToolbarLayout.setTitle(authorName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getIntent().getIntExtra(Constants.MUTED_COLOR,
                        getResources().getColor(R.color.colorPrimaryDark)));
            }
            collapsingToolbarLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://en.wikipedia.org/wiki/" + authorName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    return true;
                }
            });
            toolbarColor = getIntent().getIntExtra(Constants.MUTED_COLOR, getResources().getColor(R.color.colorPrimary));
            collapsingToolbarLayout.setBackgroundColor(toolbarColor);
            collapsingToolbarLayout.setContentScrimColor(toolbarColor);
            Glide.with(this)
                    .load(ImageUrl.getAuthorImage(authorName))
                    .into(authorImage);
        } else if (!mTwoPane && sender.equals("Tags")) {
            toolbarColor = getIntent().getIntExtra(Constants.MUTED_COLOR, getResources().getColor(R.color.colorPrimary));
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(toolbarColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getIntent().getIntExtra(Constants.MUTED_COLOR,
                        getResources().getColor(R.color.colorPrimaryDark)));
            }
            String Tag = tag.substring(0, 1).toUpperCase() + tag.substring(1);
            toolbar.setTitle(Tag);
        } else if (mTwoPane && sender.equals("Author")) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbarColor = getIntent().getIntExtra(Constants.MUTED_COLOR, getResources().getColor(R.color.colorPrimary));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getIntent().getIntExtra(Constants.MUTED_COLOR,
                        getResources().getColor(R.color.colorPrimaryDark)));
            }
            toolbar.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://en.wikipedia.org/wiki/" + authorName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    return true;
                }
            });
            toolbar.setTitle(authorName);
            toolbar.setBackgroundColor(toolbarColor);
        } else if (mTwoPane && sender.equals("Tags")) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbarColor = getIntent().getIntExtra(Constants.MUTED_COLOR, getResources().getColor(R.color.colorPrimary));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getIntent().getIntExtra(Constants.MUTED_COLOR,
                        getResources().getColor(R.color.colorPrimaryDark)));
            }
            String Tag = tag.substring(0, 1).toUpperCase() + tag.substring(1);
            toolbar.setTitle(Tag);
            toolbar.setBackgroundColor(toolbarColor);
        }

        Bundle bundle = new Bundle();
        if (sender.equals("Author")) {
            bundle.putStringArrayList(Constants.QUOTES_KEY, getIntent().getStringArrayListExtra(Constants.QUOTES_KEY));
            bundle.putString(Constants.AUTHOR_NAME_KEY, authorName);
            bundle.putInt(Constants.MUTED_COLOR, toolbarColor);
            QuoteListFragment quoteListFragment = new QuoteListFragment();
            quoteListFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.quote_list_container, quoteListFragment)
                    .commit();
        } else if (sender.equals("Tags")) {
            bundle.putStringArrayList(Constants.QUOTES_KEY, getIntent().getStringArrayListExtra(Constants.TAGS_QUOTES));
            bundle.putString(Constants.AUTHOR_NAME_KEY, tag);
            bundle.putInt(Constants.MUTED_COLOR, toolbarColor);
            QuoteListFragment quoteListFragment = new QuoteListFragment();
            quoteListFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tags_quote_list_container, quoteListFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quote_list, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            case R.id.share_action_quote:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_intent));
                startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
        }
        return true;
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (!mTwoPane)
//            supportFinishAfterTransition();
//    }


    @Override
    public void onItemSelected(ArrayList<String> quotes, String authorName, int position) {

        if (mTwoPane) {
            QuoteFragment quoteFragment = new QuoteFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.QUOTE_KEY, quotes.get(position));
            quoteFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.quote_fragment_container, quoteFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, QuoteViewActivity.class);
            intent.putStringArrayListExtra(Constants.QUOTES_KEY, quotes);
            intent.putExtra(Constants.QUOTE_NUMBER_KEY, position);
            startActivity(intent);
        }
    }

}

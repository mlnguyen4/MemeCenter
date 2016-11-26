package teamsylvanmatthew.memecenter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.handlers.TopGamesResponseHandler;
import com.mb3364.twitch.api.models.Stream;
import com.mb3364.twitch.api.models.TopGame;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {
    private static final String TAG = "BrowseActivity";
    private Twitch twitch;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinear;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView browseListView;
    private TopGameAdapter mGameAdapter;
    private StreamAdapter mStreamAdapter;
    private ArrayAdapter<String> mFollowingAdapter;
    private ArrayList<TopGame> gameList;
    private ArrayList<Stream> streamList;
    private List<String> followingList;
    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        twitch = new Twitch();
        String apikey = getResources().getString(R.string.apikey);
        twitch.setClientId(apikey);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupBrowseList();
    }


    private void setupDrawer() {
        /* Add Drawer Items */
        String[] mDrawerArray = getResources().getStringArray(R.array.drawer_array);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerArray);

        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);

        /* Setup Drawer */
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getTitle().toString());
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(BrowseActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    private void setupBrowseList() {
        gameList = new ArrayList<TopGame>();
        streamList = new ArrayList<Stream>();
        followingList = new ArrayList<String>();

        browseListView = (ListView) findViewById(R.id.browseListView);

        mGameAdapter = new TopGameAdapter(this, gameList);
        mStreamAdapter = new StreamAdapter(this, streamList);
        mFollowingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, followingList);

        updateGamesList();
        browseListView.setAdapter(mGameAdapter);
    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerLinear);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateGamesList() {

        RequestParams params = new RequestParams();
        params.put("limit", 5);

        TopGamesResponseHandler topGamesResponseHandler = new TopGamesResponseHandler() {
            @Override
            public void onSuccess(int statusCode, List<TopGame> topGames) {
                /* Successful response from the Twitch API */
                gameList.clear();

                for (TopGame topGame : topGames) {
                    gameList.add(topGame);
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        mGameAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                /* Twitch API responded with an error message */
            }

            @Override
            public void onFailure(Throwable e) {
                /* Unable to access Twitch, or error parsing the response */
            }
        };

        twitch.games().getTop(params, topGamesResponseHandler);
        mGameAdapter.notifyDataSetChanged();
    }

    private void updateStreamList() {

        RequestParams params = new RequestParams();
        params.put("limit", 5);

        StreamsResponseHandler streamsResponseHandler = new StreamsResponseHandler() {
            @Override
            public void onSuccess(int statusCode, List<Stream> streams) {
                /* Successful response from the Twitch API */
                streamList.clear();

                for (Stream stream : streams) {
                    streamList.add(stream);
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        mStreamAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                /* Twitch API responded with an error message */
            }

            @Override
            public void onFailure(Throwable e) {
                /* Unable to access Twitch, or error parsing the response */
            }

        };

        twitch.streams().get(params, streamsResponseHandler);
        mStreamAdapter.notifyDataSetChanged();
    }

    private boolean updateFollowingList() {
        followingList.clear();
        followingList.add("Following 1");
        followingList.add("Following 2");
        followingList.add("Following 3");

        return true;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(BrowseActivity.this, "Item " + position, Toast.LENGTH_SHORT).show();
            currentItem = position;

            switch (position) {
                case 0:
                    updateGamesList();
                    browseListView.setAdapter(mGameAdapter);
                    break;
                case 1:
                    updateStreamList();
                    browseListView.setAdapter(mStreamAdapter);
                    break;
                case 2:
                    updateFollowingList();
                    browseListView.setAdapter(mFollowingAdapter);
                    break;
                default:
                    updateGamesList();
                    browseListView.setAdapter(mGameAdapter);
            }

            selectItem(position);
        }
    }

}

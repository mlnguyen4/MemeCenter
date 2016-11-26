package teamsylvanmatthew.memecenter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        setupDrawer(navigationView);



        twitch = new Twitch();
        String apikey = getResources().getString(R.string.apikey);
        twitch.setClientId(apikey);


        `mDrawerList = (ListView) findViewById(R.id.drawer_list);

        `setupDrawer();

        `getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        `getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = FirstFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = SecondFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ThirdFragment.class;
                break;
            default:
                fragmentClass = FirstFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }


    @Override
    public void onResume() {
        super.onResume();

        setupBrowseList();
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

    private class StreamItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent chatIntent = new Intent(BrowseActivity.this, ChatActivity.class);
            startActivity(chatIntent);

            selectItem(position);
        }
    }

}

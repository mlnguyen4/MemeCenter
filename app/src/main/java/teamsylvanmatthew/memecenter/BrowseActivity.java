package teamsylvanmatthew.memecenter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView browseListView;

    private ArrayAdapter<String> mGameAdapter;
    private ArrayAdapter<String> mChannelAdapter;
    private ArrayAdapter<String> mFollowingAdapter;

    private List<String> gameList;
    private List<String> channelList;
    private List<String> followingList;

    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

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
    }

    private void setupBrowseList() {
        gameList = new ArrayList<String>();
        channelList = new ArrayList<String>();
        followingList = new ArrayList<String>();

        updateGamesList();

        browseListView = (ListView) findViewById(R.id.browseListView);

        mGameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gameList);
        mChannelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, channelList);
        mFollowingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, followingList);
        browseListView.setAdapter(mGameAdapter);
    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(BrowseActivity.this, "Item " + position, Toast.LENGTH_SHORT).show();
            currentItem = position;

            switch(position) {
                case 0:
                    updateGamesList();
                    browseListView.setAdapter(mGameAdapter);
                    break;
                case 1:
                    updateChannelList();
                    browseListView.setAdapter(mChannelAdapter);
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

    private boolean updateGamesList() {
        gameList.clear();
        gameList.add("Game 1");
        gameList.add("Game 2");
        gameList.add("Game 3");

        return true;
    }

    private boolean updateChannelList() {
        channelList.clear();
        channelList.add("Channel 1");
        channelList.add("Channel 2");
        channelList.add("Channel 3");

        return true;
    }

    private boolean updateFollowingList() {
        followingList.clear();
        followingList.add("Following 1");
        followingList.add("Following 2");
        followingList.add("Following 3");

        return true;
    }

}

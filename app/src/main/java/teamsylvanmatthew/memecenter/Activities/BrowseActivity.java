package teamsylvanmatthew.memecenter.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mb3364.twitch.api.Twitch;

import teamsylvanmatthew.memecenter.Fragments.FollowingFragment;
import teamsylvanmatthew.memecenter.Fragments.GameFragment;
import teamsylvanmatthew.memecenter.Fragments.HomeFragment;
import teamsylvanmatthew.memecenter.Fragments.LoadingFragment;
import teamsylvanmatthew.memecenter.Fragments.SearchFragment;
import teamsylvanmatthew.memecenter.Fragments.StreamFragment;
import teamsylvanmatthew.memecenter.R;

public class BrowseActivity extends AppCompatActivity {
    private static final String TAG = "BrowseActivity";
    public Twitch twitch;
    public FragmentManager fragmentManager;
    public NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private LinearLayout linearLayout;
    private Button btn_loginButton;
    private TextView tv_username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        sharedPreferences = getSharedPreferences("memecenter", Context.MODE_PRIVATE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle = setupDrawerToggle();
        navigationView = (NavigationView) findViewById(R.id.navigation);

        twitch = new Twitch();
        String apikey = getResources().getString(R.string.clientid);
        twitch.setClientId(apikey);

        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, homeFragment, "TAG_HOME").commit();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        checkAuthentication();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkAuthentication() {
        linearLayout = (LinearLayout) findViewById(R.id.auth_banner);
        btn_loginButton = (Button) findViewById(R.id.login_button);
        tv_username = (TextView) findViewById(R.id.username);

        if (sharedPreferences.getInt("authenticated", 0) == 1) {
            String username = sharedPreferences.getString("username", "unknown");


            tv_username.setText(username);
            linearLayout.setVisibility(View.VISIBLE);
            btn_loginButton.setVisibility(View.GONE);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("authenticated", 0);
                    editor.commit();

                    mDrawerLayout.closeDrawers();

                    linearLayout.setVisibility(View.GONE);
                    btn_loginButton.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void setupDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });

        Button loginButton = (Button) navigationView.getHeaderView(0).findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(BrowseActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    public void selectDrawerItem(MenuItem menuItem) {

        try {
            Fragment fragment = null;
            Class fragmentClass;

            addLoadingFragment();

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    fragmentClass = HomeFragment.class;
                    break;
                case R.id.nav_game:
                    fragmentClass = GameFragment.class;
                    break;
                case R.id.nav_stream:
                    fragmentClass = StreamFragment.class;
                    break;
                case R.id.nav_search:
                    fragmentClass = SearchFragment.class;
                    break;
                case R.id.nav_follow:
                    fragmentClass = FollowingFragment.class;
                    break;
                default:
                    fragmentClass = HomeFragment.class;
            }


            fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().add(R.id.fragmentLayout, fragment, "TAG_MAIN").commit();


        } catch (Exception e) {
            e.printStackTrace();
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    public boolean removeLoadingFragment() {
        try {
            Fragment loadingFragment = fragmentManager.findFragmentByTag("TAG_LOADING");

            if (loadingFragment != null) {
                fragmentManager.beginTransaction().remove(loadingFragment).commit();
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to remove loading fragment.");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean addLoadingFragment() {
        try {
            Class fragmentClass = LoadingFragment.class;
            Fragment fragment = (Fragment) fragmentClass.newInstance();

            LoadingFragment loadingFragment = new LoadingFragment();
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment, "TAG_LOADING").commit();
        } catch (Exception e) {
            Log.e(TAG, "Unable to add loading fragment.");
            e.printStackTrace();
        }
        return false;
    }
}

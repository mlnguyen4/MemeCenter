package teamsylvanmatthew.memecenter.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.handlers.TopGamesResponseHandler;
import com.mb3364.twitch.api.models.TopGame;

import java.util.ArrayList;
import java.util.List;

import teamsylvanmatthew.memecenter.Activities.BrowseActivity;
import teamsylvanmatthew.memecenter.Adapters.TopGameAdapter;
import teamsylvanmatthew.memecenter.Listeners.DataLoadListener;
import teamsylvanmatthew.memecenter.R;

public class GameFragment extends Fragment {
    private static int limit = 25;
    private static int offset = 0;
    private BrowseActivity browseActivity;
    private Activity mActivity;
    private View mView;
    private ArrayList<TopGame> gameList;
    private TopGameAdapter mGameAdapter;
    private ListView gameListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_game, container, false);
        mActivity = getActivity();
        browseActivity = (BrowseActivity) getActivity();
        gameListView = (ListView) mView.findViewById(R.id.game_listview);

        setupGameList();

        return mView;
    }

    private void setupGameList() {
        gameList = new ArrayList<TopGame>();
        mGameAdapter = new TopGameAdapter(mActivity, gameList);

        //incase user has scrolled, left to different activity and came back
        //prevents overflow
        limit = 0;

        updateGameList();
        gameListView.setAdapter(mGameAdapter);
        gameListView.setOnItemClickListener(new GameFragment.GameItemClickListener());
        gameListView.setOnScrollListener(new DataLoadListener() {
            @Override
            public void onLoadMore() {
                //System.out.println("ADD MORE");
                this.loading = true;
                addToGameList();
            }
        });
    }

    private void updateGameList() {
        limit = 25;
        offset = 0;
        addToGameList();
    }

    private void addToGameList() {
        RequestParams params = new RequestParams();
        params.put("limit", limit);
        params.put("offset", offset);
        offset += 25;

        TopGamesResponseHandler topGamesResponseHandler = new TopGamesResponseHandler() {
            @Override
            public void onSuccess(int statusCode, List<TopGame> topGames) {
                /* Successful response from the Twitch API */

                for (TopGame topGame : topGames) {
                    gameList.add(topGame);
                }

                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mGameAdapter.notifyDataSetChanged();
                        browseActivity.removeLoadingFragment();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                /* Twitch API responded with an error message
                System.out.println("The statusCode: " + statusCode);
                System.out.println("The statusMessage: " + statusMessage);
                System.out.println("The errorMessage: " + errorMessage);
                */
            }

            @Override
            public void onFailure(Throwable e) {
                /* Unable to access Twitch, or error parsing the response */
            }
        };

        browseActivity.twitch.games().getTop(params, topGamesResponseHandler);
    }

    private class GameItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            try {
                Menu menu = browseActivity.navigationView.getMenu();
                MenuItem menuItem = menu.getItem(2);
                menuItem.setChecked(true);
                getActivity().setTitle(menuItem.getTitle());

                StreamFragment streamFragment = new StreamFragment();

                Bundle args = new Bundle();
                TextView gameNameTextView = (TextView) view.findViewById(R.id.game_name);
                Toast.makeText(mActivity, gameNameTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                args.putString("GameName", gameNameTextView.getText().toString());
                streamFragment.setArguments(args);

                browseActivity.fragmentManager.beginTransaction().replace(R.id.fragmentLayout, streamFragment).commit();

            } catch (Exception e) {
                Log.e("GameFrag", "Game item click listener didn't load fragments properly.");
            }




        }
    }
}

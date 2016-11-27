package teamsylvanmatthew.memecenter.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.TopGamesResponseHandler;
import com.mb3364.twitch.api.models.TopGame;

import java.util.ArrayList;
import java.util.List;

import teamsylvanmatthew.memecenter.Adapters.TopGameAdapter;
import teamsylvanmatthew.memecenter.R;

public class GameFragment extends Fragment {
    private Twitch twitch;
    private Activity mActivity;
    private View mView;
    private ArrayList<TopGame> gameList;
    private TopGameAdapter mGameAdapter;
    private ListView gameListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_game, container, false);
        mActivity = getActivity();

        twitch = new Twitch();
        String apikey = getResources().getString(R.string.clientid);
        twitch.setClientId(apikey);

        gameListView = (ListView) mView.findViewById(R.id.game_listview);
        setupGameList();

        return mView;
    }

    private void setupGameList() {
        gameList = new ArrayList<TopGame>();
        mGameAdapter = new TopGameAdapter(mActivity, gameList);

        updateGameList();
        gameListView.setAdapter(mGameAdapter);
        gameListView.setOnItemClickListener(new GameFragment.GameItemClickListener());
    }

    private void updateGameList() {

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

                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mGameAdapter.notifyDataSetChanged();

                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment loadingFragment = fragmentManager.findFragmentByTag("TAG_LOADING");

                        if (loadingFragment != null) {
                            fragmentManager.beginTransaction().remove(loadingFragment).commit();
                        }
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

    private class GameItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(mActivity, "Item " + position, Toast.LENGTH_SHORT).show();

        }
    }
}

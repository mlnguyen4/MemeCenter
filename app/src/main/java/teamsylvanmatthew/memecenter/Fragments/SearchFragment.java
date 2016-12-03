package teamsylvanmatthew.memecenter.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.handlers.GamesResponseHandler;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.models.Game;
import com.mb3364.twitch.api.models.Stream;

import java.util.ArrayList;
import java.util.List;

import teamsylvanmatthew.memecenter.Activities.BrowseActivity;
import teamsylvanmatthew.memecenter.Activities.ChatActivity;
import teamsylvanmatthew.memecenter.Adapters.GameAdapter;
import teamsylvanmatthew.memecenter.Adapters.StreamAdapter;
import teamsylvanmatthew.memecenter.Listeners.DataLoadListener;
import teamsylvanmatthew.memecenter.R;


public class SearchFragment extends Fragment {
    private static int limit = 25;
    private static int offset = 0;
    private BrowseActivity browseActivity;
    private Activity mActivity;
    private View mView;
    private StreamAdapter mStreamAdapter;
    private GameAdapter mGameAdapter;
    private ArrayList<Stream> streamList;
    private ArrayList<Game> gameList;
    private ListView searchListView;
    private ListView gameListView;
    private Button searchButton;
    private String query;
    private EditText searchEditText;
    private RadioButton gamesRadioButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        mActivity = getActivity();
        browseActivity = (BrowseActivity) getActivity();
        FragmentManager fragmentManager = getFragmentManager();
        Fragment loadingFragment = fragmentManager.findFragmentByTag("TAG_LOADING");


        if (loadingFragment != null) {
            fragmentManager.beginTransaction().remove(loadingFragment).commit();
        }

        searchButton = (Button) mView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText = (EditText) mView.findViewById(R.id.searchEditText);
                query = searchEditText.getText().toString();

                gamesRadioButton = (RadioButton) mView.findViewById(R.id.gamesRadioButton);

                //TODO: fill radio button case logic
                if (gamesRadioButton.isChecked()) {
                    setupGameList();
                } else {
                    setupStreamList();
                }

            }
        });


        return mView;
    }


    private void setupStreamList() {
        streamList = new ArrayList<Stream>();
        mStreamAdapter = new StreamAdapter(mActivity, streamList);
        searchListView = (ListView) mView.findViewById(R.id.search_listview);

        updateStreamList();

        searchListView.setAdapter(mStreamAdapter);
        searchListView.setOnItemClickListener(new SearchFragment.StreamItemClickListener());
        searchListView.setOnScrollListener(new DataLoadListener() {
            @Override
            public void onLoadMore() {
                //System.out.println("ADD MORE");
                this.loading = true;
                addToStreamList();
            }
        });
    }


    private void updateStreamList() {
        limit = 25;
        offset = 0;
        addToStreamList();
    }


    private void addToStreamList() {
        RequestParams params = new RequestParams();
        params.put("limit", limit);
        params.put("offset", offset);


        offset += 25;

        StreamsResponseHandler streamsResponseHandler = new StreamsResponseHandler() {
            @Override
            public void onSuccess(int statusCode, List<Stream> streams) {
                /* Successful response from the Twitch API */
                for (Stream stream : streams) {
                    streamList.add(stream);
                }
                System.out.println("The Query :" + query);
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mStreamAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                //Twitch API responded with an error message
                System.out.println("The statusCode: " + statusCode);
                System.out.println("The statusMessage: " + statusMessage);
                System.out.println("The errorMessage: " + errorMessage);

            }

            @Override
            public void onFailure(Throwable e) {
                /* Unable to access Twitch, or error parsing the response */
            }

        };

        browseActivity.twitch.search().streams(query, params, streamsResponseHandler);
    }

    private void setupGameList() {
        gameList = new ArrayList<Game>();
        mGameAdapter = new GameAdapter(mActivity, gameList);

        gameListView = (ListView) mView.findViewById(R.id.search_listview);

        updateGameList();
        gameListView.setAdapter(mGameAdapter);
        gameListView.setOnItemClickListener(new SearchFragment.GameItemClickListener());
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

        GamesResponseHandler GamesResponseHandler = new GamesResponseHandler() {
            @Override
            public void onSuccess(int statusCode, List<Game> topGames) {
                /* Successful response from the Twitch API */

                for (Game game : topGames) {
                    gameList.add(game);
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

        browseActivity.twitch.search().games(query, params, GamesResponseHandler);
    }

    private class StreamItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent chatIntent = new Intent(mActivity, ChatActivity.class);
            chatIntent.putExtra("channel", streamList.get(position).getChannel().getName());
            startActivity(chatIntent);
        }
    }

    private class GameItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(mActivity, "Item " + position, Toast.LENGTH_SHORT).show();

        }
    }

}

package teamsylvanmatthew.memecenter.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.handlers.FeaturedStreamResponseHandler;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.handlers.TopGamesResponseHandler;
import com.mb3364.twitch.api.models.FeaturedStream;
import com.mb3364.twitch.api.models.Stream;
import com.mb3364.twitch.api.models.TopGame;

import java.util.ArrayList;
import java.util.List;

import teamsylvanmatthew.memecenter.Activities.BrowseActivity;
import teamsylvanmatthew.memecenter.Activities.ChatActivity;
import teamsylvanmatthew.memecenter.Adapters.HomeStreamAdapter;
import teamsylvanmatthew.memecenter.Adapters.HomeTopGameAdapter;
import teamsylvanmatthew.memecenter.Listeners.RecyclerItemClickListener;
import teamsylvanmatthew.memecenter.R;


public class HomeFragment extends Fragment {


    private static int limit = 10;
    private BrowseActivity browseActivity;
    private Activity mActivity;
    private View mView;
    private HomeStreamAdapter mStreamAdapter;
    private HomeStreamAdapter mFeaturedStreamAdapter;
    private HomeTopGameAdapter mTopGameAdapter;

    private ArrayList<Stream> streamList;
    private ArrayList<Stream> featuredStreamList;
    private ArrayList<TopGame> topGameList;

    private RecyclerView streamListView;
    private RecyclerView topGameListView;
    private RecyclerView featuredStreamListView;
    private FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mActivity = getActivity();

        /* Remove Loading when Layout is ready */
        FragmentManager fragmentManager = getFragmentManager();
        Fragment loadingFragment = fragmentManager.findFragmentByTag("TAG_LOADING");

        if (loadingFragment != null) {
            fragmentManager.beginTransaction().remove(loadingFragment).commit();
        }
        browseActivity = (BrowseActivity) getActivity();
        streamListView = (RecyclerView) mView.findViewById(R.id.homeTopStreamsRecyclerView);
        featuredStreamListView = (RecyclerView) mView.findViewById(R.id.homeFeaturedStreamsRecyclerView);
        topGameListView = (RecyclerView) mView.findViewById(R.id.homeTopGamesRecyclerView);

        TextView toStreams = (TextView) mView.findViewById(R.id.homeToTopStreamsTextView);
        TextView toTopGames = (TextView) mView.findViewById(R.id.homeToTopGamesTextView);

        toStreams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StreamFragment streamFragment = new StreamFragment();
                browseActivity.fragmentManager.beginTransaction().replace(R.id.fragmentLayout, streamFragment).commit();
            }
        });

        toTopGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameFragment gameFragment = new GameFragment();
                browseActivity.fragmentManager.beginTransaction().replace(R.id.fragmentLayout, gameFragment).commit();
            }
        });

        setupLists();

        return mView;
    }

    private void setupLists() {
        streamList = new ArrayList<Stream>();
        featuredStreamList = new ArrayList<Stream>();
        topGameList = new ArrayList<TopGame>();

        mStreamAdapter = new HomeStreamAdapter(this.getContext(), streamList);
        mFeaturedStreamAdapter = new HomeStreamAdapter(this.getContext(), featuredStreamList);
        mTopGameAdapter = new HomeTopGameAdapter(this.getContext(), topGameList);

        addToAllList();

        streamListView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        streamListView.setAdapter(mStreamAdapter);
        streamListView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), streamListView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent chatIntent = new Intent(mActivity, ChatActivity.class);
                        chatIntent.putExtra("channel", streamList.get(position).getChannel().getName());
                        startActivity(chatIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        featuredStreamListView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        featuredStreamListView.setAdapter(mFeaturedStreamAdapter);
        featuredStreamListView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), featuredStreamListView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent chatIntent = new Intent(mActivity, ChatActivity.class);
                        chatIntent.putExtra("channel", featuredStreamList.get(position).getChannel().getName());
                        startActivity(chatIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        topGameListView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        topGameListView.setAdapter(mTopGameAdapter);
        topGameListView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), topGameListView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        fragmentManager = getFragmentManager();
                        StreamFragment streamFragment = new StreamFragment();

                        Bundle args = new Bundle();
                        TextView gameNameTextView = (TextView) view.findViewById(R.id.game_name);
                        Toast.makeText(mActivity, gameNameTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                        args.putString("GameName", gameNameTextView.getText().toString());
                        streamFragment.setArguments(args);

                        browseActivity.fragmentManager.beginTransaction().replace(R.id.fragmentLayout, streamFragment).commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


    }


    private void addToAllList() {
        RequestParams params = new RequestParams();
        params.put("limit", limit);


        StreamsResponseHandler streamsResponseHandler = new StreamsResponseHandler() {
            @Override
            public void onSuccess(int statusCode, List<Stream> streams) {
                /* Successful response from the Twitch API */
                for (Stream stream : streams) {
                    streamList.add(stream);
                }

                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mStreamAdapter.notifyDataSetChanged();

                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                // Twitch API responded with an error message
                System.out.println("The statusCode: " + statusCode);
                System.out.println("The statusMessage: " + statusMessage);
                System.out.println("The errorMessage: " + errorMessage);

            }

            @Override
            public void onFailure(Throwable e) {
                /* Unable to access Twitch, or error parsing the response */
            }

        };


        FeaturedStreamResponseHandler featuredStreamResponseHandler = new FeaturedStreamResponseHandler() {
            @Override
            public void onSuccess(List<FeaturedStream> streams) {
                /* Successful response from the Twitch API */
                for (FeaturedStream stream : streams) {
                    featuredStreamList.add(stream.getStream()); //can remove .getStream() to get featured streams
                }

                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mFeaturedStreamAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                // Twitch API responded with an error message
                System.out.println("The statusCode: " + statusCode);
                System.out.println("The statusMessage: " + statusMessage);
                System.out.println("The errorMessage: " + errorMessage);

            }

            @Override
            public void onFailure(Throwable e) {
                /* Unable to access Twitch, or error parsing the response */
            }

        };

        TopGamesResponseHandler topGamesResponseHandler = new TopGamesResponseHandler() {
            @Override
            public void onSuccess(int statusCode, List<TopGame> topGames) {
                /* Successful response from the Twitch API */

                for (TopGame topGame : topGames) {
                    topGameList.add(topGame);
                }

                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mTopGameAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                // Twitch API responded with an error message
                System.out.println("The statusCode: " + statusCode);
                System.out.println("The statusMessage: " + statusMessage);
                System.out.println("The errorMessage: " + errorMessage);

            }

            @Override
            public void onFailure(Throwable e) {
                /* Unable to access Twitch, or error parsing the response */
            }
        };


        browseActivity.twitch.streams().get(params, streamsResponseHandler);
        browseActivity.twitch.streams().getFeatured(params, featuredStreamResponseHandler);
        browseActivity.twitch.games().getTop(params, topGamesResponseHandler);
    }




}

package teamsylvanmatthew.memecenter.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.models.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import teamsylvanmatthew.memecenter.Activities.BrowseActivity;
import teamsylvanmatthew.memecenter.Activities.ChatActivity;
import teamsylvanmatthew.memecenter.Adapters.StreamAdapter;
import teamsylvanmatthew.memecenter.R;

public class StreamFragment extends Fragment {
    private static int limit = 0;
    private static int offset = 100;
    private BrowseActivity browseActivity;
    private Activity mActivity;
    private View mView;
    private StreamAdapter mStreamAdapter;
    private ArrayList<Stream> streamList;
    private ListView streamListView;
    //private ScrollView streamScrollView;

    //params.put("offset", 50);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_stream, container, false);

        mActivity = getActivity();

        browseActivity = (BrowseActivity) getActivity();


        /*
        //checks when user scrolls
        final ScrollView streamScrollView = (ScrollView) mView.findViewById(R.id.stream_scrollview);
        streamScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.d("onScrollChanged Y", String.valueOf(streamScrollView.getScrollY()));
            }
        });*/



        streamListView = (ListView) mView.findViewById(R.id.stream_listview);

        setupStreamList();

        return mView;
    }


    private void setupStreamList() {
        streamList = new ArrayList<Stream>();
        mStreamAdapter = new StreamAdapter(mActivity, streamList);

        //incase user has scrolled, left to different activity and came back
        //prevents overflow
        limit = 0;
        updateStreamList();
        streamListView.setAdapter(mStreamAdapter);

        streamListView.setOnItemClickListener(new StreamItemClickListener());

    }

    //game resource.java
    private void updateStreamList() {

        RequestParams params = new RequestParams();
        limit += 15;
        offset += 15;
        System.out.println("The limit and offset :" + limit + "    " + offset);
        params.put("limit", limit);
        params.put("offset", offset);
        StreamsResponseHandler streamsResponseHandler = new StreamsResponseHandler() {
            @Override
            public void onSuccess(int statusCode, List<Stream> streams) {
                /* Successful response from the Twitch API */
                streamList.clear();

                for (Stream stream : streams) {
                    streamList.add(stream);
                }
                System.out.println("The size of stream array list :" + streamList.size());
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mStreamAdapter.notifyDataSetChanged();

                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment loadingFragment = fragmentManager.findFragmentByTag("TAG_LOADING");

                        if (loadingFragment != null) {
                            fragmentManager.beginTransaction().remove(loadingFragment).commit();
                        }

                        streamListView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                            int items = offset;

                            @Override
                            public void onScrollChanged() {
                                if (streamListView.getLastVisiblePosition() == streamListView.getAdapter().getCount() - 1 && streamListView.getChildAt(streamListView.getChildCount() - 1).getBottom() <= streamListView.getHeight()) {
                                    if (streamListView.getAdapter().getCount() < 90) {
                                        updateStreamList();
                                        try {
                                            TimeUnit.SECONDS.sleep(1);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }

                        });

                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                /* Twitch API responded with an error message */
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
    }

    private class StreamItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent chatIntent = new Intent(mActivity, ChatActivity.class);
            chatIntent.putExtra("channel", streamList.get(position).getChannel().getName());
            startActivity(chatIntent);
        }
    }
}

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
import android.widget.ListView;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.models.Stream;

import java.util.ArrayList;
import java.util.List;


import teamsylvanmatthew.memecenter.Adapters.StreamAdapter;
import teamsylvanmatthew.memecenter.R;

public class StreamFragment extends Fragment {
    private Twitch twitch;
    private Activity mActivity;
    private View mView;
    private StreamAdapter mStreamAdapter;
    private ArrayList<Stream> streamList;
    private ListView streamListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_stream, container, false);
        mActivity = getActivity();

        twitch = new Twitch();
        String apikey = getResources().getString(R.string.apikey);
        twitch.setClientId(apikey);

        streamListView = (ListView) mView.findViewById(R.id.stream_listview);
        setupStreamList();

        return mView;
    }

    private void setupStreamList() {
        streamList = new ArrayList<Stream>();
        mStreamAdapter = new StreamAdapter(mActivity, streamList);

        updateStreamList();
        streamListView.setAdapter(mStreamAdapter);
        streamListView.setOnItemClickListener(new StreamItemClickListener());
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

                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mStreamAdapter.notifyDataSetChanged();

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

        twitch.streams().get(params, streamsResponseHandler);
    }

    private class StreamItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Intent chatIntent = new Intent(mActivity, ChatActivity.class);
            //chatIntent.putExtra("channel", streamList.get(position).getChannel().getName());
            //startActivity(chatIntent);
        }
    }
}

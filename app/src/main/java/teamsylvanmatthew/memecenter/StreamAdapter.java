package teamsylvanmatthew.memecenter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mb3364.twitch.api.models.Channel;
import com.mb3364.twitch.api.models.Stream;

import java.util.ArrayList;


public class StreamAdapter extends ArrayAdapter<Stream> {
    private Activity mContext;

    private static class ViewHolder {
        TextView tv_name;
        TextView tv_game;
        TextView tv_viewers;
        TextView tv_title;
    }

    public StreamAdapter(Activity context, ArrayList<Stream> items) {
        super(context, R.layout.stream_list_item, items);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Stream stream = getItem(position);
        Channel channel = stream.getChannel();

        StreamAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new StreamAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.stream_list_item, null);

            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.streamer_name);
            viewHolder.tv_game = (TextView) convertView.findViewById(R.id.stream_game);
            viewHolder.tv_viewers = (TextView) convertView.findViewById(R.id.stream_viewers);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.stream_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StreamAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.tv_name.setText("Name: " + channel.getName());
        viewHolder.tv_game.setText("Game: " + stream.getGame());
        viewHolder.tv_viewers.setText("Viewers: " + String.valueOf(stream.getViewers()));
        viewHolder.tv_title.setText("Title: " + channel.getStatus());

        return convertView;
    }
}



package teamsylvanmatthew.memecenter.Adapters;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb3364.twitch.api.models.Channel;
import com.mb3364.twitch.api.models.Stream;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.R;
import teamsylvanmatthew.memecenter.Tasks.GetImageTask;

import static android.text.Html.FROM_HTML_MODE_COMPACT;


public class StreamAdapter extends ArrayAdapter<Stream> {
    private Activity mContext;

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

            viewHolder.tv_preview = (ImageView) convertView.findViewById(R.id.streamPreviewImageView);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.streamer_name);
            viewHolder.tv_game = (TextView) convertView.findViewById(R.id.stream_game);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.stream_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StreamAdapter.ViewHolder) convertView.getTag();
        }

        String preview = stream.getPreview().getLarge();

        Spanned text = Html.fromHtml(String.valueOf(stream.getViewers()) + " watching " + "<b>" + channel.getName() + "</b>", FROM_HTML_MODE_COMPACT);

        new GetImageTask(viewHolder.tv_preview).execute(preview);
        viewHolder.tv_name.setText(text);
        viewHolder.tv_game.setText(stream.getGame());
        viewHolder.tv_title.setText(channel.getStatus());

        return convertView;
    }


    private static class ViewHolder {
        ImageView tv_preview;
        TextView tv_name;
        TextView tv_game;
        TextView tv_title;
    }
}



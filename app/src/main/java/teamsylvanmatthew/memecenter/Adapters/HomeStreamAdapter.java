package teamsylvanmatthew.memecenter.Adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb3364.twitch.api.models.Channel;
import com.mb3364.twitch.api.models.Stream;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.R;
import teamsylvanmatthew.memecenter.Tasks.GetImageTask;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;


public class HomeStreamAdapter extends RecyclerView.Adapter<HomeStreamAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Stream> streams;

    public HomeStreamAdapter(Context context, ArrayList<Stream> streams) {
        inflater = LayoutInflater.from(context);
        this.streams = streams;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeStreamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View view = inflater.inflate(R.layout.stream_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Stream stream = streams.get(position);
        Channel channel = stream.getChannel();

        String preview = stream.getPreview().getMedium();


        new GetImageTask(viewHolder.tv_preview).execute(preview);


        //to make the name part of the string viewers watching name bold
        String viewerAndName = String.valueOf(stream.getViewers()) + " watching " + channel.getName();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(viewerAndName);
        int boldStart = viewerAndName.indexOf(channel.getName());
        int boldEnd = viewerAndName.length();
        stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), boldStart, boldEnd, SPAN_INCLUSIVE_INCLUSIVE);

        viewHolder.tv_name.setText(stringBuilder);
        viewHolder.tv_game.setText(stream.getGame());

        //for when status is too long
        String status = channel.getStatus();
        if (status.length() > 40) {
            status = status.substring(0, 37) + "...";
        }

        viewHolder.tv_title.setText(status);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return streams.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView tv_preview;
        TextView tv_name;
        TextView tv_game;
        TextView tv_title;

        public ViewHolder(View v) {
            super(v);
            tv_preview = (ImageView) v.findViewById(R.id.streamPreviewImageView);
            tv_name = (TextView) v.findViewById(R.id.streamer_name);
            tv_game = (TextView) v.findViewById(R.id.stream_game);
            tv_title = (TextView) v.findViewById(R.id.stream_title);
        }
    }
}


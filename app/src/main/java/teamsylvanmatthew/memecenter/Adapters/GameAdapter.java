package teamsylvanmatthew.memecenter.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mb3364.twitch.api.models.Game;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.R;

public class GameAdapter extends ArrayAdapter<Game> {
    private Activity mContext;

    public GameAdapter(Activity context, ArrayList<Game> items) {
        super(context, R.layout.topgame_list_item, items);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Game game = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.topgame_list_item, null);

            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.game_name);
            viewHolder.tv_viewers = (TextView) convertView.findViewById(R.id.topgame_viewers);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tv_name.setText(game.getName());
        viewHolder.tv_viewers.setText("Viewers: " + String.valueOf(game.getPopularity()));


        return convertView;
    }

    private static class ViewHolder {
        TextView tv_name;
        TextView tv_viewers;
    }
}

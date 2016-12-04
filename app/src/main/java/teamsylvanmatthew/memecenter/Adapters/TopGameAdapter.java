package teamsylvanmatthew.memecenter.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb3364.twitch.api.models.Game;
import com.mb3364.twitch.api.models.TopGame;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.R;
import teamsylvanmatthew.memecenter.Tasks.GetImageTask;

public class TopGameAdapter extends ArrayAdapter<TopGame> {
    private Activity mContext;

    public TopGameAdapter(Activity context, ArrayList<TopGame> items) {
        super(context, R.layout.topgame_list_item, items);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopGame topGame = getItem(position);
        Game game = topGame.getGame();

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.topgame_list_item, null);

            viewHolder.tv_logo = (ImageView) convertView.findViewById(R.id.gameLogoImageView);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.game_name);
            viewHolder.tv_viewers = (TextView) convertView.findViewById(R.id.topgame_viewers);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String logo = game.getBox().getLarge();

        new GetImageTask(viewHolder.tv_logo).execute(logo);
        viewHolder.tv_name.setText(game.getName());
        viewHolder.tv_viewers.setText("Viewers: " + String.valueOf(topGame.getViewers()));


        return convertView;
    }

    private static class ViewHolder {
        ImageView tv_logo;
        TextView tv_name;
        TextView tv_viewers;
    }
}

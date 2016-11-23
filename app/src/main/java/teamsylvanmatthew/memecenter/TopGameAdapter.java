package teamsylvanmatthew.memecenter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mb3364.twitch.api.models.Game;
import com.mb3364.twitch.api.models.TopGame;

import java.util.ArrayList;

public class TopGameAdapter extends ArrayAdapter<TopGame> {
    private Activity mContext;
    private ArrayList<TopGame> mItems;

    private static class ViewHolder {
        TextView tv_name;
        TextView tv_viewers;
    }

    public TopGameAdapter(Activity context, ArrayList<TopGame> items) {
        super(context, R.layout.topgame_list_item, items);
        mItems = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public TopGame getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.game_name);
            viewHolder.tv_viewers = (TextView) convertView.findViewById(R.id.topgame_viewers);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tv_name.setText("Name: " + game.getName());
        viewHolder.tv_viewers.setText("Viewers: " + String.valueOf(topGame.getViewers()));


        return convertView;
    }

    public void update(ArrayList<TopGame> topGames) {
        mItems.clear();
        mItems.addAll(topGames);
        notifyDataSetChanged();
    }
}

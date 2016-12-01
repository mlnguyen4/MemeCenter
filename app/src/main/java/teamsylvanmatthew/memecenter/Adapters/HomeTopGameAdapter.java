package teamsylvanmatthew.memecenter.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mb3364.twitch.api.models.Game;
import com.mb3364.twitch.api.models.TopGame;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.R;


public class HomeTopGameAdapter extends RecyclerView.Adapter<HomeTopGameAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<TopGame> topGames;

    public HomeTopGameAdapter(Context context, ArrayList<TopGame> topGames) {
        inflater = LayoutInflater.from(context);
        this.topGames = topGames;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeTopGameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View view = inflater.inflate(R.layout.topgame_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TopGame topGame = topGames.get(position);
        Game game = topGame.getGame();


        viewHolder.tv_name.setText("Name: " + game.getName());
        viewHolder.tv_viewers.setText("Viewers: " + String.valueOf(topGame.getViewers()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return topGames.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tv_name;
        TextView tv_viewers;

        public ViewHolder(View v) {
            super(v);
            tv_name = (TextView) v.findViewById(R.id.game_name);
            tv_viewers = (TextView) v.findViewById(R.id.topgame_viewers);
        }
    }
}


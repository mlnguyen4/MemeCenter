package teamsylvanmatthew.memecenter.Adapters;

import android.app.Activity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.R;

public class FilterAdapter extends ArrayAdapter<String> {
    private Activity mContext;
    private int position;

    public FilterAdapter(Activity context, ArrayList<String> items) {
        super(context, R.layout.item_filter, items);
        mContext = context;
    }

    @Override
    public View getView(int posn, View convertView, ViewGroup parent) {
        String name = getItem(posn);
        position = posn;

        FilterAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new FilterAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_filter, null);

            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.filtername);
            viewHolder.img_settings = (ImageView) convertView.findViewById(R.id.more);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FilterAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.tv_name.setText(name);

        viewHolder.img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.filter_menu_view, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.edit:
                                Toast.makeText(mContext, "Edit " + position, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.delete:
                                Toast.makeText(mContext, "Delete " + position, Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                break;
                        }

                        return true;
                    }
                });

            }
        });


        return convertView;
    }

    private class ViewHolder {
        TextView tv_name;
        ImageView img_settings;
    }
}
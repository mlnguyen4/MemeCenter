package teamsylvanmatthew.memecenter.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import teamsylvanmatthew.memecenter.Activities.RuleActivity;
import teamsylvanmatthew.memecenter.Database.MemeCenterDataSource;
import teamsylvanmatthew.memecenter.Models.Filter;
import teamsylvanmatthew.memecenter.R;

public class FilterAdapter extends ArrayAdapter<Filter> {
    private MemeCenterDataSource dataSource;
    private Activity mContext;
    private ArrayList<String> selected;

    public FilterAdapter(Activity context, Set<String> selected, ArrayList<Filter> items) {
        super(context, R.layout.item_filter, items);
        mContext = context;
        this.selected = new ArrayList<String>();
        if (selected != null) {
            this.selected.addAll(selected);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Filter filter = getItem(position);

        final FilterAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new FilterAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_filter, null);

            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.filtername);
            viewHolder.img_settings = (ImageView) convertView.findViewById(R.id.more);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FilterAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.tv_name.setText(filter.getName());

        if (selected.contains(filter.getName())) {
            viewHolder.checkBox.setChecked(true);
        }

        try {
            dataSource = new MemeCenterDataSource(mContext);
            dataSource.open();

            viewHolder.img_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, v);
                    popup.getMenuInflater().inflate(R.menu.filter_menu_view, popup.getMenu());
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Filter filter = getItem(position);

                            switch (item.getItemId()) {
                                case R.id.edit:
                                    Toast.makeText(mContext, "Edit " + position, Toast.LENGTH_SHORT).show();

                                    Intent ruleIntent = new Intent(mContext, RuleActivity.class);
                                    ruleIntent.putExtra("id", filter.getId());
                                    ruleIntent.putExtra("name", filter.getName());
                                    mContext.startActivity(ruleIntent);
                                    notifyDataSetChanged();

                                    break;
                                case R.id.delete:
                                    Toast.makeText(mContext, "Delete " + position, Toast.LENGTH_SHORT).show();

                                    dataSource.deleteFilter(filter);
                                    remove(filter);
                                    notifyDataSetChanged();
                                    break;

                                default:
                                    break;
                            }

                            return true;
                        }
                    });

                }
            });

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Filter filter = getItem(position);
                    if (isChecked) {
                        selected.add(filter.getName().toString());
                    } else {
                        selected.remove(filter.getName().toString());
                    }
                }
            });

            /*
            viewHolder.checkBox.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()) {
                        viewHolder.checkBox.setItemChecked(position, true);
                    }
                }
            });
*/

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }

    public ArrayList<String> getSelectedItems() {
        return selected;
    }

    private class ViewHolder {
        CheckBox checkBox;
        TextView tv_name;
        ImageView img_settings;
    }
}
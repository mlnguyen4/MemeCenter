package teamsylvanmatthew.memecenter.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.Models.Message;
import teamsylvanmatthew.memecenter.R;


public class MessageAdapter extends ArrayAdapter<Message> {
    private Activity mContext;
    private String mCurrentUser;

    public MessageAdapter(Activity context, ArrayList<Message> items, String user) {
        super(context, R.layout.right_msg_list_item, items);
        mContext = context;
        mCurrentUser = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);

        MessageAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new MessageAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.right_msg_list_item, null);


            viewHolder.tv_user = (TextView) convertView.findViewById(R.id.editTextName);
            viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.editTextMsg);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MessageAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.tv_user.setText(message.getUser());
        viewHolder.tv_msg.setText(message.getMessage());

        return convertView;
    }

    private static class ViewHolder {
        TextView tv_user;
        TextView tv_msg;
    }
}



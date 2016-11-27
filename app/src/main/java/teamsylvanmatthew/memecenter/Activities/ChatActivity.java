package teamsylvanmatthew.memecenter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.Adapters.MessageAdapter;
import teamsylvanmatthew.memecenter.Models.Message;
import teamsylvanmatthew.memecenter.R;

public class ChatActivity extends AppCompatActivity {
    private String mCurrentUser = "tempuser";
    private ListView messageListView;
    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String title = "Chat: " + intent.getStringExtra("channel");
        setTitle(title);

        messageListView = (ListView) findViewById(R.id.messageListView);
        messages = new ArrayList<Message>();

        messageAdapter = new MessageAdapter(this, messages, mCurrentUser);
        messageListView.setAdapter(messageAdapter);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText msgText = (EditText) findViewById(R.id.messageText);
                postMessage(new Message(mCurrentUser, msgText.getText().toString()));
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean postMessage(Message msg) {
        messages.add(msg);
        messageAdapter.notifyDataSetChanged();
        return true;
    }
}

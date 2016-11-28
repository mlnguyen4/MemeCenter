package teamsylvanmatthew.memecenter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.Adapters.MessageAdapter;
import teamsylvanmatthew.memecenter.Models.Message;
import teamsylvanmatthew.memecenter.R;

public class ChatActivity extends AppCompatActivity {
    private String mCurrentUser = "tempuser";
    private ListView messageListView;
    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;
    private PircBotX bot;

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
                if (!msgText.equals("")) {
                    postMessage(new Message(mCurrentUser, msgText.getText().toString()));
                    msgText.setText("");
                }
            }
        });


        new Thread(new Runnable() {
            public void run() {
                try {
                    Configuration.Builder builder = new Configuration.Builder();
                    builder.addServer("irc.freenode.net");
                    builder.setName(mCurrentUser);
                    builder.addAutoJoinChannel("#memecenter");
                    //builder.setServerPassword(authcode);
                    builder.setMessageDelay(0);
                    //builder.addListener(new ChatListener());
                    builder.setAutoReconnect(false);
                    builder.setAutoSplitMessage(false);

                    builder.addListener(new ListenerAdapter() {
                        @Override
                        public void onGenericMessage(final GenericMessageEvent event) throws Exception {
                            postMessage(new Message(event.getUser().getNick(), event.getMessage()));
                            //event.respond("recieved");
                        }

                    });


                    bot = new PircBotX(builder.buildConfiguration());
                    bot.startBot();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();


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
        if (!msg.getMessage().equals("")) {
            messages.add(msg);
            this.runOnUiThread(new Runnable() {
                public void run() {
                    messageAdapter.notifyDataSetChanged();
                }
            });
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bot.close();
    }

}

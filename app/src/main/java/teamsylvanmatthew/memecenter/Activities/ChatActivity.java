package teamsylvanmatthew.memecenter.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import teamsylvanmatthew.memecenter.Adapters.MessageAdapter;
import teamsylvanmatthew.memecenter.Database.MemeCenterDataSource;
import teamsylvanmatthew.memecenter.Models.Message;
import teamsylvanmatthew.memecenter.R;

public class ChatActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String mCurrentUser = "justinfan58503920594859";
    private String mChannel;
    private String mOauth;
    private MemeCenterDataSource dataSource;
    private ListView messageListView;
    private ArrayList<Message> messages;
    private ArrayList<Pattern> mFilterPatterns;
    private MessageAdapter messageAdapter;
    private PircBotX bot;
    private Message mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        mChannel = intent.getStringExtra("channel");
        setTitle("Chat: " + mChannel);

        getCredentials();

        dataSource = new MemeCenterDataSource(this);
        dataSource.open();

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
                    mMsg = new Message(mCurrentUser, msgText.getText().toString());
                    //postRawMessage(mMsg);
                    postRawMessage();
                    bot.sendIRC().message("#" + mChannel, mMsg.getMessage());
                    msgText.setText("");
                }
            }
        });

        getRegex();


        new Thread(new Runnable() {
            public void run() {
                try {
                    Configuration.Builder builder = new Configuration.Builder();
                    builder.addServer("irc.chat.twitch.tv");
                    builder.setName(mCurrentUser);
                    builder.addAutoJoinChannel("#" + mChannel);
                    builder.setMessageDelay(0);
                    builder.setAutoReconnect(false);
                    builder.setAutoSplitMessage(false);
                    builder.setAutoNickChange(false);
                    builder.setOnJoinWhoEnabled(false);

                    if (mOauth != null) {
                        builder.setServerPassword("oauth:" + mOauth);
                        System.out.println("Test 1");
                        builder.setCapEnabled(true);
                        builder.addCapHandler(new EnableCapHandler("twitch.tv/membership"));
                    }


                    builder.addListener(new ListenerAdapter() {
                        @Override
                        public void onGenericMessage(final GenericMessageEvent event) throws Exception {
                            mMsg = new Message(event.getUser().getNick(), event.getMessage());
                            postFilteredMessage();

                            //postFilteredMessage(new Message(event.getUser().getNick(), event.getMessage()));
                        }

                    });


                    bot = new PircBotX(builder.buildConfiguration());
                    bot.startBot();
/*
                    runOnUiThread(new Runnable() {
                        public void run() {
                            TextView textView = (TextView) findViewById(R.id.connectionText);
                            textView.setVisibility(View.GONE);
                        }
                    });
                    */


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
/*
        try {
            t.start();
            t.join();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
*/
    }

    private void getRegex() {
        Set<String> filters = sharedPreferences.getStringSet("filterList", null);
        ArrayList<String> allRules = new ArrayList<String>();

        if (filters != null) {
            for (String filter : filters) {
                long currId = dataSource.getFilterId(filter);
                allRules.addAll(dataSource.getRules(currId));
            }

            mFilterPatterns = new ArrayList<Pattern>();


            for (String rule : allRules) {
                mFilterPatterns.add(Pattern.compile(rule));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_filter:
                Intent intent = new Intent(this, FilterActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu_view, menu);
        return true;
    }

    private void getCredentials() {
        sharedPreferences = getSharedPreferences("memecenter", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("authenticated", 0) == 1) {
            mCurrentUser = sharedPreferences.getString("username", "justinfan58503920594859");
            mOauth = sharedPreferences.getString("oauth", null);
        }
    }

    private boolean postFilteredMessage() {
        Matcher m = null;

        for (Pattern pattern : mFilterPatterns) {
            m = pattern.matcher(mMsg.getMessage());
            if (m.find()) {
                return false;
            }
        }

        if (!mMsg.getMessage().equals("")) {

            this.runOnUiThread(new Runnable() {

                public void run() {
                    messages.add(mMsg);
                    messageAdapter.notifyDataSetChanged();
                }
            });
            return true;
        } else {
            return false;
        }
    }

    private boolean postRawMessage() {
        if (!mMsg.getMessage().equals("")) {

            this.runOnUiThread(new Runnable() {

                public void run() {
                    messages.add(mMsg);
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

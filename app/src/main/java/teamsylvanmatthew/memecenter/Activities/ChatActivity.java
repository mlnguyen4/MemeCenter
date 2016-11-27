package teamsylvanmatthew.memecenter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import teamsylvanmatthew.memecenter.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String title = new String("Chat: " + intent.getStringExtra("channel"));
        setTitle(title);
    }
}

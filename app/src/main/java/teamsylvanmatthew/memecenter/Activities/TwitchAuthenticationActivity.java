package teamsylvanmatthew.memecenter.Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;

import java.net.URI;
import java.net.URISyntaxException;

import teamsylvanmatthew.memecenter.R;



public class TwitchAuthenticationActivity extends AppCompatActivity {
    static String authenticationToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitch_authentication);

        twitchAuthenticationWebView();
        
    }




    //Sets up the webview to allow the user to login to their Twitch account
    //Once the user enters valid credentials, the function enters the authentication token into
    //the twitch object and redirects user to Browse Activity
    public void twitchAuthenticationWebView()
    {
        WebView webview = new WebView(this);
        setContentView(webview);
        Twitch twitch = new Twitch();
        String clientID = getResources().getString(R.string.clientid);
        String redirectUrl = getResources().getString(R.string.redirecturl);
        //The list of permissions we are asking Twitch for about the user
        //we can remove any we don't need, I included everything to start
        //they MUST be space separated
        String listOfScopes =   "user_read "+
                "user_blocks_edit "+
                "user_blocks_read "+
                "user_follows_edit "+
                "channel_read "+
                "channel_editor "+
                "channel_commercial "+
                "channel_stream "+
                "channel_subscriptions "+
                "user_subscriptions "+
                "channel_check_subscription " +
                "chat_login "+
                "channel_feed_read "+
                "channel_feed_edit";
        String twitchLoginPageUrl =    "https://api.twitch.tv/kraken/oauth2/authorize" +
                "?response_type=code"+
                "&client_id=" + clientID +
                "&redirect_uri="+ redirectUrl+
                "&scope="+ listOfScopes;


        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);

        webview.setWebViewClient(new WebViewClient(){
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                String startOfCodeParameter = "?code=";
                String endOfCodeParameter = "&scope=";
                System.out.println("The url " + url);

                //finds the start of the authentication token
                int start = url.indexOf(startOfCodeParameter);

                //when the browser enters callback url, i.e. user successfully logged in Twitch account
                if (start > -1) {

                    view.stopLoading();

                    //finds where the token ends
                    int end= url.indexOf(endOfCodeParameter);

                    String code = url.substring(start+startOfCodeParameter.length(), end);
                    System.out.println("The code " + code);


                    Twitch twitch = new Twitch();
                    String clientID = getResources().getString(R.string.clientid);
                    twitch.setClientId(clientID); // This is your registered application's client ID
                    twitch.auth().setAccessToken(code);

                    //TODO: Send authenticated Twitch object to where ever Sylvan wants it
                    //from this line onwards
                }
            }

        });
        webview.loadUrl(twitchLoginPageUrl);
        webview.setWebChromeClient(new WebChromeClient());
    }




}

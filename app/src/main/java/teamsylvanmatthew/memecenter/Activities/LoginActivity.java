package teamsylvanmatthew.memecenter.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.common.base.Splitter;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;

import java.net.URI;
import java.util.Map;

import teamsylvanmatthew.memecenter.R;
import teamsylvanmatthew.memecenter.Utils.GetUsernameTask;


public class LoginActivity extends AppCompatActivity {
    private WebView webview;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupWebview();

        sharedPreferences = getSharedPreferences("memecenter", Context.MODE_PRIVATE);

        Twitch twitch = new Twitch();
        String clientID = getResources().getString(R.string.clientid);
        String redirectUrl = getResources().getString(R.string.redirecturl);
        twitch.setClientId(clientID);

        try {
            String twitchLoginPageUrl = "https://api.twitch.tv/kraken/oauth2/authorize.html";
            URI callbackUri = new URI(redirectUrl);
            String authUrl = twitch.auth().getAuthenticationUrl(clientID, callbackUri, Scopes.USER_READ, Scopes.CHANNEL_READ);
            webview.loadUrl(authUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setupWebview() {
        webview = (WebView) findViewById(R.id.twitchAuthenticationWebView);
        webview.clearCache(true);
        webview.clearHistory();

        //CookieManager cookieManager = CookieManager.getInstance();
        //cookieManager.setAcceptCookie(false);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
                progressBar.setVisibility(View.INVISIBLE);

                String[] urlSplit = url.split("\\#");
                if (urlSplit.length >= 2) {
                    String query = urlSplit[1];
                    Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(query);
                    System.out.println(map);

                    String oauth = map.get("access_token");
                    if (oauth != null) {
                        System.out.println("OAUTH: " + oauth);

                        try {
                            String result = new GetUsernameTask()
                                    .execute("https://api.twitch.tv/kraken?oauth_token=" + oauth)
                                    .get();


                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("authenticated", 1);
                            editor.putString("username", result);
                            editor.putString("oauth", oauth);
                            editor.commit();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        onBackPressed();
                    }
                }
            }

            public void onReceivedHttpError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });


    }

}
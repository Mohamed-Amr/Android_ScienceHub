package eg.mimocodes.sciencehub.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.DownloadListener;
import android.app.DownloadManager;
import android.graphics.Bitmap;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import eg.mimocodes.sciencehub.advancedwebview.*;
import eg.mimocodes.sciencehub.utility.DownloadFileUtility;
import eg.mimocodes.sciencehub.utility.*;

import java.util.HashMap;

import eg.mimocodes.sciencehub.R;
import eg.mimocodes.sciencehub.app.App;
import eg.mimocodes.sciencehub.config.Config;
import eg.mimocodes.sciencehub.listeners.UserInfoListener;
import eg.mimocodes.sciencehub.model.UserBean;
import eg.mimocodes.sciencehub.net.DataManager;
import eg.mimocodes.sciencehub.utility.WebViewAppConfig;

public class ScienceHubActivity extends BaseAppCompatActivity {

    private Toolbar toolbarHome;
    static AdvancedWebView ShWebView;
    private UserBean userBean;
    private static final String TAG = "ScienceHubActivity";
    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private String imagePath = "";
    protected WebChromeClient mCustomWebChromeClient;
    protected WebViewClient mCustomWebViewClient;
    ImageButton feeds ;
    ImageButton pubs ;
    ImageButton groups ;
    ImageButton pages ;
    ImageButton requests ;
    ImageButton messages ;
    ImageButton notifications ;

    RelativeLayout requestsLayout ;
    RelativeLayout messagesLayout;
    RelativeLayout notificationsLayout;
    RelativeLayout requests_rl;
    RelativeLayout messages_rl;
    RelativeLayout notifications_rl;

    TextView textNotifications;
    TextView textMessages;
    TextView textRequests;




    // it is static because we will call it from second actvity
    static void webloadUrl (String Url){
        ShWebView.loadUrl(Url);
    }


   /* private static GoogleMapOptions options = new GoogleMapOptions()
            .mapType(GoogleMap.MAP_TYPE_NORMAL)
            .compassEnabled(false)
            .rotateGesturesEnabled(false)
            .tiltGesturesEnabled(false)
            .zoomControlsEnabled(false)
            .scrollGesturesEnabled(false)
            .mapToolbarEnabled(false);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sciencehub);

        initViews();
        mHandler.post(runnable);
        //setProgressScreenVisibility(true, true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.sciencehub_small);
        textMessages.setVisibility(View.GONE);
        textNotifications.setVisibility(View.GONE);
        textRequests.setVisibility(View.GONE);

        swipeView.setRefreshing(true);
        if (getIntent().getStringExtra("username") != null)
        {
            //new postLoginDataTask().execute();
            String url = getIntent().getStringExtra("url");
            ShWebView.loadUrl(url);
            swipeView.setRefreshing(false);
        }
        else {
            //new postLoginDataTask().execute();
            String url = getIntent().getStringExtra("url");
            ShWebView.loadUrl(url);
            //performWebLogin();
            swipeView.setRefreshing(false);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();

        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            openOptionsMenu();
        }
        return true;
    }

  private  void performWebLogin(){
      ShWebView.evaluateJavascript("document.getElementById(\"username\").value = \"AmrAshry\"; document.getElementById(\"password\").value = \"ultimate\"; document.querySelector(\"#html-submit\").click();", new android.webkit.ValueCallback<String>() {
          @Override
          public void onReceiveValue(String s) {
              Log.d("LogName", s); // Prints: "this"
          }
      });

  }

    private StringBuilder inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        // Read response until the end
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return full string
        return total;
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(ShWebView !=null)
            {
                ShWebView.evaluateJavascript("document.getElementById(\"message-count\").innerHTML;", new android.webkit.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        if(s!=null) {
                                if (isNumeric(s.replaceAll("^\"|\"$", ""))) {
                                    textMessages.setVisibility(View.VISIBLE);
                                    textMessages.setText(s.replaceAll("^\"|\"$", ""));
                                } else {
                                    textMessages.setVisibility(View.GONE);
                                }
                        } else
                        {
                            textMessages.setVisibility(View.GONE);

                        }

                    }
                });

                ShWebView.evaluateJavascript("document.getElementById(\"notification-count\").innerHTML;", new android.webkit.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        if(s!=null) {
                                if(isNumeric(s.replaceAll("^\"|\"$", "")))
                                {
                                    textNotifications.setVisibility(View.VISIBLE);
                                    textNotifications.setText(s.replaceAll("^\"|\"$", ""));
                                }
                                else
                                {
                                    textNotifications.setVisibility(View.GONE);
                                }
                    } else
                    {
                        textNotifications.setVisibility(View.GONE);

                    }

                    }
                });


                ShWebView.evaluateJavascript("document.getElementById(\"request-count\").innerHTML;", new android.webkit.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        if(s!=null) {
                                if(isNumeric(s.replaceAll("^\"|\"$", "")))
                                {
                                    textRequests.setVisibility(View.VISIBLE);
                                    textRequests.setText(s.replaceAll("^\"|\"$", ""));
                                }
                                else
                                {
                                    textRequests.setVisibility(View.GONE);
                                }
                    } else
                    {
                        textRequests.setVisibility(View.GONE);

                    }

                    }
                });
            }
                mHandler.postDelayed(this, 5000);
            }
    };

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


    public void initViews() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coordinatorLayout.removeView(toolbar);
        toolbarHome = (Toolbar) getLayoutInflater().inflate(R.layout.toolbar_landing_page, toolbar);
        coordinatorLayout.addView(toolbarHome, 0);
        setSupportActionBar(toolbarHome);
        ShWebView =  findViewById(R.id.sciencehubview);
        feeds = findViewById(R.id.imageButton3);
        pubs = findViewById(R.id.imageButton4);
        groups = findViewById(R.id.imageButton5);
        pages = findViewById(R.id.imageButton6);
        requests = findViewById(R.id.imageButton7);
        messages = findViewById(R.id.imageButton8);
        notifications = findViewById(R.id.imageButton9);

        requestsLayout = findViewById(R.id.layoutRequests);
        messagesLayout = findViewById(R.id.layoutMessages);
        notificationsLayout = findViewById(R.id.layoutNotifications);

        requests_rl = findViewById(R.id.requests_rl);
        messages_rl = findViewById(R.id.messages_rl);
        notifications_rl = findViewById(R.id.notifications_rl);

        textNotifications = findViewById(R.id.textNotifications);
        textMessages = findViewById(R.id.textMessages);
        textRequests = findViewById(R.id.textRequests);
        ShWebView.setListener(this, this);
        setupView();

    }




    private void setupView() {
        ShWebView.getSettings().setJavaScriptEnabled(true);
        ShWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        ShWebView.getSettings().setAppCacheEnabled(true);
        ShWebView.getSettings().setAppCachePath(this.getCacheDir().getAbsolutePath());
        ShWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        ShWebView.getSettings().setDomStorageEnabled(true);
        ShWebView.getSettings().setDatabaseEnabled(true);
        ShWebView.getSettings().setSupportZoom(true);
        ShWebView.getSettings().setBuiltInZoomControls(false);
        //ShWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        ShWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        ShWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        ShWebView.getSettings().setAllowContentAccess(true);
        ShWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        // user agent
        if (WebViewAppConfig.WEBVIEW_USER_AGENT != null && !WebViewAppConfig.WEBVIEW_USER_AGENT.equals("")) {
            ShWebView.getSettings().setUserAgentString(WebViewAppConfig.WEBVIEW_USER_AGENT);
        }

        // advanced webview settings
        ShWebView.setListener(this, this);
        ShWebView.setGeolocationEnabled(true);

        // webview style
        ShWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // fixes scrollbar on Froyo

        // webview hardware acceleration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ShWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            ShWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ShWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            ShWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        ShWebView.setCookiesEnabled(true);
        ShWebView.setGeolocationEnabled(true);
        ShWebView.setWebChromeClient(new WebChromeClient());
        ShWebView.setWebViewClient(new WebViewClient()

           /* @Override
            public void onPageFinished(WebView view, String url) {


            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                view.evaluateJavascript("document.querySelector(\"header\").remove();  document.querySelector(\"footer\").remove();", new android.webkit.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("LogNameStart", s); // Prints: "this"
                    }
                });

            }
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (getIntent().getStringExtra("username") != null && getIntent().getStringExtra("password") != null ) {
                    String user_name = getIntent().getStringExtra("username") ;
                    String user_pass = getIntent().getStringExtra("password") ;
                    getIntent().removeExtra("username");
                    getIntent().removeExtra("password");
                    view.evaluateJavascript("document.getElementById(\"username\").value = \" " + user_name + " \"; document.getElementById(\"password\").value = \""+ user_pass +"\"; document.querySelector(\"#html-submit\").click();", new android.webkit.ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.d("LogName", s); // Prints: "this"
                        }
                    });
                }

                view.evaluateJavascript("document.querySelector(\"header\").remove();  document.querySelector(\"footer\").remove();", new android.webkit.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("LogNameCommit", s); // Prints: "this"
                    }
                });


            }*/

        );



        ShWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Files");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void fetchUserInfo() {

        HashMap<String, String> urlParams = new HashMap<>();

        urlParams.put("auth_token", Config.getInstance().getAuthToken());

        DataManager.fetchUserInfo(urlParams, Config.getInstance().getUserID(), new UserInfoListener() {
            @Override
            public void onLoadCompleted(UserBean userBean) {
                System.out.println("Successfull  : UserBean : " + userBean);
                ScienceHubActivity.this.userBean = userBean;
                populateUserInfo(userBean);
                App.saveToken();

            }

            @Override
            public void onLoadFailed(String errorMsg) {
               /* Snackbar.make(coordinatorLayout, errorMsg, Snackbar.LENGTH_LONG)
                        .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();*/

            }
        });

    }

    private void populateUserInfo(UserBean userBean) {

        try {
            Config.getInstance().setName(userBean.getName());
            Config.getInstance().setProfilePhoto(userBean.getProfilePhoto());
            Config.getInstance().setEmail(userBean.getEmail());
            Config.getInstance().setAddHome(userBean.getAddHome());
            Config.getInstance().setAddWork(userBean.getAddWork());
            Config.getInstance().setHomeLatitude(userBean.getHomeLatitude());
            Config.getInstance().setHomeLongitude(userBean.getHomeLongitude());
            Config.getInstance().setWorkLatitude(userBean.getWorkLatitude());
            Config.getInstance().setWorkLongitude(userBean.getWorkLongitude());
            setUser();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setUser() {
        String username;
        username = Config.getInstance().getName();
    }

    private void resetViews(){
    feeds.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    pubs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    groups.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    pages.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    requests.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    messages.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    notifications.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    requests_rl.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    messages_rl.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    notifications_rl.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

//    requestsLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
//    messagesLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
//    notificationsLayout.setBackgroundColor(getResources().getColor(R.color.transparent));

    feeds.setImageResource(R.drawable.home_small_new);
    pubs.setImageResource(R.drawable.pub_small_new);
    groups.setImageResource(R.drawable.group_small_new);
    pages.setImageResource(R.drawable.page_small_new);
    requests.setImageResource(R.drawable.requests_small_new);
    messages.setImageResource(R.drawable.messages_small_new);
    notifications.setImageResource(R.drawable.notifications_small_new);

}
    private void setBackgroundFeedback(View view, int which){
        resetViews();

        switch(which){
            case 0:
                feeds.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                ((ImageButton) view).setImageResource(R.drawable.home_small_new_inv);
                break;
            case 1:
                pubs.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                ((ImageButton) view).setImageResource(R.drawable.pub_small_new_inv);
                break;
            case 2:
                groups.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                ((ImageButton) view).setImageResource(R.drawable.group_small_new_inv);
                break;
            case 3:
                pages.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                ((ImageButton) view).setImageResource(R.drawable.page_small_new_inv);
                break;
            case 4:
                view.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                requests_rl.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                ((ImageButton) view).setImageResource(R.drawable.requests_small_new_inv);
                break;
            case 5:
                view.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                messages_rl.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                ((ImageButton) view).setImageResource(R.drawable.messages_small_new_inv);
                break;
            case 6:
                view.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                notifications_rl.setBackgroundColor(getResources().getColor(R.color.bg_feedback));
                ((ImageButton) view).setImageResource(R.drawable.notifications_small_new_inv);
                break;
            default:
                Log.e(TAG, "Selected tab out of range.\n HINT:\nMaximum value must be < # of tabs.\nMinimum value = 0.\nReceived value: " + which +"\n");
                break;
        }
    }

    public void onSciencehubFeedsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        setBackgroundFeedback(view,0);
        ScienceHubActivity.webloadUrl("https://sciencehub.eg");

    }
    public void onSciencehubPubsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        setBackgroundFeedback(view,1);
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/publications");

    }
    public void onSciencehubGroupsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        setBackgroundFeedback(view,2);
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/groups");

    }
    public void onSciencehubPagesClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        setBackgroundFeedback(view,3);
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/pages");


    }
    public void onSciencehubRequestsClick(View view) {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            drawerLayout.closeDrawers();
            textRequests.setVisibility(View.INVISIBLE);
            setBackgroundFeedback(view,4);
            ShWebView.evaluateJavascript("var requests = document.getElementById(\"requests-list\"); if(requests != null) {requests.style.top=\"0\";} ", new android.webkit.ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.d("LogNameCommit", s);
                }
            });
            ShWebView.evaluateJavascript("var requests = document.getElementById(\"requests-list\"); if(requests != null) {requests.style.visibility='visible';} var req_click = document.getElementById(\"requests-dropdown\"); if(req_click != null) {req_click.click();}", new android.webkit.ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.d("LogNameCommit", s); // Prints: "this"
                }
            });

    }
    public void onSciencehubMessagesClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        textMessages.setVisibility(View.GONE);
        setBackgroundFeedback(view,5);

        ShWebView.evaluateJavascript("var messages = document.getElementById(\"messages-list\"); if(messages != null) {messages.style.visibility='visible'; messages.style.top=\"0\";} var messages_click = document.getElementById(\"messages-dropdown\"); if(messages_click != null) {messages_click.click();}", new android.webkit.ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("LogNameCommit", s); // Prints: "this"
            }
        });
        ShWebView.evaluateJavascript("document.getElementById(\"message-count\").innerHTML;", new android.webkit.ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("Message Count", s);
            }
        });



    }
    public void onSciencehubNotificationsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        textNotifications.setVisibility(View.INVISIBLE);
        setBackgroundFeedback(view,6);
        ShWebView.evaluateJavascript("var notifications = document.getElementById(\"notification-container\"); if(notifications != null) {notifications.style.top=\"0\";} ", new android.webkit.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("LogNameCommit", s);
                    }
                });
        ShWebView.evaluateJavascript("var notifications = document.getElementById(\"notification-container\"); if(notifications != null) {notifications.style.visibility='visible';} var notifications_click = document.getElementById(\"notifications-dropdown\"); if(notifications_click != null) {notifications_click.click();}", new android.webkit.ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("LogNameCommit", s); // Prints: "this"
            }
        });


    }
}


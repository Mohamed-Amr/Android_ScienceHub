package eg.mimocodes.sciencehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import eg.mimocodes.sciencehub.R;
import eg.mimocodes.sciencehub.app.App;
import eg.mimocodes.sciencehub.dialogs.PopupMessage;
import eg.mimocodes.sciencehub.util.AppConstants;

import eg.mimocodes.sciencehub.BuildConfig;

public class SplashActivity extends BaseAppCompatNoDrawerActivity {

    private static final String TAG = "Current Time";
    private String tripID = "";
    private String request_status = "";
    private String arrival_status = "";
    private View.OnClickListener snackBarRefreshOnClickListener;
    private PopupMessage popupMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Dynamically show the current app version
        TextView appVersion = (TextView) findViewById(R.id.version_textView);
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        if (BuildConfig.DEBUG) {
            appVersion.setText("Development " + versionName + "." + versionCode);
        } else {
            appVersion.setText("Release " + versionName + "." + versionCode);
        }


        getSupportActionBar().hide();
        swipeView.setPadding(0, 0, 0, 0);


        initViews();

      new Handler().postDelayed(splashTask, 2000);
    }

    private void initViews() {

        snackBarRefreshOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                mVibrator.vibrate(25);
                setProgressScreenVisibility(false, false);
                //getData(true);
            }
        };

    }


    private void getData(boolean isSwipeRefreshing) {
        swipeView.setRefreshing(isSwipeRefreshing);
        if (App.isNetworkAvailable()) {
            //fetchAppStatus();
        } else {
            Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.btn_retry, snackBarRefreshOnClickListener).show();
        }
    }

    Runnable splashTask = new Runnable() {
        @Override
        public void run() {
            navigate();
        }
    };




    private void navigate() {


        if (App.checkForToken() && fop.checkSPHash()) {
            Intent intent = new Intent(SplashActivity.this, ScienceHubActivity.class);
            intent.putExtra("url", "https://sciencehub.eg/");
            startActivity(intent);
            finish();
        } else

    {
        App.logout();
        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        finish();

    }

}



    private void showErrorPopup(String error) {
        if (popupMessage == null)
            popupMessage = new PopupMessage(SplashActivity.this);
        popupMessage.setPopupActionListener(new PopupMessage.PopupActionListener() {
            @Override
            public void actionCompletedSuccessfully(boolean result) {
                getData(false);
            }

            @Override
            public void actionFailed() {
                Toast.makeText(SplashActivity.this, R.string.message_thank_you, Toast.LENGTH_SHORT).show();
                App.logout();
                finish();
            }
        });
        popupMessage.show(error, 0, getString(R.string.btn_retry), getString(R.string.btn_cancel));
    }

}



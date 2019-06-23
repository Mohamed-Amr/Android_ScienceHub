package eg.mimocodes.sciencehub.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.Bitmap;


import eg.mimocodes.sciencehub.R;
import eg.mimocodes.sciencehub.util.FileOp;
import eg.mimocodes.sciencehub.utility.PermissionUtility;


public class BaseAppCompatNoDrawerActivity extends BaseActivity {


    protected FileOp fop = new FileOp(this);

    protected CoordinatorLayout lytBase;
    protected FrameLayout lytContent;
    protected SwipeRefreshLayout swipeView;
    protected CoordinatorLayout coordinatorLayout;
    protected Toolbar toolbar;
    protected View lytProgress;
    private ProgressBar progressBase;
    private View lytMessage;
    private TextView txtMessage;
    protected boolean isGetLocationEnabled = true;
    private int mStoredActivityRequestCode;
    private int mStoredActivityResultCode;
    private Intent mStoredActivityResultIntent;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                isBluetoothEnableRequestShown = false;
            } else {
                isBluetoothEnableRequestShown = false;
                Snackbar.make(coordinatorLayout, "Bluetooth Not Enabled", Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
            }
        }


    }

    public void initViewBase() {

        initBase();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lytContent = (FrameLayout) findViewById(R.id.lyt_contents_base_appcompat_no_drawer);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout_base_appcompat_no_drawer);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }*/

        lytProgress = (View) findViewById(R.id.lyt_progress_base);
        progressBase = (ProgressBar) findViewById(R.id.progress_base_appcompat_no_drawer);

        lytMessage = (View) findViewById(R.id.lyt_default_message_base_appcompat_no_drawer);
        txtMessage = (TextView) findViewById(R.id.txt_default_message_base_appcompat_no_drawer);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_base_appcompat_no_drawer);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);
        swipeView.setEnabled(false);
        swipeView.setProgressViewOffset(false, 0,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        getResources().getDisplayMetrics()) + mActionBarHeight));



  /*      if (android.os.Build.VERSION.SDK_INT >= 21) {
            swipeView.setPadding(0, (int) (getStatusBarHeight()), 0, 0);
        }*/

        setProgressScreenVisibility(false, false);
        setMessageScreenVisibility(false, getString(R.string.message_nothing_to_show));

    }

    @Override
    public void setContentView(final int layoutResID) {
        lytBase = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.layout_base_appcompat_no_drawer, null);
        lytContent = (FrameLayout) lytBase.findViewById(R.id.lyt_contents_base_appcompat_no_drawer);
        getLayoutInflater().inflate(layoutResID, lytContent, true);
        super.setContentView(lytBase);
        initViewBase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                lytContent.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                //mVibrator.vibrate(25);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* int getServerConnectionAvailableStatus(boolean isSnackbarEnabled) {
        if (Config.getInstance().getAuthToken() == null || Config.getInstance().getAuthToken().equals("")) {
            if (App.checkForToken() && !Config.getInstance().getAuthToken().equals("")) {
                if (App.isNetworkAvailable()) {
                    return SERVER_CONNECTION_AVAILABLE;
                } else {
                    if (isSnackbarEnabled)
                        Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG)
                                .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                    return NETWORK_NOT_AVAILABLE;
                }
            } else {
                if (isSnackbarEnabled)
                    Snackbar.make(coordinatorLayout, AppConstants.WEB_ERROR_MSG, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                return App.AUTH_TOKEN_NOT_AVAILABLE;
            }
        } else {
            if (App.isNetworkAvailable()) {
                return SERVER_CONNECTION_AVAILABLE;
            } else {
                if (isSnackbarEnabled)
                    Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                return NETWORK_NOT_AVAILABLE;
            }
        }
    }*/

    protected void setMessageScreenVisibility(boolean isScreenVisible, String message) {
        if (isScreenVisible) {
            lytMessage.setVisibility(View.VISIBLE);
            txtMessage.setText(message);
        } else
            lytMessage.setVisibility(View.GONE);
    }

    protected void setProgressScreenVisibility(boolean isScreenVisible, boolean isProgressVisible) {
        if (isScreenVisible) {
            lytProgress.setVisibility(View.VISIBLE);
            if (isProgressVisible) {
                progressBase.setVisibility(View.VISIBLE);
            } else {
                progressBase.setVisibility(View.GONE);
            }
        } else
            lytProgress.setVisibility(View.GONE);
    }


    public void onToolbarBackClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        onBackPressed();
    }

    public void onToolbarSearchClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

/*        startActivity(new Intent(this, SearchActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));*/

    }

    @Override
    public void onConnectionSuspended(int arg0) {

    }
    @Override
    public void onPageStarted(String url, Bitmap favicon) { }
    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtility.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_AND_CAMERA:
            case PermissionUtility.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
            case PermissionUtility.REQUEST_PERMISSION_ACCESS_LOCATION: {
                // if request is cancelled, the result arrays are empty
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // permission granted
                            if (requestCode == PermissionUtility.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_AND_CAMERA) {
                                // continue with activity result handling
                                if (mStoredActivityResultIntent != null) {
                                    ScienceHubActivity.ShWebView.onActivityResult(mStoredActivityRequestCode, mStoredActivityResultCode, mStoredActivityResultIntent);
                                    mStoredActivityRequestCode = 0;
                                    mStoredActivityResultCode = 0;
                                    mStoredActivityResultIntent = null;
                                }
                            }
                        } else {
                            // permission denied
                        }
                    }
                } else {
                    // all permissions denied
                }
                break;
            }
        }
    }

}

package eg.mimocodes.sciencehub.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.content.SharedPreferences;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.HashMap;

import eg.mimocodes.sciencehub.R;
import eg.mimocodes.sciencehub.advancedwebview.AdvancedWebView;
import eg.mimocodes.sciencehub.app.App;
import eg.mimocodes.sciencehub.config.Config;
import eg.mimocodes.sciencehub.listeners.PermissionListener;
import eg.mimocodes.sciencehub.listeners.UserInfoListener;
import eg.mimocodes.sciencehub.model.UserBean;
import eg.mimocodes.sciencehub.net.DataManager;
import eg.mimocodes.sciencehub.util.AppConstants;

import static eg.mimocodes.sciencehub.app.App.AUTH_TOKEN_NOT_AVAILABLE;
import static eg.mimocodes.sciencehub.app.App.NETWORK_NOT_AVAILABLE;
import static eg.mimocodes.sciencehub.app.App.SERVER_CONNECTION_AVAILABLE;


public class BaseAppCompatActivity extends BaseActivity {
    private static final String TAG = "BaseAppCompatActivity";

    static final int HOME_ACTIVITY = 1;

    private static int CURRENT_ACTIVITY = 1;


    protected boolean isGetLocationEnabled = true;

    protected LocationListener locationListener;
    private GoogleApiClient mGoogleApiClient;
    private static final LocationRequest mLocationRequest = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    FrameLayout lytContent;
    //protected FrameLayout leftDrawer;
    NavigationView leftDrawer;
    //	protected FrameLayout rightDrawer;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    //protected  RelativeLayout lytContents;
    protected View lytActivity;

    final Handler mHandler = new Handler();

    protected SwipeRefreshLayout swipeView;

    protected CoordinatorLayout coordinatorLayout;
    protected Toolbar toolbar;

    private MenuItem menuProgress;
    private View lytDrawer;
    private EditText etxtEmail;
    private EditText etxtPassword;
    private View lytDrawerHeader;
    private ImageView ivUserDP;
    private View lytProgress;
    private ProgressBar progressBase;
    private UserBean userBean;
    private ImageView ivProfilePhoto;
    private TextView txtName;
    private TextView txtEmail;
    private String txtUserID;
    private View lytMessage;
    private TextView txtMessage;
    public static final String ScienceHubpreference = "ShPref";
    SharedPreferences sharedpreferences ;
    //public static final String ScienceHubpreference = "ShPref";
    /*public static final String UserName = "nameKey";
    public static final String UserPass = "PassKey";
    public static final String ProfilePhoto = "ProfilePhoto";
    public static final String DisplayName = "DisplayName";
    public static final String WorkPosition = "WorkPosition";
    public static final String UserID = "IDKey";
    public static final String UserEmail = "UserEmail";*/



    /*	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.layout_base);

        getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);

    }
     */
    private void initViewBase() {

        initBase();

//        if (isGetLocationEnabled) {
        initReadWritePermissionCheck();
        //	getActionBar().setHomeButtonEnabled(true);

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionCheckCompleted(int requestCode, boolean isPermissionGranted) {
                if (requestCode == REQUEST_PERMISSIONS_LOCATION) {
                    if (isPermissionGranted) {
                        if (checkLocationSettingsStatus()) {
                            getCurrentLocation();
                        }
                    }
                }
            }
        };
        addPermissionListener(permissionListener);

//        }
//        FacebookSdk.sdkInitialize(this.getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //leftDrawer = (FrameLayout) findViewById(R.id.leftDrawer);
        leftDrawer = (NavigationView) findViewById(R.id.navigation_view_base_appcompat);
        //	rightDrawer = (FrameLayout)findViewById(R.id.rightDrawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_appcompat);
        lytContent = (FrameLayout) findViewById(R.id.lyt_contents_appcompat);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout_base_appcompat);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            toolbar.setPadding(0, App.getStatusBarHeight(getApplicationContext()), 0, 0);
            leftDrawer.setPadding(0, 0, 0, 0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        lytProgress = (View) findViewById(R.id.lyt_progress_base_appcompat);
        progressBase = (ProgressBar) findViewById(R.id.progress_base_appcompat);

        lytMessage = (View) findViewById(R.id.lyt_default_message_base_appcompat);
        txtMessage = (TextView) findViewById(R.id.txt_default_message_base_appcompat);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_base_appcompat);
        swipeView.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_blue_light,
                R.color.holo_green_light, R.color.holo_orange_light);
        swipeView.setEnabled(false);
        swipeView.setProgressViewOffset(false, 0,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()) + mActionBarHeight));

        //	llBottomBarActionPopup=(LinearLayout)findViewById(R.id.ll_bottombar_popmenu);

        FrameLayout.LayoutParams param1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, r.getDisplayMetrics());
        param2.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 155, r.getDisplayMetrics());

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                /*				if(view == rightDrawer) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, leftDrawer);
				} else if(view == leftDrawer) {
					drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, rightDrawer);
				}*/
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                /*				if(drawerView == rightDrawer) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, leftDrawer);
				} else if(drawerView == leftDrawer) {
					drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, rightDrawer);
				}*/
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);
        //drawerLayout.setDrawerShadow(R.drawable.shadow,


//        App.checkForToken();
        /*		Thread.setDefaultUncaughtExceptionHandler(this);
         */
        //	setupRightDrawer();
        setLeftDrawer();
        /*		setNotification();*/

        /*		mHandler.postDelayed(notiNotiTask, 1000);
        mHandler.postDelayed(notiReqTask, 2000);*/

        setProgressScreenVisibility(false, false);
        setMessageScreenVisibility(false, getString(R.string.message_nothing_to_show));

        mHandler.postDelayed(periodicTask, 3000);

    }

    public void onToolbarHomeClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        if (!drawerLayout.isDrawerOpen(leftDrawer)) {
            drawerLayout.openDrawer(leftDrawer);
        } else {
            drawerLayout.closeDrawer(leftDrawer);
        }
    }

    public void onToolbarSearchClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

/*        startActivity(new Intent(this, SearchActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));*/

    }

    private void initLocationPermissionCheck() {
        if (CURRENT_ACTIVITY == HOME_ACTIVITY) {
            if (!checkForLocationPermissions())
                getLocationPermissions();
            checkLocationSettingsStatus();
        }
    }

    private void initReadWritePermissionCheck() {
        if (getCurrentActivity() == HOME_ACTIVITY) {
            if (!checkForReadWritePermissions())
                getReadWritePermissions();
        }
    }

    private final Runnable periodicTask = new Runnable() {
        @Override
        public void run() {
            if (CURRENT_ACTIVITY != HOME_ACTIVITY) {
                getCurrentLocation();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

/*    @Override
    protected void onRestart() {
//        getWindow().setBackgroundDrawableResource(getWindowBackgroudResourse());
        super.onRestart();
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void onResume() {

        super.onResume();

        //if(Config.getInstance().getAuthToken()==null||Config.getInstance().getAuthToken().equals(""))
//        leftDrawer.setCheckedItem(getSelectedNavigationDrawerItem(CURRENT_ACTIVITY));

/*        if (!Config.getInstance().isLoggedIn() || Config.getInstance().getAuthToken() == null
                || Config.getInstance().getAuthToken().equals("")) {
            removeLoginDrawerFromNavigationView();
            addLoginDrawerToNavigationView();
        } else {
            removeLoginDrawerFromNavigationView();
        }*/

        if (Config.getInstance().getAuthToken() != null && !Config.getInstance().getAuthToken().equals("")) {
//            if (getServerConnectionAvailableStatus(false) == SERVER_CONNECTION_AVAILABLE) {
            if (App.isNetworkAvailable()) {
                //fetchUserInfo();
                setUser();
            }
        }
    }

    private void fetchUserInfo() {

        HashMap<String, String> urlParams = new HashMap<>();

        urlParams.put("auth_token", Config.getInstance().getAuthToken());

        DataManager.fetchUserInfo(urlParams, Config.getInstance().getUserID(), new UserInfoListener() {
            @Override
            public void onLoadCompleted(UserBean userBean) {
                System.out.println("Successfull  : UserBean : " + userBean);
                BaseAppCompatActivity.this.userBean = userBean;


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


        //sharedpreferences = getSharedPreferences(ScienceHubpreference,Context.MODE_PRIVATE);


        txtName.setText(LoginActivity.getDefaults("DisplayName", getBaseContext()));
        txtEmail.setText(LoginActivity.getDefaults("UserEmail", getBaseContext()));
        String profile_image_path = LoginActivity.getDefaults("ProfilePhoto",getBaseContext());
        Glide.with(getApplicationContext())
                .load(profile_image_path )
                .apply(new RequestOptions()
                        .error(R.drawable.avatar_2)
                        .fallback(R.drawable.avatar_2)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .circleCrop())
                .into(ivProfilePhoto);
    }

    @Override
    public void setContentView(final int layoutResID) {
        DrawerLayout lytBase = (DrawerLayout) getLayoutInflater().inflate(R.layout.layout_base_appcompat, null);
        lytContent = (FrameLayout) lytBase.findViewById(R.id.lyt_contents_appcompat);
        getLayoutInflater().inflate(layoutResID, lytContent, true);
        super.setContentView(lytBase);
        initViewBase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
/*        menuProgress = menu.findItem(R.id.action_progress);
        // Extract the action-view from the menu item
        ProgressBar progressActionBar = (ProgressBar) MenuItemCompat.getActionView(menuProgress);
        progressActionBar.setIndeterminate(true);*/
        /*		menuNotiItem = menu.findItem(R.id.action_notifications);
        MenuItemCompat.setActionView(menuNotiItem, R.layout.actionbar_notification_icon);
		menuNoti = (View) MenuItemCompat.getActionView(menuNotiItem);
		txtNoti = (TextView) menuNoti.findViewById(R.id.txt_action_notification);
		 *//*		menuNoti.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				//mVibrator.vibrate(25);
				if(isNotificationVisible){
					isNotificationVisible=false;
					notificationDrawer.setVisibility(View.GONE);
				}else{
					isNotificationVisible=true;
					notificationDrawer.setVisibility(View.VISIBLE);
				}
			}
		});*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                //mVibrator.vibrate(25);
                if (drawerLayout.isDrawerOpen(leftDrawer))
                    drawerLayout.closeDrawer(leftDrawer);
            /*			else if(drawerLayout.isDrawerOpen(rightDrawer))
                drawerLayout.closeDrawer(rightDrawer);*/
                else if (!drawerLayout.isDrawerOpen(leftDrawer))
                    drawerLayout.openDrawer(leftDrawer);
                return true;
/*            case R.id.action_search:
                drawerLayout.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                //mVibrator.vibrate(25);

                startActivity(new Intent(BaseAppCompatActivity.this, SearchActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;*/

        }
        return super.onOptionsItemSelected(item);
    }

    private void setLeftDrawer() {

        LayoutInflater inflater = getLayoutInflater();
        lytDrawer = inflater.inflate(R.layout.layout_drawer, null);

        ivProfilePhoto = (ImageView) lytDrawer.findViewById(R.id.iv_drawer_profile_photo);
        txtName = (TextView) lytDrawer.findViewById(R.id.txt_drawer_name);
        txtEmail = (TextView) lytDrawer.findViewById(R.id.txt_drawer_email);



//        leftDrawer.addView(lytDrawer);

/*        ivUserDP = (ImageView) lytDrawer.findViewById(R.id.iv_drawer_profile_photo);
        txtName = (CustomTextView) lytDrawer.findViewById(R.id.txt_drawer_name);
        txtPhone = (CustomTextView) lytDrawer.findViewById(R.id.txt_drawer_phone);
        txtEmail = (CustomTextView) lytDrawer.findViewById(R.id.txt_drawer_email);*/

        leftDrawer.addView(lytDrawer);

    }


    public void onDrawerEditClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        //mVibrator.vibrate(25);

        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }


    public void onDrawerBackupRestoreClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        //mVibrator.vibrate(25);

        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    public void onDrawerAboutUsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        //mVibrator.vibrate(25);

        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    public void onDrawerHelpClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        //mVibrator.vibrate(25);

        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);


    }

    public void onDrawerFeedbackClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        //mVibrator.vibrate(25);

        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    public void onDrawerSettingsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        //mVibrator.vibrate(25);

        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }


    private void clearApplicationData() {
        File cache = getApplication().getFilesDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                //		if (!s.equals("lib")) {
                (new File(appDir, s)).delete();
                //		}
            }
        }
    }

    /*
    private void saveToken() {
        System.out.println("SAVE STARTED");
        SharedPreferences preferences = getSharedPreferences(AppConstants.PREFERENCE_NAME_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_TOKEN, Config.getInstance().getAuthToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_USERID, Config.getInstance().getUserID());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_EMAIL, Config.getInstance().getEmail());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_PASSWORD, Config.getInstance().getPassword());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GENDER, Config.getInstance().getGender());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_DOB, Config.getInstance().getDOB());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_FBACCESSTOKEN, Config.getInstance().getFacebookAccessToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GOOGLEPLUSACCESSTOKEN, Config.getInstance().getGoogleAccessToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GCM_ID, Config.getInstance().getGCMID());
        editor.commit();
        fop.writeHash();
        System.out.println("SAVE COMPLETE");
    }
*/


    int getServerConnectionAvailableStatus(boolean isSnackbarEnabled) {
        if (Config.getInstance().getAuthToken() == null || Config.getInstance().getAuthToken().equals("")) {
            if (App.checkForToken() && !Config.getInstance().getAuthToken().equals("")) {
                if (Config.getInstance().isPhoneVerified()) {
                    if (App.isNetworkAvailable()) {
                        return SERVER_CONNECTION_AVAILABLE;
                    } else {
                        if (isSnackbarEnabled)
                            Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                        return NETWORK_NOT_AVAILABLE;
                    }
                } else {
//                    startActivity(new Intent(this, VerificationActivity.class));
                    return AUTH_TOKEN_NOT_AVAILABLE;
                }
            } else {
                if (isSnackbarEnabled)
                    Snackbar.make(coordinatorLayout, AppConstants.WEB_ERROR_MSG, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                return AUTH_TOKEN_NOT_AVAILABLE;
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
    }

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

    void setCurrentActivity(int currentActivity) {
        CURRENT_ACTIVITY = currentActivity;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(leftDrawer)) {
                drawerLayout.closeDrawer(leftDrawer);
            }
            /*			else if(drawerLayout.isDrawerOpen(rightDrawer)){
                drawerLayout.closeDrawer(rightDrawer);
			}*/
            else {
                onBackPressed();
//                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            openOptionsMenu();
        }
        return true;
    }






    void setUpLocationClientIfNeeded() {
       /* if(!checkForLocationPermissions())
            getLocationPermissions();*/


        if (App.getInstance().getGoogleApiClient() == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(App.getInstance().getApplicationContext())
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            //		mGoogleApiClient = new LocationClient(getApplicationContext(),this,this);
            App.getInstance().setGoogleApiClient(mGoogleApiClient);
        }
    }

    protected void getCurrentLocation() {
        setUpLocationClientIfNeeded();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!checkForLocationPermissions())
                getLocationPermissions();
        } else {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

                if (LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) != null) {
                    Config.getInstance().setCurrentLatitude(""
                            + LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude());
                    Config.getInstance().setCurrentLongitude(""
                            + LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude());
//                    getLocationName();
                }
            }
            /*else{
                System.out.println("Last Location : " + mockLocation);
				currentLatitude = ""+mockLocation.getLatitude();
				currentLongitude = ""+mockLocation.getLongitude();
			}*/
            /*if ((Config.getInstance().getCurrentLatitude() == null || Config.getInstance().getCurrentLongitude() == null)
                    || (Config.getInstance().getCurrentLatitude().equals("") || Config.getInstance().getCurrentLatitude().equals(""))) {
//            Toast.makeText(BaseAppCompatActivity.this, "Retrieving Current Location...", Toast.LENGTH_SHORT).show();

                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }

                //			mHandler.postDelayed(periodicTask, 3000);
            }*/ /*else {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.disconnect();
            }
        }*/
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if ((Config.getInstance().getCurrentLatitude() == null || Config.getInstance().getCurrentLongitude() == null)
                || (Config.getInstance().getCurrentLatitude().equals("") || Config.getInstance().getCurrentLatitude().equals(""))) {
            Config.getInstance().setCurrentLatitude("" + location.getLatitude());
            Config.getInstance().setCurrentLongitude("" + location.getLongitude());
        } else {
            Config.getInstance().setCurrentLatitude("" + location.getLatitude());
            Config.getInstance().setCurrentLongitude("" + location.getLongitude());
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }


    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult arg0) {
    }

    @Override
    public void onConnected(Bundle arg0) {
        try {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (!checkForLocationPermissions())
                    getLocationPermissions();
                checkLocationSettingsStatus();
            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                //	mGoogleApiClient.requestLocationUpdates(mLocationRequest,HomeActivity.this);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onConnectionSuspended(int arg0) {

    }
    public void onFeedsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/");
    }
    public void onProfileClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        String user_name = LoginActivity.getDefaults("UserName", getBaseContext());
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/"+ user_name + "/");
    }
    public void onSearchClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/search");
    }
    public void onGroupsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/groups");
    }
    public void onTechPagesClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/pages");
    }
    public void onPublicationsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/publications");
    }
    public void onMyOpportunitiesClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/my-opportunities");
    }
    public void onOpportunitiesClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/opportunities");
    }
    public void onMyPublicationsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/my-publications");
    }
    public void onMoviesClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/movies");
    }
    public void onEventsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/events");
    }
    public void onForumClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/forum");
    }
    public void onAlbumsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/albums");
    }
    public void onSettingClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/setting");
    }
    public void onLogoutClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        LoginActivity.setDefaults("IsLoggedIn", "0", getBaseContext());
        LoginActivity.setDefaults("AppLoggingOut", "1", getBaseContext());
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/logout");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                App.logout();

            }
        }, 2000);

        startActivity(new Intent(this, SplashActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    public void onEditClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        drawerLayout.closeDrawers();
        ScienceHubActivity.webloadUrl("https://sciencehub.eg/setting/"+ LoginActivity.getDefaults("UserName", getBaseContext()) + "/avatar-setting");
    }

}

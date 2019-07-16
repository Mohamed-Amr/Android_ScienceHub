package eg.mimocodes.sciencehub.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import eg.mimocodes.sciencehub.R;
import eg.mimocodes.sciencehub.app.App;
import eg.mimocodes.sciencehub.listeners.LoginListener;
import eg.mimocodes.sciencehub.model.AuthBean;
import eg.mimocodes.sciencehub.net.DataManager;
import eg.mimocodes.sciencehub.util.AppConstants;

import static java.lang.Thread.sleep;

public class LoginActivity extends BaseAppCompatNoDrawerActivity {

    private EditText etxtUserName;
    private EditText etxtPassword;
    private View.OnClickListener snackBarRefreshOnClickListener;
    private String TAG = " ";
    private String ful_phone_regix = "^[+]?[0-9]{13,13}$";
    private String phone_regix = "^[0-9]{11,11}$";
    public String deviceid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
        getSupportActionBar().hide();
        swipeView.setPadding(0, 0, 0, 0);
//        lytBase.setFitsSystemWindows(false);

        initViews();
        etxtUserName.setText(LoginActivity.getDefaults("UserName", getBaseContext()));
        etxtPassword.setText(LoginActivity.getDefaults("UserPass", getBaseContext()));

    }

    public void initViews() {

        etxtUserName = (EditText) findViewById(R.id.etxt_login_email);
        etxtPassword = (EditText) findViewById(R.id.etxt_login_password);

        etxtPassword.setTypeface(typeface);
        etxtUserName.setTypeface(typeface);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            onBackPressed();
        }
        return true;
    }

    public void onLoginButtonClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        if (collectLoginData()) {
            if (App.isNetworkAvailable()) {
                performLogin();
            } else {
                Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG)
                        .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
            }
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean collectLoginData() {

        if (etxtUserName.getText().toString().equals("")) {
            Snackbar.make(coordinatorLayout, R.string.message_email_is_required, Snackbar.LENGTH_LONG)
                    .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
            return false;
        } /*else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etxtUserName.getText().toString()).matches()) {
            Snackbar.make(coordinatorLayout, R.string.message_enter_a_valid_email_address, Snackbar.LENGTH_LONG)
                    .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
            return false;
        }*/
        if (etxtPassword.getText().toString().equals("")) {
            Snackbar.make(coordinatorLayout, R.string.message_password_is_required, Snackbar.LENGTH_LONG)
                    .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
            return false;
        }

        return true;
    }


    public void performLogin() {

        swipeView.setRefreshing(true);
        JSONObject postData = getLoginJSObj();

        DataManager.performLogin(postData, new LoginListener() {
            @Override
            public void onLoadCompleted(AuthBean authBean) {
                swipeView.setRefreshing(false);
                authBean.setPhoneVerified(true);
                App.saveToken(authBean);



                Toast.makeText(getApplicationContext(), R.string.message_login_is_successful,
                        Toast.LENGTH_LONG).show();

                setDefaults("UserName", etxtUserName.getText().toString(), getBaseContext());
                setDefaults("UserPass", etxtPassword.getText().toString(), getBaseContext());
                setDefaults("ProfilePhoto", authBean.getProfilePhoto(), getBaseContext());
                setDefaults("DisplayName", authBean.getName(), getBaseContext());
                setDefaults("WorkPosition", authBean.getWorkPosition(), getBaseContext());
                setDefaults("UserEmail", authBean.getEmail(), getBaseContext());
                setDefaults("IsLoggedIn", "0", getBaseContext());
                setDefaults("UserID", authBean.getUserID(), getBaseContext());
                setDefaults("AppLoggingOut", "0", getBaseContext());


                Intent intent = new Intent(LoginActivity.this, ScienceHubActivity.class);
                intent.putExtra("url", "https://sciencehub.eg/");
                intent.putExtra("username", etxtUserName.getText().toString());
                intent.putExtra("password", etxtPassword.getText().toString());
                startActivity(intent);
                finish();

            }

            @Override
            public void onLoadFailed(String error) {

                Snackbar.make(coordinatorLayout, R.string.message_login_failed, Snackbar.LENGTH_LONG)
                        .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                swipeView.setRefreshing(false);

            }
        });
    }

    private JSONObject getLoginJSObj() {
        JSONObject postData = new JSONObject();
        //JSONObject innerData = new JSONObject();

        String email="";

        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        deviceid = status.getSubscriptionStatus().getUserId();

        if (!etxtUserName.getText().toString().equals("")) {
            if (etxtUserName.getText().toString().matches(ful_phone_regix)) {
                email = etxtUserName.getText().toString();
            } else if (etxtUserName.getText().toString().matches(phone_regix)) {
                email = "+2" + etxtUserName.getText().toString();
            } else {
                email = etxtUserName.getText().toString();
            }
        }

        try {
            postData.put("credentials", "");
            postData.put("username", email);
            postData.put("password", etxtPassword.getText().toString());
            postData.put("device_id", deviceid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postData;
    }

    public void onForgotPasswordClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }
}


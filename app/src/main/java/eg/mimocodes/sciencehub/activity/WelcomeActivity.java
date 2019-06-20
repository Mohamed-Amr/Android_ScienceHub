package eg.mimocodes.sciencehub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import eg.mimocodes.sciencehub.R;
import eg.mimocodes.sciencehub.app.App;
import eg.mimocodes.sciencehub.listeners.BasicListener;
import eg.mimocodes.sciencehub.model.BasicBean;
import eg.mimocodes.sciencehub.net.DataManager;
import eg.mimocodes.sciencehub.util.FileOp;

public class WelcomeActivity extends BaseAppCompatNoDrawerActivity {

    //    private AuthConfig authConfig;
    private String TAG = "";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getSupportActionBar().hide();
        swipeView.setPadding(0, 0, 0, 0);
        builder = new AlertDialog.Builder(this);
        initViews();

        if (!checkForReadWritePermissions()) {
            getReadWritePermissions();
        } else {
            new FileOp(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.logout();
    }

    private void initViews() {

       /* AuthConfig.Builder builder = new AuthConfig.Builder();
        new Digits.Builder().withTheme(R.style.AppTheme).build();

        builder.withAuthCallBack(new AuthCallback() {

            @Override
            public void success(DigitsSession session, String phoneNumber) {

                *//*Toast.makeText(getApplicationContext(), "Your Phone Number Was Succesfully Verified",
                        Toast.LENGTH_LONG).show();*//*

                performMobileAvailabilityCheck(phoneNumber);

            }

            @Override
            public void failure(DigitsException exception) {

//                Toast.makeText(getApplicationContext(), "Phone Verification Failed..... Try Again!", Toast.LENGTH_LONG).show();

//                Log.i("Digits", "Sign in with Digits failure", exception);
            }
        });

        authConfig = builder.build();*/
    }

    public void onWelcomeLoginClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //mVibrator.vibrate(25);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void onWelcomeSignUpClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        openRegistrationDialog();
    }

    private JSONObject getMobileAvailabilityCheckJSObj(String phoneNumber) {

        JSONObject postData = new JSONObject();

        try {

            postData.put("phone", phoneNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postData;
    }
    public void openRegistrationDialog(){

        builder.setMessage("For more convenient registration process, we highly recommend doing so on a desktop computer.")
                .setCancelable(false)
                .setPositiveButton("Proceed anyway", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sciencehub.eg/register"));
                        startActivity(browserIntent);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("ScienceHub");
        alert.show();
    }
}

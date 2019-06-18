package eg.mimocodes.sciencehub.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import eg.mimocodes.sciencehub.R;
import eg.mimocodes.sciencehub.listeners.BasicListener;
import eg.mimocodes.sciencehub.listeners.PasswordResetListener;
import eg.mimocodes.sciencehub.model.BasicBean;
import eg.mimocodes.sciencehub.net.DataManager;


public class ForgotPasswordActivity extends BaseAppCompatNoDrawerActivity {

    private EditText etxtEmail;
    private String email;
    private String emailPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }

        getSupportActionBar().hide();
        swipeView.setPadding(0, 0, 0, 0);

        initViews();

    }

    private void initViews() {

        etxtEmail = (EditText) findViewById(R.id.etxt_email_forgot_password);
    }

    private boolean validateEmail() {

        email = etxtEmail.getText().toString();
        emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (!email.matches(emailPattern)) {
            Snackbar.make(coordinatorLayout, R.string.message_enter_a_valid_email_address, Snackbar.LENGTH_LONG)
                    .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
            return false;
        }
        return true;
    }


    public void performNewPassword() {

        swipeView.setRefreshing(true);
        JSONObject postData = getNewPasswordJSObj();

        DataManager.performNewPassword(postData, new PasswordResetListener() {

            @Override
            public void onLoadCompleted(BasicBean basicBean) {
                swipeView.setRefreshing(false);
                Toast.makeText(getApplicationContext(), R.string.message_your_new_password_is_sent_to_your_email_address,
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLoadFailed(String error) {
                swipeView.setRefreshing(false);
                Toast.makeText(getApplicationContext(), R.string.message_enter_a_valid_email_address,
                        Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    private JSONObject getNewPasswordJSObj() {

        JSONObject postData = new JSONObject();

        try {

            postData.put("recover_email", etxtEmail.getText().toString());
            postData.put("auth_token", R.string.api_key);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postData;
    }

    public void onSubmitButtonClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        if (validateEmail()){}
            performNewPassword();

    }
}
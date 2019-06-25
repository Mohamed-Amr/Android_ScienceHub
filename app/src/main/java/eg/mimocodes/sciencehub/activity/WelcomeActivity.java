package eg.mimocodes.sciencehub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.HapticFeedbackConstants;
import android.view.View;

import eg.mimocodes.sciencehub.R;
import eg.mimocodes.sciencehub.app.App;
import eg.mimocodes.sciencehub.util.FileOp;

public class WelcomeActivity extends BaseAppCompatNoDrawerActivity {

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getSupportActionBar().hide();
        swipeView.setPadding(0, 0, 0, 0);


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

     public void onWelcomeLoginClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void onWelcomeSignUpClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        openRegistrationDialog();
    }

    public void openRegistrationDialog(){
        builder = new AlertDialog.Builder(this);
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

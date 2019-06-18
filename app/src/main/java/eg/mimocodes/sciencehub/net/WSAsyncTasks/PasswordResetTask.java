package eg.mimocodes.sciencehub.net.WSAsyncTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import eg.mimocodes.sciencehub.model.BasicBean;
import eg.mimocodes.sciencehub.net.invokers.PasswordResetInvoker;


public class PasswordResetTask extends AsyncTask<String, Integer, BasicBean> {

    private PasswordResetListener passwordResetListener;

    private JSONObject postData;

    public PasswordResetTask(JSONObject postData) {
        super();
        this.postData = postData;
    }

    @Override
    protected BasicBean doInBackground(String... params) {
        System.out.println(">>>>>>>>>doInBackground");
        PasswordResetInvoker ResetInvoker = new PasswordResetInvoker(null, postData);
        return ResetInvoker.invokeLoginWS();
    }

    @Override
    protected void onPostExecute(BasicBean result) {
        super.onPostExecute(result);
        if (result != null)
            passwordResetListener.dataDownloadedSuccessfully(result);
        else
            passwordResetListener.dataDownloadFailed();
    }

    public static interface PasswordResetListener {
        void dataDownloadedSuccessfully(BasicBean basicBean);

        void dataDownloadFailed();
    }

    public PasswordResetListener getPasswordResetListener() {
        return passwordResetListener;
    }

    public void setLoginTaskListener(PasswordResetListener passwordResetListener) {
        this.passwordResetListener = passwordResetListener;
    }
}

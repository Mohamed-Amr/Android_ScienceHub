package eg.mimocodes.sciencehub.net.WSAsyncTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

import eg.mimocodes.sciencehub.model.AuthBean;
import eg.mimocodes.sciencehub.net.invokers.LoginInvoker;


public class LoginTask extends AsyncTask<String, Integer, AuthBean> {

    private LoginTaskListener loginTaskListener;

    private JSONObject postData;

    public LoginTask(JSONObject postData) {
        super();
        this.postData = postData;
    }

    @Override
    protected AuthBean doInBackground(String... params) {
        System.out.println(">>>>>>>>>doInBackground");
        LoginInvoker loginInvoker = new LoginInvoker(null, postData);
        return loginInvoker.invokeLoginWS();
    }

    @Override
    protected void onPostExecute(AuthBean result) {
        super.onPostExecute(result);
        if (result != null)
            loginTaskListener.dataDownloadedSuccessfully(result);
        else
            loginTaskListener.dataDownloadFailed();
    }

    public static interface LoginTaskListener {
        void dataDownloadedSuccessfully(AuthBean authBean);

        void dataDownloadFailed();
    }

    public LoginTaskListener getLoginTaskListener() {
        return loginTaskListener;
    }

    public void setLoginTaskListener(LoginTaskListener loginTaskListener) {
        this.loginTaskListener = loginTaskListener;
    }
}

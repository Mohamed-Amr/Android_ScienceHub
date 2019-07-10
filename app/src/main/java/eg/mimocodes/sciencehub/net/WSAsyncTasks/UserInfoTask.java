package eg.mimocodes.sciencehub.net.WSAsyncTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

import eg.mimocodes.sciencehub.model.UserBean;
import eg.mimocodes.sciencehub.net.invokers.UserInfoInvoker;

public class UserInfoTask extends AsyncTask<String, Integer, UserBean> {

    private UserInfoTaskListener userInfoTaskListener;

    private HashMap<String, String> urlParams;
    private JSONObject postData;

    public UserInfoTask(JSONObject postData) {
        super();
        this.postData = postData;
    }


    @Override
    protected UserBean doInBackground(String... params) {
        System.out.println(">>>>>>>>>doInBackground");
        UserInfoInvoker userInfoInvoker = new UserInfoInvoker(null, postData);
        return userInfoInvoker.invokeUserInfoWS();
    }

    @Override
    protected void onPostExecute(UserBean result) {
        if (result != null)
            userInfoTaskListener.dataDownloadedSuccessfully(result);
        else
            userInfoTaskListener.dataDownloadFailed();
    }

    public interface UserInfoTaskListener {
        void dataDownloadedSuccessfully(UserBean userBean);

        void dataDownloadFailed();
    }

    public UserInfoTaskListener getUserInfoTaskListener() {
        return userInfoTaskListener;
    }

    public void setUserInfoTaskListener(UserInfoTaskListener userInfoTaskListener) {
        this.userInfoTaskListener = userInfoTaskListener;
    }
}

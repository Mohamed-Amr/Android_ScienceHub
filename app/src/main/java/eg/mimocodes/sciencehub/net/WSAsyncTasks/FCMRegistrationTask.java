package eg.mimocodes.sciencehub.net.WSAsyncTasks;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;

public class FCMRegistrationTask extends AsyncTask<String, Integer, String> {
    private FCMRegistrationTaskListener gcmRegistrationTaskListener;

    @Override
    protected String doInBackground(String... params) {
        System.out.println(">>>>>>>>>doInBackground");
        String regID = "";
        regID = FirebaseInstanceId.getInstance().getToken();
        return regID;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null)
            gcmRegistrationTaskListener.dataDownloadedSuccessfully(result);
        else
            gcmRegistrationTaskListener.dataDownloadFailed();
    }


    public interface FCMRegistrationTaskListener {
        void dataDownloadedSuccessfully(String result);

        void dataDownloadFailed();
    }

    public FCMRegistrationTaskListener getFCMRegistrationTaskListener() {
        return gcmRegistrationTaskListener;
    }

    public void setFCMRegistrationTaskListener(FCMRegistrationTaskListener gcmRegistrationTaskListener) {
        this.gcmRegistrationTaskListener = gcmRegistrationTaskListener;
    }
}
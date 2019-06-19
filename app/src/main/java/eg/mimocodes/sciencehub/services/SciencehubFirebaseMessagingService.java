package eg.mimocodes.sciencehub.services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import eg.mimocodes.sciencehub.activity.ScienceHubActivity;

public class SciencehubFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "LFMService";
    //private String Notification_URL = "";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("Refreshed token:",token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData().get("custom"));

            JSONObject NotificationJson;
            JSONObject AJson;
            try {
                NotificationJson =new JSONObject(remoteMessage.getData().get("custom"));
                if(NotificationJson != null)
                {
                    try{
                        AJson = NotificationJson.getJSONObject("a");
                        Log.i(TAG, "Message URL: " + AJson.get("url"));
                        //InitiateScienceHubActivity(AJson.get("url").toString());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            Log.i(TAG, "Response: " + remoteMessage.getData().get("response"));


            String body = remoteMessage.getData().get("response");


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String body = remoteMessage.getNotification().getBody();

            //TripEndParser tripEndParser = new TripEndParser();
            //BasicBean basicBean = tripEndParser.parseBasicResponse(body);

            //if (basicBean == null)
            //    stopSelf();
            //else {
            //    if (basicBean.getArrivaltStatus().equalsIgnoreCase("1"))
             //   {
                    // Do nothing, Just skip

             //   }
             //   else if (basicBean.getStatus().equalsIgnoreCase("Success")) {
//                    initiateDriverRatingService(basicBean.getId());
              //      initiateDriverRatingService(basicBean.getId());
              //  } else if (basicBean.getStatus().equalsIgnoreCase("Error")) {
              //      stopSelf();
              //  } else {
              //      stopSelf();
              //  }
           // }


        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    public void InitiateScienceHubActivity(String url) {

        Log.i(TAG, "InitiateScienceHubActivity: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SERVICE STARTED>>>>>>>>>>>>>>>>>>>>>");

        startActivity(new Intent(this, ScienceHubActivity.class)
                .putExtra("url", "https://sciencehub.eg/" + url)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

    }
}

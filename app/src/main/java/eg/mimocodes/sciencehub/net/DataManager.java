package eg.mimocodes.sciencehub.net;

import org.json.JSONObject;

import java.util.HashMap;

import eg.mimocodes.sciencehub.listeners.BasicListener;
import eg.mimocodes.sciencehub.listeners.LoginListener;
import eg.mimocodes.sciencehub.listeners.PasswordResetListener;
import eg.mimocodes.sciencehub.listeners.RegistrationListener;
import eg.mimocodes.sciencehub.listeners.UserInfoListener;
import eg.mimocodes.sciencehub.model.AuthBean;
import eg.mimocodes.sciencehub.model.BasicBean;

import eg.mimocodes.sciencehub.model.UserBean;
import eg.mimocodes.sciencehub.net.WSAsyncTasks.LoginTask;
import eg.mimocodes.sciencehub.net.WSAsyncTasks.PasswordResetTask;
import eg.mimocodes.sciencehub.net.WSAsyncTasks.UpdateFCMTokenTask;
import eg.mimocodes.sciencehub.net.WSAsyncTasks.UserInfoTask;
import eg.mimocodes.sciencehub.util.AppConstants;

public class DataManager {

    public static void performUpdateFCMToken(JSONObject postData, final BasicListener listener) {

        UpdateFCMTokenTask updateFCMTokenTask = new UpdateFCMTokenTask(postData);
        updateFCMTokenTask.setUpdateFCMTokenTaskListener(new UpdateFCMTokenTask.UpdateFCMTokenTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(BasicBean basicBean) {
                if (basicBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (basicBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(basicBean);
                    } else if (basicBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(basicBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }
        });
        updateFCMTokenTask.execute();
    }



    /*public static void performOTPSubmit(JSONObject postData, final OTPSubmitListener listener) {

        OTPSubmitTask otpSubmitTask = new OTPSubmitTask(postData);
        otpSubmitTask.setOtpSubmitTaskListener(new OTPSubmitTask.OTPSubmitTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(OTPBean otpBean) {
                if (otpBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (otpBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(otpBean);
                    } else if (otpBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(otpBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }
        });
        otpSubmitTask.execute();
    }*/



    public static void performLogin(JSONObject postData, final LoginListener listener) {

        LoginTask loginTask = new LoginTask(postData);
        loginTask.setLoginTaskListener(new LoginTask.LoginTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(AuthBean authBean) {
                if (authBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (authBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(authBean);
                    } else if (authBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(authBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }
        });
        loginTask.execute();
    }


    public static void performNewPassword(JSONObject postData, final PasswordResetListener listener) {

        PasswordResetTask ResetTask = new PasswordResetTask(postData);
        ResetTask.setLoginTaskListener(new PasswordResetTask.PasswordResetListener() {
            @Override
            public void dataDownloadedSuccessfully(BasicBean basicBean) {
                if (basicBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (basicBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(basicBean);
                    } else if (basicBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(basicBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }
        });
        ResetTask.execute();
    }



    public static void fetchUserInfo(JSONObject postData, final UserInfoListener listener) {

        UserInfoTask userInfoTask = new UserInfoTask(postData);
        userInfoTask.setUserInfoTaskListener(new UserInfoTask.UserInfoTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(UserBean userBean) {
                if (userBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (userBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(userBean);
                    } else if (userBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(userBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }
        });
        userInfoTask.execute();
    }







}



    /*public static void fetchSearchResults(HashMap<String, String> urlParams, final SearchResultsListener listener){

        SearchResultsTask searchResultsTask = new SearchResultsTask(urlParams);
        searchResultsTask.setSearchResultsTaskListener(new SearchResultsTask.SearchResultsTaskListener() {

            @Override
            public void dataDownloadedSuccessfully(SearchResultsBean searchResultsBean) {
                if (searchResultsBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (searchResultsBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(searchResultsBean);
                    } else if (searchResultsBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(searchResultsBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);

            }
        });

        searchResultsTask.execute();
        }
    }*/


package eg.mimocodes.sciencehub.net.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import eg.mimocodes.sciencehub.app.App;
import eg.mimocodes.sciencehub.model.AuthBean;
import eg.mimocodes.sciencehub.model.UserBean;


public class UserInfoParser {

    public UserBean parseUserInfoResponse(String wsResponseString) {

        UserBean userBean = new UserBean();

        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(wsResponseString);


            if (jsonObj.has("error")) {
                JSONObject errorJSObj;
                try {
                    errorJSObj = jsonObj.getJSONObject("error");
                    if (errorJSObj != null) {
                        if (errorJSObj.has("message")) {
                            userBean.setErrorMsg(errorJSObj.optString("message"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userBean.setStatus("error");
            }
            if (jsonObj.has("status")) {
                userBean.setStatus(jsonObj.optString("status"));
                if (jsonObj.optString("status").equals("error")) {
                    if (jsonObj.has("message")) {
                        userBean.setErrorMsg(jsonObj.optString("message"));
                    } else {
                        userBean.setErrorMsg("Something Went Wrong. Please Try Again Later!!!");
                    }
                }
                if (jsonObj.optString("status").equals("500")) {
                    if (jsonObj.has("error")) {
                        userBean.setErrorMsg(jsonObj.optString("error"));
                    }
                }
                if (jsonObj.optString("status").equals("404")) {
                    if (jsonObj.has("error")) {
                        userBean.setErrorMsg(jsonObj.optString("error"));
                    }
                }
                if (jsonObj.has("message")) {
                    userBean.setErrorMsg(jsonObj.optString("message"));
                }
                if (jsonObj.optString("status").equals("updation success")) {
                    userBean.setStatus("success");
                }
            }
            try {
                if (jsonObj.has("message")) {
                    userBean.setWebMessage(jsonObj.optString("message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonObj.has("status")) {
                userBean.setStatus(jsonObj.optString("status"));
                if (jsonObj.optString("status").equals("notfound"))
                    userBean.setErrorMsg("Email is Incorrect");
                if (jsonObj.optString("status").equals("invalid"))
                    userBean.setErrorMsg("Password Is Incorrect");
                if (jsonObj.optString("status").equals("500")) {
                    if (jsonObj.has("error")) {
                        userBean.setErrorMsg(jsonObj.optString("error"));
                    }
                }
            }

            if (jsonObj.has("error")) {
                userBean.setErrorMsg(jsonObj.optString("error"));
            }
            if (jsonObj.has("response")) {
                userBean.setErrorMsg(jsonObj.optString("response"));
            }


            if (jsonObj.has("data")) {
                JSONObject dataObj = jsonObj.optJSONObject("data");
                if (dataObj != null) {
                    try {


                        if (dataObj.has("user_id")) {
                            userBean.setUserID(dataObj.optString("user_id"));
                        }
                        if (dataObj.has("profilephoto")) {
                            userBean.setProfilePhoto(App.getImagePath(dataObj.optString("profilephoto")));
                        }
                        if (dataObj.has("name")) {
                            userBean.setName(dataObj.optString("name"));
                        }
                        if (dataObj.has("workPosition")) {
                            userBean.setWorkPosition(dataObj.optString("workPosition"));
                        }

                        if (dataObj.has("email")) {
                            userBean.setEmail(dataObj.optString("email"));
                        }
                        if (dataObj.has("user")) {
                            JSONObject userObj = dataObj.optJSONObject("user");

                            if (userObj != null) {
                                if (userObj.has("user_id")) {
                                    userBean.setUserID(userObj.optString("user_id"));
                                }
                                if (userObj.has("id")) {
                                    userBean.setUserID(userObj.optString("id"));
                                }
                                if (userObj.has("profile_photo")) {
                                    userBean.setProfilePhoto(App.getImagePath(userObj.optString("profile_photo")));
                                }
                                if (userObj.has("name")) {
                                    userBean.setName(userObj.optString("name"));
                                }
                                if (userObj.has("workPosition")) {
                                    userBean.setWorkPosition(userObj.optString("workPosition"));
                                }
                                if (userObj.has("email")) {
                                    userBean.setEmail(userObj.optString("email"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return userBean;
    }
}

package eg.mimocodes.sciencehub.net.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import eg.mimocodes.sciencehub.app.App;
import eg.mimocodes.sciencehub.model.AuthBean;


public class LoginParser {

    public AuthBean parseLoginResponse(String wsResponseString) {

        AuthBean authBean = new AuthBean();

        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(wsResponseString);


            if (jsonObj.has("error")) {
                JSONObject errorJSObj;
                try {
                    errorJSObj = jsonObj.getJSONObject("error");
                    if (errorJSObj != null) {
                        if (errorJSObj.has("message")) {
                            authBean.setErrorMsg(errorJSObj.optString("message"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                authBean.setStatus("error");
            }
            if (jsonObj.has("status")) {
                authBean.setStatus(jsonObj.optString("status"));
                if (jsonObj.optString("status").equals("error")) {
                    if (jsonObj.has("message")) {
                        authBean.setErrorMsg(jsonObj.optString("message"));
                    } else {
                        authBean.setErrorMsg("Something Went Wrong. Please Try Again Later!!!");
                    }
                }
                if (jsonObj.optString("status").equals("500")) {
                    if (jsonObj.has("error")) {
                        authBean.setErrorMsg(jsonObj.optString("error"));
                    }
                }
                if (jsonObj.optString("status").equals("404")) {
                    if (jsonObj.has("error")) {
                        authBean.setErrorMsg(jsonObj.optString("error"));
                    }
                }
                if (jsonObj.has("message")) {
                    authBean.setErrorMsg(jsonObj.optString("message"));
                }
                if (jsonObj.optString("status").equals("updation success")) {
                    authBean.setStatus("success");
                }
            }
            try {
                if (jsonObj.has("message")) {
                    authBean.setWebMessage(jsonObj.optString("message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonObj.has("status")) {
                authBean.setStatus(jsonObj.optString("status"));
                if (jsonObj.optString("status").equals("notfound"))
                    authBean.setErrorMsg("Email is Incorrect");
                if (jsonObj.optString("status").equals("invalid"))
                    authBean.setErrorMsg("Password Is Incorrect");
                if (jsonObj.optString("status").equals("500")) {
                    if (jsonObj.has("error")) {
                        authBean.setErrorMsg(jsonObj.optString("error"));
                    }
                }
            }

            if (jsonObj.has("error")) {
                authBean.setErrorMsg(jsonObj.optString("error"));
            }
            if (jsonObj.has("response")) {
                authBean.setErrorMsg(jsonObj.optString("response"));
            }


            if (jsonObj.has("data")) {
                JSONObject dataObj = jsonObj.optJSONObject("data");
                if (dataObj != null) {
                    try {

                        if (dataObj.has("token")) {
                            authBean.setAuthToken(dataObj.optString("token"));
                        }
                        if (dataObj.has("auth_token")) {
                            authBean.setAuthToken(dataObj.optString("auth_token"));
                        }
                        if (dataObj.has("user_id")) {
                            authBean.setUserID(dataObj.optString("user_id"));
                        }
                        if (dataObj.has("profilephoto")) {
                            authBean.setProfilePhoto(App.getImagePath(dataObj.optString("profilephoto")));
                        }
                        if (dataObj.has("name")) {
                            authBean.setName(dataObj.optString("name"));
                        }
                        if (dataObj.has("workPosition")) {
                            authBean.setWorkPosition(dataObj.optString("workPosition"));
                        }
                        if (dataObj.has("phone")) {
                            authBean.setPhone(dataObj.optString("phone"));
                        }
                        if (dataObj.has("email")) {
                            authBean.setEmail(dataObj.optString("email"));
                        }
                        if (dataObj.has("user")) {
                            JSONObject userObj = dataObj.optJSONObject("user");

                            if (userObj != null) {
                                if (userObj.has("auth_token")) {
                                    authBean.setAuthToken(userObj.optString("auth_token"));
                                }
                                if (userObj.has("username")) {
                                    authBean.setUserName(userObj.optString("username"));
                                }
                                if (userObj.has("user_id")) {
                                    authBean.setUserID(userObj.optString("user_id"));
                                }
                                if (userObj.has("id")) {
                                    authBean.setUserID(userObj.optString("id"));
                                }
                                if (userObj.has("profile_photo")) {
                                    authBean.setProfilePhoto(App.getImagePath(userObj.optString("profile_photo")));
                                }
                                if (userObj.has("name")) {
                                    authBean.setName(userObj.optString("name"));
                                }
                                if (userObj.has("workPosition")) {
                                    authBean.setWorkPosition(userObj.optString("workPosition"));
                                }
                                if (userObj.has("phone")) {
                                    authBean.setPhone(userObj.optString("phone"));
                                }
                                if (userObj.has("email")) {
                                    authBean.setEmail(userObj.optString("email"));
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
        return authBean;
    }
}

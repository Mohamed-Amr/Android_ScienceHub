package eg.mimocodes.sciencehub.net.invokers;

import org.json.JSONObject;

import java.util.HashMap;


public class BaseInvoker {
    HashMap<String, String> urlParams;
    JSONObject postData;


    BaseInvoker(HashMap<String, String> urlParams, JSONObject postData) {
        super();
        this.urlParams = urlParams;
        this.postData = postData;
    }

    BaseInvoker() {
        super();

    }

    public HashMap<String, String> getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(HashMap<String, String> urlParams) {
        this.urlParams = urlParams;
    }

    public JSONObject getPostData() {
        return postData;
    }

    public void setPostData(JSONObject postData) {
        this.postData = postData;
    }
}

package eg.mimocodes.sciencehub.net.invokers;

import org.json.JSONObject;

import java.util.HashMap;

import eg.mimocodes.sciencehub.model.AuthBean;
import eg.mimocodes.sciencehub.model.UserBean;
import eg.mimocodes.sciencehub.net.ServiceNames;
import eg.mimocodes.sciencehub.net.WebConnector;
import eg.mimocodes.sciencehub.net.parsers.LoginParser;
import eg.mimocodes.sciencehub.net.parsers.UserInfoParser;
import eg.mimocodes.sciencehub.net.utils.WSConstants;

public class UserInfoInvoker extends BaseInvoker {

    public UserInfoInvoker() {
        super();
    }

    public UserInfoInvoker(HashMap<String, String> urlParams,
                          JSONObject postData) {
        super(urlParams, postData);
    }

    public UserBean invokeUserInfoWS() {

        WebConnector webConnector;
        System.out.println("POSTDATA===>>>>>>>" + postData);

        webConnector = new WebConnector(new StringBuilder(ServiceNames.USER_INFO), WSConstants.PROTOCOL_HTTP, null, postData);

        String wsResponseString = webConnector.connectToPOST_service();
        System.out.println(">>>>>>>>>>> response: " + wsResponseString);
        UserBean userBean = null;
        if (wsResponseString.equals("")) {
            return userBean = null;
        } else {
            userBean = new UserBean();
            UserInfoParser userinfoParser = new UserInfoParser();
            userBean = userinfoParser.parseUserInfoResponse(wsResponseString);
            return userBean;
        }
    }
}

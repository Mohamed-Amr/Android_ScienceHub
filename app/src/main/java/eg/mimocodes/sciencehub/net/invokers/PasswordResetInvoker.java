package eg.mimocodes.sciencehub.net.invokers;

import org.json.JSONObject;

import java.util.HashMap;

import eg.mimocodes.sciencehub.model.BasicBean;
import eg.mimocodes.sciencehub.net.ServiceNames;
import eg.mimocodes.sciencehub.net.WebConnector;
import eg.mimocodes.sciencehub.net.parsers.PasswordResetParser;
import eg.mimocodes.sciencehub.net.utils.WSConstants;

public class PasswordResetInvoker extends BaseInvoker {

    public PasswordResetInvoker() {
        super();
    }

    public PasswordResetInvoker(HashMap<String, String> urlParams,
                                JSONObject postData) {
        super(urlParams, postData);
    }

    public BasicBean invokeLoginWS() {

        System.out.println("POSTDATA>>>>>>>" + postData);

        WebConnector webConnector;

        webConnector = new WebConnector(new StringBuilder(ServiceNames.NEW_PASSWORD), WSConstants.PROTOCOL_HTTP, null, postData);

        //		webConnector= new WebConnector(new StringBuilder(ServiceNames.AUTH_EMAIL), WSConstants.PROTOCOL_HTTP, postData,null);
        //webConnector= new WebConnector(new StringBuilder(ServiceNames.MODELS), WSConstants.PROTOCOL_HTTP, null);
        String wsResponseString = webConnector.connectToPOST_service();
        //	String wsResponseString=webConnector.connectToGET_service(true);
        System.out.println(">>>>>>>>>>> response: " + wsResponseString);
        BasicBean basicBean = null;
        if (wsResponseString.equals("")) {
            return basicBean = null;
        } else {
            basicBean = new BasicBean();
            PasswordResetParser resetParser = new PasswordResetParser();
            basicBean = resetParser.parseLoginResponse(wsResponseString);
            return basicBean;
        }
    }
}

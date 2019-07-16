package eg.mimocodes.sciencehub.net;


public class ServiceNames {

    /*Set BASE URL here*/
    private static final String PRODUCTION_API = "https://sciencehub.eg";


    /* Set API VERSION here*/
    public static final String API_VERSION = "/";
 

    /*Set UPLOAD PATH. DO NOT CHANGE THIS UNLESS YOU KNOW WHAT YOU ARE DOING.*/
    public static final String PATH_UPLOADS = "/sciencehub";

    /*Set API URL here*/
    private static final String API = PRODUCTION_API + API_VERSION;
    public static final String API_UPLOADS = PRODUCTION_API + PATH_UPLOADS;
    public static final String USER_LOGIN = API + "do_login.php?";
    public static final String NEW_PASSWORD = API + "do_login.php?";
    public static final String UPDATE_FCM_TOKEN = API + "/save_fcmtoken?";
    public static final String USER_INFO = API + "do_login.php?";





}

package eg.mimocodes.sciencehub.listeners;


import eg.mimocodes.sciencehub.model.UserBean;

public interface UserInfoListener {

    void onLoadCompleted(UserBean userBean);

    void onLoadFailed(String error);

}

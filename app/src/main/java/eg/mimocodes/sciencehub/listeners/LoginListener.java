package eg.mimocodes.sciencehub.listeners;


import eg.mimocodes.sciencehub.model.AuthBean;

public interface LoginListener {

    void onLoadCompleted(AuthBean authBean);

    void onLoadFailed(String error);
}

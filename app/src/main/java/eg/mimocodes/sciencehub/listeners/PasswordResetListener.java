package eg.mimocodes.sciencehub.listeners;


import eg.mimocodes.sciencehub.model.BasicBean;

public interface PasswordResetListener {

    void onLoadCompleted(BasicBean basicBean);

    void onLoadFailed(String error);
}

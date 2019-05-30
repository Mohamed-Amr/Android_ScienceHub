package eg.mimocodes.sciencehub.listeners;

import eg.mimocodes.sciencehub.model.BaseBean;
import eg.mimocodes.sciencehub.model.BasicBean;

public interface BasicListener {

    void onLoadCompleted(BasicBean basicBean);

    void onLoadFailed(String error);

}

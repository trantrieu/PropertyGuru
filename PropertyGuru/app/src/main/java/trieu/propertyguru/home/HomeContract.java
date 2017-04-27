package trieu.propertyguru.home;

import trieu.propertyguru.base.presenter.BaseActionUserInterface;
import trieu.propertyguru.base.presenter.BaseViewInterface;
import trieu.propertyguru.data.model.Item;

/**
 * Created by Apple on 4/24/17.
 */

public interface HomeContract {

    interface View extends BaseViewInterface{
        void showLoading();
        void setData(String [] arrayItem);
        void showError(String error);
    }

    interface UserAction extends BaseActionUserInterface{
        void getListHackerNews();
        void gotoPageDetail(Item item);
    }

}

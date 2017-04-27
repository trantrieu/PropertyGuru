package trieu.propertyguru.item;

import trieu.propertyguru.base.presenter.BaseActionUserInterface;
import trieu.propertyguru.base.presenter.BaseViewInterface;
import trieu.propertyguru.data.model.Item;

/**
 * Created by Apple on 4/24/17.
 */

public interface ItemContract {

    interface View extends BaseViewInterface{
        void showData(Item parent, Item item);
        void showErrorDialog(String error);
        void showError(String error);
    }

    interface UserAction extends BaseActionUserInterface{
        void getDetailItems(Item item);
    }

}

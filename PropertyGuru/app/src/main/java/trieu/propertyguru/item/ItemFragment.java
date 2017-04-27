package trieu.propertyguru.item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import org.parceler.Parcels;

import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;
import trieu.propertyguru.R;
import trieu.propertyguru.base.presenter.BaseActionUserInterface;
import trieu.propertyguru.base.view.BaseFragment;
import trieu.propertyguru.base.view.InfoView;
import trieu.propertyguru.base.view.comment.CommentView;
import trieu.propertyguru.base.view.dialog.DialogCreator;
import trieu.propertyguru.data.model.Item;

/**
 * Created by Apple on 4/24/17.
 */

public class ItemFragment extends BaseFragment implements ItemContract.View{

    private Hashtable<Item, CommentView> hashtable = new Hashtable<>();

    @BindView(R.id.fragment_comment_container)
    LinearLayout containerView;

    @BindView(R.id.fragment_comment_info_view)
    InfoView infoView;

    private Item item;
    static public final String EXTRA_ITEM = "EXTRA_ITEM";
    private ItemContract.UserAction presenter;
    static public ItemFragment getInstance(Item item){
        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ITEM, Parcels.wrap(item));
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = Parcels.unwrap(getArguments().getParcelable(EXTRA_ITEM));
        item.setLevel(0);
    }

    @Override
    protected BaseActionUserInterface createActionUserInterface() {
        presenter = new ItemPresenter(this);
        return presenter;
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void onCreateView(View view) {
        CommentView.FIRST_COMMENT = true;
        ButterKnife.bind(this, view);
        infoView.setMainLayout(containerView);
        loadData();
    }

    private void loadData(){
        infoView.showLoading();
        if(item.getKids() == null || item.getKids().isEmpty()){
            showError(getString(R.string.empty_data));
        }else {
            presenter.getDetailItems(item);
        }
    }

    @Override
    protected String setTagName() {
        return ItemFragment.class.getSimpleName();
    }

    @Override
    public Context getCContext() {
        return getContext();
    }

    @Override
    public void showData(Item parent, Item item) {
        infoView.showMainView();
        CommentView commentView = new CommentView(getContext());
        if(item != null && item.getLevel() == 1) {
            containerView.addView(commentView);
            hashtable.put(item, commentView);

            //only more one time
            //presenter.getDetailItems(item);
        }else{
            CommentView parentView = hashtable.get(parent);
            parentView.addView(commentView);
        }
        commentView.init(item);
    }

    @Override
    public void showErrorDialog(String error) {
        DialogCreator.showDialogMessage(getContext(), error, null);
    }

    @Override
    public void showError(String error) {
        infoView.showError(error, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        hashtable.clear();
        super.onDestroyView();
    }
}

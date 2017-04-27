package trieu.propertyguru.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import trieu.propertyguru.R;
import trieu.propertyguru.base.action.RecyclerTouchListener;
import trieu.propertyguru.base.presenter.BaseActionUserInterface;
import trieu.propertyguru.base.view.BaseFragment;
import trieu.propertyguru.base.view.InfoView;

/**
 * Created by Apple on 4/24/17.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {
    private HomeContract.UserAction presenter;
    private ItemAdapter itemAdapter;
    @BindView(R.id.fragment_home_rv)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_home_info_view)
    InfoView infoView;

    @BindView(R.id.fragment_home_swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_home_btn)
    Button tmpBtn;

    static public HomeFragment getInstance(){
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(itemAdapter == null) {
            presenter.getListHackerNews();
        }else{
            infoView.showMainView();
            recyclerView.setAdapter(itemAdapter);
        }
    }

    @Override
    protected BaseActionUserInterface createActionUserInterface() {
        presenter = new HomePresenter(this);
        return presenter;
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateView(View view) {
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                presenter.gotoPageDetail(itemAdapter.getItemAt(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        infoView.setMainLayout(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getListHackerNews();
            }
        });

        tmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.gotoPageDetail(itemAdapter.getItemAt(0));
            }
        });
        tmpBtn.setVisibility(View.GONE);
    }

    @Override
    protected String setTagName() {
        return HomeFragment.class.getSimpleName();
    }

    @Override
    public Context getCContext() {
        return getContext();
    }

    @Override
    public void showLoading() {
        infoView.showLoading();
    }

    @Override
    public void setData(String[] arrayItem) {
        itemAdapter = new ItemAdapter(getContext(), arrayItem);
        recyclerView.setAdapter(itemAdapter);

        infoView.showMainView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {
        swipeRefreshLayout.setRefreshing(false);
        infoView.showError(error, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getListHackerNews();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if(itemAdapter != null){
            itemAdapter.destroy();
        }
        super.onDestroyView();
    }

}

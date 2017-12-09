package voora.com.queuedownloader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateActivity;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

public class MainActivity extends MvpLceViewStateActivity<RelativeLayout,List<DItem>,MainView,MainPresenter>
    implements  MainView {

    DItemsAdapter adapter;
    RecyclerView recyclerView;

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new DItemsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadData(false);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public List<DItem> getData() {
        return adapter == null ? null : adapter.getItemList();    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return "Error";
    }

    @NonNull
    @Override
    public LceViewState<List<DItem>, MainView> createViewState() {
        return new RetainingLceViewState<List<DItem>,MainView>();
    }

    @Override
    public void setData(List<DItem> data) {
        adapter.setItemList(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadDownloadItems();
    }
}

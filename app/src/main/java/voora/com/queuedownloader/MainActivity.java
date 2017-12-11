package voora.com.queuedownloader;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import voora.com.queuedownloader.database.DItem;
import voora.com.queuedownloader.downloader.DownloadService;

public class MainActivity extends MvpLceActivity<RelativeLayout,List<DItem>,MainView,MainPresenter>
    implements  MainView,MovieClickListener {

    private static final String TAG = "MAIN";
    private static final int WRITE_PERMISSION_REQUEST_CODE = 123;

    private DItemsAdapter adapter;
    private RecyclerView recyclerView;
    private DItem selectedItem;

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new DItemsAdapter(this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.addItem).setOnClickListener(v ->
                presenter.insertTestData(getApplicationContext()));

        loadData(false);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }


    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return "Error";
    }

    @Override
    public void showAddItem() {
        findViewById(R.id.addItem).setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyItemChanged(List<DItem> data, int position) {
        adapter.setItemList(data);
        adapter.notifyItemChanged(position);
    }


    @Override
    public void setData(List<DItem> data) {
        Toast.makeText(this, "refresh data", Toast.LENGTH_SHORT).show();
        adapter.setItemList(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadDownloadItems(getApplicationContext());
    }

    @Override
    public void onDownloadClick(DItem dItem) {
        selectedItem = dItem;
        askPermissionsAndStartDownload();
    }

    @Override
    public void onPauseClick(DItem dItem) {
        DownloadService.pauseDownload(this,dItem);
    }

    @Override
    public void onResumeClick(DItem dItem) {
        DownloadService.resumeDownload(this,dItem);
    }

    @Override
    public void onCancleClick(DItem dItem) {
        DownloadService.cancelDownload(this,dItem);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(WRITE_PERMISSION_REQUEST_CODE)
    private void askPermissionsAndStartDownload() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this,perms)){
            startFileDownload(selectedItem);
        } else {
            EasyPermissions.requestPermissions(this,"Please Give Permissions",
                    WRITE_PERMISSION_REQUEST_CODE,perms);
        }
    }

    private void startFileDownload(DItem selectedItem) {
        DownloadService.startDownload(this,selectedItem);
    }


}

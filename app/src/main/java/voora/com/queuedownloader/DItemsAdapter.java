package voora.com.queuedownloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import voora.com.queuedownloader.database.AppDatabase;
import voora.com.queuedownloader.database.DItem;
import voora.com.queuedownloader.downloader.DownloadState;

/**
 * * Created by tarun on 12/9/17.
 */

public class DItemsAdapter extends RecyclerView.Adapter<DItemsAdapter.ItemViewHolder> {

    @Nullable
    private List<DItem> itemList;
    private Context context;
    private MovieClickListener listener;

    DItemsAdapter(Context context, MovieClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.download_item,parent,false),context);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final DItem dItem = itemList.get(position);
        holder.setListeners(listener,dItem);
        holder.setDownloadSubscription(dItem);
        holder.setUI(dItem);
    }



    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public void setItemList(@NonNull List<DItem> itemList) {
        this.itemList = itemList;
    }

    @Nullable
    public List<DItem> getItemList() {
        return itemList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView downloadPercent;
        TextView downloadState;
        Button download;
        Button cancel;
        Button pause;
        Button resume;
        Context context;
        Disposable disposable;

        public ItemViewHolder(View itemView,Context context) {
            super(itemView);
            this.context = context;
            itemName = itemView.findViewById(R.id.itemName);
            downloadPercent = itemView.findViewById(R.id.downloadPercent);
            downloadState = itemView.findViewById(R.id.downloadState);
            download = itemView.findViewById(R.id.download);
            cancel = itemView.findViewById(R.id.cancel);
            pause = itemView.findViewById(R.id.pause);
            resume = itemView.findViewById(R.id.resume);
        }

        void setListeners(MovieClickListener listener ,DItem dItem) {
            download.setOnClickListener(v -> listener.onDownloadClick(dItem));
            cancel.setOnClickListener(v -> listener.onCancleClick(dItem));
            pause.setOnClickListener(v -> listener.onPauseClick(dItem));
            resume.setOnClickListener(v -> listener.onResumeClick(dItem));
        }

        void setUI(DItem dItem) {

            downloadPercent.setText(String.valueOf(dItem.getDownloadPercent()));
            itemName.setText(dItem.getFileName());
            download.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
            resume.setVisibility(View.GONE);

            if (dItem.getState() == DownloadState.DOWNLOADING.ordinal()) {
                downloadState.setText("DOWNLOADING");
                pause.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            } else if (dItem.getState() == DownloadState.PAUSED.ordinal()) {
                downloadState.setText("PAUSED");
                resume.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            } else if (dItem.getState() == DownloadState.QUEUED.ordinal()) {
                downloadState.setText("QUEUED");
                cancel.setVisibility(View.VISIBLE);
            } else if (dItem.getState() == DownloadState.CANCEL.ordinal()) {
                downloadState.setText("CANCELLED");
                download.setVisibility(View.VISIBLE);
            } else if (dItem.getState() == DownloadState.UNKNOWN.ordinal()) {
                downloadState.setText("UNKNOWN");
                download.setVisibility(View.VISIBLE);
            } else if (dItem.getState() == DownloadState.NORMAL.ordinal()) {
                downloadState.setText("NORMAL");
                download.setVisibility(View.VISIBLE);
            } else if (dItem.getState() == DownloadState.COMPLETED.ordinal()) {
                downloadState.setText("COMPLETED");
            }
        }

        void setDownloadSubscription(DItem dItem) {
           Flowable<DItem> dItemSubscriber = AppDatabase
                    .getAppDatabase(context.getApplicationContext())
                    .downloadDao().findItemById(dItem.getId());

           if(disposable != null) {
               return;
           }

           disposable = dItemSubscriber
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(dItem1 -> {
                  //  Log.d("Presenter", " changing data for item " + dItem1.getFileName());
                    setUI(dItem1);
               }, throwable -> {

               });
        }
    }
}



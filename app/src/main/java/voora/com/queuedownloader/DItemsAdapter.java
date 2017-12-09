package voora.com.queuedownloader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * * Created by tarun on 12/9/17.
 */

public class DItemsAdapter extends RecyclerView.Adapter<DItemsAdapter.ItemViewHolder> {

    private List<DItem> itemList;
    private Context context;

    DItemsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.download_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        DItem dItem = itemList.get(position);
        holder.downloadState.setText(String.valueOf(dItem.getState()));
        holder.downloadPercent.setText(String.valueOf(dItem.getDownloadPercent()));
        holder.itemName.setText(dItem.getFileName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<DItem> itemList) {
        this.itemList = itemList;
    }

    public List<DItem> getItemList() {
        return itemList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView downloadPercent;
        TextView downloadState;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            downloadPercent = itemView.findViewById(R.id.downloadPercent);
            downloadState = itemView.findViewById(R.id.downloadState);
        }
    }
}



package jp.aknot.retromo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {

    private final List<String> dataList;

    private final OnItemListener listener;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        final TextView textLabel;

        ItemViewHolder(View view) {
            super(view);
            this.textLabel = (TextView) view.findViewById(R.id.text_label);
        }
    }

    MyAdapter(List<String> dataList, OnItemListener listener) {
        super();
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.textLabel.setText(dataList.get(position));
        holder.textLabel.setOnClickListener(v -> listener.onItemClicked(v, position));
    }
}

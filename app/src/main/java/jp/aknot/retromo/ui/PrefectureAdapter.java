package jp.aknot.retromo.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jp.aknot.retromo.R;
import jp.aknot.retromo.data.Prefecture;

public class PrefectureAdapter extends RecyclerView.Adapter<PrefectureAdapter.ItemViewHolder> {

    private final List<Prefecture> prefectureList;

    private final OnItemListener listener;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        final TextView textLabel;

        ItemViewHolder(View view) {
            super(view);
            this.textLabel = (TextView) view.findViewById(R.id.text_label);
        }
    }

    PrefectureAdapter(List<Prefecture> prefectureList, OnItemListener listener) {
        super();
        this.prefectureList = prefectureList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return prefectureList.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        Prefecture prefecture = prefectureList.get(position);
        holder.textLabel.setText(prefecture.name);
        holder.textLabel.setOnClickListener(v -> listener.onItemClicked(v, position));
    }
}

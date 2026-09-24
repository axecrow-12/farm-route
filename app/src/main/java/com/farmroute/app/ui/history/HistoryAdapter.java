package com.farmroute.app.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.farmroute.app.R;
import com.farmroute.app.data.local.Diagnosis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

    private final List<Diagnosis> items = new ArrayList<>();
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US);

    public void submit(List<Diagnosis> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diagnosis, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Diagnosis d = items.get(position);
        holder.label.setText(d.predictedLabel.replace("___", ", ").replace("_", " "));
        holder.meta.setText(String.format(Locale.US, "%.0f%%  |  %s",
                d.confidence * 100, dateFormat.format(new Date(d.timestamp))));
        holder.sync.setText(d.syncedToServer ? R.string.synced : R.string.pending_sync);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView label, meta, sync;
        VH(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.item_label);
            meta = itemView.findViewById(R.id.item_meta);
            sync = itemView.findViewById(R.id.item_sync);
        }
    }
}

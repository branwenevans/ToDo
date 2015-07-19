package com.branwenevans.todo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<ListItem> dataSet;

    private List<ListItem> doneItems = new ArrayList<>();

    private boolean showCompletedTasks = true;

    public TodoAdapter(List<ListItem> dataSet) {
        this.dataSet = dataSet;
        for (ListItem item : dataSet) {
            if (item.isDone()) {
                doneItems.add(item);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = dataSet.get(position);
        if (!listItem.isDone() || showCompletedTasks) {
            holder.textView.setText(listItem.getLabel());
            holder.textView.getPaint().setStrikeThruText(listItem.isDone());
            holder.doneLink.setVisibility(listItem.isDone() ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public void toggleShowCompletedTasks() {
        showCompletedTasks = !showCompletedTasks;

        if (doneItems.size() > 0) {
            if (showCompletedTasks) {
                dataSet.addAll(doneItems);
                notifyItemRangeInserted(dataSet.size() - doneItems.size(), dataSet.size() - 1);
            } else {
                for (ListItem doneItem : doneItems) {
                    int index = dataSet.indexOf(doneItem);
                    dataSet.remove(index);
                    notifyItemRemoved(index);
                }
            }
        }
    }

    public void taskDone(int position) {
        dataSet.get(position).done();
        if (!showCompletedTasks) {
            doneItems.add(dataSet.remove(position));
            notifyItemRemoved(position);
        } else {
            doneItems.add(dataSet.get(position));
            notifyItemChanged(position);
        }
    }

    public void taskDeleted(int position) {
        doneItems.remove(dataSet.remove(position));
        notifyItemRemoved(position);
    }

    public void addItem(ListItem listItem) {
        dataSet.add(listItem);
        notifyItemInserted(dataSet.size() - 1);
    }

    public Collection<ListItem> getAllItems() {
        LinkedHashSet<ListItem> allItems = new LinkedHashSet<ListItem>(dataSet);
        allItems.addAll(doneItems);
        return allItems;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        private final ImageButton doneLink;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text);
            doneLink = (ImageButton) itemView.findViewById(R.id.item_done);
        }

    }
}

package com.branwenevans.todo;

import android.support.v7.widget.RecyclerView;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<ListItem> dataSet;

    public TodoAdapter(List<ListItem> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = dataSet.get(position);
        holder.textView.setText(listItem.getLabel());
        holder.textView.getPaint().setStrikeThruText(listItem.isDone());
        holder.doneLink.setVisibility(listItem.isDone() ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
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

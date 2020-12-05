package com.inti.student.a202sgi_source_joesentiow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderHistoryListAdapter extends RecyclerView.Adapter<OrderHistoryListAdapter.WordViewHolder> {
    private ArrayList<String> mTitleList;
    private ArrayList<String> mDateList;
    private LayoutInflater mInflater;

    OrderHistoryListAdapter(Context context, ArrayList<String> titleList, ArrayList<String> dateList) {
        mInflater = LayoutInflater.from(context);
        mTitleList = titleList;
        mDateList = dateList;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView titleListItem;
        final TextView dateListItem;
        final OrderHistoryListAdapter mAdapter;

        WordViewHolder(View itemView, OrderHistoryListAdapter adapter) {
            super(itemView);
            titleListItem   = itemView.findViewById(R.id.tvTitleHistory);
            dateListItem    = itemView.findViewById(R.id.tvDateHistory);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


    @NonNull
    @Override
    public OrderHistoryListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.order_history_list_item, viewGroup, false);
        return new WordViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryListAdapter.WordViewHolder wordViewHolder, int i) {
        String currentTitle = mTitleList.get(i);
        wordViewHolder.titleListItem.setText(currentTitle);

        String currentDate = mDateList.get(i);
        wordViewHolder.dateListItem.setText("Purchased on " + currentDate);
    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }
}

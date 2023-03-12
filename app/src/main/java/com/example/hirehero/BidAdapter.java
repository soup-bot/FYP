package com.example.hirehero;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.ViewHolder> {

    private ArrayList<Bid> mBidsList;
    private OnViewClickListener mListener;

    public interface OnViewClickListener {
        void onViewClick(int position);
    }

    public BidAdapter(ArrayList<Bid> bidsList, OnViewClickListener listener) {
        mBidsList = bidsList;
        mListener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bid currentBid = mBidsList.get(position);
        holder.amount.setText(String.valueOf(currentBid.getBidAmount()));
        holder.name.setText(currentBid.getBidderName());
        holder.contact.setText(currentBid.getBidderContact());

    }

    @Override
    public int getItemCount() {
        return mBidsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView amount;
        public TextView name;
        public TextView contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.tvservice2);
            name = itemView.findViewById(R.id.tvprice2);
            contact = itemView.findViewById(R.id.tvdetails2);
        }

    }
}
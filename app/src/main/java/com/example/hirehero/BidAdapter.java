package com.example.hirehero;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.ViewHolder> {

    private ArrayList<Bid> mBidsList;
    private OnViewClickListener mListener;
    private OnDeleteClickListener mDeleteListener;
    private Listing listing;

    private Bid bid;
    private int tempPos;
    public interface OnViewClickListener {
        void onViewClick(int position);

        void onDeleteClick(int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setListing(Listing listing){
       this.listing = listing;

    }


    public BidAdapter(ArrayList<Bid> bidsList, OnViewClickListener listener,OnDeleteClickListener deleteListener) {
        mBidsList = bidsList;
        mListener = listener;
        mDeleteListener = deleteListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Bid currentBid = mBidsList.get(position);
        tempPos = position;
        holder.amount.setText(String.valueOf(currentBid.getBidAmount()));
        holder.name.setText(currentBid.getBidderName());
        holder.contact.setText(currentBid.getBidderContact());
        Log.d("UID", "Bidder name = "+ currentBid.getBidderName());

        String service, details, contact, price, listername = "";
        if (currentBid.getListing() != null){
            service = currentBid.getListing().getService();
            details = currentBid.getListing().getDetails();
            contact = currentBid.getListing().getContact();
            price = currentBid.getListing().getPrice();
            listername = currentBid.getListing().getUserName();

            holder.getservice.setText(service);
            holder.getdetails.setText(details);
            holder.getcontact.setText(contact);
            holder.getprice.setText(price);
            holder.getname.setText(listername);
        }



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentBid.getUid().equals(currentUser.getUid())) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteListener != null) {
                        mDeleteListener.onDeleteClick(position);
                    }
                }
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onViewClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBidsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView amount;
        public TextView name;
        public TextView contact;
        public Button deleteButton;
        public TextView listingdetails;
        public CardView cardView;
        public TextView getservice, getdetails, getcontact, getprice, getname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Bid currentBid = mBidsList.get(tempPos);
            amount = itemView.findViewById(R.id.tvservice2);
            name = itemView.findViewById(R.id.tvprice2);
            contact = itemView.findViewById(R.id.tvdetails2);
            deleteButton = itemView.findViewById(R.id.deletebid);
            cardView = itemView.findViewById(R.id.listingcardref);

            getname = itemView.findViewById(R.id.listingref0);
            getservice = itemView.findViewById(R.id.listingref1);
            getdetails = itemView.findViewById(R.id.listingref2);
            getcontact = itemView.findViewById(R.id.listingref3);
            getprice = itemView.findViewById(R.id.listingref4);
           // listingdetails = itemView.findViewById(R.id.listingdetails);




        }

    }

}
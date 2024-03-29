//Adapter for recyclerview for services

package com.example.hirehero;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder>{

    Context context;
    private boolean showDeleteButton;
    private OnDeleteClickListener onDeleteClickListener;
    private BidAdapter.OnViewClickListener onViewClickListener;

    public ServiceAdapter(Context context, ArrayList<Listing> list, boolean showDeleteButton, OnDeleteClickListener onDeleteClickListener, BidAdapter.OnViewClickListener onViewClickListener) {
        this.context = context;
        this.list = list;
        this.showDeleteButton = showDeleteButton;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onViewClickListener = onViewClickListener;
    }

    ArrayList<Listing> list;

    public interface OnViewClickListener {
        void onViewClick(int position);
    }

    public interface OnDeleteClickListener {
        void onClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Listing listing = list.get(position);
        holder.service.setText(listing.getService());
        holder.price.setText(listing.getPrice());
        holder.details.setText(listing.getDetails());
        holder.contact.setText(listing.getContact());
        holder.name.setText(listing.getUserName());

        if (showDeleteButton) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.viewbids.setVisibility(View.VISIBLE);
            holder.clicktobid.setVisibility(View.GONE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteClickListener.onClick(position);
                }
            });
            holder.viewbids.setOnClickListener(v -> {
                if (onViewClickListener != null) {
                    onViewClickListener.onViewClick(position);
                }
            });

        } else {
            holder.viewbids.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Listing clickedListing = list.get(clickedPosition);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (clickedListing.getUserID().equals(currentUser.getUid())) {
                        //if user clicks their own listing, do nothing
                    } else {
                        //otherwise, start the biddingactivity
                        Intent intent = new Intent(context, BiddingActivity.class);
                        intent.putExtra("listing", clickedListing);
                        context.startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView service, price, details, contact, clicktobid, name;
        Button deleteButton, viewbids;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvname);
            service = itemView.findViewById(R.id.tvservice);
            price = itemView.findViewById(R.id.tvprice);
            details = itemView.findViewById(R.id.tvdetails);
            contact = itemView.findViewById(R.id.tvcontact);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            viewbids = itemView.findViewById(R.id.viewbids);
            clicktobid = itemView.findViewById(R.id.clicktobid);

        }
    }

    public void clearList(){
        list.clear();
    }
}
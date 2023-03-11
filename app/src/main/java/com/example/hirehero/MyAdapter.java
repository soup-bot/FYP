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

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    private boolean showDeleteButton;
    private OnDeleteClickListener onDeleteClickListener;

    public MyAdapter(Context context, ArrayList<Listing> list, boolean showDeleteButton, OnDeleteClickListener onDeleteClickListener) {
        this.context = context;
        this.list = list;
        this.showDeleteButton = showDeleteButton;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    ArrayList<Listing> list;

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

        if (showDeleteButton) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.viewbids.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteClickListener.onClick(position);
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
                    Intent intent = new Intent(context, BiddingActivity.class);
                    intent.putExtra("listing", clickedListing);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView service, price, details, contact;
        Button deleteButton, viewbids;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            service = itemView.findViewById(R.id.tvservice);
            price = itemView.findViewById(R.id.tvprice);
            details = itemView.findViewById(R.id.tvdetails);
            contact = itemView.findViewById(R.id.tvcontact);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            viewbids = itemView.findViewById(R.id.viewbids);

        }
    }

    public void clearList(){
        list.clear();
    }
}

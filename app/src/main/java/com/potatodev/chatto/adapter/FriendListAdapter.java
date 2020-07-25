package com.potatodev.chatto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potatodev.chatto.R;

import java.util.List;
import java.util.Map;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    List<Map<String, String>> friends;
    Context parentContext;

    public FriendListAdapter(List<Map<String, String>> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list, parent, false);
        parentContext = parent.getContext();

        return new FriendListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {
        Map<String, String> friend = friends.get(position);

        final String name = friend.get("name");
        holder.tvFriendName.setText(name);
        holder.llFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Chatroom between you and " + name + " is not created yet.", Toast.LENGTH_SHORT);
            }
        });

        holder.llFriendList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showToast("This feature is not implemented yet", Toast.LENGTH_SHORT);

                return true;
            }
        });

        holder.imgFriendPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You cannot see " + name + "'s profile yet", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class FriendListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFriendPP;
        TextView tvFriendName;
        LinearLayout llFriendList;

        public FriendListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFriendPP = itemView.findViewById(R.id.imgPPFriend);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            llFriendList = itemView.findViewById(R.id.llFriendList);
        }
    }

    public void showToast(String message, int duration){
        Toast.makeText(parentContext, message, duration).show();
    }
}

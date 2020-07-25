package com.potatodev.chatto.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potatodev.chatto.R;

import java.util.List;
import java.util.Map;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    List<Map<String, String>> friends;

    public FriendListAdapter(List<Map<String, String>> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list, parent, false);

        return new FriendListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {
        Map<String, String> friend = friends.get(position);

        String name = friend.get("name");
        holder.tvFriendName.setText(name);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class FriendListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFriendPP;
        TextView tvFriendName;

        public FriendListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFriendPP = itemView.findViewById(R.id.imgPPFriend);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
        }
    }
}

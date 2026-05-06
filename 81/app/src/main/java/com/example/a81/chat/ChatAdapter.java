package com.example.a81.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a81.R;
import com.example.a81.database.MessageEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_USER = 1;
    private static final int VIEW_BOT = 2;

    List<MessageEntity> list;

    public ChatAdapter(List<MessageEntity> list) {
        this.list = list;
    }

    public void updateList(List<MessageEntity> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isUser ? VIEW_USER : VIEW_BOT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MessageEntity msg = list.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = sdf.format(new Date(msg.timestamp));

        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).tvMessage.setText(msg.message);
            ((UserViewHolder) holder).tvTime.setText(time);
        } else {
            ((BotViewHolder) holder).tvMessage.setText(msg.message);
            ((BotViewHolder) holder).tvTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // VIEW HOLDERS

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;

        public BotViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
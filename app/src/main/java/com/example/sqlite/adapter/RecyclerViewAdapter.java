package com.example.sqlite.adapter;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlite.ModifyItemActivity;
import com.example.sqlite.R;
import com.example.sqlite.model.Item;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.HomeViewHolder> {
    private List<Item> list;
    private ItemListener itemListener;
    private ContextItemListener contextItemListener;

    public RecyclerViewAdapter() {
        list = new ArrayList<>();
    }

    public Item getItem(int position) {
        return list.get(position);
    }

    public void setList(List<Item> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setContextItemListener(ContextItemListener contextItemListener) {
        this.contextItemListener = contextItemListener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Item item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.category.setText(item.getCategory());
        holder.price.setText(item.getPrice() + "K");
        String sMonth = item.getDate().substring(5,7);
        String sDate = item.getDate().substring(8,10);
        holder.date.setText(sDate + "/" + sMonth + "/2022");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView title, category, price, date;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            category = itemView.findViewById(R.id.tvCategory);
            price = itemView.findViewById(R.id.tvPrice);
            date = itemView.findViewById(R.id.tvDate);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemListener != null) {
                itemListener.onItemClickListener(view, getAdapterPosition());
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem Edit = contextMenu.add(Menu.NONE, 1, 1, "Sửa");
            MenuItem Delete = contextMenu.add(Menu.NONE, 2, 2, "Xóa");

            Edit.setOnMenuItemClickListener(onChange);
            Delete.setOnMenuItemClickListener(onChange);
        }

        private final MenuItem.OnMenuItemClickListener onChange = item -> {
            if (contextItemListener != null) {
                switch (item.getItemId()) {
                    case 1:
                        contextItemListener.onContextItemClickListener(1, getAdapterPosition());
                        return true;
                    case 2:
                        contextItemListener.onContextItemClickListener(2, getAdapterPosition());
                        return true;
                }
            }
            return false;
        };
    }

    public interface ContextItemListener {
        void onContextItemClickListener(int action, int position);
    }

    public interface ItemListener {
        void onItemClickListener(View view, int position);
    }

}

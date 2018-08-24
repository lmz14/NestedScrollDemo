package com.lmz.viewdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lmz.viewdemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class ListAdapter extends RecyclerView.Adapter{

    private List<String> listData;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context context){
        this.context = context;
        listData = new ArrayList<>();
        for(int i=0;i<50;i++){
            listData.add("Item-"+i);
        }
        layoutInflater = LayoutInflater.from(context);
    }

    public ListAdapter(Context context,List<String> listData){
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(layoutInflater.inflate(R.layout.item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            String name = listData.get(position);
            holder.itemView.setTag(R.id.item_position,position);
            ((ItemViewHolder) holder).tvItemName.setText(name+"");
            ((ItemViewHolder) holder).tvItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"test item",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvItemName)
        TextView tvItemName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

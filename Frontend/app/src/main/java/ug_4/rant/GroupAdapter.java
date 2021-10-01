package ug_4.rant;

import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private ArrayList<GroupContainer> groupContainerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView info;


        public GroupViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.group_title);
            info = itemView.findViewById(R.id.group_info);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) ;
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public GroupAdapter(ArrayList<GroupContainer> list) {
        this.groupContainerList = list;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        GroupViewHolder gvh = new GroupViewHolder(v, this.listener);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupContainer current = groupContainerList.get(position);
        holder.image.setImageResource(current.getImageResource());
        holder.title.setText(current.getGroupName());
        holder.info.setText(current.getGroupInfo());
        if (current.isSelected()) {
            holder.title.setTextColor(Color.GREEN);
        } else {
            holder.title.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return groupContainerList.size();
    }

    public int getGroupIdInList(int pos){

        return groupContainerList.get(pos).getGroupID();

    }
    public String getGroupNameInList(int pos){
        return groupContainerList.get(pos).getGroupName();
    }
}

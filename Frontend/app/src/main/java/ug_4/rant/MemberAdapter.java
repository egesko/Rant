package ug_4.rant;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private ArrayList<MemberContainer> memberContainerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {

        public TextView email;
        public TextView info;


        public MemberViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            email = itemView.findViewById(R.id.member_name);
            info = itemView.findViewById(R.id.member_info);


        }
    }

    public MemberAdapter(ArrayList<MemberContainer> list) {
        this.memberContainerList = list;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
        MemberViewHolder gvh = new MemberViewHolder(v, this.listener);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        MemberContainer current = memberContainerList.get(position);

        holder.email.setText(current.getMemberEmail()+" ("+current.getMemberRole()+") ");
        holder.info.setText(current.getMemberRole());

    }

    @Override
    public int getItemCount() {
        return memberContainerList.size();
    }

    public String getMemberEmailInList(int pos){

        return memberContainerList.get(pos).getMemberEmail();

    }
    public String getMemberRoleInList(int pos){
        return memberContainerList.get(pos).getMemberRole();
    }

    public int getMemberIdInList(int pos){
        return memberContainerList.get(pos).getMemberID();
    }


}

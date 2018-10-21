package com.ithought.rahularity.missingchild.Home2;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ithought.rahularity.missingchild.R;
import com.ithought.rahularity.missingchild.models.Children;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.Url;

/**
 * Created by Rahul on 10/19/2017.
 */

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    private List<Children> children;
    private int layout;
    private Context context;

    public ChildAdapter(List<Children> children, int layout, Context context) {
        this.children = children;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent ,false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        holder.name.setText(children.get(position).getName());
        holder.father.setText(children.get(position).getFather());
        holder.contact.setText(children.get(position).getContact());
        holder.date.setText(children.get(position).getDate_of_missing());


        String url = "http://18.215.250.153:8000" + children.get(position).getImage_url();
        Picasso.get().load(url).into(holder.childPic);

    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder{

        CircleImageView childPic;
        TextView name;
        TextView father;
        TextView contact;
        TextView date;

        public ChildViewHolder(View itemView) {
            super(itemView);
            childPic = (CircleImageView) itemView.findViewById(R.id.child_photo);
            name = (TextView)itemView.findViewById(R.id.name);
            father = (TextView)itemView.findViewById(R.id.father_name);
            contact = (TextView)itemView.findViewById(R.id.contact);
            date = (TextView)itemView.findViewById(R.id.date);
        }
    }
}

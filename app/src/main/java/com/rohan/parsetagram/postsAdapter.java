package com.rohan.parsetagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class postsAdapter extends RecyclerView.Adapter<postsAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public postsAdapter(Context context, List<Post> posts){
        this.context=context;
        this.posts=posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView postImage;
        TextView postUsername;
        ImageView postUserImage;
        TextView postComment;
        TextView postCUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage= itemView.findViewById(R.id.postImage);
            postUsername=itemView.findViewById(R.id.postUsername);
            postUserImage=itemView.findViewById(R.id.postUserImage);
            postComment=itemView.findViewById(R.id.postComment);
            postCUsername=itemView.findViewById(R.id.postCUsername);
        }

        @SuppressLint("CheckResult")
        public void bind(Post post){
            postUsername.setText(post.getUser().getUsername().toString());

            postComment.setText(post.getDescription());
            postCUsername.setText(post.getUser().getUsername());
            Glide.with(context)
                    .load(post.getUser().getParseFile("image").getUrl())
                    .into(postUserImage);
            Glide.with(context)
                 .load(post.getImage().getUrl())
                 .dontAnimate()
                 .into(postImage);
        }
    }
}

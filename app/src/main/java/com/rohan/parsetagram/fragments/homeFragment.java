package com.rohan.parsetagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.rohan.parsetagram.Post;
import com.rohan.parsetagram.R;
import com.rohan.parsetagram.postsAdapter;

import java.util.List;


public class homeFragment extends Fragment {

    private RecyclerView rvPosts;
    private List<Post> posts;
    private postsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;


    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryPosts();
        adapter = new postsAdapter(getContext(), posts);
        rvPosts = view.findViewById(R.id.rvPosts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }


    private void refreshPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.setSkip(posts.size());
        try {
            posts.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(posts.size()>20){
            while(posts.size()>20){
                posts.remove(0);
            }
        }

    }
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        try {
            posts = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
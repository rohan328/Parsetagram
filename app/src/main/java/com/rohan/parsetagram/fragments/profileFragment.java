package com.rohan.parsetagram.fragments;

import android.content.Intent;
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
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rohan.parsetagram.LoginActivity;
import com.rohan.parsetagram.MainActivity;
import com.rohan.parsetagram.Post;
import com.rohan.parsetagram.R;
import com.rohan.parsetagram.postsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    private RecyclerView rvPosts;
    private List<Post> posts;
    private postsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private Button btnLogout;


    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnLogout=view.findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        swipeContainer = view.findViewById(R.id.swipeContainerProfile);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        posts = new ArrayList<>();

        queryPosts();
        adapter = new postsAdapter(getContext(), posts);
        rvPosts = view.findViewById(R.id.rvPostsProfile);
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
        posts.clear();
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
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
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.setLimit(20);
        try {
            posts.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
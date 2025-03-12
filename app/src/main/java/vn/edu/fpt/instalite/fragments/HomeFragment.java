package vn.edu.fpt.instalite.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.adapters.PostAdapter;
import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.fragments.home.HeaderFragment;
import vn.edu.fpt.instalite.fragments.home.PostListFragment;
import vn.edu.fpt.instalite.services.LikeCommentService;
import vn.edu.fpt.instalite.services.PostService;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostDTO> postList;
    private PostService postService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postService = new PostService(getContext());
        postList = postService.getAllPosts();
        Log.d("PostListSize", "Size: " + (postList != null ? postList.size() : "null"));

        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        postList.clear();
        postList.addAll(postService.getAllPosts());
        postAdapter.notifyDataSetChanged(); // Cập nhật dữ liệu cho RecyclerView
    }


}


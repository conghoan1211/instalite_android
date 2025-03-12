package vn.edu.fpt.instalite.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.adapters.SearchPostAdapter;
import vn.edu.fpt.instalite.adapters.SearchUserAdapter;
import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.services.PostService;
import vn.edu.fpt.instalite.services.UserService;

public class SearchFragment extends Fragment {
    private RecyclerView rvSearchPosts;
    private SearchView searchView;
    private SearchPostAdapter searchPostAdapter;
    private List<PostDTO> allPosts;
    private ListView lvUsers;
    private List<User> allUsers;
    private List<User> filteredUsers;
    private SearchUserAdapter userAdapter;

    private UserService userService;
    private PostService postService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        userService = new UserService(requireContext());
        postService = new PostService(requireContext());

        searchView = view.findViewById(R.id.searchView);
        rvSearchPosts = view.findViewById(R.id.rvSearchPosts);
        allPosts = postService.getAllPosts();
        searchPostAdapter = new SearchPostAdapter(requireContext(), allPosts);
        // Thiết lập RecyclerView dạng lưới (3 cột)
        rvSearchPosts.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rvSearchPosts.setAdapter(searchPostAdapter);

        lvUsers = view.findViewById(R.id.lvUsers);
        allUsers = userService.getAllUsers();
        filteredUsers = new ArrayList<>();
        userAdapter =  new SearchUserAdapter(requireContext(), filteredUsers);
        lvUsers.setAdapter( userAdapter);

        // Khi vừa vào Fragment, hiển thị danh sách bài viết, ẩn ListView
        lvUsers.setVisibility(View.GONE);
        rvSearchPosts.setVisibility(View.VISIBLE);

        // Xử lý tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPosts(query);
                // Tắt bàn phím và clear focus khi ấn Enter
                searchView.clearFocus();
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                lvUsers.setVisibility(View.GONE);
                rvSearchPosts.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.hasFocus()) {
                    // Chỉ khi đang focus vào SearchView mới hiển thị danh sách users
                    lvUsers.setVisibility(View.VISIBLE);
                    rvSearchPosts.setVisibility(View.GONE);
                    handleSearchQuery(newText);
                }
                return true;
            }
        });

        // Bắt sự kiện click vào ô search
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Khi focus vào SearchView, hiển thị danh sách users
                lvUsers.setVisibility(View.VISIBLE);
                rvSearchPosts.setVisibility(View.GONE);
                handleSearchQuery(searchView.getQuery().toString());
            } else {
                // Khi mất focus, hiển thị lại danh sách bài viết
                lvUsers.setVisibility(View.GONE);
                rvSearchPosts.setVisibility(View.VISIBLE);
                filterPosts(searchView.getQuery().toString());
            }
        });

        // Bắt sự kiện click vào item trong danh sách user
        lvUsers.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedUser = filteredUsers.get(position).getUsername();
            Toast.makeText(requireContext(), "Clicked: " + selectedUser, Toast.LENGTH_SHORT).show();

            ProfileFragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("otherUserId", filteredUsers.get(position).getUserId());
            fragment.setArguments(bundle);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameContainer, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            // Sau khi chọn user, hiển thị lại danh sách bài viết
            searchView.clearFocus();
            lvUsers.setVisibility(View.GONE);
            rvSearchPosts.setVisibility(View.VISIBLE);
        });
        return view;
    }

    private void handleSearchQuery(String query) {
        filteredUsers.clear();
        if (query == null || query.trim().isEmpty()) {
            userAdapter.notifyDataSetChanged();
            return;
        }

        for (User user : allUsers) {
            if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        userAdapter.notifyDataSetChanged();
    }

    // Lọc bài viết dựa trên query
    private void filterPosts(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchPostAdapter.updatePosts(allPosts); // Hiển thị tất cả nếu không có query
            return;
        }

        List<PostDTO> filteredPosts = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (PostDTO post : allPosts) {
            if (post.getUsername().toLowerCase().contains(lowerQuery) ||
                    post.getContent().toLowerCase().contains(lowerQuery)) {
                filteredPosts.add(post);
            }
        }
        searchPostAdapter.updatePosts(filteredPosts);
    }
    @Override
    public void onResume() {
        super.onResume();
        allPosts.clear();
        allUsers = userService.getAllUsers();
        allPosts.addAll(postService.getAllPosts());
        searchPostAdapter.notifyDataSetChanged(); // Cập nhật dữ liệu cho RecyclerView
    }
}
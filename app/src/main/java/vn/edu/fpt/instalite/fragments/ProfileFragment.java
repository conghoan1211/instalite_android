package vn.edu.fpt.instalite.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.activities.LoginActivity;
import vn.edu.fpt.instalite.adapters.PostAdapter;
import vn.edu.fpt.instalite.adapters.ProfilePostAdapter;
import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.services.PostService;
import vn.edu.fpt.instalite.services.UserService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;

public class ProfileFragment extends Fragment {
    private ImageView ivAvatar, ivMenuProfile, ivBackProfile;
    private TextView tvUsername, tvBio, tvPostCount, tvFollowersCount, tvFollowingCount;
    private Button btnEditProfile, btnAddFriend;
    private RecyclerView rvProfilePosts;
    private ProfilePostAdapter profilePostAdapter;
    private PostService postService;
    List<PostDTO> userPosts;
    private UserService userService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);

        postService = new PostService(getContext());
        userService = new UserService(getContext());
        initView();
        return view;
    }

    void initView() {
        // Lấy thông tin người dùng
        String otherUserId = "";
        Bundle args = getArguments();
        if (args != null && args.containsKey("otherUserId")) {
            otherUserId = args.getString("otherUserId");
        }
        User user;
        if (!otherUserId.isEmpty()) {
            user = userService.getUserById(otherUserId);
            btnEditProfile.setVisibility(View.GONE);
            btnAddFriend.setVisibility(View.GONE);
        } else {
            user = DataLocalManager.getUser();
            btnEditProfile.setVisibility(View.VISIBLE);
            btnAddFriend.setVisibility(View.VISIBLE);
        }
        if (user != null) {
            if (user.getAvatar() != null && user.getAvatar().startsWith("/")) {
                File imageFile = new File(user.getAvatar());
                if (imageFile.exists()) {
                    Glide.with(requireContext())
                            .load(imageFile)
                            .placeholder(R.drawable.img_default_avatar)
                            .into(ivAvatar);
                } else {
                    ivAvatar.setImageResource(R.drawable.img_default_avatar);
                }
            } else {
                ivAvatar.setImageResource(R.drawable.img_default_avatar);
            }
            tvUsername.setText(user.getUsername().toString());
            tvBio.setText(user.getBio()  != null ? user.getBio()  : "Chưa có bio" );
            tvFollowersCount.setText(String.valueOf(user.getFollowersCount()));
            tvFollowingCount.setText(String.valueOf(user.getFollowingCount()));
            tvPostCount.setText(String.valueOf(user.getPostsCount()));
        } else {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        userPosts = postService.getPostsByUserId(user.getUserId());
        rvProfilePosts.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        profilePostAdapter = new ProfilePostAdapter(requireContext(), userPosts);
        rvProfilePosts.setAdapter(profilePostAdapter);

        // Xử lý nút chỉnh sửa hồ sơ
        btnEditProfile.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameContainer, new EditProfileFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        ivMenuProfile.setOnClickListener(v -> showPostOptionsBottomSheet());

        ivBackProfile.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void showPostOptionsBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_profile_options, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        LinearLayout llLogout = bottomSheetView.findViewById(R.id.llLogout);
        User currentUser = DataLocalManager.getUser();

        llLogout.setOnClickListener(v -> {
            DataLocalManager.clearUser(); // nếu bạn có hàm clearUser() để xóa user đã lưu

            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa backstack
            startActivity(intent);

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    public void onResume() {
        super.onResume();
        userPosts.clear();
        initView();
        profilePostAdapter.notifyDataSetChanged(); // Cập nhật dữ liệu cho RecyclerView
    }

    void init(View view) {
        ivAvatar = view.findViewById(R.id.ivAvatarProfile);
        tvUsername = view.findViewById(R.id.tvUsernameProfile);
        tvBio = view.findViewById(R.id.tvBio);
        tvPostCount = view.findViewById(R.id.tvPostCount);
        tvFollowersCount = view.findViewById(R.id.tvFollowersCount);
        tvFollowingCount = view.findViewById(R.id.tvFollowingCount);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        rvProfilePosts = view.findViewById(R.id.rvPostProfile);
        ivMenuProfile = view.findViewById(R.id.ivMenuProfile);
        ivBackProfile = view.findViewById(R.id.ivBackProfile);
        btnAddFriend = view.findViewById(R.id.btnAddFriend);
    }
}
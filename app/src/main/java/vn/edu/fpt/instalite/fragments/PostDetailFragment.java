package vn.edu.fpt.instalite.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.activities.LoginActivity;
import vn.edu.fpt.instalite.adapters.PostAdapter;
import vn.edu.fpt.instalite.adapters.PostImageAdapter;
import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.services.LikeCommentService;
import vn.edu.fpt.instalite.services.PostService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;
import vn.edu.fpt.instalite.utils.Common;

public class PostDetailFragment extends Fragment {
    private ImageButton btnBack;
    private View postContentView;
    private PostDTO post;
    private PostAdapter.PostViewHolder postViewHolder;
    private PostService postService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lấy postId từ Bundle
        int postId = requireArguments().getInt("postId");
        postService = new PostService(requireContext());
        try {
            post = postService.getPostById(String.valueOf(postId));
        } catch (Exception e) {
            Common.showToast(requireContext(), e.getMessage().toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        // Khởi tạo view
        btnBack = view.findViewById(R.id.btnBack);
        postContentView = view.findViewById(R.id.postContent);

        // Gắn PostViewHolder vào nội dung bài post
        postViewHolder = new PostAdapter.PostViewHolder(postContentView);
        bindPostData();

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    // Gắn dữ liệu bài post vào giao diện
    private void bindPostData() {
        if (post == null) {
            Toast.makeText(requireContext(), "Không tìm thấy bài viết", Toast.LENGTH_SHORT).show();
            return;
        }
        postViewHolder.tvUsername.setText(post.getUsername());
        postViewHolder.tvLikeCounts.setText(String.valueOf(post.getLikes()));
        postViewHolder.tvCmtCounts.setText(String.valueOf(post.getComments()));

        String relativeTime = DateUtils.getRelativeTimeSpanString(post.getCreatedAt(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        postViewHolder.tvCreatedAt.setText(relativeTime);

        String fullText = post.getUsername() + " " + post.getContent();
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, post.getUsername().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(16, true), 0, post.getUsername().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        postViewHolder.tvCaption.setText(spannableString);

        postViewHolder.tvCaption.setOnClickListener(v -> {
            if (postViewHolder.tvCaption.getMaxLines() == 2) {
                postViewHolder.tvCaption.setMaxLines(Integer.MAX_VALUE);
                postViewHolder.tvCaption.setEllipsize(null);
            } else {
                postViewHolder.tvCaption.setMaxLines(2);
                postViewHolder.tvCaption.setEllipsize(TextUtils.TruncateAt.END);
            }
        });
        displayImageUrl(post.getAvatar(), postViewHolder.ivAvatar);

        PostImageAdapter imageAdapter = new PostImageAdapter(
                requireContext(), post.getImageUrls(), new LikeCommentService(requireContext()), post.getId(), postViewHolder, null
        );
        postViewHolder.vpPostImage.setAdapter(imageAdapter);
        postViewHolder.indicatorDotPost.setViewPager(postViewHolder.vpPostImage);
        if (post.getImageUrls().size() <= 1) {
            postViewHolder.indicatorDotPost.setVisibility(View.GONE);
        } else {
            postViewHolder.indicatorDotPost.setVisibility(View.VISIBLE);
        }
        // Toggle like
        postViewHolder.ivLike.setOnClickListener(v -> toggleLike(post));
    }

    private void toggleLike(PostDTO post) {
        LikeCommentService likeCommentService = new LikeCommentService(requireContext());
        try {
            User user = DataLocalManager.getUser();
            if (user == null) {
                Toast.makeText(requireContext(), "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), LoginActivity.class));
                return;
            }

            boolean result = likeCommentService.toggleLikePost(post.getId(), user.getUserId());
            if (result) {
                int newLikes = likeCommentService.getLikesPost(post.getId());
                post.setLikes(newLikes);
                postViewHolder.tvLikeCounts.setText(String.valueOf(newLikes));
                updateLikeIcon(post);
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLikeIcon(PostDTO post) {
        LikeCommentService likeCommentService = new LikeCommentService(requireContext());
        User user = DataLocalManager.getUser();
        boolean isLiked = user != null && likeCommentService.isLikedPost(post.getId(), user.getUserId());
        postViewHolder.ivLike.setImageResource(isLiked ? R.drawable.ic_heart_tym : R.drawable.ic_heart);
    }


    public void displayImageUrl(String imageUrl, ImageView imageViewv) {
        Log.d("posst detail url", "displayImageUrl: " + imageUrl);
        try {
            if (imageUrl.startsWith("/")) {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    Glide.with(getContext())
                            .load(imageFile)
                            .placeholder(R.drawable.img_default_avatar)
                            .into(imageViewv);
                } else {
                    imageViewv.setImageResource(R.drawable.img_default_avatar);
                }
            }
        } catch (Exception e) {
            Log.e("PostImageAdapter", "Error loading image: " + e.getMessage(), e);
            imageViewv.setImageResource(R.drawable.img_default_avatar);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int postId = getArguments().getInt("postId", -1);
        post = postService.getPostById(String.valueOf(postId));
        // Gắn PostViewHolder vào nội dung bài post
        postViewHolder = new PostAdapter.PostViewHolder(postContentView);
        bindPostData();
    }

}
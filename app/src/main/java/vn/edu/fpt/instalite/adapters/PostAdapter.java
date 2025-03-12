package vn.edu.fpt.instalite.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.activities.LoginActivity;
import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.services.LikeCommentService;
import vn.edu.fpt.instalite.services.PostService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;
import vn.edu.fpt.instalite.utils.Common;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<PostDTO> postList;
    private LikeCommentService likeCmtService;

    public PostAdapter(Context context, List<PostDTO> postList) {
        this.context = context;
        this.postList = postList;
        this.likeCmtService = new LikeCommentService(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostDTO post = postList.get(position);
        holder.tvUsername.setText(post.getUsername());
        holder.tvLikeCounts.setText(String.valueOf(post.getLikes()));
        holder.tvCmtCounts.setText(String.valueOf(post.getComments()));

        long createdAt = post.getCreatedAt();
        String relativeTime = DateUtils.getRelativeTimeSpanString(createdAt, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        holder.tvCreatedAt.setText(relativeTime);

        // Kết hợp username và caption
        String fullText = post.getUsername() + " " + post.getContent();
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, post.getUsername().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(16, true), 0, post.getUsername().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvCaption.setText(spannableString);

        // Xử lý click để mở rộng caption
        holder.tvCaption.setOnClickListener(v -> {
            if (holder.tvCaption.getMaxLines() == 2) {
                holder.tvCaption.setMaxLines(Integer.MAX_VALUE);
                holder.tvCaption.setEllipsize(null);
            } else {
                holder.tvCaption.setMaxLines(2);
                holder.tvCaption.setEllipsize(TextUtils.TruncateAt.END);
            }
        });
        displayImageUrl(post.getAvatar(), holder);
        // Tải avatar
//        Glide.with(context)
//                .load(context.getResources().getIdentifier(post.getAvatar(), "drawable", context.getPackageName()))
//                .placeholder(R.drawable.img_default_avatar)
//                .into(holder.ivAvatar);

        // Tải nhiều ảnh vào ViewPager2
        PostImageAdapter imageAdapter = new PostImageAdapter(context, post.getImageUrls(), likeCmtService, post.getId(), holder, postList);
        holder.vpPostImage.setAdapter(imageAdapter);
        CircleIndicator3 circleIndicator3 = holder.indicatorDotPost;
        circleIndicator3.setViewPager(holder.vpPostImage);

        // Toggle like khi click vào ivLike
        holder.ivLike.setOnClickListener(v -> toggleLike(post, holder));

        // Khởi tạo trạng thái ban đầu của ivLike
        updateLikeIcon(post, holder);
        // Xử lý click vào ivOption
        holder.ivOption.setOnClickListener(v -> showPostOptionsBottomSheet(post, holder, position));
    }

    // Hàm toggle like chung cho cả click và double tap
    public void toggleLike(PostDTO post, PostViewHolder holder) {
        try {
            User user = DataLocalManager.getUser();
            if (user == null) {
                Common.showToast(context, "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                return;
            }
            boolean result = likeCmtService.toggleLikePost(post.getId(), user.getUserId());
            if (result) {
                int newLikes = likeCmtService.getLikesPost(post.getId());
                post.setLikes(newLikes);
                holder.tvLikeCounts.setText(String.valueOf(newLikes));
                updateLikeIcon(post, holder);
            }
        } catch (Exception e) {
            Common.showToast(context, e.getMessage());
        }
    }

    // Hàm cập nhật icon like dựa trên trạng thái
    public void updateLikeIcon(PostDTO post, PostViewHolder holder) {
        boolean isLiked = likeCmtService.isLikedPost(post.getId(), DataLocalManager.getUser().getUserId());
        holder.ivLike.setImageResource(isLiked ? R.drawable.ic_heart_tym : R.drawable.ic_heart);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    private void showPostOptionsBottomSheet(PostDTO post, PostViewHolder holder, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_post_options, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView tvDeletePost = bottomSheetView.findViewById(R.id.tvDeletePost);
        TextView tvReportPost = bottomSheetView.findViewById(R.id.tvReportPost);
        TextView tvHidePost = bottomSheetView.findViewById(R.id.tvHidePost);
        LinearLayout llDeletepost = bottomSheetView.findViewById(R.id.llDeletePost);
        User currentUser = DataLocalManager.getUser();
        if (currentUser != null && currentUser.getUserId().equals(post.getUserId())) {
            tvDeletePost.setVisibility(View.VISIBLE);
            llDeletepost.setVisibility(View.VISIBLE);
        } else {
            tvDeletePost.setVisibility(View.GONE);
            llDeletepost.setVisibility(View.GONE);
        }
        tvDeletePost.setOnClickListener(v -> {
            deletePost(post, position);
            bottomSheetDialog.dismiss();
        });
        tvReportPost.setOnClickListener(v -> {
            reportPost(post);
            bottomSheetDialog.dismiss();
        });
        tvHidePost.setOnClickListener(v -> {
            hidePost(post, position);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }
    private void deletePost(PostDTO post, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa bài viết")
                .setMessage("Bạn có chắc chắn muốn xóa bài viết này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    PostService postService = new PostService(context);
                    boolean success = postService.deletePost(post.getId());
                    if (success) {
                        postList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, postList.size());
                        Common.showToast(context, "Đã xóa bài viết");
                    } else {
                        Common.showToast(context, "Xóa bài viết thất bại");
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void reportPost(PostDTO post) {
        // Logic báo cáo bài viết (có thể gửi thông báo đến admin hoặc lưu vào DB)
        Common.showToast(context, "Đã báo cáo bài viết: " + post.getId());
    }

    private void hidePost(PostDTO post, int position) {
        // Logic ẩn bài viết (xóa khỏi danh sách hiển thị tạm thời)
        postList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, postList.size());
        Common.showToast(context, "Đã ẩn bài viết");
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivAvatar, ivLike, ivCmt, ivOption;
        public TextView tvUsername, tvCaption, tvLikeCounts, tvCmtCounts, tvCreatedAt;
        public ViewPager2 vpPostImage;
        public  CircleIndicator3 indicatorDotPost;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            init();
        }

        void init() {
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            vpPostImage = itemView.findViewById(R.id.vpPostImages);
            indicatorDotPost = itemView.findViewById(R.id.indicatorDotPost);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivCmt = itemView.findViewById(R.id.ivComment);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvLikeCounts = itemView.findViewById(R.id.tvLikeCounts);
            tvCmtCounts = itemView.findViewById(R.id.tvCmtCounts);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            ivOption = itemView.findViewById(R.id.ivOptions);
        }
    }
    public void displayImageUrl(String imageUrl, PostViewHolder holder) {
        try {
            // Check if this is a local file path
            if (imageUrl != null && imageUrl.startsWith("/")) {
                // This is a local file path
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    // Use Glide to load the local file
                    Glide.with(context)
                            .load(imageFile)
                            .placeholder(R.drawable.img_default_avatar)
                            .into(holder.ivAvatar);
                } else {
                    holder.ivAvatar.setImageResource(R.drawable.img_default_avatar);
                }
            }
            // Check if this is a remote URL
            else if (imageUrl.startsWith("http")) {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.img_default_avatar)
                        .into(holder.ivAvatar);
            } else {
                holder.ivAvatar.setImageResource(R.drawable.img_default_avatar);
            }
        } catch (Exception e) {
            Log.e("PostImageAdapter", "Error loading image: " + e.getMessage(), e);
            holder.ivAvatar.setImageResource(R.drawable.img_default_avatar);
        }
    }
}
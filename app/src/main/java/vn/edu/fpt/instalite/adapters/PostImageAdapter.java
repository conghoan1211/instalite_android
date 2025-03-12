package vn.edu.fpt.instalite.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.activities.LoginActivity;
import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.services.LikeCommentService;
import vn.edu.fpt.instalite.services.NotificationService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;
import vn.edu.fpt.instalite.utils.Common;

public class PostImageAdapter extends RecyclerView.Adapter<PostImageAdapter.ImageViewHolder> {
    private Context context;
    private List<String> imageUrls;
    private LikeCommentService likeCmtService;
    private String postId;
    private PostAdapter.PostViewHolder parentHolder;
    private List<PostDTO> postList;


    public PostImageAdapter(Context context, List<String> imageUrls, LikeCommentService likeCommentService,
                            String postId, PostAdapter.PostViewHolder parentHolder, List<PostDTO> postList) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.likeCmtService = likeCommentService;
        this.postId = postId;
        this.parentHolder = parentHolder;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        displayImageUrl(imageUrl, holder);
//        int imageResId = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
//        if (imageResId != 0) {
//            Glide.with(context)
//                    .load(imageResId)
//                    .placeholder(R.drawable.img_default_avatar)
//                    .into(holder.imageView);
//        } else {
//            holder.imageView.setImageResource(R.drawable.img_default_avatar);
//        }

        // Hiển thị số trang (ví dụ: "1/3")
        String pageText = (position + 1) + "/" + imageUrls.size();
        holder.tvPageNumber.setText(pageText);

        // Xử lý double tap
        GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                showHeartAnimation();
                User fromUser = DataLocalManager.getUser();
                if (fromUser == null || fromUser.getUserId() == null || fromUser.getUserId().isEmpty()) {
                    Common.showToast(context, "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    return false;
                }
                boolean isLiked = likeCmtService.isLikedPost(postId, fromUser.getUserId());
                if (!isLiked) {
                    boolean result = likeCmtService.toggleLikePost(postId, fromUser.getUserId());
                    if (result) {
                        int newLikes = likeCmtService.getLikesPost(postId);
                        if (postList != null && parentHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                            postList.get(parentHolder.getAdapterPosition()).setLikes(newLikes);
                            parentHolder.tvLikeCounts.setText(String.valueOf(newLikes));
                            parentHolder.ivLike.setImageResource(R.drawable.ic_heart_tym);
                        }
                        NotificationService notificationService = new NotificationService(context);
                        String userOwnerId = notificationService.getUserIdByPostId(Integer.parseInt(postId));
                        if (!userOwnerId.equals(fromUser.getUserId())) {
                            String content = fromUser.getUsername() + " đã thích bài viết của bạn.";
                            notificationService.insertNotification(userOwnerId, fromUser.getUserId(), Integer.parseInt(postId), content);
                        }
                    }
                }
                return true;
            }

            public void showHeartAnimation() {
                holder.ivHeartTab.setVisibility(View.VISIBLE);
                holder.ivHeartTab.setScaleX(0f);
                holder.ivHeartTab.setScaleY(0f);
                holder.ivHeartTab.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .withEndAction(() -> {
                            holder.ivHeartTab.animate()
                                    .alpha(0f)
                                    .setDuration(300)
                                    .withEndAction(() -> {
                                        holder.ivHeartTab.setVisibility(View.GONE);
                                        holder.ivHeartTab.setAlpha(0.8f); // reset alpha
                                    }).start();
                        }).start();
                holder.ivHeartTab.setAlpha(1f);
                holder.ivHeartTab.animate()
                        .alpha(0f)
                        .setDuration(600)
                        .withEndAction(() -> holder.ivHeartTab.setVisibility(View.GONE))
                        .start();
            }
        });
        holder.imageView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

    public void displayImageUrl(String imageUrl, ImageViewHolder holder) {
        Log.d(" Post image adapter", "displayImageUrl: " + imageUrl);
        try {
            // Check if this is a local file path
            if (imageUrl.startsWith("/")) {
                // This is a local file path
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    // Use Glide to load the local file
                    Glide.with(context)
                            .load(imageFile)
                            .placeholder(R.drawable.img_default_avatar)
                            .into(holder.imageView);
                } else {
                    holder.imageView.setImageResource(R.drawable.img_default_avatar);
                }
            }
            // Check if this is a remote URL
            else if (imageUrl.startsWith("http")) {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.img_default_avatar)
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.img_default_avatar);
            }
        } catch (Exception e) {
            Log.e("PostImageAdapter", "Error loading image: " + e.getMessage(), e);
            holder.imageView.setImageResource(R.drawable.img_default_avatar);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, ivHeartTab;
        TextView tvPageNumber;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivSinglePostImage);
            tvPageNumber = itemView.findViewById(R.id.tvPageNumber);
            ivHeartTab = itemView.findViewById(R.id.ivHeartTab);
        }
    }
}

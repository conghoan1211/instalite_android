package vn.edu.fpt.instalite.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.fragments.PostDetailFragment;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.ViewHolder> {
    private Context context;
    private List<PostDTO> postList;

    public SearchPostAdapter(Context context, List<PostDTO> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public SearchPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPostAdapter.ViewHolder holder, int position) {
        PostDTO post = postList.get(position);
        List<String> imageUrls = post.getImageUrls();
        if (!imageUrls.isEmpty()) {
            String imageUrl = imageUrls.get(0);
            displayImageUrl(imageUrl, holder);
        } else {
            holder.ivPostSearchImage.setImageResource(R.drawable.img_default_avatar);
        }

        holder.ivPostSearchImage.setOnClickListener(v -> {
            PostDetailFragment detailFragment = new PostDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("postId", Integer.parseInt(post.getId()));
            detailFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity) context)
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.frameContainer, detailFragment); // fragment_container là ID layout chứa fragment
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    public void displayImageUrl(String imageUrl, ViewHolder holder) {
        try {
            if (imageUrl.startsWith("/")) {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    Glide.with(context)
                            .load(imageFile)
                            .placeholder(R.drawable.img_default_avatar)
                            .into(holder.ivPostSearchImage);
                } else {
                    holder.ivPostSearchImage.setImageResource(R.drawable.img_default_avatar);
                }
            }
        } catch (Exception e) {
            Log.e("PostImageAdapter", "Error loading image: " + e.getMessage(), e);
            holder.ivPostSearchImage.setImageResource(R.drawable.img_default_avatar);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPostSearchImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPostSearchImage = itemView.findViewById(R.id.ivPostSearchImage);
        }
    }

    // Cập nhật danh sách bài viết khi tìm kiếm
    public void updatePosts(List<PostDTO> newPostList) {
        this.postList = newPostList;
        // Post notifyDataSetChanged to RecyclerView's message queue
        new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
    }
}

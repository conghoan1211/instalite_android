package vn.edu.fpt.instalite.adapters;

import android.content.Context;
import android.os.Bundle;
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

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ProfilePostViewHolder> {
    private Context context;
    private List<PostDTO> postList;

    public ProfilePostAdapter(Context context, List<PostDTO> postList) {
        this.context = context;
        this.postList = postList;
    }
    @NonNull
    @Override
    public ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_search, parent, false);
        return new ProfilePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostViewHolder holder, int position) {
        PostDTO post = postList.get(position);
        List<String> imageUrls = post.getImageUrls();
        if (!imageUrls.isEmpty()) {
            String imageUrl = imageUrls.get(0);
            displayImageUrl(imageUrl, holder);
        } else {
            holder.ivPostImage.setImageResource(R.drawable.img_default_avatar);
        }
        holder.ivPostImage.setOnClickListener(v -> {
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

    public static class ProfilePostViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPostImage;
        public ProfilePostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPostImage = itemView.findViewById(R.id.ivPostSearchImage);
        }
    }
    public void displayImageUrl(String imageUrl, ProfilePostAdapter.ProfilePostViewHolder holder) {
        try {
            if (imageUrl.startsWith("/")) {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    Glide.with(context)
                            .load(imageFile)
                            .placeholder(R.drawable.img_default_avatar)
                            .into(holder.ivPostImage);
                } else {
                    holder.ivPostImage.setImageResource(R.drawable.img_default_avatar);
                }
            }
        } catch (Exception e) {
            Log.e("PostImageAdapter", "Error loading image: " + e.getMessage(), e);
            holder.ivPostImage.setImageResource(R.drawable.img_default_avatar);
        }
    }
}

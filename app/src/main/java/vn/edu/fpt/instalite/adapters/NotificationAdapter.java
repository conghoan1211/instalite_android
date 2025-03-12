package vn.edu.fpt.instalite.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.dto.NotificationDTO;
import vn.edu.fpt.instalite.fragments.PostDetailFragment;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<NotificationDTO> notificationList;

    public NotificationAdapter(Context context, List<NotificationDTO> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationDTO notification = notificationList.get(position);

        if (notification.getAvatar() != null && notification.getAvatar().startsWith("/")) {
            File imageFile = new File(notification.getAvatar());
            if (imageFile.exists()) {
                Glide.with(context)
                        .load(imageFile)
                        .placeholder(R.drawable.img_default_avatar)
                        .into(holder.ivAvatarNotify);
            } else {
                holder.ivAvatarNotify.setImageResource(R.drawable.img_default_avatar);
            }
        } else {
            holder.ivAvatarNotify.setImageResource(R.drawable.img_default_avatar);
        }
        // Nội dung thông báo
        String content = notification.getContent();
        SpannableString spannable = new SpannableString(content);
        // Tìm vị trí kết thúc từ đầu tiên (tính đến khoảng trắng đầu tiên)
        int firstSpace = content.indexOf(" ");
        if (firstSpace == -1) {
            // Trường hợp chỉ có 1 từ, tô đậm toàn bộ
            firstSpace = content.length();
        }
        spannable.setSpan(
                new StyleSpan(Typeface.BOLD), // Kiểu chữ đậm
                0,                             // Bắt đầu từ ký tự đầu tiên
                firstSpace,                   // Kết thúc tại khoảng trắng đầu tiên
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        holder.tvNotificationContent.setText(spannable);

        // Thời gian
        String relativeTime = DateUtils.getRelativeTimeSpanString(
                notification.getCreatedAt(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS
        ).toString();
        holder.tvTimeNotify.setText(relativeTime);

        // Chuyển sang PostDetailFragment nếu có postId
        if (notification.getId() != 0) {
            holder.itemView.setOnClickListener(v -> {
                PostDetailFragment fragment = new PostDetailFragment();
                Bundle args = new Bundle();
                args.putInt("postId", notification.getRelatedId());
                fragment.setArguments(args);

                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotificationContent, tvTimeNotify;
        ImageView ivAvatarNotify;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationContent = itemView.findViewById(R.id.tvNotificationContent);
            tvTimeNotify = itemView.findViewById(R.id.tvTimeNotify);
            ivAvatarNotify = itemView.findViewById(R.id.ivAvatarNotify);
        }
    }
}
package vn.edu.fpt.instalite.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.models.User;

public class SearchUserAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;

    public SearchUserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void updateList(List<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_search, parent, false);
        }

        ImageView imgAvatar = view.findViewById(R.id.ivSearchUserAvtar);
        TextView tvUsername = view.findViewById(R.id.tvSearchUsername);
        TextView tvEmail = view.findViewById(R.id.tvSearchEmail);

        User user = userList.get(position);

        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());

//        Glide.with(context)
//                .load(context.getResources().getIdentifier(user.getAvatar(), "drawable", context.getPackageName()))
//                .placeholder(R.drawable.img_default_avatar)
//                .into(imgAvatar);
        Log.d("Image url", "displayImageUrl: " + user.getAvatar());

        if (user.getAvatar() != null && user.getAvatar().startsWith("/")) {
            File imageFile = new File(user.getAvatar());
            if (imageFile.exists()) {
                Glide.with(context)
                        .load(imageFile)
                        .placeholder(R.drawable.img_default_avatar)
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.img_default_avatar);
            }
        } else if (user.getAvatar() != null) {
            // Nếu avatar là URL (không bắt đầu bằng "/")
            Glide.with(context)
                    .load(user.getAvatar())
                    .placeholder(R.drawable.img_default_avatar)
                    .into(imgAvatar);
        } else {
            // Nếu avatar null
            imgAvatar.setImageResource(R.drawable.img_default_avatar);
        }
        return view;
    }
}

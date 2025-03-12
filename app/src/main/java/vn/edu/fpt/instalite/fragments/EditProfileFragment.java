package vn.edu.fpt.instalite.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.activities.LoginActivity;
import vn.edu.fpt.instalite.adapters.PostImageAdapter;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.repositories.PostRepository;
import vn.edu.fpt.instalite.services.UserService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;
import vn.edu.fpt.instalite.utils.Common;

public class EditProfileFragment extends Fragment {
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private ImageButton btnBack;
    private ImageView ivAvatar;
    private Button btnChangeAvatar, btnSave;
    private EditText etUsername, etBio;
    private UserService userService;
    private Uri selectedImageUri;
    private TextView tvChangeAvatar, tvEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Khởi tạo view
        btnBack = view.findViewById(R.id.btnBack);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvChangeAvatar = view.findViewById(R.id.tvChangeAvatar);
        btnSave = view.findViewById(R.id.btnSave);
        etUsername = view.findViewById(R.id.etUsername);
        etBio = view.findViewById(R.id.etBio);
        tvEmail = view.findViewById(R.id.tvEmailProfile);
        userService = new UserService(requireContext());

        // Lấy thông tin người dùng hiện tại
        User user = DataLocalManager.getUser();
        if (user == null) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().getSupportFragmentManager().popBackStack();
            return view;
        }
        // Hiển thị thông tin hiện tại
        displayImageUrl(user.getAvatar(),  ivAvatar);

        etUsername.setText(user.getUsername());
        etBio.setText(user.getBio() != null ? user.getBio() : "");
        tvEmail.setText(user.getEmail());
        // Xử lý nút Back
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        tvChangeAvatar.setOnClickListener(v -> requestStoragePermission());
        btnSave.setOnClickListener(v -> saveProfile(user));
        return view;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        } else {
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery();
        } else {
            Toast.makeText(requireContext(), "Cần quyền để truy cập ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Glide.with(this).load(selectedImageUri).into(ivAvatar);
        }
    }

    private void saveProfile(User user) {
        String newUsername = etUsername.getText().toString().trim();
        String newBio = etBio.getText().toString().trim();

        if (newUsername.isEmpty()) {
            etUsername.setError("Tên người dùng không được để trống");
            return;
        }

        // Cập nhật thông tin người dùng
        user.setUsername(newUsername);
        user.setBio(newBio);
        if (selectedImageUri != null) {
            String imageSaveUrl = Common.saveImageToInternalStorage(selectedImageUri, requireContext(), "Avatar");
            user.setAvatar(imageSaveUrl);
        }
        // Lưu vào database
        boolean success = userService.updateUser(user);
        if (success) {
            DataLocalManager.setUser(user); // Cập nhật dữ liệu local
            Toast.makeText(requireContext(), "Đã cập nhật hồ sơ", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(requireContext(), "Cập nhật hồ sơ thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayImageUrl(String imageUrl, ImageView ivAvatar) {
        try {
            if (imageUrl.startsWith("/")) {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    Glide.with(getContext())
                            .load(imageFile)
                            .placeholder(R.drawable.img_default_avatar)
                            .into(ivAvatar);
                } else {
                    ivAvatar.setImageResource(R.drawable.img_default_avatar);
                }
            }
            else if (imageUrl.startsWith("http")) {
                Glide.with(getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.img_default_avatar)
                        .into(ivAvatar);
            } else {
                ivAvatar.setImageResource(R.drawable.img_default_avatar);
            }
        } catch (Exception e) {
            Log.e("PostImageAdapter", "Error loading image: " + e.getMessage(), e);
            ivAvatar.setImageResource(R.drawable.img_default_avatar);
        }
    }
}
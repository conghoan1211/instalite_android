package vn.edu.fpt.instalite.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.activities.LoginActivity;
import vn.edu.fpt.instalite.adapters.SelectedImageAdapter;
import vn.edu.fpt.instalite.dto.UploadPostDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.services.PostService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;
import vn.edu.fpt.instalite.utils.Common;

public class AddPostFragment extends Fragment {

    private static final int REQUEST_PICK_IMAGES = 100;
    private static final int REQUEST_CAPTURE_IMAGE = 101;

    private Button btnUpload;
    private CardView btnPickImages, btnCapture;
    private EditText edtCaption;
    private RecyclerView rvSelectedImages;

    private List<Uri> selectedImageUris = new ArrayList<>();
    private SelectedImageAdapter imageAdapter;
    private Uri capturedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_post, container, false);
        btnPickImages = view.findViewById(R.id.btnPickImages);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnUpload = view.findViewById(R.id.btnUpload);
        edtCaption = view.findViewById(R.id.edtCaption);
        rvSelectedImages = view.findViewById(R.id.rvSelectedImages);

        imageAdapter = new SelectedImageAdapter(selectedImageUris, requireContext());
        rvSelectedImages.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvSelectedImages.setAdapter(imageAdapter);

        btnPickImages.setOnClickListener(v -> pickImages());
        btnCapture.setOnClickListener(v -> captureImage());
        btnUpload.setOnClickListener(v -> uploadPost());
        // Thiết lập để ẩn bàn phím khi chạm ngoài EditText
        view.setOnTouchListener((v, event) -> {
            if (edtCaption != null && edtCaption.isFocused()) {
                edtCaption.clearFocus();
                hideKeyboard(view);
            }
            return false;
        });
        return view;
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), REQUEST_PICK_IMAGES);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                capturedImageUri = FileProvider.getUriForFile(requireContext(),
                        requireContext().getPackageName() + ".provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
                startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() {
        try {
            String fileName = "IMG_" + System.currentTimeMillis();
            File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(fileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_PICK_IMAGES) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                selectedImageUris.add(data.getData());
            }
            imageAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (capturedImageUri != null) {
                selectedImageUris.add(capturedImageUri);
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    // 2. Fix the uploadPost method
    private void uploadPost() {
        String caption = edtCaption.getText().toString();

        if (selectedImageUris.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn ít nhất một ảnh", Toast.LENGTH_SHORT).show();
            return;
        }
        // Show loading indicator
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Đang đăng bài...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // Copy the list to avoid clearing the original before processing is complete
        List<Uri> urisToProcess = new ArrayList<>(selectedImageUris);

        List<String> imagePaths = new ArrayList<>();
        for (Uri uri : urisToProcess) {
            String path = Common.saveImageToInternalStorage(uri, requireContext(),"PostImages");
            if (path != null) {
                imagePaths.add(path);
                Log.d("AddPostFragment", "Added image path: " + path);
            } else {
                Log.e("AddPostFragment", "Failed to save image: " + uri);
            }
        }
        if (imagePaths.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Lỗi: Không thể lưu ảnh. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            return;
        }
        String imageUrls = TextUtils.join(",", imagePaths);
        Log.d("AddPostFragment", "Image URLs: " + imageUrls);

        // Rest of your code...
        PostService postService = new PostService(getContext());
        User user = DataLocalManager.getUser();
        if (user == null) {
            progressDialog.dismiss();
            Toast.makeText(requireContext(), "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            return;
        }
        UploadPostDTO uploadPostDTO = new UploadPostDTO(user.getUserId(), caption, imageUrls, 0, 0, 0, 0, System.currentTimeMillis());
        try {
            boolean result = postService.insertPost(uploadPostDTO);
            progressDialog.dismiss();
            if (result) {
                // Only clear after successful upload
                edtCaption.setText("");
                selectedImageUris.clear();
                imageAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Đăng bài thành công với " + imagePaths.size() + " ảnh", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Đăng bài thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("AddPostFragment", "Error uploading post", e);
        }
    }
}

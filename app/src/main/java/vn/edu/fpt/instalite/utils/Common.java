package vn.edu.fpt.instalite.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Common {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String saveImageToInternalStorage(Uri uri, Context context, String typeUrl) {
        if (typeUrl.isEmpty()) typeUrl = "PostImages";
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                Log.e("AddPostFragment", "Could not open input stream for URI: " + uri);
                return null;
            }

            File directory = new File(context.getFilesDir(), typeUrl);
            if (!directory.exists()) directory.mkdirs();
            // Generate a unique filename without relying on the URI path
            String filename = "IMG_" + System.currentTimeMillis() + ".jpg";
            File file = new File(directory, filename);
            OutputStream outputStream = new FileOutputStream(file);
            // Add logging
            Log.d("AddPostFragment", "Saving image to: " + file.getAbsolutePath());
            byte[] buffer = new byte[4096]; // Larger buffer for efficiency
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            // Verify file was created
            if (file.exists() && file.length() > 0) {
                Log.d("AddPostFragment", "Image saved successfully: " + file.length() + " bytes");
                return file.getAbsolutePath();
            } else {
                Log.e("AddPostFragment", "File not created or empty");
                return null;
            }
        } catch (Exception e) {
            Log.e("AddPostFragment", "Error saving image", e);
            e.printStackTrace();
            return null;
        }
    }
}

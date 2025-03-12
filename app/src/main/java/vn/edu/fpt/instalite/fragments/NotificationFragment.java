package vn.edu.fpt.instalite.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.activities.LoginActivity;
import vn.edu.fpt.instalite.adapters.NotificationAdapter;
import vn.edu.fpt.instalite.dto.NotificationDTO;
import vn.edu.fpt.instalite.fragments.home.HeaderFragment;
import vn.edu.fpt.instalite.fragments.home.PostListFragment;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.repositories.NotificationRepository;
import vn.edu.fpt.instalite.services.NotificationService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;

public class NotificationFragment extends Fragment {
    private RecyclerView rvNotifications;
    private NotificationAdapter notificationAdapter;
    private NotificationService notificationService;
    private List<NotificationDTO> notifications;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Khởi tạo
        rvNotifications = view.findViewById(R.id.rvNotifications);
        notificationService = new NotificationService(requireContext());

        // Lấy thông tin người dùng hiện tại
        User user = DataLocalManager.getUser();
        if (user == null) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().getSupportFragmentManager().popBackStack();
            return view;
        }

        // Lấy danh sách thông báo
        notifications = notificationService.getNotifications(user.getUserId());
        notificationAdapter = new NotificationAdapter(requireContext(), notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvNotifications.setAdapter(notificationAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notifications.clear();
        notifications.addAll(notificationService.getNotifications(DataLocalManager.getUser().getUserId()));
        notificationAdapter.notifyDataSetChanged(); // Cập nhật dữ liệu cho RecyclerView
    }

}
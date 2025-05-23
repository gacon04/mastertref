package com.example.mastertref.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mastertref.R;
import com.example.mastertref.ui.SettingFragmentChild.CacBanBep;
import com.example.mastertref.ui.SettingFragmentChild.ChuDe;
import com.example.mastertref.ui.SettingFragmentChild.DieuKhoanChinhSach;
import com.example.mastertref.ui.SettingFragmentChild.GuiPhanHoi;
import com.example.mastertref.ui.SettingFragmentChild.QuocGia;
import com.example.mastertref.ui.SettingFragmentChild.TaiKhoan;
import com.example.mastertref.ui.SettingFragmentChild.ThongKeBep;
import com.example.mastertref.ui.SettingFragmentChild.VeMasterTref;
import com.example.mastertref.utils.SessionManager;


public class SettingFragment extends Fragment {
    private SessionManager sessionManager  ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tìm các LinearLayout

        LinearLayout taiKhoanSetting = view.findViewById(R.id.layout_taikhoan);
        LinearLayout cacBanBepSetting = view.findViewById(R.id.layout_cacbanbep);
        LinearLayout thongKeBepSetting = view.findViewById(R.id.layout_thongkebep);
        LinearLayout dieuKhoanVaChinhSachSetting = view.findViewById(R.id.layout_dieukhoanchinhsach);
        LinearLayout nhungCauHoiThuongGapSetting = view.findViewById(R.id.layout_nhungcauhoithuonggap);
        LinearLayout veMasterTrefSetting = view.findViewById(R.id.layout_vemastertref);
        LinearLayout chuDeSetting = view.findViewById(R.id.layout_chude);
        LinearLayout guiPhanHoiSetting = view.findViewById(R.id.layout_guiphanhoi);

        // Gọi Fragment tương ứng khi click

        taiKhoanSetting.setOnClickListener(v -> replaceFragment(new TaiKhoan()));
        cacBanBepSetting.setOnClickListener(v -> replaceFragment(new CacBanBep()));
        thongKeBepSetting.setOnClickListener(v -> replaceFragment(new ThongKeBep()));
        dieuKhoanVaChinhSachSetting.setOnClickListener(v -> replaceFragment(new DieuKhoanChinhSach()));
        veMasterTrefSetting.setOnClickListener(v -> replaceFragment(new VeMasterTref()));
        chuDeSetting.setOnClickListener(v -> replaceFragment(new ChuDe()));
        guiPhanHoiSetting.setOnClickListener(v -> replaceFragment(new GuiPhanHoi()));

        // Nút thoát ứng dụng
        Button thoatButton = view.findViewById(R.id.logout_button);
        thoatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            sessionManager.clearSession(); // Xóa session
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Đảm bảo không quay lại
                            startActivity(intent);
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });



        // Gán link vào phần những câu hỏi thường gặp
        LinearLayout layoutNhungCauHoiThuongGap = view.findViewById(R.id.layout_nhungcauhoithuonggap);
        layoutNhungCauHoiThuongGap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://cookpad.com/vn/faq";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

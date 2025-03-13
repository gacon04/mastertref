package com.example.mastertref;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.mastertref.SettingFragmentChild.CacBanBep;
import com.example.mastertref.SettingFragmentChild.ChuDe;
import com.example.mastertref.SettingFragmentChild.DieuKhoanChinhSach;
import com.example.mastertref.SettingFragmentChild.GuiPhanHoi;
import com.example.mastertref.SettingFragmentChild.QuocGia;
import com.example.mastertref.SettingFragmentChild.TaiKhoan;
import com.example.mastertref.SettingFragmentChild.ThongKeBep;
import com.example.mastertref.SettingFragmentChild.VeMasterTref;


public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tìm các LinearLayout
        LinearLayout quocGiaSetting = view.findViewById(R.id.layout_quocgia);
        LinearLayout taiKhoanSetting = view.findViewById(R.id.layout_taikhoan);
        LinearLayout cacBanBepSetting = view.findViewById(R.id.layout_cacbanbep);
        LinearLayout thongKeBepSetting = view.findViewById(R.id.layout_thongkebep);
        LinearLayout dieuKhoanVaChinhSachSetting = view.findViewById(R.id.layout_dieukhoanchinhsach);
        LinearLayout nhungCauHoiThuongGapSetting = view.findViewById(R.id.layout_nhungcauhoithuonggap);
        LinearLayout veMasterTrefSetting = view.findViewById(R.id.layout_vemastertref);
        LinearLayout chuDeSetting = view.findViewById(R.id.layout_chude);
        LinearLayout guiPhanHoiSetting = view.findViewById(R.id.layout_guiphanhoi);


        // Gọi Fragment tương ứng khi click
        quocGiaSetting.setOnClickListener(v -> replaceFragment(new QuocGia()));
        taiKhoanSetting.setOnClickListener(v -> replaceFragment(new TaiKhoan()));
        cacBanBepSetting.setOnClickListener(v -> replaceFragment(new CacBanBep()));
        thongKeBepSetting.setOnClickListener(v -> replaceFragment(new ThongKeBep()));
        dieuKhoanVaChinhSachSetting.setOnClickListener(v -> replaceFragment(new DieuKhoanChinhSach()));
        veMasterTrefSetting.setOnClickListener(v -> replaceFragment(new VeMasterTref()));
        chuDeSetting.setOnClickListener(v -> replaceFragment(new ChuDe()));
        guiPhanHoiSetting.setOnClickListener(v -> replaceFragment(new GuiPhanHoi()));


        // Nút thoát ứng dụng
        Button thoatButton = view.findViewById(R.id.logout_button);
        thoatButton.setOnClickListener(v -> requireActivity().finish());

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
        transaction.replace(R.id.fragment_setting, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

package com.example.mastertref.utils;

import com.example.mastertref.data.local.NguyenLieuEntity;

import java.util.Arrays;
import java.util.List;
// NHIỆM VỤ: PHÂN TÁCH NGUYÊN LIỆU THÀNH ĐỊNH LƯỢNG RIÊNG VÀ TÊN NGUYÊN LIỆU RIÊNG
public class NguyenLieuUtils {

    private static final List<String> UNITS = Arrays.asList(
            "cân", "kí", "kg", "lạng", "cái", "trái", "quả", "bịch", "gói", "ml", "lít", "gram", "g", "muỗng"
    );

    /**
     * Phân tích chuỗi nguyên liệu, tách định lượng và tên nguyên liệu
     * @param input chuỗi nhập từ EditText, ví dụ: "2 cái trứng", "muối 1 muỗng"
     * @return NguyenLieuEntity nếu hợp lệ, null nếu không chứa định lượng
     */
    public static NguyenLieuEntity parseNguyenLieu(String input) {
        input = input.trim();
        if (input.isEmpty()) return null;

        String[] words = input.split("\\s+");
        String dinhLuong = "";
        String tenNguyenLieu = "";

        int indexOfNumber = -1;
        for (int j = 0; j < words.length; j++) {
            if (words[j].matches("\\d+")) {
                indexOfNumber = j;
                break;
            }
        }

        if (indexOfNumber != -1) {
            if (words.length == 2) {
                dinhLuong = words[indexOfNumber];
                tenNguyenLieu = words[1 - indexOfNumber];
            } else {
                StringBuilder tenBuilder = new StringBuilder();
                for (int j = 0; j < words.length; j++) {
                    if (j == indexOfNumber) {
                        if (j + 1 < words.length && UNITS.contains(words[j + 1].toLowerCase())) {
                            dinhLuong = words[j] + " " + words[j + 1];
                            j++;
                        } else {
                            dinhLuong = words[j];
                        }
                    } else {
                        tenBuilder.append(words[j]).append(" ");
                    }
                }
                tenNguyenLieu = tenBuilder.toString().trim();
            }

            return new NguyenLieuEntity(0, tenNguyenLieu, dinhLuong); // monanId sẽ set sau
        }

        return null; // Không tìm thấy số
    }
}

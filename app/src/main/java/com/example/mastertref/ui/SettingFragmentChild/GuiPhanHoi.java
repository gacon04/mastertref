package com.example.mastertref.ui.SettingFragmentChild;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mastertref.R;
import com.example.mastertref.utils.SessionManager;

public class GuiPhanHoi extends Fragment {
    private Button btn_send;
    private EditText ed_feedback;
    private SessionManager sessionManager;
    // Email address to send feedback to
    private static final String FEEDBACK_EMAIL = "nhokljlom99@gmail.com"; // Replace with your actual email
    private String EMAIL_SUBJECT;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gui_phan_hoi__setting, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tìm ImageView trong layout fragment
        ImageView backButton = view.findViewById(R.id.back_button);

        // Bắt sự kiện click và quay lại Fragment trước đó
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        sessionManager = new SessionManager(getContext());
        btn_send = view.findViewById(R.id.btn_send);
        ed_feedback = view.findViewById(R.id.feedback_input);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedbackEmail();
            }
        });
    }
    
    /**
     * Send feedback via email client
     */
    private void sendFeedbackEmail() {
        String feedback = ed_feedback.getText().toString().trim();
        
        if (feedback.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập góp ý của bạn", Toast.LENGTH_SHORT).show();
            return;
        }
        EMAIL_SUBJECT = new String("Góp ý ứng dụng MasterTref từ @"+sessionManager.getUsername());
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{FEEDBACK_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback);
        
        try {
            startActivity(Intent.createChooser(emailIntent, "Gửi góp ý qua..."));
            // Clear the feedback field after sending
            ed_feedback.setText("");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), 
                "Không tìm thấy ứng dụng email nào trên thiết bị", 
                Toast.LENGTH_SHORT).show();
        }
    }
}
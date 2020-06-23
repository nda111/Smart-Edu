package com.gachon.smartedu.messaging.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gachon.smartedu.R;
import com.gachon.smartedu.messaging.Message;

import java.util.regex.Pattern;

/**
 * An activity to send a message
 *
 * @author 유근혁
 * @since June 22nd, 2020
 */
public class SendMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        /*
         * Initialize GUI Components
         */
        final ImageView backButton = findViewById(R.id.send_message_button_back);
        final ImageView sendButton = findViewById(R.id.send_message_button_send);
        final EditText titleEditText = findViewById(R.id.send_message_edit_text_title);
        final EditText toEditText = findViewById(R.id.send_message_edit_text_to);
        final EditText contentEditText =  findViewById(R.id.send_message_edit_text_content);

        // backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // sendButton
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                final String to = toEditText.getText().toString().trim();
                final String content = contentEditText.getText().toString();

                final Pattern emailPattern = Pattern.compile("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)");
                if (to.length() == 0) {
                    Toast.makeText(SendMessageActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    toEditText.requestFocus();
                    return;
                } else if (!emailPattern.matcher(to).matches()) {
                    Toast.makeText(SendMessageActivity.this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    toEditText.requestFocus();
                    toEditText.selectAll();
                    return;
                }

                if (title.length() == 0) {
                    title = titleEditText.getHint().toString();
                }

                if (content.length() == 0) {
                    contentEditText.setText("내용을 입력해주세요.");
                    contentEditText.requestFocus();
                    contentEditText.selectAll();

                    return;
                }

                // todo: send message
                onBackPressed();
            }
        });

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final Message toReply = Message.unpack(bundle);

            if (toReply != null) { // If is reply mode

                // Initialize message information for reply
                titleEditText.setText(String.format("RE: %s", toReply.getTitle()));
                toEditText.setText(toReply.getFromEmail());
                contentEditText.setText(String.format("RE:\n%s\n\n====================\n\n", toReply.getContent()));
            }
        }
    }
}
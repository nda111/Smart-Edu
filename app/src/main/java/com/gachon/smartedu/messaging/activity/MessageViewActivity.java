package com.gachon.smartedu.messaging.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gachon.smartedu.R;
import com.gachon.smartedu.messaging.Message;

import java.util.Objects;

/**
 * An activity to show a specified message
 *
 * @author 유근혓
 * @since June 22nd, 2020
 */
public class MessageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        final Bundle packedMessage = getIntent().getExtras();
        final Message message = Message.unpack(Objects.requireNonNull(packedMessage));

        /*
         * Initialize GUI Components
         */
        final ImageView backButton = findViewById(R.id.message_view_button_back);
        final ImageView deleteButton = findViewById(R.id.message_view_button_delete);
        final ImageView replyButton = findViewById(R.id.message_view_button_reply);
        final TextView titleTextView = findViewById(R.id.message_view_text_view_title);
        final TextView fromTextView = findViewById(R.id.message_view_text_view_from);
        final TextView whenTextView = findViewById(R.id.message_view_text_view_when);
        final TextView contentTextView = findViewById(R.id.message_view_text_view_content);

        // backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(MessageListActivity.ACTIVITY_RESULT_NONE);
                finish();
            }
        });

        // deleteButton
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.putExtras(packedMessage);

                setResult(MessageListActivity.ACTIVITY_RESULT_DELETE, intent);
                finish();
            }
        });

        // replyButton
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.putExtras(packedMessage);

                setResult(MessageListActivity.ACTIVITY_RESULT_REPLY, intent);
                finish();
            }
        });

        // titleTextView
        titleTextView.setText(message.getTitle());

        // fromTextView
        fromTextView.setText(String.format("From. %s", message.getFrom()));

        // whenTextView
        final Calendar when = Calendar.getInstance();
        when.setTimeInMillis(message.getWhen());

        StringBuilder whenBuilder = new StringBuilder();
        if (dateWiseEquals(when, Calendar.getInstance())) {
            whenBuilder.append("오늘 ");
        } else {
            whenBuilder.append(when.get(Calendar.YEAR));
            whenBuilder.append("년 ");

            whenBuilder.append(when.get(Calendar.MONTH) + 1);
            whenBuilder.append("월 ");

            whenBuilder.append(when.get(Calendar.DAY_OF_MONTH));
            whenBuilder.append("일 ");
        }
        if (when.get(Calendar.HOUR_OF_DAY) < 12) {
            whenBuilder.append("오전 ");
        } else {
            whenBuilder.append("오후 ");
        }
        whenBuilder.append(when.get(Calendar.HOUR));
        whenBuilder.append("시 ");

        whenBuilder.append(when.get(Calendar.MINUTE));
        whenBuilder.append("분");

        whenTextView.setText(whenBuilder.toString());

        // contentTextView
        contentTextView.setText(message.getContent());
    }

    private boolean dateWiseEquals(@NonNull Calendar date1, @NonNull Calendar date2) {
        return
                date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                        date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
                        date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }
}
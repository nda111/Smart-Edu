package com.gachon.smartedu.messaging.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gachon.smartedu.R;
import com.gachon.smartedu.messaging.Message;
import com.gachon.smartedu.messaging.MessageListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * An activity that downloads and lists every messages corresponds to the account
 *
 * @author 유근혁
 * @since June 22nd 2020
 */
public class MessageListActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_VIEW = 0x01;

    public static final int ACTIVITY_RESULT_NONE = 0x00;
    public static final int ACTIVITY_RESULT_DELETE = 0x01;
    public static final int ACTIVITY_RESULT_REPLY = 0x02;

    private ArrayList<Message> messageList = new ArrayList<Message>();

    private RecyclerView messageListView;
    private TextView noMessageTextView;
    private FloatingActionButton sendFab;

    /**
     * An event listener for list items
     */
    private final MessageListAdapter.OnItemClickedListener onItemClickedListener = new MessageListAdapter.OnItemClickedListener() {

        @Override
        public void onItemClicked(@NonNull Message message) {
            final Intent intent = new Intent(MessageListActivity.this, MessageViewActivity.class);
            intent.putExtras(message.toBundle());

            startActivityForResult(intent, ACTIVITY_REQUEST_VIEW);
        }

        @Override
        public void onReplyButtonClicked(@NonNull Message message) {
            final Intent intent = new Intent(MessageListActivity.this, SendMessageActivity.class);
            intent.putExtras(message.toBundle());

            startActivity(intent);
        }

        @Override
        public void onDeleteButtonClicked(@NonNull Message message) {
            // todo: request deletion
            Toast.makeText(MessageListActivity.this, "Delete clicked", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        /*
         * Initialize GUI Components
         */
        messageListView = findViewById(R.id.message_list_recycler_view_messages);
        noMessageTextView = findViewById(R.id.message_list_text_view_mo_message);
        sendFab = findViewById(R.id.message_list_fab_send);

        // messageListView
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        messageListView.addItemDecoration(new DividerItemDecoration(messageListView.getContext(), DividerItemDecoration.VERTICAL));

        // noMessageTextView
        noMessageTextView.setVisibility(View.VISIBLE);

        // sendFab
        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MessageListActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case ACTIVITY_REQUEST_VIEW:
                switch (resultCode) {

                    case ACTIVITY_RESULT_DELETE:
                        onItemClickedListener.onDeleteButtonClicked(Message.unpack(data.getExtras()));
                        break;

                    case ACTIVITY_RESULT_REPLY:
                        onItemClickedListener.onReplyButtonClicked(Message.unpack(data.getExtras()));
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        downloadMessages();
    }

    private void downloadMessages() {

        final Calendar today = Calendar.getInstance();
        final Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);

        final Message[] messages = new Message[] {
                new Message(0, "from A", "abcdef@abab.com", "Title1", "This is the first content.", today.getTimeInMillis()),
                new Message(1, "from B", "ghijkl@cdcd.net", "Title2", "This is the second content.", yesterday.getTimeInMillis()),
        };
        // todo: receive message

        messageList.clear();
        for (Message msg : messages) {
            messageList.add(msg);
        }

        final MessageListAdapter adapter = new MessageListAdapter(messageList);
        adapter.setOnClickedListener(onItemClickedListener);
        messageListView.setAdapter(adapter);

        noMessageTextView.setVisibility(messageList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}
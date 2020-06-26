package com.gachon.smartedu.messaging.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * An activity that downloads and lists every messages corresponds to the account
 *
 * @author 유근혁
 * @since June 22nd 2020
 */
public class MessageListActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_VIEW = 0x01;
    private static final int ACTIVITY_REQUEST_SEND = 0x02;

    public static final int ACTIVITY_RESULT_NONE = 0x00;
    public static final int ACTIVITY_RESULT_DELETE = 0x01;
    public static final int ACTIVITY_RESULT_REPLY = 0x02;
    public static final int ACTIVITY_RESULT_SEND = 0x03;

    private final FirebaseDatabase Database = FirebaseDatabase.getInstance();

    private ArrayList<Message> messageList = new ArrayList<Message>();
    private long MaxMessageId = -1;

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
            final String MyId = getMyId();
            final DatabaseReference ref = Database.getReference().child("messaging");
            ref.child(MyId).child(Long.toString(message.getId())).removeValue();

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
                startActivityForResult(intent, ACTIVITY_REQUEST_SEND);
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

            case ACTIVITY_REQUEST_SEND:
                switch (resultCode) {

                    case ACTIVITY_RESULT_SEND:
                        DatabaseReference ref = Database.getReference("messaging");
                        ref = ref.child(hashEmail(data.getStringExtra("TO")));

                        final Message msg = Message.unpack(data.getExtras());
                        final DatabaseReference postBox = ref;
                        ref.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                final Long id = snapshot.child(Message.KEY_ID).getValue(Long.class) + 1;

                                HashMap<String, Object> input = new HashMap<String, Object>();
                                input.put(Message.KEY_FROM, getMyId());
                                input.put(Message.KEY_TITLE, msg.getTitle());
                                input.put(Message.KEY_CONTENT, msg.getContent());
                                input.put(Message.KEY_WHEN, msg.getWhen());

                                DatabaseReference ref = postBox.child(id.toString());
                                ref.updateChildren(input);
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
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

        /*
         * Start realtime messaging
         */
        final String MyId = getMyId();
        final DatabaseReference messageReference = Database.getReference("messaging").child(MyId);
        messageReference.orderByKey().addChildEventListener(new ChildEventListener() {

            private HashMap<Long, Message> messages = new HashMap<Long, Message>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                final long id = Long.valueOf(snapshot.getKey());
                MaxMessageId = Math.max(MaxMessageId, id);

                final String title = snapshot.child(Message.KEY_TITLE).getValue(String.class);
                final String from = snapshot.child(Message.KEY_FROM).getValue(String.class);
                final Long when = snapshot.child(Message.KEY_WHEN).getValue(Long.class);
                final String content = snapshot.child(Message.KEY_CONTENT).getValue(String.class);

                final Message msg = new Message(id, from, title, content, when);
                messages.put(id, msg);

                refreshMessageList();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /* EMPTY */
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                final long id = Long.valueOf(snapshot.getKey());
                messages.remove(id);
                refreshMessageList();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /* EMPTY */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                /* EMPTY */
            }

            private void refreshMessageList() {

                messageList.clear();
                for (Map.Entry<Long, Message> entry : messages.entrySet()) {
                    messageList.add(entry.getValue());
                }

                final MessageListAdapter adapter = new MessageListAdapter(messageList);
                adapter.setOnClickedListener(onItemClickedListener);
                messageListView.setAdapter(adapter);

                noMessageTextView.setVisibility(messageList.size() == 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    private String getMyId() {
        // return FirebaseAuth.getInstance().getUid();
        return hashEmail("abcdef@naver.com");
    }

    private String hashEmail(@NonNull String email) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(email.getBytes());

            byte[] bytes = digest.digest();
            StringBuilder builder = new StringBuilder();
            final String HexString = "0123456789ABCDEF";
            for (byte b : bytes) {
                builder.append(HexString.charAt((b >> 4) & 0xF));
                builder.append(HexString.charAt(b & 0xF));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
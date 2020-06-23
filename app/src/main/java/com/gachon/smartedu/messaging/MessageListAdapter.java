package com.gachon.smartedu.messaging;

import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachon.smartedu.R;

import java.util.ArrayList;

/**
 * A class that represents the adapter for recycler view that shows message list
 *
 * @author 유근혁
 * @since June 22nd, 2020
 */
public final class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    /**
     * An interface that
     *
     * @author 유근혁
     * @since June 22nd, 2020
     */
    public interface OnItemClickedListener {

        /**
         * When the item clicked to show details
         *
         * @param message The message instance
         */
        void onItemClicked(@NonNull Message message);

        /**
         * When the reply button clicked
         *
         * @param message The message instance
         */
        void onReplyButtonClicked(@NonNull Message message);

        /**
         * When the delete button clicked
         *
         * @param message The message instance
         */
        void onDeleteButtonClicked(@NonNull Message message);
    }

    /**
     * A class that holds GUI components of the list item
     *
     * @author 유근혁
     * @since June 22nd, 2020
     */
    public final class ViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView fromTextView;
        public TextView titleTextView;
        public TextView whenTextView;
        public ImageButton replyButton;
        public ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;
            fromTextView = itemView.findViewById(R.id.item_message_list_text_view_from);
            titleTextView = itemView.findViewById(R.id.item_message_list_text_view_title);
            whenTextView = itemView.findViewById(R.id.item_message_list_text_view_when);
            replyButton = itemView.findViewById(R.id.item_message_list_image_button_reply);
            deleteButton = itemView.findViewById(R.id.item_message_list_image_button_delete);
        }
    }

    private ArrayList<Message> messages;
    private OnItemClickedListener onClickedListener;

    /**
     * Create an instance
     *
     * @param messages The {ArrayList} of the messages to list
     */
    public MessageListAdapter(@NonNull ArrayList<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Message message = messages.get(position);

        // title
        final int MaxLength = 30;
        String title = message.getTitle();
        if (title.length() > MaxLength) {
            title = title.substring(0, MaxLength - 3) + "...";
        }
        holder.titleTextView.setText((title));

        // From
        holder.fromTextView.setText(message.getFrom());

        // When
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

        holder.whenTextView.setText(whenBuilder.toString());

        // onReply
        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickedListener != null) {
                    onClickedListener.onReplyButtonClicked(message);
                }
            }
        });

        // onDelete
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickedListener != null) {
                    onClickedListener.onDeleteButtonClicked(message);
                }
            }
        });

        // rootView
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickedListener != null) {
                    onClickedListener.onItemClicked(message);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * Get the event listener
     */
    public OnItemClickedListener getOnClickedListener() {
        return onClickedListener;
    }

    /**
     * Set the event listener
     */
    public void setOnClickedListener(OnItemClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    /**
     * Compare the equality between two {Calendar} instances' year, month, dayOfMonth values
     *
     * @return True if equals, false otherwise
     */
    private boolean dateWiseEquals(@NonNull Calendar date1, @NonNull Calendar date2) {
        return
                date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
                date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }
}

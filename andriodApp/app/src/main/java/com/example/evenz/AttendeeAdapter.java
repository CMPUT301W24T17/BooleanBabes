package com.example.evenz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * The AttendeeAdapter class is an adapter for displaying a list of Attendee objects in a RecyclerView.
 * <p>
 * This class extends the RecyclerView.Adapter class and provides the necessary methods for binding the Attendee data to the RecyclerView.
 * <p>
 * Each AttendeeAdapter has a list of Attendee objects that it displays. This list is passed to the AttendeeAdapter's constructor.
 * <p>
 * The AttendeeAdapter class provides a ViewHolder class that represents the view for each item in the RecyclerView. The ViewHolder class includes a profile image view, a name text view, and a contact info text view for displaying the Attendee's profile image, name, and contact info, respectively.
 * <p>
 * The AttendeeAdapter class also provides a method for logging the details of all Attendees in the list for debugging purposes.
 *
 * @version 1.0
 * @see Attendee
 * @see RecyclerView
 * @see RecyclerView.Adapter
 * @see ViewHolder
 */
class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.ViewHolder> {

    // List of attendees to be displayed
    private final List<Attendee> attendeesList;

    /**
     * Constructs a new AttendeeAdapter with the specified list of attendees.
     *
     * @param attendeesList the list of attendees to be displayed
     */
    public AttendeeAdapter(List<Attendee> attendeesList) {
        this.attendeesList = attendeesList;
        logAttendees();
    }

    /**
     * Logs the details of all attendees in the list for debugging purposes.
     */
    public void logAttendees() {
        for (Attendee attendee : attendeesList) {
            Log.d("AttendeeAdapter", "Name: " + attendee.getName() + ", Email: " + attendee.getEmail());
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendee, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Attendee attendee = attendeesList.get(position);
        holder.nameTextView.setText(attendee.getName());
        holder.contactInfoTextView.setText(String.format("%s\n%s", attendee.getPhone(), attendee.getEmail()));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return attendeesList.size();
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     * <p>
     * ViewHolder objects are created by the onCreateViewHolder(ViewGroup, int) method of the adapter, and they are then bound to their data by the onBindViewHolder(ViewHolder, int) method.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;
        TextView contactInfoTextView;

        /**
         * Constructs a new ViewHolder with the specified view.
         *
         * @param itemView The view that this ViewHolder should hold.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.image_banner);
            nameTextView = itemView.findViewById(R.id.text_categories);
            contactInfoTextView = itemView.findViewById(R.id.text_contact_info);
        }
    }
}
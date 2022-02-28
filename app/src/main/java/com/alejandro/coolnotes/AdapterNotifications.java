package com.alejandro.coolnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.coolnotes.ui.dashboard.DashboardFragment;
import com.alejandro.coolnotes.ui.notifications.NotificationsFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.ViewHolder> {
    private final ArrayList<Notification> mData;
    private final LayoutInflater mInflater;
    private final Context context;
    private final NotificationsFragment fragment;

    private int pos = 0;

    public AdapterNotifications(Context context, ArrayList<Notification> data, NotificationsFragment fragment) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.fragment = fragment;
    }

    public int getPos() {
        return this.pos;
    }

    public void decrementarPos() {
        this.pos--;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notif = mData.get(position);
        Calendar date = notif.getDate();
        String hour = String.valueOf(date.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(date.get(Calendar.MINUTE));
        String day = String.valueOf(date.get(Calendar.DATE));
        String month = String.valueOf(date.get(Calendar.MONTH) + 1);

        hour = (hour.length() <=1) ? "0" + hour : hour;
        minute = (minute.length() <=1) ? "0" + minute : minute;

        String dateString = day + "/" + month + "\n" + hour + ":" + minute;

        holder.notifDate.setText(dateString);
        holder.notifText.setText(notif.getDescription());

        //Delete notification button
        holder.delNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.deleteNotificationService(mData.get(position).getId());
                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, mData.size());
                decrementarPos();
                PersistenceVault vault = fragment.getVault();
                vault.setNotificationsList(mData);
                vault.saveVaultToFile(fragment.getActivity().getFilesDir());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notifDate;
        TextView notifText;
        ImageButton delNotif;

        ViewHolder(View itemView) {
            super(itemView);
            notifDate = itemView.findViewById(R.id.notif_date);
            notifText = itemView.findViewById(R.id.notif_text);
            delNotif = itemView.findViewById(R.id.delNotification);
        }
    }
}



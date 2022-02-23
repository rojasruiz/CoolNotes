package com.alejandro.coolnotes.ui.notifications;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alejandro.coolnotes.AdapterNotes;
import com.alejandro.coolnotes.MainActivity;
import com.alejandro.coolnotes.PersistenceVault;
import com.alejandro.coolnotes.R;
import com.alejandro.coolnotes.ReminderBroadcast;
import com.alejandro.coolnotes.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Activity main;
    private PersistenceVault vault;
    private RecyclerView recycler;
    private FloatingActionButton fab;
    private SharedPreferences preferences;
    private int notificationId;
    private Calendar date;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createNotificationChannel();

        //Persistence
        main = getActivity();
        vault = new PersistenceVault(main.getFilesDir());

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        setUpRecycler();
        configureButton();
    }

    private void configureButton() {
        fab = getActivity().findViewById(R.id.fab_add_notification);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar currentDate = Calendar.getInstance();
                date = Calendar.getInstance();
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                launchNotification();
                                newNotificationPersistence();
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
    }

    private void newNotificationPersistence() {
        //TODO esta mierda
    }

    public void launchNotification() {
        long milisSet = date.getTimeInMillis();

        //Get notification id
        notificationId = preferences.getInt("notificationId",0);

        //Notification launch
        ReminderBroadcast.setTittle("Hola");
        ReminderBroadcast.setDescription("Funciona?");
        ReminderBroadcast.setId(notificationId);

        Intent intent = new Intent(getContext(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), notificationId, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,milisSet,pendingIntent);

        //Delete notification

                /*
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent2 = new Intent(getContext(), ReminderBroadcast.class);
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getContext(), notificationId, intent, 0);
                alarmManager.cancel(pendingIntent2);
                pendingIntent2.cancel();*/

        //Save notification id
        notificationId++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notificationId", notificationId);
        editor.commit();
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setUpRecycler() {

        recycler = getView().findViewById(R.id.recycler_notifications);
        LinearLayoutManager mLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLayout);
        RecyclerView.Adapter adapter;
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new LandingAnimator(new OvershootInterpolator(2.0f)));
        recycler.getItemAnimator().setRemoveDuration(220);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
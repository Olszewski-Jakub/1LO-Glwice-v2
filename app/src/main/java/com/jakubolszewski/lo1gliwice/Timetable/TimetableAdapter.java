package com.jakubolszewski.lo1gliwice.Timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakubolszewski.lo1gliwice.R;

import java.util.ArrayList;

public class TimetableAdapter extends ArrayAdapter<Timetable_element> {
    public TimetableAdapter(@NonNull Context context, ArrayList<Timetable_element> timetableAdapter) {
        super(context, 0, timetableAdapter);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Timetable_element timetable_element = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.timetable_element, parent, false);
        }
        TextView time = convertView.findViewById(R.id.time);
        TextView lesson = convertView.findViewById(R.id.lesson);
        TextView room = convertView.findViewById(R.id.room);
        time.setText(timetable_element.hours);
        lesson.setText(timetable_element.lesson);
        room.setText(timetable_element.room);
        return convertView;
    }
}

package com.jakubolszewski.lo1gliwice.Substitute;

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

public class SubstituteAdapter extends ArrayAdapter<Substitute> {

    public SubstituteAdapter(@NonNull Context context, ArrayList<Substitute> substitutes) {
        super(context, 0, substitutes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Substitute substitute = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.substitute_item,parent,false);
        }
        TextView content = convertView.findViewById(R.id.content_textView);
        content.setText(substitute.content);
        return convertView;
    }
}

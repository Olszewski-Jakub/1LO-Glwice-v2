package com.jakubolszewski.lo1gliwice;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SupportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SupportFragment newInstance(String param1, String param2) {
        SupportFragment fragment = new SupportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    EditText nameData;
    EditText emailData;
    EditText messageData;
    Button send;
    Button details;
    Firebase firebase;

    CheckBox error_CB, opinion_CB, idea_CB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        CardView email = view.findViewById(R.id.email);
        CardView support = view.findViewById(R.id.support);
        CardView feedback = view.findViewById(R.id.feedback);


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"j.olszewski05@gmail.com"});
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://buycoffee.to/jakubolszewski");
                Intent launchWeb = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(launchWeb);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        List<String> updateList = new ArrayList<>();
        updateList.add("Przygotowano aplikacje na rok szkolny 2021/2022");
        updateList.add("Naprawiono błędy");
        updateList.add("Dodano plan lekcji");
        updateList.add("Naprawiono błędy");
        updateList.add("Poprawiono wygląd");
        updateList.add("Zoptymalizowano\nosiągnięcia uczniów");
        updateList.add("Zmieniono działanie\naktualności");
        updateList.add("Dodano zakładke\n\"Organizacja roku szkolnego\"");

        int l = updateList.size();
        GridLayout gridLayout = view.findViewById(R.id.grid);
        gridLayout.setRowCount(1);
        gridLayout.setColumnCount(l);

        for (int i = 0; i < updateList.size(); i++) {

            LinearLayout.LayoutParams paramsC;
            paramsC = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            paramsC.setMargins(20, 20, 20, 20);

            CardView cardView = new CardView(requireContext());
            cardView.setRadius(15);
            cardView.setCardElevation(15);
            cardView.setLayoutParams(paramsC);

            LinearLayout.LayoutParams params;
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            LinearLayout linearLayout = new LinearLayout(requireContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(params);
            linearLayout.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams paramsT;
            paramsT = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            paramsT.setMargins(50, 50, 50, 50);
            TextView textView = new TextView(requireContext());
            textView.setText(updateList.get(i));
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTextSize(16);
            textView.setLayoutParams(paramsT);
            textView.setGravity(Gravity.CENTER);

            linearLayout.addView(textView);
            cardView.addView(linearLayout);
            gridLayout.addView(cardView);
        }


        return view;
    }

    public void showDialog() {
        Dialog dialog = new Dialog(requireContext(), R.style.DialogStyle);
        dialog.setContentView(R.layout.feedback_dialog);

//FEEDBACK
        nameData = dialog.findViewById(R.id.editText_nameData);
        emailData = dialog.findViewById(R.id.editText_emailData);
        messageData = dialog.findViewById(R.id.editText_messageData);

        error_CB = dialog.findViewById(R.id.checkBox_error);
        opinion_CB = dialog.findViewById(R.id.checkBox_opinion);
        idea_CB = dialog.findViewById(R.id.checkBox_idea);

        send = dialog.findViewById(R.id.button_send);
        details = dialog.findViewById(R.id.button_details);


        Firebase.setAndroidContext(requireContext());

        String UniqueID = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        error_CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opinion_CB.setChecked(false);
                idea_CB.setChecked(false);
                firebase = new Firebase("https://lo-gliwice.firebaseio.com/error/" + UniqueID);
            }
        });

        opinion_CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_CB.setChecked(false);
                idea_CB.setChecked(false);
                firebase = new Firebase("https://lo-gliwice.firebaseio.com/opinion/" + UniqueID);
            }
        });

        idea_CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_CB.setChecked(false);
                opinion_CB.setChecked(false);
                firebase = new Firebase("https://lo-gliwice.firebaseio.com/idea/" + UniqueID);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setEnabled(true);
                final String name = nameData.getText().toString();
                final String email = emailData.getText().toString();
                final String message = messageData.getText().toString();

                Firebase child_name = firebase.child("Name");
                child_name.setValue(name);
                if (name.isEmpty()) {
                    nameData.setError("To pole jest wymagane!");
                    send.setEnabled(false);

                } else {
                    nameData.setError(null);
                    send.setEnabled(true);
                }

                Firebase child_email = firebase.child("Email");
                child_email.setValue(email);
                if (email.isEmpty()) {
                    emailData.setError("To pole jest wymagane!");
                    send.setEnabled(false);

                } else {
                    emailData.setError(null);
                    send.setEnabled(true);
                }

                Firebase child_message = firebase.child("Message");
                child_message.setValue(message);
                if (message.isEmpty()) {
                    messageData.setError("To pole jest wymagane!");
                    send.setEnabled(false);

                } else {
                    messageData.setError(null);
                    send.setEnabled(true);
                }
                Toast.makeText(requireContext(), "Wiadomość zostałą wysłana", Toast.LENGTH_SHORT).show();

                details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Szczegoły wiadomości:")
                                .setMessage("Imię - " + name + "\n\nEmail - " + email + "\n\nWiadomość - " + message)
                                .show();
                    }
                });
            }
        });


        dialog.show();
    }
}
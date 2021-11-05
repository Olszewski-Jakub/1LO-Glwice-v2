package com.jakubolszewski.lo1gliwice;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
    AdView mAdView;
    String chosenClass = "";
    String mySurname = "null";
    boolean is_Teacher = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

//ADS
        mAdView = new AdView(requireContext());

        mAdView.setAdSize(AdSize.BANNER);

        mAdView.setAdUnitId("ca-app-pub-8202098045766771/2474089364");
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Settings code
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
        sp.getBoolean("notification", true);
        sp.getString("yourClass", "Wszytskie");
        sp.getString("surname", "Wpisz nazwisko");
        ///Intit leyout
        readSettings();
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch is_teacher = view.findViewById(R.id.if_teacher_switch);
        is_teacher.setChecked(is_Teacher);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch nootification_status = view.findViewById(R.id.notification_switch);
        Spinner spinner = view.findViewById(R.id.classSpinner);
        CardView student_card = view.findViewById(R.id.student_layout);
        CardView student_card2 = view.findViewById(R.id.student_layout2);
        CardView teacher_card = view.findViewById(R.id.teacher_layout);
        CardView teacher_card2 = view.findViewById(R.id.teacher_layout2);
        TextView surname_TV = view.findViewById(R.id.surname_text);
        TextView student_TV = view.findViewById(R.id.student_text);
        Button edit2 = view.findViewById(R.id.edit2_btn);

        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student_card.setVisibility(View.VISIBLE);
                student_card2.setVisibility(View.GONE);
            }
        });

        if (!is_Teacher){
            teacher_card.setVisibility(View.GONE);
            teacher_card2.setVisibility(View.GONE);
            if (!chosenClass.equals("Wszytskie")){
                student_card.setVisibility(View.GONE);
                student_card2.setVisibility(View.VISIBLE);
                student_TV.setText(chosenClass);
            } else{
                student_card.setVisibility(View.VISIBLE);
                student_card2.setVisibility(View.GONE);

            }
        } else {
            student_card.setVisibility(View.GONE);
            student_card2.setVisibility(View.GONE);
            if (!mySurname.equals("null")){
                teacher_card2.setVisibility(View.VISIBLE);
                surname_TV.setText(mySurname);
            } else {
                teacher_card.setVisibility(View.VISIBLE);
            }
        }

        is_teacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    teacher_card.setVisibility(View.GONE);
                    teacher_card2.setVisibility(View.GONE);
                    if (!chosenClass.equals("Wszytskie")){
                        student_card.setVisibility(View.GONE);
                        student_card2.setVisibility(View.VISIBLE);
                        student_TV.setText(chosenClass);
                    } else{
                        student_card.setVisibility(View.VISIBLE);
                        student_card2.setVisibility(View.GONE);

                    }
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("is_teacher", isChecked);
                    editor.apply();
                } else {
                    student_card.setVisibility(View.GONE);
                    student_card2.setVisibility(View.GONE);
                    if (!mySurname.equals("null")){
                        teacher_card2.setVisibility(View.VISIBLE);
                        surname_TV.setText(mySurname);
                    } else {
                        teacher_card.setVisibility(View.VISIBLE);
                    }

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("is_teacher", isChecked);
                    editor.apply();
                }
            }
        });

        //SPINNER
        List<String> classList = new ArrayList<>();
        classList.add("");
        classList.add("IA");
        classList.add("IB");
        classList.add("IC");
        classList.add("ID");
        classList.add("IE");
        classList.add("IIA");
        classList.add("IIB");
        classList.add("IIC");
        classList.add("IID");
        classList.add("IIE");
        classList.add("IIIAp");
        classList.add("IIIBp");
        classList.add("IIICp");
        classList.add("IIIAg");
        classList.add("IIIBg");
        classList.add("IIICg");
        classList.add("Matura międzynarodowa");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, classList);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().length() > 1){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("yourClass",parent.getItemAtPosition(position).toString());
                    editor.apply();
                    chosenClass = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button save2 = view.findViewById(R.id.save2_btn);
        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student_card.setVisibility(View.GONE);
                student_card2.setVisibility(View.VISIBLE);
                student_TV.setText(chosenClass);
                Toast.makeText(requireContext(), chosenClass+ " została wybrana jakodomyślna klasa", Toast.LENGTH_SHORT).show();

            }
        });


        EditText editText = view.findViewById(R.id.surname);
        editText.setText(mySurname);

        Button save = view.findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty()){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("teacher", editText.getText().toString());
                    editor.apply();
                    Toast.makeText(requireContext(), "Zapisano pomyślnie", Toast.LENGTH_SHORT).show();
                    teacher_card2.setVisibility(View.VISIBLE);
                    teacher_card.setVisibility(View.GONE);
                    surname_TV.setText(editText.getText().toString());
                } else {
                    editText.setError("Musisz wpisać teskt");
                }
            }
        });
        Button edit = view.findViewById(R.id.edit_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacher_card.setVisibility(View.VISIBLE);
                teacher_card2.setVisibility(View.GONE);
            }
        });

        return view;
    }

    public void readSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        chosenClass = preferences.getString("yourClass", "Wszytskie");
        mySurname = preferences.getString("teacher","");
        is_Teacher = preferences.getBoolean("is_teacher",false);
    }
}
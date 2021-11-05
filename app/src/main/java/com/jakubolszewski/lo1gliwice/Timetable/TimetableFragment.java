package com.jakubolszewski.lo1gliwice.Timetable;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jakubolszewski.lo1gliwice.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimetableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimetableFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimetableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimetableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimetableFragment newInstance(String param1, String param2) {
        TimetableFragment fragment = new TimetableFragment();
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

    CardView pon, wt, sr, czw, pt;
    Spinner spinner;
    //FirebaseFirestore db;
    Map<String, Object>[] a;
    ListView timetableList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        //db = FirebaseFirestore.getInstance();
        pon = view.findViewById(R.id.pon);
        wt = view.findViewById(R.id.wt);
        sr = view.findViewById(R.id.sr);
        czw = view.findViewById(R.id.czw);
        pt = view.findViewById(R.id.pt);
        spinner = view.findViewById(R.id.classSpinner);
        timetableList = view.findViewById(R.id.listView);
        pon.setEnabled(false);
        wt.setEnabled(false);
        sr.setEnabled(false);
        czw.setEnabled(false);
        pt.setEnabled(false);
        spinner.setEnabled(false);

        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        List<String> classList = new ArrayList<>();
        classList.add("1A");
        classList.add("1B");
        classList.add("1C");
        classList.add("1E");
        classList.add("1D");
        classList.add("2A");
        classList.add("2B");
        classList.add("2C");
        classList.add("2E");
        classList.add("2D");
        classList.add("3Ap");
        classList.add("3Bp");
        classList.add("3Cp");
        classList.add("3Ag");
        classList.add("3Bg");
        classList.add("3Cg");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, classList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String myClass = preferences.getString("yourClass", "null");
        switch (myClass) {
            case "III/^[A-Za-z]+$/":
                myClass = myClass.replace("III", "3");
                break;
            case "II/^[A-Za-z]+$/":
                myClass = myClass.replace("II", "2");
                break;
            case "I/^[A-Za-z]+$/":
                myClass = myClass.replace("I", "1");
                break;
        }

        if (!myClass.equals("null")) {
            Toast.makeText(getContext(), myClass, Toast.LENGTH_SHORT).show();
            for(int i = 0 ; i < classList.size(); i++){
                if(classList.get(i).equals(myClass.trim())){
                    Toast.makeText(requireContext(), "Kinda works", Toast.LENGTH_SHORT).show();
                    spinner.setSelection(i);
                }
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pon.setEnabled(false);
                wt.setEnabled(false);
                sr.setEnabled(false);
                czw.setEnabled(false);
                pt.setEnabled(false);
                spinner.setEnabled(false);
                spinner.setClickable(false);

                a = new Map[]{new HashMap<>()};
                DocumentReference docRef = db.collection("Timetable").document(parent.getItemAtPosition(position).toString().toLowerCase());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                a[0] = document.getData();
                                pon.setEnabled(true);
                                wt.setEnabled(true);
                                sr.setEnabled(true);
                                czw.setEnabled(true);
                                pt.setEnabled(true);
                                Calendar calendar = Calendar.getInstance();
                                int day = calendar.get(Calendar.DAY_OF_WEEK);

                                switch (day) {
                                    case Calendar.SUNDAY:
                                        Monday();
                                        break;
                                    case Calendar.MONDAY:
                                        Monday();
                                        break;
                                    case Calendar.TUESDAY:
                                        Tuesday();
                                        break;
                                    case Calendar.WEDNESDAY:
                                        Wednesday();
                                        break;
                                    case Calendar.THURSDAY:
                                        Thursday();
                                        break;
                                    case Calendar.FRIDAY:
                                        Friday();
                                        break;
                                    case Calendar.SATURDAY:
                                        Monday();
                                        break;
                                }


                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }

                    }
                });


                pon.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        Monday();
                    }
                });

                wt.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        Tuesday();
                    }
                });

                sr.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        Wednesday();
                    }
                });

                czw.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        Thursday();
                    }
                });

                pt.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        Friday();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Monday() {
        Object o = a[0].get("monday");
        List<String> list = new ArrayList<>();
        list = (List<String>) o;
        showPlan(list);

        pon.setScaleX((float) 1.2);
        pon.setScaleY((float) 1.2);
        wt.setScaleX((float) 1);
        wt.setScaleY((float) 1);
        sr.setScaleX((float) 1);
        sr.setScaleY((float) 1);
        czw.setScaleX((float) 1);
        czw.setScaleY((float) 1);
        pt.setScaleX((float) 1);
        pt.setScaleY((float) 1);

        pon.setCardBackgroundColor(Color.parseColor("#fffcf0"));
        wt.setCardBackgroundColor(Color.parseColor("#ffffff"));
        sr.setCardBackgroundColor(Color.parseColor("#ffffff"));
        czw.setCardBackgroundColor(Color.parseColor("#ffffff"));
        pt.setCardBackgroundColor(Color.parseColor("#ffffff"));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Tuesday() {
        Object o = a[0].get("tuesday");
        List<String> list = new ArrayList<>();
        list = (List<String>) o;
        showPlan(list);

        pon.setScaleX((float) 1);
        pon.setScaleY((float) 1);
        wt.setScaleX((float) 1.2);
        wt.setScaleY((float) 1.2);
        sr.setScaleX((float) 1);
        sr.setScaleY((float) 1);
        czw.setScaleX((float) 1);
        czw.setScaleY((float) 1);
        pt.setScaleX((float) 1);
        pt.setScaleY((float) 1);

        pon.setCardBackgroundColor(Color.parseColor("#ffffff"));
        wt.setCardBackgroundColor(Color.parseColor("#fffcf0"));
        sr.setCardBackgroundColor(Color.parseColor("#ffffff"));
        czw.setCardBackgroundColor(Color.parseColor("#ffffff"));
        pt.setCardBackgroundColor(Color.parseColor("#ffffff"));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Wednesday() {
        Object o = a[0].get("wednesday");
        List<String> list = new ArrayList<>();
        list = (List<String>) o;
        showPlan(list);

        pon.setScaleX((float) 1);
        pon.setScaleY((float) 1);
        wt.setScaleX((float) 1);
        wt.setScaleY((float) 1);
        sr.setScaleX((float) 1.2);
        sr.setScaleY((float) 1.2);
        czw.setScaleX((float) 1);
        czw.setScaleY((float) 1);
        pt.setScaleX((float) 1);
        pt.setScaleY((float) 1);


        pon.setCardBackgroundColor(Color.parseColor("#ffffff"));
        wt.setCardBackgroundColor(Color.parseColor("#ffffff"));
        sr.setCardBackgroundColor(Color.parseColor("#fffcf0"));
        czw.setCardBackgroundColor(Color.parseColor("#ffffff"));
        pt.setCardBackgroundColor(Color.parseColor("#ffffff"));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Thursday() {
        Object o = a[0].get("thursday");
        List<String> list = new ArrayList<>();
        list = (List<String>) o;
        showPlan(list);

        pon.setScaleX((float) 1);
        pon.setScaleY((float) 1);
        wt.setScaleX((float) 1);
        wt.setScaleY((float) 1);
        sr.setScaleX((float) 1);
        sr.setScaleY((float) 1);
        czw.setScaleX((float) 1.2);
        czw.setScaleY((float) 1.2);
        pt.setScaleX((float) 1);
        pt.setScaleY((float) 1);

        pon.setCardBackgroundColor(Color.parseColor("#ffffff"));
        wt.setCardBackgroundColor(Color.parseColor("#ffffff"));
        sr.setCardBackgroundColor(Color.parseColor("#ffffff"));
        czw.setCardBackgroundColor(Color.parseColor("#fffcf0"));
        pt.setCardBackgroundColor(Color.parseColor("#ffffff"));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Friday() {
        Object o = a[0].get("friday");
        List<String> list = new ArrayList<>();
        list = (List<String>) o;
        showPlan(list);

        pon.setScaleX((float) 1);
        pon.setScaleY((float) 1);
        wt.setScaleX((float) 1);
        wt.setScaleY((float) 1);
        sr.setScaleX((float) 1);
        sr.setScaleY((float) 1);
        czw.setScaleX((float) 1);
        czw.setScaleY((float) 1);
        pt.setScaleX((float) 1.2);
        pt.setScaleY((float) 1.2);

        pon.setCardBackgroundColor(Color.parseColor("#ffffff"));
        wt.setCardBackgroundColor(Color.parseColor("#ffffff"));
        sr.setCardBackgroundColor(Color.parseColor("#ffffff"));
        czw.setCardBackgroundColor(Color.parseColor("#ffffff"));
        pt.setCardBackgroundColor(Color.parseColor("#fffcf0"));

    }

    @RequiresApi()
    private void showPlan(List<String> list) {

        String[] arr = {
                "7:10 - 7:55",
                "8:00 - 8:45",
                "8:50 - 9:35",
                "9:45 - 10:30",
                "10:40 - 11:25",
                "11:35 - 12:20",
                "12:50 - 13:35",
                "13:40 - 14:25",
                "14:30 - 15:15",
                "15:20 - 16:05",
                "16:10 - 16:55",
                "17:00 - 17:45"
        };


        ArrayList<Timetable_element> timetable_elements = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String lesson = "", room = "";
            String[] temp = list.get(i).split(",");
            List<String> temp2 = new ArrayList<String>();
            for (int j = 0; j < temp.length; j++) {
                if (temp[j].contains("zaj."))
                    temp2.add(temp[j] + temp[j + 1]);
                if (!temp[j].contains("wych.") && !temp[j].contains("zaj."))
                    temp2.add(temp[j]);

            }
            for (int j = 0; j < temp.length - 1; j = j + 2) {
                lesson += temp2.get(j) + "\n";
                room += temp2.get(j + 1) + "\n";
            }
            if (lesson.length() > 5) {
                lesson = lesson.substring(0, lesson.length() - 1);
                room = room.substring(0, room.length() - 1);
            }
            if (lesson.length() > 5)
                timetable_elements.add(new Timetable_element(arr[i], lesson, room));
        }
        TimetableAdapter timetableAdapter = new TimetableAdapter(requireContext(), timetable_elements);
        timetableList.setAdapter(timetableAdapter);
        spinner.setEnabled(true);
        spinner.setClickable(true);
    }
}
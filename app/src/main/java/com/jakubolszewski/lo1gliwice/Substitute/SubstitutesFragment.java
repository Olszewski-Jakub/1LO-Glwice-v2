package com.jakubolszewski.lo1gliwice.Substitute;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jakubolszewski.lo1gliwice.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubstitutesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubstitutesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubstitutesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubstitutesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubstitutesFragment newInstance(String param1, String param2) {
        SubstitutesFragment fragment = new SubstitutesFragment();
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

    String date;
    List<String> list;
    HashMap<String, List<String>> map;
    ListView listView;
    String default_class, mySurname;
    boolean is_Teacher = false;
    boolean isDataReady = false;
    TextView choosen_TV;
    Button myClass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_substitutes, container, false);

        //Adview
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        choosen_TV = view.findViewById(R.id.choosen);
        list = new ArrayList<String>();
        map = new HashMap<>();

        readSettings();
        myClass = view.findViewById(R.id.button_yourClass);

        if (is_Teacher) {
            myClass.setText("Moje zastępstwa");
        } else {
            myClass.setText("Moja klasa");
        }
        new getSubstitution().execute();


        //ArrayList<Substitute> substituteArrayList = new ArrayList<Substitute>();
        //SubstituteAdapter adapter = new SubstituteAdapter(requireContext(), substituteArrayList);
        listView = view.findViewById(R.id.listView);

        List<String> classList = new ArrayList<>();

        classList.add("Wybierz");
        classList.add("Wszystkie");
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

        Spinner spinner = view.findViewById(R.id.classSpinner);
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, classList);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> temp = showReplacement(parent.getItemAtPosition(position).toString());
                ArrayList<Substitute> substitutes = new ArrayList<>();
                for (int i = 0; i < temp.size(); i++) {
                    substitutes.add(new Substitute(temp.get(i)));
                }
                SubstituteAdapter adapter = new SubstituteAdapter(requireContext(), substitutes);

                listView.setAdapter(adapter);
                choosen_TV.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        myClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_Teacher) {
                    List<String> temp;
                    if (default_class.equals("null")) {
                        Toast.makeText(requireContext(),
                                "Przejdź do ustawień i wybierz klasę lub wpisz swoje nazwisko",
                                Toast.LENGTH_SHORT).show();
                        temp = new  ArrayList<String>(Collections.singleton("Przejdź do ustawień i wybierz klasę lub wpisz swoje nazwisko"));
                    } else {
                        temp = showReplacement(default_class);
                        choosen_TV.setText(default_class);
                    }
                    ArrayList<Substitute> substitutes = new ArrayList<>();
                    for (int i = 0; i < temp.size(); i++) {
                        substitutes.add(new Substitute(temp.get(i)));
                    }
                    SubstituteAdapter adapter = new SubstituteAdapter(requireContext(), substitutes);

                    listView.setAdapter(adapter);
                } else {
                    List<String> temp = showReplacementsForTeacher(mySurname);
                    ArrayList<Substitute> substitutes = new ArrayList<>();
                    for (int i = 0; i < temp.size(); i++) {
                        substitutes.add(new Substitute(temp.get(i)));
                    }
                    SubstituteAdapter adapter = new SubstituteAdapter(requireContext(), substitutes);
                    choosen_TV.setText(mySurname);
                    listView.setAdapter(adapter);
                }

            }
        });

        return view;
    }


    public class getSubstitution extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.lo1.gliwice.pl/zastepstwa-2/").userAgent("Mozilla/5.0").get();
                Elements elements = doc.select("div#post-3833").select("p");

                for (Element element : elements) {
                    list.add(element.text().toString());
                }

                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i));
                }

                date = doc.select("u").text();
                if (list.size() == 0) {
                    list.add("Brak zastępstw");
                }

                if ((list.size() > 0)) {
                    list.remove(0);
                    sortReplacemnts(list);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            ArrayList<Substitute> substitutes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                substitutes.add(new Substitute(list.get(i)));
            }
            SubstituteAdapter adapter = new SubstituteAdapter(requireContext(), substitutes);
            choosen_TV.setText("Wszytskie");
            listView.setAdapter(adapter);
        }
    }

    public void runAll() {

    }

    public void sortReplacemnts(List<String> list) {
        List<String> IA = new ArrayList<>();
        List<String> IB = new ArrayList<>();
        List<String> IC = new ArrayList<>();
        List<String> ID = new ArrayList<>();
        List<String> IE = new ArrayList<>();
        List<String> IIA = new ArrayList<>();
        List<String> IIB = new ArrayList<>();
        List<String> IIC = new ArrayList<>();
        List<String> IID = new ArrayList<>();
        List<String> IIE = new ArrayList<>();
        List<String> IIIAp = new ArrayList<>();
        List<String> IIIBp = new ArrayList<>();
        List<String> IIICp = new ArrayList<>();
        List<String> IIIAg = new ArrayList<>();
        List<String> IIIBg = new ArrayList<>();
        List<String> IIICg = new ArrayList<>();
        List<String> Matrua = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String set_value = list.get(i);
            String temp = set_value.trim().substring(0, Math.min(set_value.length(), 15));
            // for 1st class

            if (temp.contains("IA") && !temp.contains("IIA") && !temp.contains("IIIA")) {
                IA.add(set_value);
            }
            if (temp.contains("IB") && !temp.contains("IIB") && !temp.contains("IIIB")) {
                IB.add(set_value);
            }
            if (temp.contains("IC") && !temp.contains("IIC") && !temp.contains("IIIC")) {
                IC.add(set_value);
            }
            if (temp.contains("ID") && !temp.contains("IID") && !temp.contains("IIID")) {
                ID.add(set_value);
            }

            if (temp.contains("IE") && !temp.contains("IIE") && !temp.contains("IIIE")) {
                IE.add(set_value);
            }
            //FOr 2nd class
            if (temp.contains("IIA") && !temp.contains("IIIA") && !temp.contains("IA")) {
                IIA.add(set_value);
            }
            if (temp.contains("IIB") && !temp.contains("IIIB") && !temp.contains("IB")) {
                IIB.add(set_value);
            }
            if (temp.contains("IIC") && !temp.contains("IIIC") && !temp.contains("IC")) {
                IIC.add(set_value);
            }
            if (temp.contains("IID") && !temp.contains("IIID") && !temp.contains("ID")) {
                IID.add(set_value);
            }
            if (temp.contains("IIE") && !temp.contains("IIIE") && !temp.contains("IE")) {
                IIE.add(set_value);
            }
            //for thrid class
            if (temp.contains("IIIAp")) {
                IIIAp.add(set_value);
            }
            if (temp.contains("IIIBp")) {
                IIIBp.add(set_value);
            }
            if (temp.contains("IIICp")) {
                IIICp.add(set_value);
            }
            if (temp.contains("IIIAg")) {
                IIIAg.add(set_value);
            }
            if (temp.contains("IIIBg")) {
                IIIBg.add(set_value);
            }
            if (temp.contains("IIICg")) {
                IIICg.add(set_value);
            }
            if (temp.contains("międzynarodow")){
                Matrua.add(set_value);
            }

        }
        map.put("IA", IA);
        map.put("IB", IB);
        map.put("IC", IC);
        map.put("ID", ID);
        map.put("IE", IE);
        map.put("IIA", IIA);
        map.put("IIB", IIB);
        map.put("IIC", IIC);
        map.put("IID", IID);
        map.put("IIE", IIE);
        map.put("IIIAp", IIIAp);
        map.put("IIIBp", IIIBp);
        map.put("IIICp", IIICp);
        map.put("IIIAg", IIIAg);
        map.put("IIIBg", IIIBg);
        map.put("IIICg", IIICg);
        map.put("Matura", Matrua);
        isDataReady = true;

    }

    public List<String> showReplacement(String chosenClass) {
        if (isDataReady) {
            List<String> temp = new ArrayList<>();
            if (chosenClass.equals("Wszystkie"))
                return list;
            if (chosenClass.equals("Matura międzynarodowa"))
                chosenClass = "Matura";
            return ((new ArrayList<String>(Objects.requireNonNull(map.get(chosenClass)))).size() != 0 ?
                    new ArrayList<String>(Objects.requireNonNull(map.get(chosenClass))) :
                    new ArrayList<String>(Collections.singleton("Brak zastępstw")));

        } else {
            return new ArrayList<String>(Collections.singleton("Ladowanie danych"));
        }
    }

    public List<String> showReplacementsForTeacher(String name) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains(name))
                temp.add(list.get(i));
        }
        if (temp.size() == 0) {
            temp.add("Brak zastępstw");
        }
        return temp;
    }

    public void readSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        default_class = preferences.getString("yourClass", "null");
        mySurname = preferences.getString("teacher", "");
        is_Teacher = preferences.getBoolean("is_teacher", false);
    }



}
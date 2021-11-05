package com.jakubolszewski.lo1gliwice.Achievements;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jakubolszewski.lo1gliwice.News.News;
import com.jakubolszewski.lo1gliwice.News.NewsAdapter;
import com.jakubolszewski.lo1gliwice.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AchievementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchievementsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AchievementsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchievementsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AchievementsFragment newInstance(String param1, String param2) {
        AchievementsFragment fragment = new AchievementsFragment();
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
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> link = new ArrayList<String>();
    ArrayList<String> title2 = new ArrayList<String>();
    ArrayList<String> link2= new ArrayList<String>();
    ArrayList<String> contain = new ArrayList<String>();
    ArrayList<News> newsArrayList = new ArrayList<>();


    ListView listView;
    String article;
    TextView title_TV;
    TextView article_TV;
    String mLink = "";
    int pageN = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);


        listView = view.findViewById(R.id.listView);
        new doIt().execute();

        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = (News) parent.getItemAtPosition(position);
                showDialog(news.title,news.link);
            }
        });

        return view;
    }

    public class doIt extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            String url;

            for (int i = 1; i <= 5; i++) {

                url = "http://www.lo1.gliwice.pl/category/dla-uczniow/aktualnosci-dla-uczniow/page/" + i + "/";
                try {
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                    Elements elementsT = doc.body().select("a[title]");

                    contain.add("wynik");
                    contain.add("w finale");
                    contain.add("etapie");
                    contain.add("sukces");
                    contain.add("olimpiad");
                    contain.add("mistrz");
                    contain.add("laureat");
                    contain.add("finalist");

                    int j;
                    for (Element t : elementsT) {
                        for ( j = 0; j < contain.size(); j++) {
                            if (t.text().toLowerCase().contains(contain.get(j))) {
                                title.add(t.text());
                                link.add(t.attr("href"));
                            }
                        }
                    }
                    Set<String> set = new LinkedHashSet<>(title);
                    title.clear();
                    title.addAll(set);

                    Set<String> set2 = new LinkedHashSet<>(link);
                    link.clear();
                    link.addAll(set2);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            newsArrayList.clear();
            for (int k = 0; k < title.size(); k++) {
                String text = title.get(k);
                String link1 = link.get(k);
                newsArrayList.add(new News(link1, text));
            }
            NewsAdapter newsAdapter = new NewsAdapter(requireContext(), newsArrayList);
            listView.setAdapter(newsAdapter);
        }
    }
    public class doIt2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            String url;

            for (int i = 6; i <= 10; i++) {

                url = "http://www.lo1.gliwice.pl/category/dla-uczniow/aktualnosci-dla-uczniow/page/" + i + "/";
                try {
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                    Elements elementsT = doc.body().select("a[title]");

                    contain.add("wynik");
                    contain.add("w finale");
                    contain.add("etapie");
                    contain.add("sukces");
                    contain.add("olimpiad");
                    contain.add("mistrz");
                    contain.add("laureat");
                    contain.add("finalist");

                    int j;
                    for (Element t : elementsT) {
                        for ( j = 0; j < contain.size(); j++) {
                            if (t.text().toLowerCase().contains(contain.get(j))) {
                                title2.add(t.text());
                                link2.add(t.attr("href"));
                            }
                        }
                    }
                    Set<String> set3 = new LinkedHashSet<>(title2);
                    title2.clear();
                    title2.addAll(set3);

                    Set<String> set4 = new LinkedHashSet<>(link2);
                    link2.clear();
                    link2.addAll(set4);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            newsArrayList.clear();
            for (int k = 0; k < title.size(); k++) {
                String text = title.get(k);
                String link1 = link.get(k);
                newsArrayList.add(new News(link1, text));
            }
            NewsAdapter newsAdapter = new NewsAdapter(requireContext(), newsArrayList);
            listView.setAdapter(newsAdapter);
        }
    }

    private void showDialog(String title, String link){
        Dialog dialog = new Dialog(requireContext(), R.style.DialogStyle);
        dialog.setContentView(R.layout.article_dialog);

        title_TV= dialog.findViewById(R.id.title);
        article_TV= dialog.findViewById(R.id.article);
        Button close = dialog.findViewById(R.id.close);
        title_TV.setText(title);
        new getArticle().execute();
        mLink = link;


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public class getArticle extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;

            try {
                doc = Jsoup.connect(mLink).get();

                article = doc.select("article").text();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            article_TV.setText(article);
            int length;
            length = article_TV.length();
            if (length > 0) {
            } else {
                article = "Przepraszamy za utrudnienia, aplikacja nie wsperia tego formatu.";
                article_TV.setText(article);
                article_TV.setMovementMethod(LinkMovementMethod.getInstance());
            }

        }
    }

}
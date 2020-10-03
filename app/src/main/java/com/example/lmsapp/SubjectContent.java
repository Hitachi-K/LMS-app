package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class SubjectContent extends AppCompatActivity {

    //Declaring variables
    FirebaseAuth fbAuth;
    FirebaseFirestore firebaseFirestore;
    TextView FullName;
    ChipNavigationBar buttonNah;
    ViewPager viewPager;
    TabLayout tabLayout;
    String userID;

    //fragments
    all_Content all_content;
    add_Content add_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_content);

        // mapping variables to the items
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout_tab);
        FullName = (TextView)findViewById(R.id.txtFullName);
        buttonNah = findViewById(R.id.bottom_nav);

        buttonNah.setItemSelected(R.id.course, true);

        //on-selected item listener for the bottonNav
        buttonNah.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),StudentHomePage.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(), AllCourses.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), StudentProfile.class));
                        overridePendingTransition(0,0);
                        break;
                }
            }
        });

        all_content = new all_Content();
        add_content = new add_Content();

        tabLayout.setupWithViewPager(viewPager);

        // view pager adapter
        SubjectContent.ViewPagerAdapter viewPagerAdapter = new SubjectContent.ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(all_content, "All Content");
        viewPagerAdapter.addFragment(add_content, "Add Content");
        viewPager.setAdapter(viewPagerAdapter);

        //Instantiating
        /*firebaseFirestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //getting the User ID
        userID = fbAuth.getCurrentUser().getUid();

        //Obtaining the Full Name of the current user
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                FullName.setText(value.getString("Name"));
            }
        });*/
    }

    // inner view pager adapter class
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        // custom method to add fragment by title
        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}
package com.example.healthcare;

import android.app.Activity;
import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new  ActivityTestRule<MainActivity>(MainActivity.class) ;

    private MainActivity mActivity = null ;

    @Before
    public void setUp() throws Exception {
          mActivity = mainActivityActivityTestRule.getActivity() ;
    }

    @Test
    public void TestLaunch(){
        FirebaseFirestore db = mActivity.db;
        FirebaseAuth auth = mActivity.firebaseAuth ;
        assertNotNull(auth);
        assertNotNull(db);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null ;
    }
}
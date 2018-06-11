package com.example.pc.zadanie_cloudy;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pc on 16.12.2017.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{
    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testEdits() throws Throwable {
        final EditText mLogin = (EditText) getActivity().findViewById(R.id.loginEdit);
        final EditText mPassword = (EditText) getActivity().findViewById(R.id.passEdit);

        uiThreadTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogin.setText("admin");
                mPassword.setText("admin");
            }
        });

        assertEquals("admin", mLogin.getText().toString());
        assertEquals("admin", mPassword.getText().toString());
    }

    @After
    public void tearDown() throws Exception {
    }

}
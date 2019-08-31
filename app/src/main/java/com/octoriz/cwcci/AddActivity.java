package com.octoriz.cwcci;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.octoriz.cwcci.db.CWCCIDbHelper;
import com.octoriz.cwcci.db.DatabaseManager;
import com.octoriz.cwcci.db.MemberContract;
import com.octoriz.cwcci.singleton.MySingleton;
import com.octoriz.cwcci.util.MyPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "SSS";
    String fcmUrl = "https://cwcci.org/api/send";
    Intent intent;
    ConnectionChangeReceiver connectionChangeReceiver;
    boolean connected = false;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginActivity.UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Button cancelButton;
    private String url = "https://cwcci.org/api/users";

    CWCCIDbHelper cwcciDbHelper;
    SQLiteDatabase db;
    ContentValues contentValues;
    DatabaseManager databaseManager;
    List<String> memberList = new ArrayList<>();

    boolean found = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        this.setFinishOnTouchOutside(false);
        cwcciDbHelper = CWCCIDbHelper.getInstance(this);
        db = cwcciDbHelper.getWritableDatabase();
        contentValues = new ContentValues();
        databaseManager = new DatabaseManager(this);

        connectionChangeReceiver = new ConnectionChangeReceiver();

        memberList.addAll(databaseManager.getAllMembers());


        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (!connected) {
                Toast.makeText(this, "No internet connection. Please connect to internet", Toast.LENGTH_SHORT).show();
            } else {

                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mLoginFormView.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                mProgressView.setVisibility(View.INVISIBLE);


                                try {
                                    if (response.getString("status").equals("success")) {
                                        found = false;
                                        for (int i = 0; i < memberList.size(); i++) {
                                            if (memberList.get(i).equals(response.getString("member_name"))) {
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (found) {
                                            Toast.makeText(AddActivity.this, "You are already registered with this company", Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        } else {
                                            MyPreferences.getPreferences(getApplicationContext()).setToken(response.getString("token"));
                                            MyPreferences.getPreferences(getApplicationContext()).setMemberId(response.getInt("member_id"));
                                            Log.d("cwcci", response.getString("token"));

                                            contentValues.put(MemberContract.MemberEntry.COLUMN_NAME_MEMBER_NAME, response.getString("member_name"));
                                            contentValues.put(MemberContract.MemberEntry.COLUMN_NAME_MEMBERSHIP_ID, response.getString("membership_id"));

                                            // Insert the new row, returning the primary key value of the new row
                                            cwcciDbHelper = new CWCCIDbHelper(getApplicationContext());
                                            db = cwcciDbHelper.getWritableDatabase();
                                            long newRowId = db.insert(MemberContract.MemberEntry.TABLE_NAME, null, contentValues);

                                            Map<String, String> params = new HashMap<String, String>();

                                            Log.d("cwcci fcm", MyPreferences.getPreferences(getApplicationContext()).getFirebaseToken());

                                            params.put("fcm_token", MyPreferences.getPreferences(getApplicationContext()).getFirebaseToken());
                                            params.put("token", MyPreferences.getPreferences(getApplicationContext()).getToken());
                                            params.put("member_id", String.valueOf(MyPreferences.getPreferences(getApplicationContext()).getMemberId()));

                                            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                                    fcmUrl, new JSONObject(params),
                                                    new Response.Listener<JSONObject>() {

                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            //Log.d(TAG, response.toString());
                                                            Log.d(TAG, "successful");
                                                            try {
                                                                if (response.getString("status").equals("success")) {
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(AddActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(getApplicationContext(),
                                                                        "Error: " + e.getMessage(),
                                                                        Toast.LENGTH_LONG).show();
                                                            }


                                                        }
                                                    }, new Response.ErrorListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d(TAG, "Error: " + error.getMessage());
                                                }
                                            });
                                            // Adding request to request queue
                                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
                                            Toast.makeText(AddActivity.this, "Successfully added new company", Toast.LENGTH_SHORT).show();
                                            finish();
                                            showProgress(false);
                                        }
                                    } else {
                                        showProgress(false);
                                        Toast.makeText(AddActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.getMessage());
                        //pDialog.hide();
                        Toast.makeText(AddActivity.this, "You are not a member of CWCCI", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
                // Adding request to request queue
                MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
            }

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public void updateInternetConnectionStatusView(boolean isConnected) {

        if (isConnected) {
            connected = true;
        } else {
            connected = false;
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectionChangeReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectionChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));


    }
}

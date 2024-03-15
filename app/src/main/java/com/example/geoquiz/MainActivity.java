package com.example.geoquiz;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mSentButton;
    private Button mGetContactsButton;
    private TextView mQuestionTextView;
    private TextView contactNameTextView;
    static final int PICK_CONTACT_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setText(R.string.question_text);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mSentButton = (Button) findViewById(R.id.messageSent_button);
        mGetContactsButton = (Button) findViewById(R.id.getContactDetails_button);
        contactNameTextView =  (TextView) findViewById(R.id.ContactName_Text);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.correct_toast, Toast.LENGTH_LONG).show();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.incorrect_toast, Toast.LENGTH_LONG).show();
            }
        });

        mSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the text message with a string
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "The Vatican City is the smallest city in the world.");
                // Start the activity
                startActivity(sendIntent);
            }
        });

        mGetContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            try (Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String displayName = cursor.getString(displayNameIndex);

                    // Check if the display name is null
                    if (displayName == null) {
                        displayName = "No name found :(";
                    }

                    // Display the contact's name (or handle it as needed)
                    Toast.makeText(this, "Selected contact: " + displayName, Toast.LENGTH_LONG).show();
                    contactNameTextView.setText(displayName);
                }
            } catch (Exception e) {
                contactNameTextView.setText(e.toString());
                Toast.makeText(this, "Failed to retrieve contact details", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
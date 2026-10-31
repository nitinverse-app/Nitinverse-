package com.nitinverse.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.*;
import android.text.InputType;
import java.util.ArrayList;

public class MainActivity extends Activity {

    LinearLayout layout;
    EditText shayariInput;
    Spinner categorySpinner;
    TextView savedText;
    SharedPreferences preferences;
    ArrayList<String> savedList = new ArrayList<>();

    int gold = Color.rgb(212, 175, 55);
    int black = Color.rgb(15, 15, 15);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("NitinVerse", MODE_PRIVATE);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(22, 25, 22, 20);
        layout.setBackgroundColor(black);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(5, 5, 5, 20);

        TextView title = new TextView(this);
        title.setText("NitinVerse");
        title.setTextColor(gold);
        title.setTextSize(32);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        content.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("तुम्हारे अल्फ़ाज़, तुम्हारी पहचान");
        subtitle.setTextColor(Color.WHITE);
        subtitle.setTextSize(16);
        subtitle.setGravity(Gravity.CENTER);
        content.addView(subtitle);

        TextView heading = new TextView(this);
        heading.setText("\nअपनी शायरी लिखें");
        heading.setTextColor(gold);
        heading.setTextSize(21);
        content.addView(heading);

        categorySpinner = new Spinner(this);
        String[] categories = {
            "प्रेम", "दर्द", "वीर रस", "समाज", "जीवन", "अन्य"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, categories
        );
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );
        categorySpinner.setAdapter(adapter);
        content.addView(categorySpinner);

        shayariInput = new EditText(this);
        shayariInput.setHint("यहाँ अपनी शायरी लिखें...");
        shayariInput.setTextColor(Color.WHITE);
        shayariInput.setHintTextColor(Color.LTGRAY);
        shayariInput.setTextSize(18);
        shayariInput.setGravity(Gravity.TOP);
        shayariInput.setMinLines(5);
        shayariInput.setInputType(
            InputType.TYPE_CLASS_TEXT |
            InputType.TYPE_TEXT_FLAG_MULTI_LINE
        );
        content.addView(shayariInput);

        Button saveButton = new Button(this);
        saveButton.setText("शायरी सेव करें");
        content.addView(saveButton);

        TextView savedHeading = new TextView(this);
        savedHeading.setText("\nमेरी सुरक्षित रचनाएँ");
        savedHeading.setTextColor(gold);
        savedHeading.setTextSize(21);
        content.addView(savedHeading);

        savedText = new TextView(this);
        savedText.setTextColor(Color.WHITE);
        savedText.setTextSize(17);
        content.addView(savedText);

        saveButton.setOnClickListener(v -> saveShayari());

        loadShayari();

        scroll.addView(content);
        layout.addView(scroll);
        setContentView(layout);
    }

    void saveShayari() {
        String text = shayariInput.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (text.isEmpty()) {
            Toast.makeText(this, "पहले शायरी लिखें", Toast.LENGTH_SHORT).show();
            return;
        }

        savedList.add(category + "\n" + text);
        preferences.edit().putString("saved", joinList()).apply();

        shayariInput.setText("");
        showShayari();

        Toast.makeText(this, "शायरी सुरक्षित हो गई", Toast.LENGTH_SHORT).show();
    }

    String joinList() {
        StringBuilder result = new StringBuilder();
        for (String item : savedList) {
            result.append(item.replace("\n", "|||")).append("###");
        }
        return result.toString();
    }

    void loadShayari() {
        String data = preferences.getString("saved", "");
        if (!data.isEmpty()) {
            for (String item : data.split("###")) {
                if (!item.isEmpty()) {
                    savedList.add(item.replace("|||", "\n"));
                }
            }
        }
        showShayari();
    }

    void showShayari() {
        StringBuilder result = new StringBuilder();
        for (int i = savedList.size() - 1; i >= 0; i--) {
            result.append("\n")
                  .append(savedList.get(i))
                  .append("\n\n──────────────\n");
        }

        if (savedList.isEmpty()) {
            result.append("अभी कोई रचना सुरक्षित नहीं है।");
        }

        savedText.setText(result.toString());
    }
          }

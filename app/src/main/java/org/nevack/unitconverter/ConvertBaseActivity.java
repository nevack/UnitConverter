package org.nevack.unitconverter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.nevack.unitconverter.model.converter.Converter;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.LengthConverter;
import org.nevack.unitconverter.model.converter.MassConverter;
import org.nevack.unitconverter.model.converter.TemperatureConverter;
import org.nevack.unitconverter.model.converter.VolumeConverter;

public class ConvertBaseActivity extends AppCompatActivity implements OnItemSelectedListener, TextWatcher {
    private EditText sourceText;
    private EditText resultText;
    private Spinner sourceSpinner;
    private Spinner resultSpinner;
    private ImageButton imageButton;

    private Converter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!resultText.getText().toString().equals("")) {
                    ClipboardManager clipboard = (ClipboardManager)
                            getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("result value", resultText.getText().toString());
                    clipboard.setPrimaryClip(clipData);
                    Toast.makeText(ConvertBaseActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switch (EUnitCategory.values()[getIntent().getIntExtra("ConverterID", 0)]) {
            case MASS:
                converter = new MassConverter(this);
                break;
            case VOLUME:
                converter = new VolumeConverter(this);
                break;
            case LENGTH:
                converter = new LengthConverter(this);
                break;
            case TEMPERATURE:
                converter = new TemperatureConverter(this);
                break;
            default:
                converter = new MassConverter(this);
                break;
        }

        setTitle(converter.getTitle());

        sourceSpinner = (Spinner) findViewById(R.id.sourcespinner);
        resultSpinner = (Spinner) findViewById(R.id.resultspinner);

        initData();

        sourceText = (EditText) findViewById(R.id.sourcevalue);
        resultText = (EditText) findViewById(R.id.resultvalue);

        sourceText.addTextChangedListener(this);

        sourceSpinner.setOnItemSelectedListener(this);
        resultSpinner.setOnItemSelectedListener(this);

        imageButton = (ImageButton) findViewById(R.id.swapbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = sourceText.getText().toString();
                sourceText.setText(resultText.getText().toString());
                resultText.setText(temp);
                int temppos = sourceSpinner.getSelectedItemPosition();
                sourceSpinner.setSelection(resultSpinner.getSelectedItemPosition());
                resultSpinner.setSelection(temppos);
                convert();
            }
        });
    }

    private void initData() {
        sourceSpinner.setAdapter(converter.getAdapter());
        resultSpinner.setAdapter(converter.getAdapter());
    }

    private void convert() {
        if (sourceText.getText().toString().equals("")) resultText.setText("");
        else resultText.setText(converter.convert(sourceText.getText().toString(),
                sourceSpinner.getSelectedItemPosition(),
                resultSpinner.getSelectedItemPosition()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        convert();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        convert();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        convert();
    }
}

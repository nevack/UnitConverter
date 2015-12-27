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
import android.widget.TextView;
import android.widget.Toast;

import org.nevack.unitconverter.model.converter.Converter;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.LengthConverter;
import org.nevack.unitconverter.model.converter.MassConverter;
import org.nevack.unitconverter.model.converter.OtherConverter;
import org.nevack.unitconverter.model.converter.SpeedConverter;
import org.nevack.unitconverter.model.converter.TemperatureConverter;
import org.nevack.unitconverter.model.converter.TimeConverter;
import org.nevack.unitconverter.model.converter.VolumeConverter;

public class ConvertBaseActivity extends AppCompatActivity implements OnItemSelectedListener, TextWatcher {
    private EditText sourceValue;
    private TextView sourceValueSymbol;
    private EditText resultValue;
    private TextView resultValueSymbol;
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
                if (!resultValue.getText().toString().equals("")) {
                    copyResultToClipboard(resultValue.getText().toString());
                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!resultValue.getText().toString().equals("")) {
                    copyResultToClipboard(resultValue.getText().toString() + resultValueSymbol.getText().toString());
                }
                return true;
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
            case SPEED:
                converter = new SpeedConverter(this);
                break;
            case TIME:
                converter = new TimeConverter(this);
                break;
            case OTHER:
                converter = new OtherConverter(this);
                break;
            default:
                converter = new OtherConverter(this);
                break;
        }

        setTitle(converter.getTitle());

        sourceSpinner = (Spinner) findViewById(R.id.sourcespinner);
        resultSpinner = (Spinner) findViewById(R.id.resultspinner);

        sourceValue = (EditText) findViewById(R.id.sourcevalue);
        sourceValueSymbol = (TextView) findViewById(R.id.sourcevaluesymbol);
        resultValue = (EditText) findViewById(R.id.resultvalue);
        resultValueSymbol = (TextView) findViewById(R.id.resultvaluesymbol);

        initData();

        sourceValue.addTextChangedListener(this);

        sourceSpinner.setOnItemSelectedListener(this);
        resultSpinner.setOnItemSelectedListener(this);

        imageButton = (ImageButton) findViewById(R.id.swapbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = sourceValue.getText().toString();
                sourceValue.setText(resultValue.getText().toString());
                resultValue.setText(temp);
                int position = sourceSpinner.getSelectedItemPosition();
                sourceSpinner.setSelection(resultSpinner.getSelectedItemPosition());
                resultSpinner.setSelection(position);
                convert();
            }
        });
    }

    private void copyResultToClipboard(String valueToCopy) {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("result", valueToCopy);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(ConvertBaseActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void initData() {
        sourceSpinner.setAdapter(converter.getAdapter());
        resultSpinner.setAdapter(converter.getAdapter());
        sourceValueSymbol.setText(converter.getUnitSymbol(0));
        resultValueSymbol.setText(converter.getUnitSymbol(0));
    }

    private void convert() {
        if (sourceValue.getText().toString().equals("")) resultValue.setText("");
        else resultValue.setText(converter.convert(sourceValue.getText().toString(),
                sourceSpinner.getSelectedItemPosition(),
                resultSpinner.getSelectedItemPosition()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sourceValueSymbol.setText(converter.getUnitSymbol(sourceSpinner.getSelectedItemPosition()));
        resultValueSymbol.setText(converter.getUnitSymbol(resultSpinner.getSelectedItemPosition()));
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
        if (sourceValue.getText().toString().equals(".")) {
            sourceValue.setText("0.");
            sourceValue.setSelection(sourceValue.getText().toString().length());
        }
        if (!sourceValue.getText().toString().equals("-")) convert();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.leave_in_anim, R.anim.leave_out_anim);
    }
}

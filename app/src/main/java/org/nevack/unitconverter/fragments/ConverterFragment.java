package org.nevack.unitconverter.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.AreaConverter;
import org.nevack.unitconverter.model.converter.Converter;
import org.nevack.unitconverter.model.converter.CurrencyConverter;
import org.nevack.unitconverter.model.converter.LengthConverter;
import org.nevack.unitconverter.model.converter.MassConverter;
import org.nevack.unitconverter.model.converter.MemoryConverter;
import org.nevack.unitconverter.model.converter.OtherConverter;
import org.nevack.unitconverter.model.converter.SpeedConverter;
import org.nevack.unitconverter.model.converter.TemperatureConverter;
import org.nevack.unitconverter.model.converter.TimeConverter;
import org.nevack.unitconverter.model.converter.VolumeConverter;

public class ConverterFragment extends Fragment{

    private static final String CATEGORY_ID = "category_id";
    private static final String COPY_RESULT = "converter_result";

    private Spinner mSourceSpinner;
    private EditText mSourceEditText;
    private TextView mSourceTextView;
    private EditText mResultEditText;
    private TextView mResultTextView;
    private Spinner mResultSpinner;
    private ImageButton mSwapImageButton;

    private EUnitCategory mCategory;
    private Converter mConverter;
    private AsyncTask mTask;
    private SpinnerListener mListener = new SpinnerListener();

    public static ConverterFragment newInstance(EUnitCategory unitCategory) {
        Bundle args = new Bundle();
        args.putSerializable(CATEGORY_ID, unitCategory);
        ConverterFragment fragment = new ConverterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = (EUnitCategory) getArguments().getSerializable(CATEGORY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_convert, container, false);

        mSourceSpinner = (Spinner) view.findViewById(R.id.sourcespinner);
        mSourceSpinner.setOnItemSelectedListener(mListener);

        mResultSpinner = (Spinner) view.findViewById(R.id.resultspinner);
        mResultSpinner.setOnItemSelectedListener(mListener);

        mSourceEditText = (EditText) view.findViewById(R.id.sourcevalue);
        mSourceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSourceEditText.getText().toString().equals(".")) {
                    mSourceEditText.setText("0.");
                    mSourceEditText.setSelection(mSourceEditText.getText().toString().length());
                }
                if (!mSourceEditText.getText().toString().equals("-")) convert();
            }
        });
        mResultEditText = (EditText) view.findViewById(R.id.resultvalue);

        mSourceTextView = (TextView) view.findViewById(R.id.sourcevaluesymbol);
        mResultTextView = (TextView) view.findViewById(R.id.resultvaluesymbol);

        mSwapImageButton = (ImageButton) view.findViewById(R.id.swapbutton);
        mSwapImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = mSourceEditText.getText().toString();
                mSourceEditText.setText(mResultEditText.getText().toString());
                mResultEditText.setText(temp);
                int position = mSourceSpinner.getSelectedItemPosition();
                mSourceSpinner.setSelection(mResultSpinner.getSelectedItemPosition());
                mResultSpinner.setSelection(position);
                convert();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTask = new InitDataAsync().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTask != null) mTask.cancel(true);
    }

    private void convert() {
        if (mSourceEditText.getText().toString().equals("")) mResultEditText.setText("");
        else mResultEditText.setText(mConverter.convert(mSourceEditText.getText().toString(),
                mSourceSpinner.getSelectedItemPosition(),
                mResultSpinner.getSelectedItemPosition()));
    }

    public ClipData getResult(boolean withUnitSymbol) {
        String textToCopy = "";
        if (!mResultEditText.getText().toString().equals("")) {
            textToCopy = mResultEditText.getText().toString();
        }
        if (withUnitSymbol)
        {
            textToCopy += mResultTextView.getText().toString();
        }
        return ClipData.newPlainText(COPY_RESULT, textToCopy);
    }

    private class SpinnerListener implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mSourceTextView.setText(mConverter.getUnitSymbol(mSourceSpinner.getSelectedItemPosition()));
            mResultTextView.setText(mConverter.getUnitSymbol(mResultSpinner.getSelectedItemPosition()));
            convert();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            convert();
        }
    }

    private class InitDataAsync extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(getString(R.string.loading_data));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            getActivity().setTitle(mConverter.getTitle());

            mSourceSpinner.setAdapter(mConverter.getAdapter());
            mResultSpinner.setAdapter(mConverter.getAdapter());
            mSourceTextView.setText(mConverter.getUnitSymbol(0));
            mResultTextView.setText(mConverter.getUnitSymbol(0));

            if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            switch (mCategory) {
                case MASS:
                    mConverter = new MassConverter(getActivity());
                    break;
                case VOLUME:
                    mConverter = new VolumeConverter(getActivity());
                    break;
                case LENGTH:
                    mConverter = new LengthConverter(getActivity());
                    break;
                case TEMPERATURE:
                    mConverter = new TemperatureConverter(getActivity());
                    break;
                case SPEED:
                    mConverter = new SpeedConverter(getActivity());
                    break;
                case TIME:
                    mConverter = new TimeConverter(getActivity());
                    break;
                case OTHER:
                    mConverter = new OtherConverter(getActivity());
                    break;
                case MEMORY:
                    mConverter = new MemoryConverter(getActivity());
                    break;
                case CURRENCY:
                    mConverter = new CurrencyConverter(getActivity());
                    break;
                case AREA:
                    mConverter = new AreaConverter(getActivity());
                    break;
                default:
                    mConverter = new OtherConverter(getActivity());
                    break;
            }

            return null;
        }
    }
}

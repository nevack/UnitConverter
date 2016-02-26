package org.nevack.unitconverter;

import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.nevack.unitconverter.fragments.ConverterFragment;
import org.nevack.unitconverter.model.EUnitCategory;

public class ConverterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FragmentManager manager = getFragmentManager();
        final ConverterFragment fragment = ConverterFragment
                .newInstance(EUnitCategory.values()[getIntent().getIntExtra("ConverterID", 0)]);
        manager.beginTransaction().add(R.id.fragment_container, fragment).commit();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyResultToClipboard(fragment.getResult(false));
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyResultToClipboard(fragment.getResult(true));
                return true;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.leave_in_anim, R.anim.leave_out_anim);
    }

    private void copyResultToClipboard(ClipData clipData) {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(ConverterActivity.this, R.string.copy_result_toast, Toast.LENGTH_SHORT).show();
    }
}

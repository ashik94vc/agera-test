package com.cepheuen.ageratest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.agera.Observable;
import com.google.android.agera.Receiver;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textView);
        EditText editText = (EditText) findViewById(R.id.editText);

        final DataSupplier mDataSupplier = new DataSupplier();

        Updatable updatable = new Updatable() {
            @Override
            public void update() {
                textView.setText(mDataSupplier.get());
            }
        };

        mDataSupplier.addUpdatable(updatable);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mDataSupplier.accept(s.toString());
            }
        });

    }

    private static class DataSupplier implements Observable,Supplier<String>,Receiver<String> {

        List<Updatable> mUpdatable = new ArrayList<>();
        private String mValue;

        @Override
        public void addUpdatable(@NonNull Updatable updatable) {

            mUpdatable.add(updatable);

        }

        @Override
        public void removeUpdatable(@NonNull Updatable updatable) {

            mUpdatable.remove(updatable);

        }

        @Override
        public void accept(@NonNull String value) {

            this.mValue = value;

            for(Updatable updatable : mUpdatable)
            {
                updatable.update();
            }

        }

        @NonNull
        @Override
        public String get() {
            return mValue;
        }
    }
}

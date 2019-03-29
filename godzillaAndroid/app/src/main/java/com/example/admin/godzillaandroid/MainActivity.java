package com.example.admin.godzillaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();

        BootstrapButton button = this.findViewById(R.id.login);
        button.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
        button.setRounded(true);
        button.setShowOutline(false);

        BootstrapButton button2 = this.findViewById(R.id.reset);
        button2.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
        button2.setRounded(true);
        button2.setShowOutline(false);
    }
}


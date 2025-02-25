package com.example.sqliteandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.sqliteandroid.databinding.FragmentLongitudBinding;
import com.example.sqliteandroid.databinding.FragmentTemperaturaBinding;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class LongitudFragment extends Fragment {

    private FragmentLongitudBinding longitud_biding;

    private final String[] unidades = {"Metro (m)", "Kilómetro (km)", "Centímetro (cm)", "Milímetro (mm)", "Pulgada (in)"};
    private final Map<String, Double> factoresConversion = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        longitud_biding = FragmentLongitudBinding.inflate(inflater, container, false);
        View view = longitud_biding.getRoot();

        // Configurar tasas de conversión con respecto al metro
        inicializarFactoresConversion();

        // Configurar los Spinners con las unidades
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, unidades);
        longitud_biding.spinnerFromLength.setAdapter(adapter);
        longitud_biding.spinnerToLength.setAdapter(adapter);

        // Escuchar cambios en el EditText y los Spinners
        longitud_biding.inputLongitud.addTextChangedListener(textWatcher);
        longitud_biding.spinnerFromLength.setOnItemSelectedListener(spinnerListener);
        longitud_biding.spinnerToLength.setOnItemSelectedListener(spinnerListener);

        return view;
    }

    private void inicializarFactoresConversion() {

        // Valores de converion

        factoresConversion.put("Metro (m)", 1.0);
        factoresConversion.put("Kilómetro (km)", 0.001);
        factoresConversion.put("Centímetro (cm)", 100.0);
        factoresConversion.put("Milímetro (mm)", 1000.0);
        factoresConversion.put("Pulgada (in)", 39.3701);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            convertirLongitud();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            convertirLongitud();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private void convertirLongitud() {
        String unidadDesde = longitud_biding.spinnerFromLength.getSelectedItem().toString();
        String unidadHasta = longitud_biding.spinnerToLength.getSelectedItem().toString();
        String inputTexto = longitud_biding.inputLongitud.getText().toString();

        if (inputTexto.isEmpty()) {
            longitud_biding.textResultLength.setText("Ingrese un valor válido");
            return;
        }

        double cantidad = Double.parseDouble(inputTexto);
        double factorDesde = factoresConversion.get(unidadDesde);
        double factorHasta = factoresConversion.get(unidadHasta);

        double resultado = (cantidad / factorDesde) * factorHasta;

        longitud_biding.textResultLength.setText(String.format(Locale.US, "%.4f %s", resultado, unidadHasta));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        longitud_biding = null; // Evitar memory leaks
    }

}
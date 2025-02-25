package com.example.sqliteandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sqliteandroid.databinding.FragmentTemperaturaBinding;


public class TemperaturaFragment extends Fragment {

    private FragmentTemperaturaBinding temp_biding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        temp_biding = FragmentTemperaturaBinding.inflate(inflater, container, false);
        View view = temp_biding.getRoot();

        EditText inputTemperature = temp_biding.inputTemperature;
        Spinner spinnerFrom = temp_biding.spinnerFrom;
        Spinner spinnerTo = temp_biding.spinnerTo;
        TextView textResult = temp_biding.textResult;

        String[] unidades = {"Celsius (°C)", "Fahrenheit (°F)", "Kelvin (K)", "Rankine (°R)", "Réaumur (°Re)"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, unidades);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calcularTemperatura(inputTemperature, spinnerFrom, spinnerTo, textResult);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerFrom.setOnItemSelectedListener(listener);
        spinnerTo.setOnItemSelectedListener(listener);

        inputTemperature.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularTemperatura(inputTemperature, spinnerFrom, spinnerTo, textResult);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        return view;
    }



    private void calcularTemperatura(EditText inputTemperature, Spinner spinnerFrom, Spinner spinnerTo, TextView textResult) {
        String tempStr = inputTemperature.getText().toString();
        if (tempStr.isEmpty()) {
            textResult.setText("Ingrese un valor");
            return;
        }

        double valor = Double.parseDouble(tempStr);
        String unidadDesde = spinnerFrom.getSelectedItem().toString();
        String unidadHasta = spinnerTo.getSelectedItem().toString();

        // Convertir a Celsius como punto de referencia
        double tempEnCelsius = convertirACelsius(valor, unidadDesde);

        // Convertir desde Celsius a la unidad final
        double resultado = convertirDesdeCelsius(tempEnCelsius, unidadHasta);

        // Mostrar el resultado formateado
        textResult.setText(String.format("%.2f %s", resultado, unidadHasta));
    }

    private double convertirACelsius(double valor, String unidadDesde) {
        switch (unidadDesde) {
            case "Fahrenheit (°F)": return (valor - 32) * 5 / 9;
            case "Kelvin (K)": return valor - 273.15;
            case "Rankine (°R)": return (valor - 491.67) * 5 / 9;
            case "Réaumur (°Re)": return valor * 5 / 4;
            default: return valor;  // --> Devolver valor si no hay conversion
        }
    }

    private double convertirDesdeCelsius(double valor, String unidadHasta) {
        switch (unidadHasta) {
            case "Fahrenheit (°F)": return (valor * 9 / 5) + 32;
            case "Kelvin (K)": return valor + 273.15;
            case "Rankine (°R)": return (valor * 9 / 5) + 491.67;
            case "Réaumur (°Re)": return valor * 4 / 5;
            default: return valor; // Si es Celsius, no se convierte
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        temp_biding = null; // Evitar memory leaks
    }
}


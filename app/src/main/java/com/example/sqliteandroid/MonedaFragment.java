package com.example.sqliteandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqliteandroid.databinding.FragmentMonedaBinding;

import java.util.HashMap;
import java.util.Map;

public class MonedaFragment extends Fragment {
    private FragmentMonedaBinding moneda_binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        moneda_binding = FragmentMonedaBinding.inflate(inflater, container, false);
        View view = moneda_binding.getRoot();

        EditText inputMonto = moneda_binding.inputMonto;
        Spinner spinnerFrom = moneda_binding.spinnerFromCurrency;
        Spinner spinnerTo = moneda_binding.spinnerToCurrency;
        TextView textResult = moneda_binding.textResult;

            /*
                "USD ($) - Dólar estadounidense",
                "EUR (€) - Euro",
                "GBP (£) - Libra esterlina",
                "JPY (¥) - Yen japonés",
                "PEN (S/) - Sol peruano"
           */

        String[] monedas = {
                "USD ($)",
                "EUR (€)",
                "GBP (£)",
                "JPY (¥)",
                "PEN (S/)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, monedas);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        Map<String, Double> tasasCambio = new HashMap<>();
        tasasCambio.put("USD ($)", 1.0);
        tasasCambio.put("EUR (€)", 0.95);
        tasasCambio.put("GBP (£)", 0.79);
        tasasCambio.put("JPY (¥)", 149.14);
        tasasCambio.put("PEN (S/)", 3.680);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calcularConversion(inputMonto, spinnerFrom, spinnerTo, textResult, tasasCambio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerFrom.setOnItemSelectedListener(listener);
        spinnerTo.setOnItemSelectedListener(listener);

        // Detectar cambios en el campo de monto
        inputMonto.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularConversion(inputMonto, spinnerFrom, spinnerTo, textResult, tasasCambio);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        return view;
    }

    private void calcularConversion(EditText inputMonto, Spinner spinnerFrom, Spinner spinnerTo, TextView textResult, Map<String, Double> tasasCambio) {
        String montoStr = inputMonto.getText().toString();
        if (montoStr.isEmpty()) {
            textResult.setText("Ingrese un Monto");
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            textResult.setText("Entrada inválida");
            return;
        }

        String fromCurrency = spinnerFrom.getSelectedItem().toString();
        String toCurrency = spinnerTo.getSelectedItem().toString();

        double tasaFrom = tasasCambio.getOrDefault(fromCurrency, 1.0);
        double tasaTo = tasasCambio.getOrDefault(toCurrency, 1.0);

        // Conversión basada en el dólar como punto de referencia
        double resultado = (cantidad / tasaFrom) * tasaTo;

        // Mostrar el resultado formateado
        textResult.setText(String.format("%.2f %s", resultado, toCurrency));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        moneda_binding = null;
    }
}
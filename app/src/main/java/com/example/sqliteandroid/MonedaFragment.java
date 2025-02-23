package com.example.sqliteandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.sqliteandroid.databinding.FragmentMonedaBinding;

import java.util.HashMap;
import java.util.Map;

public class MonedaFragment extends Fragment {
    //private FragmentMonedaBinding moneda_biding;
    private FragmentMonedaBinding monedaBinding;
    private final String[] monedas = {"USD", "EUR", "GBP", "JPY", "PEN"};
    private final Map<String, Double> tasasCambio = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        monedaBinding = FragmentMonedaBinding.inflate(inflater, container, false);
        return monedaBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configurarSpinners();
        inicializarTasasCambio();

        // Acción al presionar el botón de conversión
        monedaBinding.buttonConvert.setOnClickListener(v -> convertirMoneda());
    }


    private void configurarSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, monedas);
        monedaBinding.spinnerFromCurrency.setAdapter(adapter);
        monedaBinding.spinnerToCurrency.setAdapter(adapter);

        // Seleccionar por defecto USD -> PEN
        monedaBinding.spinnerFromCurrency.setSelection(0);
        monedaBinding.spinnerToCurrency.setSelection(4);

        monedaBinding.spinnerFromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertirMoneda(); // Actualizar conversión cuando cambie la moneda
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        monedaBinding.spinnerToCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertirMoneda();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void inicializarTasasCambio() {
        tasasCambio.put("USD", 1.0);
        tasasCambio.put("EUR", 0.92);
        tasasCambio.put("GBP", 0.78);
        tasasCambio.put("JPY", 145.5);
        tasasCambio.put("PEN", 3.75);
    }

    private void convertirMoneda() {
        String fromCurrency = monedaBinding.spinnerFromCurrency.getSelectedItem().toString();
        String toCurrency = monedaBinding.spinnerToCurrency.getSelectedItem().toString();
        String cantidadTexto = monedaBinding.inputAmount.getText().toString();

        if (cantidadTexto.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad = Double.parseDouble(cantidadTexto);
        double tasaFrom = tasasCambio.get(fromCurrency);
        double tasaTo = tasasCambio.get(toCurrency);

        // Conversión: (cantidad / tasaFrom) * tasaTo
        double resultado = (cantidad / tasaFrom) * tasaTo;
        String resultadoTexto = String.format("%.2f %s", resultado, toCurrency);

        monedaBinding.textResult.setText(resultadoTexto);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        monedaBinding = null;
    }
}
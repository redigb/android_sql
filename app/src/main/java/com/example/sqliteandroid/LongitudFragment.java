package com.example.sqliteandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sqliteandroid.databinding.FragmentLongitudBinding;


public class LongitudFragment extends Fragment {

    private FragmentLongitudBinding longitud_biding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_longitud, container, false);
    }

}
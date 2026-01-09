package com.example.ferrol_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class InicioFragment extends Fragment {

    public InicioFragment() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflar layout
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Referencias a los botones
        Button btnScoreboard = view.findViewById(R.id.btn_scoreboard);
        Button btnForos = view.findViewById(R.id.btn_foros);

        // Listener del botón Scoreboard
        btnScoreboard.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new BlankFragment()) //cambiar por scoreboard fragment
                        .addToBackStack(null) // Permite volver al InicioFragment
                        .commit();
            }
        });

        // Listener del botón Foros
        btnForos.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new BlankFragment()) //cambiar por foros fragment
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}

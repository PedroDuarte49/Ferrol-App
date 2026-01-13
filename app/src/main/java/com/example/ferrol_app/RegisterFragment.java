package com.example.ferrol_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterFragment extends Fragment {

    public RegisterFragment() { }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        TextView tvError = view.findViewById(R.id.tvError);
        Button btnCreate = view.findViewById(R.id.btnCreateAccount);

        btnCreate.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            tvError.setVisibility(View.GONE);

            // VALIDACIÓN BÁSICA
            if (username.isEmpty() || password.isEmpty()) {
                tvError.setText("Rellena todos los campos");
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            // SIMULACIÓN: usuario ya existente
            if (username.equalsIgnoreCase("admin")) {
                tvError.setText("Ese nombre ya existe");
                tvError.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(
                        getContext(),
                        "Cuenta creada correctamente",
                        Toast.LENGTH_SHORT
                ).show();
                // Registro correcto → ir a Login
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
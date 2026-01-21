package com.example.ferrol_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {}

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
        TextView tvGoLogin = view.findViewById(R.id.tvGoLogin);
        Button btnCreate = view.findViewById(R.id.btnCreateAccount);

        btnCreate.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            tvError.setVisibility(View.GONE);

            if (username.isEmpty() || password.isEmpty()) {
                tvError.setText("Rellena todos los campos");
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            registrarUsuario(username, password, tvError);
        });

        tvGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navegar al LoginFragment
                // Usando FragmentTransaction
                Fragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                transaction.replace(R.id.fragmentContainer, loginFragment);
                transaction.addToBackStack(null); // Para poder volver
                transaction.commit();
            }
        });

        return view;
    }

    private void registrarUsuario(String username, String password, TextView tvError) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        RegisterRequest body = new RegisterRequest(username, password);

        service.register(body).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .commit();

                } else if (response.code() == 409) {
                    tvError.setText("Ese nombre ya existe");
                    tvError.setVisibility(View.VISIBLE);

                } else {
                    tvError.setText("Error al registrar usuario");
                    tvError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                tvError.setText("No se pudo conectar al servidor");
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }
}

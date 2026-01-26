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

import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvGoLogin;
    private Button btnCreate;
    private TextInputLayout tilUser;
    private TextInputLayout tilPassword;

    public RegisterFragment() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etUsername = view.findViewById(R.id.etUser);
        etPassword = view.findViewById(R.id.etPassword);
        tvGoLogin = view.findViewById(R.id.tvGoLogin);
        btnCreate = view.findViewById(R.id.btnCreateAccount);
        tilUser = view.findViewById(R.id.tilUser);
        tilPassword = view.findViewById(R.id.tilPassword);

        etUsername.addTextChangedListener(new LoginFragment.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilUser.setError(null);
            }
        });

        etPassword.addTextChangedListener(new LoginFragment.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setError(null);
            }
        });

        btnCreate.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty()) {
                tilUser.setError("Usuario no válido");
                return;
            } else {
                tilUser.setError(null);
            }

            if (password.isEmpty() || password.length() < 6) {
                tilPassword.setError("Mínimo 6 caracteres");
                return;
            } else {
                tilPassword.setError(null);
            }

            registrarUsuario(username, password);
        });

        return view;
    }

    private void registrarUsuario(String username, String password) {

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
                    //tilUser.setError("Usuario no registrado");
                    tilUser.setError("Ese nombre ya existe");

                } else {
                    Toast.makeText(getContext(), "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

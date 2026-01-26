package com.example.ferrol_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginFragment extends Fragment {

    private TextInputLayout tilUser;
    private TextInputLayout tilPassword;
    private EditText editUsuario;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Referencias
        editUsuario = view.findViewById(R.id.etUser);
        editPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_register);
        tilUser = view.findViewById(R.id.tilUser);
        tilPassword = view.findViewById(R.id.tilPassword);

        editUsuario.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilUser.setError(null);
            }
        });

        editPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setError(null);
            }
        });

        // BOTÓN LOGIN
        btnLogin.setOnClickListener(v -> {
            String username = editUsuario.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

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

            login(username, password);
        });

        // BOTÓN REGISTER
        btnRegister.setOnClickListener(v -> {
             RegisterFragment registerFragment = new RegisterFragment();

            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, registerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }




    private void login(String username, String password) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://10.0.2.2:8000/auth/login");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("username", username);
                json.put("password", password);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                // Leer stream correcto según código
                BufferedReader br;
                if (responseCode >= 200 && responseCode < 300) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                int finalResponseCode = responseCode;
                String finalResponse = response.toString();

                requireActivity().runOnUiThread(() -> {
                    try {
                        JSONObject respJson = new JSONObject(finalResponse);

                        if (finalResponseCode == HttpURLConnection.HTTP_CREATED) {
                            String token = respJson.getString("token");
                            saveToken(token);
                            Snackbar.make(requireView(), "Login correcto", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(Color.parseColor("#00FFAA")) // color de fondo, estilo neon
                                    .setTextColor(Color.BLACK) // color del texto
                                    .setAnchorView(R.id.btn_login) // opcional, aparece sobre el botón
                                    .show();
                            InicioFragment inicioFragment = new InicioFragment();

                            if (getActivity() != null) {
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragmentContainer, inicioFragment)
                                        .commit();
                            }
                        } else if (finalResponseCode == 404) {
                            tilUser.setError("Usuario no registrado");

                        } else if (finalResponseCode == 401) {
                            tilPassword.setError("Contraseña incorrecta");

                        } else {
                            Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error procesando la respuesta", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Error de conexión del servidor", Toast.LENGTH_SHORT).show()
                );
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
    public abstract static class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
    private void saveToken(String token) {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        prefs.edit().putString("AUTH_TOKEN", token).apply();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).actualizarTextoLogin();
        }
    }
}

package com.example.ferrol_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginFragment extends Fragment {

    private EditText editUsuario;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Referencias
        editUsuario = view.findViewById(R.id.edit_usuario);
        editPassword = view.findViewById(R.id.edit_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_register);

        // BOTÓN LOGIN
        btnLogin.setOnClickListener(v -> {
            String username = editUsuario.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                login(username, password);
            }
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
                            Toast.makeText(getContext(), "Login correcto", Toast.LENGTH_SHORT).show();
                            InicioFragment inicioFragment = new InicioFragment();

                            if (getActivity() != null) {
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragmentContainer, inicioFragment)
                                        .commit();
                            }
                        } else if (finalResponseCode == 404) {
                            Toast.makeText(getContext(), "Usuario no registrado", Toast.LENGTH_SHORT).show();

                        } else if (finalResponseCode == 401) {
                            Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Error del servidor", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error procesando la respuesta", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
                );
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
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

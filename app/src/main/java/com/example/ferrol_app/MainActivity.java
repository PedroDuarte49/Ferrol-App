package com.example.ferrol_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        actualizarTextoLogin();

        // Cargar fragment inicial (Menú Principal)
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new InicioFragment())
                    .commit();
        }

        // Listener del menú inferior
        bottomNavigationView.setOnItemSelectedListener(item -> {
            actualizarTextoLogin();
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_main) {
                selectedFragment = new InicioFragment();

            } else if (item.getItemId() == R.id.nav_login) {

                SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
                String token = prefs.getString("AUTH_TOKEN", null);

                if (token == null) {
                    // NO logueado → ir a login
                    selectedFragment = new LoginFragment();
                } else {
                    // YA logueado → preguntar si quiere cerrar sesión
                    mostrarDialogoCerrarSesion();
                    return false; // ⚠️ importante: no cambia de fragment
                }
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });
    }
    private void mostrarDialogoCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Sesión iniciada")
                .setMessage("Ya estás logueado. ¿Quieres cerrar sesión?")
                .setPositiveButton("Cerrar sesión", (dialog, which) -> {
                    SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
                    prefs.edit().clear().apply();
                    actualizarTextoLogin();
                    Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    public void actualizarTextoLogin() {
        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        String token = prefs.getString("AUTH_TOKEN", null);

        if (token != null) {
            bottomNavigationView.getMenu()
                    .findItem(R.id.nav_login)
                    .setTitle("Cerrar sesión");
        } else {
            bottomNavigationView.getMenu()
                    .findItem(R.id.nav_login)
                    .setTitle("Iniciar sesión");
        }
    }

}

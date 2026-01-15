package com.example.ferrol_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScoreboardFragment extends Fragment {

    private ScoreAdapter adapter;
    private RecyclerView recyclerView;

    public ScoreboardFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scoreboard_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Configurar RecyclerView
        recyclerView = view.findViewById(R.id.rvScoreboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 2. Inicializar el adaptador con una lista vacía (para que no de error al inicio)
        adapter = new ScoreAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 3. Llamar a la API para obtener los datos reales
        cargarDatosDesdeApi();
    }

    private void cargarDatosDesdeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                // 10.0.2.2 es la IP para acceder al localhost del PC desde el emulador
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.getScoreboard().enqueue(new Callback<ScoreResponse>() {
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Obtenemos la lista "scores" del JSON y actualizamos el adapter
                    List<Score> listaReal = response.body().scores;
                    adapter.updateData(listaReal);
                } else {
                    Log.e("API_ERROR", "Respuesta no exitosa: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error de red: " + t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
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

public class ForoFragment extends Fragment {

    private ForoAdapter adapter;
    private RecyclerView recyclerView;

    public ForoFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerForos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 2. Inicializar adapter vacío
        adapter = new ForoAdapter(new ArrayList<>(), foro -> {
            Toast.makeText(getContext(), "Foro: " + foro.getTitulo(), Toast.LENGTH_SHORT).show();

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, PagForosFragment.newInstance(foro.getId()))
                        .addToBackStack(null) // para volver atrás
                        .commit();

        });



        recyclerView.setAdapter(adapter);

        // 3. Llamar a la API
        cargarForosDesdeApi();
    }

    private void cargarForosDesdeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.getForos().enqueue(new Callback<ForosResponse>() {
            @Override
            public void onResponse(Call<ForosResponse> call, Response<ForosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Foro> listaReal = response.body().getForos();
                    adapter.updateData(listaReal);

                } else {
                    Log.e("API_ERROR", "Respuesta no exitosa: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ForosResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error de red: " + t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

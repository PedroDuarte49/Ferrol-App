package com.example.ferrol_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ferrol_app.Foro;
import com.example.ferrol_app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForoFragment extends Fragment {

    private LinearLayout foroContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_foro, container, false);
        foroContainer = view.findViewById(R.id.foroContainer);

        loadForos();

        return view;
    }

    private void loadForos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ForosResponse> call = apiService.getForos();

        call.enqueue(new Callback<ForosResponse>() {
            @Override
            public void onResponse(Call<ForosResponse> call, Response<ForosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Foro> foros = response.body().getForos();

                    for (Foro foro : foros) {
                        Button boton = new Button(getContext());
                        boton.setText(foro.getTitulo());
                        boton.setOnClickListener(v ->
                                Toast.makeText(getContext(),
                                        "Foro ID: " + foro.getId(),
                                        Toast.LENGTH_SHORT).show()
                        );
                        foroContainer.addView(boton);
                    }
                } else {
                    Toast.makeText(getContext(), "Error cargando foros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForosResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
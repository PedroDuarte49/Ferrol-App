package com.example.ferrol_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagForosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagForosFragment extends Fragment {

    private static final String ARG_ID_FORO = "id_foro";

    public static PagForosFragment newInstance(int idForo) {
        PagForosFragment fragment = new PagForosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_FORO, idForo);
        fragment.setArguments(args);
        return fragment;
    }

    public PagForosFragment() {
        super(R.layout.fragment_pag_foros);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton info = view.findViewById(R.id.info);
        TextView txtTitulo = view.findViewById(R.id.txtTitulo);
        RecyclerView recycler = view.findViewById(R.id.recyclerMensajes);
        ImageButton btnAgregar = view.findViewById(R.id.btnAgregar);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        int idForo = getArguments() != null ? getArguments().getInt(ARG_ID_FORO) : 1;


        cargarTituloForo(idForo, txtTitulo);

        cargarComentarios(idForo, recycler);

        info.setOnClickListener(v -> {
            mostrarContenidoForo(idForo);
        });
        btnAgregar.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Agregar mensaje", Toast.LENGTH_SHORT).show()
        );
    }
    private void mostrarContenidoForo(int idForo) {

        new Thread(() -> {
            try {
                String urlString = "http://10.0.2.2:8000/foros";

                java.net.URL url = new java.net.URL(urlString);
                java.net.HttpURLConnection conn =
                        (java.net.HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                java.io.BufferedReader reader =
                        new java.io.BufferedReader(
                                new java.io.InputStreamReader(conn.getInputStream()));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                org.json.JSONObject json = new org.json.JSONObject(response.toString());
                org.json.JSONArray foros = json.getJSONArray("foros");

                String contenido = "Sin contenido";

                for (int i = 0; i < foros.length(); i++) {
                    org.json.JSONObject foro = foros.getJSONObject(i);

                    if (foro.getInt("id") == idForo) {
                        contenido = foro.getString("contenido");
                        break;
                    }
                }

                String finalContenido = contenido;

                requireActivity().runOnUiThread(() -> {
                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setTitle("Contenido del foro")
                            .setMessage(finalContenido)
                            .setPositiveButton("Cerrar", null)
                            .show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(),
                                "Error cargando contenido",
                                Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void cargarTituloForo(int idForo, TextView txtTitulo) {

        new Thread(() -> {
            try {
                String urlString = "http://10.0.2.2:8000/foros";

                java.net.URL url = new java.net.URL(urlString);
                java.net.HttpURLConnection conn =
                        (java.net.HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                java.io.BufferedReader reader =
                        new java.io.BufferedReader(
                                new java.io.InputStreamReader(conn.getInputStream()));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                org.json.JSONObject json = new org.json.JSONObject(response.toString());
                org.json.JSONArray foros = json.getJSONArray("foros");

                String tituloEncontrado = "Foro";

                for (int i = 0; i < foros.length(); i++) {
                    org.json.JSONObject foro = foros.getJSONObject(i);

                    int id = foro.getInt("id");

                    if (id == idForo) {
                        tituloEncontrado = foro.getString("titulo");
                        break;
                    }
                }

                String finalTitulo = tituloEncontrado;

                requireActivity().runOnUiThread(() ->
                        txtTitulo.setText(finalTitulo)
                );

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        txtTitulo.setText("Foro")
                );
            }
        }).start();
    }
    private void cargarComentarios(int idForo, RecyclerView recycler) {

        new Thread(() -> {
            try {
                String urlString = "http://10.0.2.2:8000/foros/" + idForo;

                java.net.URL url = new java.net.URL(urlString);
                java.net.HttpURLConnection conn =
                        (java.net.HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                java.io.BufferedReader reader =
                        new java.io.BufferedReader(
                                new java.io.InputStreamReader(conn.getInputStream()));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                org.json.JSONObject json = new org.json.JSONObject(response.toString());
                org.json.JSONArray comentarios = json.getJSONArray("comentarios");

                List<Mensaje> lista = new ArrayList<>();

                for (int i = 0; i < comentarios.length(); i++) {
                    org.json.JSONObject c = comentarios.getJSONObject(i);

                    String user = c.getString("username");
                    String texto = c.getString("comentario");
                    String fecha = c.getString("datetime");

                    lista.add(new Mensaje(user + " / " + fecha, texto));
                }

                requireActivity().runOnUiThread(() -> {
                    recycler.setAdapter(new MensajeAdapter(lista));
                });

            } catch (Exception e) {
                e.printStackTrace();

                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(),
                                "Error cargando comentarios",
                                Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}
package com.example.ferrol_app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagForosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagForosFragment extends Fragment {

    private static final String ARG_ID_FORO = "id_foro";

    private String getToken() {
        return requireActivity()
                .getSharedPreferences("APP_PREFS", requireContext().MODE_PRIVATE)
                .getString("AUTH_TOKEN", null);
    }

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

        TextView txtTitulo = view.findViewById(R.id.txtTitulo);
        RecyclerView recycler = view.findViewById(R.id.recyclerMensajes);
        TextView btnAgregar = view.findViewById(R.id.tvAgregarComentario);
        LinearLayout layoutComentario = view.findViewById(R.id.layoutComentario);
        EditText editComentario = view.findViewById(R.id.editComentarioMini);
        ImageButton btnEnviar = view.findViewById(R.id.btnEnviarComentario);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        int idForo = getArguments() != null ? getArguments().getInt(ARG_ID_FORO) : 1;

        txtTitulo.setClickable(true);
        txtTitulo.setFocusable(true);
        txtTitulo.setTextColor(Color.parseColor("#00F5FF")); // color tipo “activo”
        txtTitulo.setPaintFlags(txtTitulo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // opcional subrayado

        btnAgregar.setOnClickListener(v -> {
            layoutComentario.setVisibility(View.VISIBLE);

            // Foco y teclado
            editComentario.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editComentario, InputMethodManager.SHOW_IMPLICIT);
        });
        btnEnviar.setOnClickListener(v -> {
            String texto = editComentario.getText().toString().trim();
            if(texto.isEmpty()) {
                Toast.makeText(requireContext(), "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            enviarComentario(idForo, texto);

            // Cerrar teclado
            InputMethodManager imm = (InputMethodManager) requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editComentario.getWindowToken(), 0);

            // limpiar y ocultar
            editComentario.setText("");
            layoutComentario.setVisibility(View.GONE);
        });

        cargarTituloForo(idForo, txtTitulo);

        cargarComentarios(idForo, recycler);

        txtTitulo.setOnClickListener(v -> {
            mostrarContenidoForo(idForo);
        });
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

                    // Formatear fecha a local
                    String fechaLocal = formatearFechaLocal(fecha);

                    lista.add(new Mensaje(user + " / " + fechaLocal, texto));
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
    private void enviarComentario(int idForo, String textoComentario) {

        new Thread(() -> {
            try {
                String token = getToken();

                if (token == null) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(),
                                    "Debes iniciar sesión para comentar",
                                    Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                URL url = new URL("http://10.0.2.2:8000/foros/" + idForo);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", token); // 🔐 TOKEN
                conn.setDoOutput(true);

                // JSON body
                org.json.JSONObject json = new org.json.JSONObject();
                json.put("comentario", textoComentario);

                java.io.OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_CREATED) {

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(),
                                "Comentario enviado",
                                Toast.LENGTH_SHORT).show();

                        // Recargar comentarios
                        cargarComentarios(idForo,
                                (RecyclerView) getView().findViewById(R.id.recyclerMensajes));
                    });

                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(),
                                    "Error al enviar comentario",
                                    Toast.LENGTH_SHORT).show()
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(),
                                "Error de conexión",
                                Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    private String formatearFechaLocal(String isoTime) {
        try {
            // Parse ISO 8601
            ZonedDateTime utcDateTime = ZonedDateTime.parse(isoTime);

            // Convertir a zona horaria local del dispositivo
            ZonedDateTime localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault());

            // Formato: día/mes/año hora:minutos:segundos
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    .withLocale(Locale.getDefault());

            return localDateTime.format(formatter);

        } catch (Exception e) {
            e.printStackTrace();
            return isoTime; // Si falla, devolver el texto original
        }
    }
}
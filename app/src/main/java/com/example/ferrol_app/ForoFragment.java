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

public class ForoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_foro, container, false);
        LinearLayout foroContainer = view.findViewById(R.id.foroContainer);

        // 🔹 SIMULACIÓN (esto vendrá luego del backend)
        List<Foro> foros = new ArrayList<>();
        foros.add(new Foro("bugs", "Foro sobre bugs"));
        foros.add(new Foro("general", "Foro general"));
        foros.add(new Foro("ayuda", "Foro de ayuda"));

        // Crear botones dinámicos
        for (Foro foro : foros) {
            Button btn = new Button(getContext());
            btn.setText(foro.getId());

            btn.setOnClickListener(v -> {
                // AQUÍ irá la llamada a la API
                Toast.makeText(
                        getContext(),
                        "Abrir foro: " + foro.getId(),
                        Toast.LENGTH_SHORT
                ).show();
            });

            foroContainer.addView(btn);
        }

        // 🔸 BOTÓN DE PRUEBA TOTAL
        Button testButton = new Button(getContext());
        testButton.setText("Foro prueba API (/foro/bugs)");
        testButton.setOnClickListener(v ->
                Toast.makeText(
                        getContext(),
                        "GET /foro/bugs con token",
                        Toast.LENGTH_LONG
                ).show()
        );

        foroContainer.addView(testButton);

        return view;
    }
}

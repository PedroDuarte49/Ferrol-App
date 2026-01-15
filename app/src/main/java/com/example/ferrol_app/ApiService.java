package com.example.ferrol_app;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

// Clase para mapear el objeto raíz { "scores": [...] }
class ScoreResponse {
    public List<Score> scores;
}

public interface ApiService {
    @GET("score") // El path que definiste en tu urls.py
    Call<ScoreResponse> getScoreboard();
}
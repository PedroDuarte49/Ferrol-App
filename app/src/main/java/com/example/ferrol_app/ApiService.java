package com.example.ferrol_app;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

// Clase para mapear el objeto raíz { "scores": [...] }
class ScoreResponse {
    public List<Score> scores;
}
class ForosResponse {
    private List<Foro> foros;
    public List<Foro> getForos() { return foros; }
}
class RegisterResponse {
    public String message;
    public String error;
}
class RegisterRequest {
    public String username;
    public String password;

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public interface ApiService {
    @GET("score") // El path que definiste en tu urls.py
    Call<ScoreResponse> getScoreboard();
    @GET("foros")
    Call<ForosResponse> getForos();
    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest body);
}
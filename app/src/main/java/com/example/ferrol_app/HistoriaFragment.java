package com.example.ferrol_app;

import android.media.MediaPlayer;

import androidx.fragment.app.Fragment;

public class HistoriaFragment extends Fragment {

    private MediaPlayer mediaPlayer;

    public HistoriaFragment() {
        super(R.layout.fragment_historia);
    }

    @Override
    public void onStart() {
        super.onStart();

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.himnoracingclubferrol);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
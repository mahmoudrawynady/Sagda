package com.example.mahmoudrawy.eladan.Views.Lisenters;

import android.os.Bundle;


public interface MapsLisenter {
    void playProgress();

    void hideProgress();

    void launchDetailsActivityWithData(Bundle bundle);

    void showMessageToUser(String message);
}

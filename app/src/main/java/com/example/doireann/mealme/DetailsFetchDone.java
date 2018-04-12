package com.example.doireann.mealme;

import android.content.Context;

/**
 * Created by Doireann on 2018-04-08.
 */

public interface DetailsFetchDone {
    void onDetailsFetchDone(Recipe recipe);
    Context getTriviaContext();

}

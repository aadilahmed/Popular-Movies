package com.example.android.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavoriteEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<FavoriteEntry>> favorites;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        favorites = db.favoriteDao().loadAllFavorites();
    }

    public LiveData<List<FavoriteEntry>> getFavorites() {
        return favorites;
    }
}

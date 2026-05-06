package com.example.a81.database;

import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;

@Database(entities = {MessageEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDao messageDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "chat_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

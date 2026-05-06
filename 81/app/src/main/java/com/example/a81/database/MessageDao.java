package com.example.a81.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(MessageEntity message);

    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    List<MessageEntity> getAll();

    @Query("DELETE FROM messages")
    void clearAll();
}

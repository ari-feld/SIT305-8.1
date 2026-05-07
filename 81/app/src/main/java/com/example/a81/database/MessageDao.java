package com.example.a81.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(MessageEntity message);

    @Query("SELECT * FROM messages WHERE username = :username ORDER BY timestamp ASC")
    List<MessageEntity> getMessagesForUser(String username);

    @Query("DELETE FROM messages")
    void clearAll();
}
package com.example.a81.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class MessageEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;   // who sent (user or bot)
    public String message;
    public long timestamp;
    public boolean isUser;    // true = user, false = bot
}

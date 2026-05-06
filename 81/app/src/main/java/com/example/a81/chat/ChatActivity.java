package com.example.a81.chat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a81.R;
import com.example.a81.database.AppDatabase;
import com.example.a81.database.MessageEntity;
import com.example.a81.network.ChatApi;

import com.example.a81.network.OpenAIRequest;
import com.example.a81.network.OpenAIResponse;
import com.example.a81.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "CHAT_DEBUG";
    RecyclerView recyclerView;
    EditText etMessage;
    Button btnSend;

    ChatAdapter adapter;
    List<MessageEntity> messageList = new ArrayList<>();

    AppDatabase db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Bind views
        recyclerView = findViewById(R.id.recyclerView);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        username = getIntent().getStringExtra("username");

        db = AppDatabase.getInstance(this);

        // Recycler setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(adapter);

        loadMessages();

        // Send button
        btnSend.setOnClickListener(v -> {

            String text = etMessage.getText().toString().trim();
            if (text.isEmpty()) return;

            MessageEntity userMsg = new MessageEntity();
            userMsg.username = username;
            userMsg.message = text;
            userMsg.timestamp = System.currentTimeMillis();
            userMsg.isUser = true;

            etMessage.setText("");

            new Thread(() -> {
                db.messageDao().insert(userMsg);

                runOnUiThread(() -> {
                    messageList.add(userMsg);
                    adapter.notifyItemInserted(messageList.size() - 1);
                    scrollToBottom();
                });

                getBotReply(text);
            }).start();
        });
    }

    // =========================
    // LOAD MESSAGES FROM ROOM
    // =========================
    private void loadMessages() {
        new Thread(() -> {

            List<MessageEntity> data = db.messageDao().getAll();

            runOnUiThread(() -> {
                messageList.clear();
                messageList.addAll(data);
                adapter.notifyDataSetChanged();
                scrollToBottom();
            });

        }).start();
    }

    // =========================
    // CALL OPENAI API
    // =========================
    private void getBotReply(String userText) {

        ChatApi api = RetrofitClient.getApi();

        List<OpenAIRequest.Message> messages = new ArrayList<>();

        messages.add(new OpenAIRequest.Message(
                "system",
                "You are an unhelpful assistant that talks like a pirate."
        ));

        for (MessageEntity m : messageList) {
            messages.add(new OpenAIRequest.Message(
                    m.isUser ? "user" : "assistant",
                    m.message
            ));
        }

        OpenAIRequest request = new OpenAIRequest(messages);

        api.sendMessage(request).enqueue(new Callback<OpenAIResponse>() {

            @Override
            public void onResponse(Call<OpenAIResponse> call,
                                   Response<OpenAIResponse> response) {

                Log.d("CHAT", "Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    String reply = response.body()
                            .choices.get(0)
                            .message.content;

                    MessageEntity botMsg = new MessageEntity();
                    botMsg.username = "Bot";
                    botMsg.message = reply;
                    botMsg.timestamp = System.currentTimeMillis();
                    botMsg.isUser = false;

                    new Thread(() -> {
                        db.messageDao().insert(botMsg);

                        runOnUiThread(() -> {
                            messageList.add(botMsg);
                            adapter.notifyItemInserted(messageList.size() - 1);
                            scrollToBottom();
                        });
                    }).start();
                } else {
                    Log.e("CHAT", "Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                Log.e("CHAT", "FAILED", t);
            }
        });
    }

    // =========================
    // HELPER
    // =========================
    private void scrollToBottom() {
        if (!messageList.isEmpty()) {
            recyclerView.scrollToPosition(messageList.size() - 1);
        }
    }
}
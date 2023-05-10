package com.example.artmart.community

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.artmart.R
import com.example.artmart.community.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var messageList: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    private lateinit var messages: ArrayList<Message>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageList = findViewById(R.id.messageList)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        database = FirebaseDatabase.getInstance()
        messages = ArrayList()

        adapter = MessageAdapter(messages)
        messageList.adapter = adapter

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                val sender = FirebaseAuth.getInstance().currentUser?.uid
                val timestamp = ServerValue.TIMESTAMP
                val messageRef = database.getReference("messages").push()
                messageRef.setValue(sender?.let { it1 -> Message(message, it1, timestamp) })
                messageInput.text.clear()
            }
        }

        database.getReference("messages").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(message)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Not used in this example
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Not used in this example
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Not used in this example
            }

            override fun onCancelled(error: DatabaseError) {
                // Not used in this example
            }
        })
    }
}


class MessageAdapter(private val messages: ArrayList<Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.text
    }
    }
package com.example.artmart.community

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
                messageRef.setValue(sender?.let { it1 -> Message(it1, message, it1, timestamp) })
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
                val message = snapshot.getValue(Message::class.java)
                if(message != null){
                    //find message in list and update
                    val index = messages.indexOfFirst { it.id == message.id }
                    if(index >= 0){
                        messages[index] = message
                        adapter.notifyItemChanged(index)
                    }

                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val message = snapshot.getValue(Message::class.java)
                if (messages != null){
                    //finding message and removing it
                    val index = messages.indexOfFirst { it.id == message!!.id }
                    if (index >= 0){
                        messages.removeAt(index)
                        adapter.notifyItemRemoved(index)

                    }

                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    // find the message in the list and move it to its new position
                    val index = messages.indexOfFirst { it.id == message.id }
                    if (index >= 0) {
                        messages.removeAt(index)
                        val newIndex = if (previousChildName == null) 0 else messages.indexOfFirst { it.id == previousChildName } + 1
                        messages.add(newIndex, message)
                        adapter.notifyItemMoved(index, newIndex)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Chat Activity","Error reading from database: ${error.message}" )
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
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.text
    }
    }
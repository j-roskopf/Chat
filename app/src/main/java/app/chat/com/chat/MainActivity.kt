package app.chat.com.chat

import adapters.MessageAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import models.Message
import java.util.*


class MainActivity : AppCompatActivity() {

    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        
        setupSendButton()

        createFirebaseListener()

    }

    /**
     * Setup firebase
     */
    private fun initFirebase() {
        //init firebase
        FirebaseApp.initializeApp(applicationContext)

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)

        //get reference to our db
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    /**
     * Set listener for Firebase
     */
    private fun createFirebaseListener(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val toReturn: ArrayList<Message> = ArrayList();

                for(data in dataSnapshot.children){
                    val messageData = data.getValue<Message>(Message::class.java)

                    //unwrap
                    val message = messageData?.let { it } ?: continue

                    toReturn.add(message)
                }

                //sort so newest at bottom
                toReturn.sortBy { message ->
                    message.timestamp
                }

                setupAdapter(toReturn)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //log error
            }
        }
        databaseReference?.child("messages")?.addValueEventListener(postListener)
    }

    /**
     * Once data is here - display it
     */
    private fun setupAdapter(data: ArrayList<Message>){
        val linearLayoutManager = LinearLayoutManager(this)
        mainActivityRecyclerView.layoutManager = linearLayoutManager
        mainActivityRecyclerView.adapter = MessageAdapter(data) {
            Toast.makeText(this, "${it.text} clicked", Toast.LENGTH_SHORT).show()
        }

        //scroll to bottom
        mainActivityRecyclerView.scrollToPosition(data.size - 1)
    }

    /**
     * OnClick action for the send button
     */
    private fun setupSendButton() {
        mainActivitySendButton.setOnClickListener {
            if (!mainActivityEditText.text.toString().isEmpty()){
                sendData()
            }else{
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Send data to firebase
     */
    private fun sendData(){
        databaseReference?.
                child("messages")?.
                child(java.lang.String.valueOf(System.currentTimeMillis()))?.
                setValue(Message(mainActivityEditText.text.toString()))

        //clear the text
        mainActivityEditText.setText("")
    }
}

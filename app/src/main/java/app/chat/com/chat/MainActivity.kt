package app.chat.com.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupSendButton()

    }

    /**
     * OnClick action for the send button
     */
    private fun setupSendButton() {
        mainActivitySendButton.setOnClickListener {
        }
    }
}

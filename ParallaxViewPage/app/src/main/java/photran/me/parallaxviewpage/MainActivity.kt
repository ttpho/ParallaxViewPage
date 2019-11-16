package photran.me.parallaxviewpage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        fun callMainActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}

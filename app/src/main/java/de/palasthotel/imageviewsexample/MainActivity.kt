package de.palasthotel.imageviewsexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		findViewById<ImageView>(R.id.imageview).setImageDrawable(getDrawable(R.drawable.lorempixel))
	}
}

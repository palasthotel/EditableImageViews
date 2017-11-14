package de.palasthotel.imageviewsexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import de.palasthotel.blurimageview.BlurImageView

class MainActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		val blur: BlurImageView = findViewById(R.id.imageview)
		blur.setImageDrawable(getDrawable(R.drawable.lorempixel))
		
		val image = findViewById<ImageView>(R.id.imageview_result)
		image.visibility = View.GONE
		
		findViewById<Button>(R.id.save).setOnClickListener{
			Toast.makeText(this, "Show", Toast.LENGTH_LONG).show()
			image.visibility = View.VISIBLE
			val bitmap = blur.getBlurredBitmap()
			image.setImageBitmap(bitmap)
			blur.visibility = View.GONE
		}
		
		findViewById<Button>(R.id.undo).setOnClickListener {
			blur.undo()
		}
		
		findViewById<ImageView>(R.id.imageview).setImageDrawable(getDrawable(R.drawable.lorempixel))
	}
}

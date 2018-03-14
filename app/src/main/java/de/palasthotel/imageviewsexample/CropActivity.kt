package de.palasthotel.imageviewsexample

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import de.palasthotel.cropimageview.ImageCropView

class CropActivity : AppCompatActivity() {
	
	var activeDrawable = R.drawable.ultra_high
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_crop)
		
		val editor: ImageCropView = findViewById(R.id.imageview)
		editor.setImageDrawable(ContextCompat.getDrawable(this, activeDrawable)!!)
		
		val image = findViewById<ImageView>(R.id.imageview_result)
		image.visibility = View.GONE
		
		findViewById<Button>(R.id.save).setOnClickListener{
			
			Toast.makeText(this, "Switched", Toast.LENGTH_SHORT).show()
			if(image.visibility != View.VISIBLE){
				image.visibility = View.VISIBLE
				
				val bitmap = editor.getCroppedBitmap()
				image.setImageBitmap(bitmap)
				editor.visibility = View.GONE
			} else {
				image.visibility = View.GONE
				editor.visibility = View.VISIBLE
			}
			
		}
		
		findViewById<Button>(R.id.ratio).setOnClickListener{
			
			Toast.makeText(this, "Ratio", Toast.LENGTH_SHORT).show()
			
			activeDrawable = when(activeDrawable){
				R.drawable.lorempixel -> R.drawable.ultra_high
				R.drawable.ultra_high -> R.drawable.flat
				else -> R.drawable.lorempixel
			}
			image.setImageDrawable(ContextCompat.getDrawable(this, activeDrawable))
		}
		
	}
}

package de.palasthotel.imageviewsexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import de.palasthotel.cropimageview.ImageCropView

class CropActivity : AppCompatActivity() {
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_crop)
		
		val editor: ImageCropView = findViewById(R.id.imageview)
		editor.setImageDrawable(getDrawable(R.drawable.lorempixel))
		
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
		
	}
}

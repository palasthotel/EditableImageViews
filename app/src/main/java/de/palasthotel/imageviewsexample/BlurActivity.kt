package de.palasthotel.imageviewsexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import de.palasthotel.blurimageview.BlurImageView
import de.palasthotel.blurimageview.BlurLine

class BlurActivity : AppCompatActivity() {
	
	val lines = arrayListOf<BlurLine>()
	var activeDrawable = R.drawable.ultra_high
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_blur)
		
		val blur: BlurImageView = findViewById(R.id.imageview)
		blur.setImageDrawable(resources.getDrawable(activeDrawable))
		
		val image = findViewById<ImageView>(R.id.imageview_result)
		image.visibility = View.GONE
		
		findViewById<Button>(R.id.save).setOnClickListener{
			
			Toast.makeText(this, "Switched", Toast.LENGTH_SHORT).show()
			if(image.visibility != View.VISIBLE){
				image.visibility = View.VISIBLE
				lines.clear()
				lines.addAll(blur.getHistory())
				val bitmap = blur.getBlurredBitmap()
				image.setImageBitmap(bitmap)
				blur.visibility = View.GONE
			} else {
				image.visibility = View.GONE
				blur.visibility = View.VISIBLE
				blur.setHistory(lines)
				blur.invalidatePixelation()
				blur.invalidate()
			}
			
		}
		
		findViewById<Button>(R.id.undo).setOnClickListener {
			blur.undo()
		}
		
		findViewById<Button>(R.id.ratio).setOnClickListener {
			blur.setHistory(emptyList())
			lines.clear()
			activeDrawable = when(activeDrawable){
				R.drawable.lorempixel -> R.drawable.ultra_high
				R.drawable.ultra_high -> R.drawable.flat
				else -> R.drawable.lorempixel
			}
			blur.setImageDrawable(resources.getDrawable(activeDrawable))
			blur.reset()
		}
		
	}
}

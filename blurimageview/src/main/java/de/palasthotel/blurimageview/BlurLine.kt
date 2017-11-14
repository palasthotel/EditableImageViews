package de.palasthotel.blurimageview

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Created by edward on 13.11.17.
 */
class BlurLine(
	val p1: PointF,
    val p2: PointF
) {
	
	 val x: Float
		get() = if(p1.x < p2.x) p1.x else p2.x
	
	 val y: Float
		get() = p1.y
	
	 val width: Float
		get() = Math.abs(p1.x - p2.x)
	
	val hasBlurBitmap: Boolean get() = blurBitmap != null
	var blurBitmap: Bitmap? = null
	
	
	fun draw(canvas: Canvas, paint: Paint) {
		canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint)
	}
}
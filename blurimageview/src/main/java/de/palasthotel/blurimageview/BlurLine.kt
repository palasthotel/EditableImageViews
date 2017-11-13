package de.palasthotel.blurimageview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.drawable.Drawable

/**
 * Created by edward on 13.11.17.
 */
class BlurLine(
	val p1: PointF,
    val p2: PointF
) {
	fun draw(canvas: Canvas, paint: Paint) {
		canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint)
	}
}
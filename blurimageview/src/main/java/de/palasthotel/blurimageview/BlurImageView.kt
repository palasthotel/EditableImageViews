package de.palasthotel.blurimageview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import de.palasthotel.cropimageview.GestureCallback
import de.palasthotel.cropimageview.GestureListener


class BlurImageView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0,
	defStyleRes: Int = 0) :
	ImageView(context, attrs, defStyle, defStyleRes), GestureCallback {
	
	private val paint = Paint()
	
	private var scrollDetector: GestureDetector = GestureDetector(context, GestureListener(this))
	
	private var lines: ArrayList<BlurLine> = ArrayList()
	
	init {
		paint.color = Color.BLACK
		paint.strokeCap = Paint.Cap.ROUND
		paint.strokeWidth = 40.0f
		isDrawingCacheEnabled = true
	}
	
	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		
		for (line in lines) {
			line.draw(canvas, paint)
		}
	}
	
	fun undo() {
		if (lines.size > 0) {
			lines.removeAt(lines.size - 1)
			invalidate()
		}
	}
	
	fun getBlurredBitmap(): Bitmap {
		buildDrawingCache()
		val cache = getDrawingCache(true)
		val bitmap = Bitmap.createBitmap(cache)
		destroyDrawingCache()
		return bitmap
	}
	
	private fun getPointerCoords(e: MotionEvent): PointF {
		val index = e.actionIndex
		return PointF(e.getX(index), e.getY(index))
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean {
		scrollDetector.onTouchEvent(event)
		return true
	}
	
	override fun scrollStarted(e: MotionEvent) {
		val coords = getPointerCoords(e)
		lines.add(BlurLine(
			PointF(coords.x,coords.y),
			PointF(coords.x, coords.y)
		))
	}
	
	override fun scrollMoved(e1: MotionEvent, e2: MotionEvent) {
		val current = getPointerCoords(e2)
		val line = lines.last()
		line.p2.x = current.x
		invalidate()
	}
	
	override fun onTap(e: MotionEvent) {
//		val coords = getPointerCoords(e)
//		val x = coords[0]
//		val y = coords[1]
//
//		drawables.add(Point(x, y))
//		invalidate()
	}
}


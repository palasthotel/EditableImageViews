package de.palasthotel.blurimageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import nl.dionsegijn.pixelate.Pixelate


class BlurImageView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0
) :
	ImageView(context, attrs, defStyle), GestureCallback {
	
	val blurPreviewPaint = Paint()
	val safetyOffset = 2
	
	// private variables
	private var scrollDetector: GestureDetector = GestureDetector(context, GestureListener(this))
	private val bitmap
		get() = (drawable as BitmapDrawable).bitmap
	
	private var _bitmapBlurred: Bitmap? = null
	private val bitmapBlurred: Bitmap
		get() {
			if (_bitmapBlurred == null) {
				_bitmapBlurred = Bitmap.createBitmap(bitmap, 0, 1, bitmap.width, bitmap.height - 1)
//				_bitmapBlurred = Gausianize(context, _bitmapBlurred).make()
				_bitmapBlurred = bitmapBounds.resize(_bitmapBlurred!!)
				Pixelate(_bitmapBlurred!!).setDensity(100)
					.setListener { bitmap, _ ->
						_bitmapBlurred = bitmap
					}
					.make()
				
				
			}
			return _bitmapBlurred!!
		}
	
	private var _bitmapBounds: BitmapBounds? = null
	private val bitmapBounds: BitmapBounds
		get() {
			if (_bitmapBounds == null) {
				_bitmapBounds = BitmapBounds(this)
			}
			return _bitmapBounds!!
		}
	
	private var blurLineHeight: Float
		get() = blurPreviewPaint.strokeWidth
		set(value) {
			blurPreviewPaint.strokeWidth = value
		}
	
	private var lines: ArrayList<BlurLine> = ArrayList()
	
	init {
		scaleType = ImageView.ScaleType.FIT_CENTER
		blurPreviewPaint.color = Color.BLACK
//		blurPreviewPaint.strokeCap = Paint.Cap.ROUND
		blurPreviewPaint.strokeWidth = 40.0f
		blurPreviewPaint.style = Paint.Style.FILL_AND_STROKE
		blurPreviewPaint.alpha = 100
		isDrawingCacheEnabled = true
		
	}
	
	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		
		lines.forEach {
			if (it.hasBlurBitmap) {
				canvas.drawBitmap(
					it.blurBitmap,
					it.x,
					it.y - blurPreviewPaint.strokeWidth/2,
					null
				)
			} else {
				it.draw(canvas, blurPreviewPaint)
			}
		}
	}
	
	fun reset(){
		_bitmapBlurred = null
		_bitmapBounds = null
		lines.clear()
		invalidatePixelation()
		invalidate()
	}
	
	val canUndo: Boolean
		get() = lines.size > 0
	
	fun undo() {
		if (lines.size > 0) {
			lines.removeAt(lines.size - 1)
			invalidate()
		}
	}
	
	
	fun setHistory(history: List<BlurLine>) {
		lines.clear()
		lines.addAll(history)
	}
	
	fun getHistory(): List<BlurLine> {
		return lines.map {
			BlurLine(
				it.p1,
				it.p2
			)
		}
	}
	
	fun getBlurredBitmap(): Bitmap {
		buildDrawingCache()
		val cache = getDrawingCache(true)
		val bitmap = Bitmap.createBitmap(
			cache,
			bitmapBounds.left+1,
			bitmapBounds.top+1,
			bitmapBounds.right-bitmapBounds.left-1,
			bitmapBounds.bottom-bitmapBounds.top-1
		)
		destroyDrawingCache()
		return bitmap
	}
	
	
	private fun getPointerCoords(e: MotionEvent): PointF {
		val index = e.actionIndex
		return PointF(e.getX(index), e.getY(index))
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean {
		when (event.action) {
			MotionEvent.ACTION_UP -> {
				invalidatePixelation()
				invalidate()
			}
		}
		scrollDetector.onTouchEvent(event)
		return true
	}
	
	fun invalidatePixelation(){
		lines
			.filter { !it.hasBlurBitmap }
			.forEach {
				val safetyOffset = 5
				// x and y in bitmap without bounds to imageview
				var x = it.x.toInt() - (_bitmapBounds?.left ?: 0)
				var y = (it.y.toInt() - (blurPreviewPaint.strokeWidth / 2f)).toInt() - (_bitmapBounds?.top ?: 0)
				var width = it.width.toInt()
				var height = blurPreviewPaint.strokeWidth.toInt()
				
				if(x+safetyOffset > bitmapBlurred.width){
					x = bitmapBlurred.width - safetyOffset/2
				} else if(x < 0){
					x = safetyOffset/2
				}
				
				if( x + width + safetyOffset > bitmapBlurred.width){
					width = bitmapBlurred.width - x - safetyOffset
				}
				
				if(y+ safetyOffset > bitmapBlurred.height){
					y = bitmapBlurred.height - safetyOffset/2
				} else if(y < 0){
					y = safetyOffset/2
				}
				
				if(y + height + safetyOffset > bitmapBlurred.height){
					height = bitmapBlurred.height - y - (safetyOffset/2)
				}
				
				try {
					it.blurBitmap = Bitmap.createBitmap(
						bitmapBlurred,
						x,
						y,
						width,
						height
					)
				} catch (e: Exception){
					e.printStackTrace()
				}
				
			}
	}
	
	fun getBoundSafeCoord(point: PointF): PointF{
		
		var x = point.x
		
		if( x < bitmapBounds.left - safetyOffset){
			x = (bitmapBounds.left+safetyOffset).toFloat()
		} else if(x > bitmapBounds.right - safetyOffset ){
			x = (bitmapBounds.right - safetyOffset).toFloat()
		}
		
		var y = point.y
		if(y < bitmapBounds.top - safetyOffset){
			y = (bitmapBounds.top + safetyOffset).toFloat()
		} else if( y > bitmapBounds.bottom - safetyOffset ){
			y = (bitmapBounds.bottom - safetyOffset).toFloat()
		}
		
		return PointF(x,y)
	}
	
	override fun scrollStarted(e: MotionEvent) {
		val coords = getBoundSafeCoord(getPointerCoords(e))
		
		lines.add(BlurLine(
			PointF(coords.x, coords.y),
			PointF(coords.x, coords.y)
		))
		
	}
	
	override fun scrollMoved(e1: MotionEvent, e2: MotionEvent) {
		val current = getBoundSafeCoord(getPointerCoords(e2))
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


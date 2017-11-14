package de.palasthotel.blurimageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import nl.dionsegijn.pixelate.Pixelate


class BlurImageView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0,
	defStyleRes: Int = 0) :
	ImageView(context, attrs, defStyle, defStyleRes), GestureCallback {
	
	private val paint = Paint()
	
	private var scrollDetector: GestureDetector = GestureDetector(context, GestureListener(this))
	
	private var lines: ArrayList<BlurLine> = ArrayList()
	
	private val bitmap
		get() = (drawable as BitmapDrawable).bitmap
	private var _bitmapBlurred: Bitmap? = null
	private val bitmapBlurred: Bitmap
		get(){
			if(_bitmapBlurred == null){
				_bitmapBlurred = Bitmap.createBitmap(bitmap, 0, 1, bitmap.width, bitmap.height-1)
//				_bitmapBlurred = Gausianize(context, _bitmapBlurred).make()
				_bitmapBlurred = bitmapBounds.resize(_bitmapBlurred!!)
				Pixelate(_bitmapBlurred!!).setDensity(100)
					.setListener{
						bitmap, _ ->
						_bitmapBlurred = bitmap
					}
					.make()
				
				
			}
			return _bitmapBlurred!!
		}
	
	private var _bitmapBounds: BitmapBounds? = null
	private val bitmapBounds: BitmapBounds
		get() {
			if(_bitmapBounds == null){
				_bitmapBounds = BitmapBounds(this)
			}
			return _bitmapBounds!!
		}
	
	val scale: Float
		get() =	(drawable as BitmapDrawable).bitmap.height.toFloat() / height
	
	init {
		
		scaleType = ImageView.ScaleType.FIT_CENTER
		
		paint.color = Color.BLACK
		paint.strokeCap = Paint.Cap.ROUND
		paint.strokeWidth = 40.0f
		paint.style = Paint.Style.FILL_AND_STROKE
		isDrawingCacheEnabled = true
		
		//setLayerType(LAYER_TYPE_SOFTWARE, null);
		
//		mOverlayPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
//		mOverlayPaint.color = Color.RED
//		mOverlayPaint.strokeWidth = 40.0f
	
	
		
	}
	
	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		
		bitmapBlurred
		
		lines.forEach {
			if(it.hasBlurBitmap){
				canvas.drawBitmap(it.blurBitmap, it.x + bitmapBounds.left, it.y + bitmapBounds.top, null)
			} else {
				it.draw(canvas, paint)
			}
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
	
	private fun getCoordsOnBitmap(e: MotionEvent): PointF{
		val index = e.actionIndex
		
		val bitmap_width = scale * (drawable as BitmapDrawable).bitmap.width
		
		val offset_x = (width - bitmap_width) / 2
		return PointF(
			e.getX(index) - offset_x, e.getY(index)
		)
	}
	
	private fun bitmapToImageViewPoint(point: PointF): PointF{
		return PointF(
		
		)
	}
	
	private fun imageViewToBitmapPoint(point: PointF): PointF{
		return PointF(
			point.x * scale,
			point.y * scale
		)
	}
	
	private fun getPointerCoords(e: MotionEvent): PointF {
		val index = e.actionIndex
		return PointF(e.getX(index), e.getY(index))
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean {
		when(event.action){
			MotionEvent.ACTION_UP ->{
				lines
					.filter { !it.hasBlurBitmap }
					.forEach {
						it.blurBitmap = Bitmap.createBitmap(bitmapBlurred, it.x.toInt(), it.y.toInt(), it.width.toInt(), 30)
				}
				invalidate()
			}
		}
		scrollDetector.onTouchEvent(event)
		return true
	}
	
	override fun scrollStarted(e: MotionEvent) {
		val coords = getPointerCoords(e)
		if(coords.x > bitmapBounds.right || coords.x > bitmapBlurred.width) return
		lines.add(BlurLine(
			PointF(coords.x,coords.y),
			PointF(coords.x, coords.y)
		))
		
	}
	
	override fun scrollMoved(e1: MotionEvent, e2: MotionEvent) {
		val current = getPointerCoords(e2)
		val line = lines.last()
		
		if(current.x > bitmapBounds.right || current.x > bitmapBlurred.width) return
		
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


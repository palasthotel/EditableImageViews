package de.palasthotel.cropimageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

import android.widget.ImageView
import palasthotel.de.cropimageview.R

class ImageCropView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), GestureCallback {
	
	
	companion object {
		private val GRABBER_HIT_AREA = 20
	}
	
	private var imageView: ImageView
	private var topHandle: View
	private var bottomHandle: View
	private var mainView: View = inflate(context, R.layout.cropview, this)
	
	private var gestureDetector: GestureDetector
	private var topHandleHeight: Int = -60
	private var bottomHandleHeight: Int = -60
	
	val minHeight = 100
	
	init {
		
		imageView = mainView.findViewById(R.id.imageView)
		
		topHandle = mainView.findViewById(R.id.topGrabberView)
		bottomHandle = mainView.findViewById(R.id.bottomGrabberView)
		
		val parent = mainView.findViewById<ConstraintLayout>(R.id.container)
		parent.viewTreeObserver.addOnGlobalLayoutListener {
			if (topHandleHeight == -60 && bottomHandleHeight == -60) {
				topHandleHeight = topHandle.height
				bottomHandleHeight = bottomHandle.height
			}
		}
		
		gestureDetector = GestureDetector(context, GestureListener(this))
	}
	
	fun setImageDrawable(drawable: Drawable){
		imageView.setImageDrawable(drawable)
	}
	fun setBitmap(image: Bitmap) {
		imageView.setImageBitmap(image)
	}
	
	val scale: Float
		get() =	(imageView.drawable as BitmapDrawable).bitmap.height.toFloat() / imageView.height
	
	val crop_top: Float
		get() = (topHandle.height ) * scale
	
	val crop_height: Float
		get() = ( imageView.height - ( bottomHandle.height + topHandle.height ) ) * scale
	
	fun getCroppedBitmap(): Bitmap {
		val bm = (imageView.drawable as BitmapDrawable).bitmap
		return Bitmap.createBitmap(bm, 0, crop_top.toInt(), bm.width, crop_height.toInt())
	}
	
	override fun onTouchEvent(event: MotionEvent?): Boolean {
		gestureDetector.onTouchEvent(event)
		
		if (event != null && event.action == MotionEvent.ACTION_UP) {
			topHandleHeight = topHandle.height
			bottomHandleHeight = bottomHandle.height
		}
		
		return true
	}
	
	fun getTopHandleHeight(): Int = topHandleHeight
	fun setTopHandleHeight(height: Int){
		val layoutParams = topHandle.layoutParams
		layoutParams.height = height
		topHandle.layoutParams = layoutParams
	}
	private fun addTopHandleHeight(height: Int) {
		setTopHandleHeight(height + topHandleHeight)
	}
	
	fun getBottomHandleHeight(): Int = bottomHandleHeight
	fun setBottomHandleHeight(height: Int) {
		val layoutParams = bottomHandle.layoutParams
		layoutParams.height  = height
		bottomHandle.layoutParams = layoutParams
	}
	private fun addBottomHandleHeight(height: Int) {
		setBottomHandleHeight(height + bottomHandleHeight)
	}
	
	private fun touchIsInTopHandle(y: Float): Boolean {
		return y < (topHandle.y + topHandle.height + GRABBER_HIT_AREA)
	}
	
	private fun touchIsInBottomHandle(y: Float): Boolean {
		return y > (bottomHandle.y - GRABBER_HIT_AREA)
	}
	
	override fun scrollStarted(e: MotionEvent) {}
	
	override fun scrollMoved(e1: MotionEvent, e2: MotionEvent) {
		val startY = e1.y
		val currentY = e2.y
		
		if (touchIsInTopHandle(currentY) || touchIsInTopHandle(startY)) {
			val distance = currentY - startY
			addTopHandleHeight(distance.toInt())
		} else if (touchIsInBottomHandle(currentY) || touchIsInBottomHandle(startY)) {
			val distance = currentY - startY
			addBottomHandleHeight(-distance.toInt())
		}
	}
	
	override fun onTap(e: MotionEvent) {}
}
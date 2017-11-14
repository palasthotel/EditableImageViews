package de.palasthotel.blurimageview

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.Log


/**
 * Created by edward on 14.11.17.
 */
class BitmapBounds(imageview: BlurImageView){
	
	private	val bitmap = (imageview.drawable as BitmapDrawable).bitmap
	private val bitmapHeight = bitmap.height
	private val bitmapWidth = bitmap.width
	private val bitmapRatio = bitmapWidth.toFloat()/bitmapHeight.toFloat()
	
	private val imageViewRatio = imageview.width.toFloat()/imageview.height.toFloat()
	
	val top: Int
	val right: Int
	val bottom: Int
	val left: Int
	
	val rect: Rect
		get() = Rect(
		left,top, right, bottom
	)
	
	init{
		
		if(bitmapRatio > imageViewRatio)
		{
			left = 0;
			right = imageview.width
			val drawHeight = (imageViewRatio/bitmapRatio) * imageview.height;
			top = ((imageview.height - drawHeight) /2).toInt();
			bottom = (top + drawHeight).toInt()
		}
		else
		{
			top = 0;
			bottom = imageview.height
			val drawWidth = (bitmapRatio/imageViewRatio) * imageview.width;
			left = ((imageview.width - drawWidth)/2).toInt()
			right = (left + drawWidth).toInt()
		}
		Log.d("BitmapBounds", "Draw: top ${top} right ${right} bottom ${bottom} left ${left}")
	}
	
	fun isInBounds(point: PointF): Boolean{
		return point.x <= right && point.x >= left
			&& point.y <= bottom && point.y >= top
	}
	
	fun resize(bm: Bitmap): Bitmap{
		val newWidth = Math.abs( right - left)
		val newHeight = Math.abs(bottom-top)
		val width = bm.width
		val height = bm.height
		val scaleWidth = newWidth.toFloat() / width
		val scaleHeight = newHeight.toFloat() / height
		// CREATE A MATRIX FOR THE MANIPULATION
		val matrix = Matrix()
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight)
		
		// "RECREATE" THE NEW BITMAP
		val resizedBitmap = Bitmap.createBitmap(
			bm, 0, 0, width, height, matrix, false)
		bm.recycle()
		return resizedBitmap
	}
}
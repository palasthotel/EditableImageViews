package de.palasthotel.blurimageview

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

/**
 * Created by edward on 14.11.17.
 */
class Gausianize(val context: Context, val bitmap: Bitmap) {
	
	fun make(): Bitmap {
		val outputBitmap = Bitmap.createBitmap(bitmap)
		val renderScript = RenderScript.create(context)
		val tmpIn = Allocation.createFromBitmap(renderScript, bitmap)
		val tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap)
		
		//Intrinsic Gausian blur filter
		val theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
		theIntrinsic.setRadius(25f)
		theIntrinsic.setInput(tmpIn)
		theIntrinsic.forEach(tmpOut)
		tmpOut.copyTo(outputBitmap)
		return outputBitmap
	}
	
}
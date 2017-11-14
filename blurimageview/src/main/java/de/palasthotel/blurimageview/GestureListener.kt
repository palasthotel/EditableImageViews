package de.palasthotel.blurimageview

import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Created by Benni on 05.09.17.
 */

class GestureListener(callback: GestureCallback): GestureDetector.SimpleOnGestureListener() {
	private var _callback: GestureCallback = callback
	private var isScrolling = false

	override fun onDown(e: MotionEvent?): Boolean {
		isScrolling = false

		return super.onDown(e)
	}

	override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
		if (!isScrolling && e1 != null) {
			_callback.scrollStarted(e1)
			isScrolling = true
		} else if (e1 != null && e2 != null) {
			_callback.scrollMoved(e1, e2)
		}

		return super.onScroll(e1, e2, distanceX, distanceY)
	}

	override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
		if (e != null) {
			_callback.onTap(e)
		}

		isScrolling = false
		return super.onSingleTapConfirmed(e)
	}

}
package de.palasthotel.cropimageview

import android.view.MotionEvent

interface GestureCallback {
	fun scrollStarted(e: MotionEvent)
	fun scrollMoved(e1: MotionEvent, e2: MotionEvent)
	fun onTap(e: MotionEvent)
}
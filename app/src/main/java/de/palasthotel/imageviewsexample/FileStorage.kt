package de.palasthotel.imageviewsexample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by edward on 16.08.17.
 */
class FileStorage( c: Context) {

	/**
	 * get internal directory
	 * @return
	 */
	private val internalDirectory = File(c.cacheDir.toString())

	/**
	 * external cache directory
	 * @return
	 */
	private val externalDirectory: File?

	init {
		val ex = c.externalCacheDir
		if ( ex != null && !ex.exists()) ex.mkdirs()
		externalDirectory = ex
	}

	/**
	 * check if external is available
	 * @return
	 */
	private fun hasExternal(): Boolean =
		Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
		&& externalDirectory != null
		&& externalDirectory.exists()

	fun saveImage( filename: String, bitmap: Bitmap){
		val file = getFile(filename)

		try {
			if (file.exists()) {
				file.delete()
			}
			val fos = FileOutputStream(file)
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
			fos.close()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	fun getFile(chat_id: Int): File = getFile(chat_id.toString())

	fun getFile(filename:String): File {
		if (this.hasExternal()) {
			val ex = this.externalDirectory
			if (!ex!!.exists() || !ex.isDirectory) {
				Log.e("FileStorage", "could not mkdirs")
				Log.e("FileStorage", ex.absolutePath)
			}
			return File(ex.absolutePath, filename)
		}
		return File(internalDirectory.absolutePath, filename)
	}

}
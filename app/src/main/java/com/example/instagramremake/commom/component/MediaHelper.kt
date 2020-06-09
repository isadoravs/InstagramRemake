package com.example.instagramremake.commom.component

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object MediaHelper {
    private lateinit var mSavedImageUri: Uri
    private lateinit var mCropImageUri: Uri

    private lateinit var activity: Activity;
    private lateinit var fragment: Fragment;
    private const val CAMERA_CODE: Int = 1
    private const val GALLERY_CODE: Int = 2
    lateinit var listener: OnImageCroppedListener


    fun cropView(cropImageView: CropImageView) {
            cropImageView.setAspectRatio(1, 1)
            cropImageView.setFixedAspectRatio(true)
            cropImageView.setOnCropImageCompleteListener {_, result ->
                val uri = result.uri
                uri?.let{
                    listener.onImageCropped(it)
                }
            }
        }

    fun chooseGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, GALLERY_CODE)
    }

    fun chooseCamera(activity: Activity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(activity.packageManager)?.let{
            try {
                val photoFile = createImageFile(activity)
                mCropImageUri = FileProvider.getUriForFile(activity, "com.example.instagramremake.fileprovider", photoFile)

                val sharedPreferences = activity.getSharedPreferences("camera_image", 0)
                val edit = sharedPreferences.edit()
                edit.putString("url", mCropImageUri.toString())
                edit.apply()

                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImageUri)
                activity.startActivityForResult(intent, CAMERA_CODE)
            } catch (e: IOException){

            }
        }
    }

    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        val sharedPreferences = activity.getSharedPreferences("camera_image", 0)
        val url = sharedPreferences.getString("url", null)
        url?.let{
            mCropImageUri = Uri.parse(url)
        }

        if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            if(CropImage.isReadExternalStoragePermissionsRequired(activity, mCropImageUri)){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    requestPermissions(activity = activity, string = Array(1){android.Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode = CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
                }
            }
            else {
                listener.onImagePicked(mCropImageUri)
            }
        } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            mCropImageUri = CropImage.getPickImageResultUri(activity, data)
            listener.onImagePicked(mCropImageUri)
        }
    }



    fun cropImage(context: Context, cropImageView: CropImageView) {
        context.externalCacheDir?.path?.let{
            mSavedImageUri = Uri.fromFile(File(it, System.currentTimeMillis().toString() + ".jpeg"))
            cropImageView.saveCroppedImageAsync(mSavedImageUri)
        }
    }


    private fun createImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timestamp}_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    fun getCameraInstance(fragment: Fragment): Camera? {
        var camera: Camera? = null
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(fragment = fragment, string = Array(3){android.Manifest.permission.CAMERA; android.Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode = 300)
            }
            camera = Camera.open()
        } catch (e: Exception){

        }
        return camera
    }

    private fun requestPermissions(activity: Activity? = null, fragment: Fragment? = null, string: Array<String>, requestCode: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.requestPermissions(string, requestCode)
            fragment?.requestPermissions(string, requestCode)
        }
    }

    fun saveCameraFile(
        context: Context,
        data: ByteArray
    ): Uri? {
        val pictureFile = createCameraFile(context, true)
        if (pictureFile == null) {
            Log.d("Teste", "Error createing media file, check storage permission")
            return null
        }
        var outputMediaFile: File? = null
        try {
            var fos = FileOutputStream(pictureFile)
            var realImage = BitmapFactory.decodeByteArray(data, 0, data.size)
            val exif = ExifInterface(pictureFile.toString())

            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("6", ignoreCase = true)) {
                realImage = rotate(realImage, 90)
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("8", ignoreCase = true)) {
                realImage = rotate(realImage, 270)
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("3", ignoreCase = true)) {
                realImage = rotate(realImage, 180)
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("0", ignoreCase = true)) {
                realImage = rotate(realImage, 90)
            }

            realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            val matrix = Matrix()
            outputMediaFile = createCameraFile(context, false)
            if (outputMediaFile == null) {
                Log.d("Teste", "Error creating media file, check storage permissions")
                return null
            }
            Log.i("Teste", realImage.width.toString() + " x " + realImage.height)
            val result = Bitmap.createBitmap(
                realImage, 0, 0,
                realImage.width,
                realImage.width, matrix, true
            )
            fos = FileOutputStream(outputMediaFile)
            result.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.e("Teste", e.localizedMessage, e)
        } catch (e: IOException) {
            Log.e("Teste", e.localizedMessage, e)
        }
        return Uri.fromFile(outputMediaFile)
    }

    private fun rotate(bitmap: Bitmap, degree: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val matrix = Matrix()
        matrix.setRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
    }

    private fun createCameraFile(
        context: Context,
        temp: Boolean
    ): File? {
        val mediaStorageDir =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (mediaStorageDir != null && !mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        val timestamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File(mediaStorageDir!!.path + File.separator + (if (temp) "TEMP_" else "IMG_") + timestamp + ".jpg")
    }

    public interface OnImageCroppedListener{
        fun onImageCropped(uri : Uri)
        fun onImagePicked(mCropImageUri: Uri) {

        }
    }
}
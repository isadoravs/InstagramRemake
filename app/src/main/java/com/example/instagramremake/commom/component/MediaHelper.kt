package com.example.instagramremake.commom.component

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object MediaHelper {
    private lateinit var mSavedImageUri: Uri
    private lateinit var mCropImageUri: Uri

    //  private lateinit var activity: Activity;
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
                    activity.requestPermissions(Array(1){android.Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
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

    public interface OnImageCroppedListener{
        fun onImageCropped(uri : Uri)
        fun onImagePicked(mCropImageUri: Uri) {

        }
    }
}
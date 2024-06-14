package com.mobile.freshpicks.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.mobile.freshpicks.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private var threshold: Float = 0.1f,
    private var maxResults: Int = 3,
    private val modelName: String = "fruit_vegetable_classifier.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {

    private var imageClassifier: ImageClassifier? = null

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }

            bitmap?.let { originalBitmap ->
                val modelInputSize = 150 // Confirm this matches your model's expected input size

                val resizedBitmap = Bitmap.createScaledBitmap( originalBitmap, modelInputSize, modelInputSize, true )
                val copiedBitmap = resizedBitmap.copy(Bitmap.Config.ARGB_8888, true)

                if (imageClassifier == null) {
                    setupImageClassifier()
                }

                val imageProcessor = ImageProcessor.Builder()
                    .add(ResizeOp(150, 150, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                    .add(CastOp(DataType.FLOAT32))
                    .add(NormalizeOp(0f, 1f))
                    .build()

                val tensorImage = imageProcessor.process(TensorImage.fromBitmap(copiedBitmap))


                imageClassifier?.let { classifier ->
                    val startTime = System.currentTimeMillis()
                    val results = classifier.classify(tensorImage)
                    val endTime = System.currentTimeMillis()

                    classifierListener?.onResults(results, endTime - startTime)
                }
            }
        } catch (e: Exception) {
            // Handle exceptions, e.g., file not found, decoding errors, etc.
            classifierListener?.onError(e.message ?: "Error classifying image")
            Log.e(TAG, "Error classifying image: ${e.message}")
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
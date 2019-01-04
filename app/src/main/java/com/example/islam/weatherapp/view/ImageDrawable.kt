package com.example.islam.weatherapp.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

class ImageDrawable {
    init {

    }


        fun drawMultilineTextToBitmap(gContext: Context,
                                      bitmapParam: Bitmap,
                                      gText: String): Bitmap {

            // prepare canvas
            val resources = gContext.getResources()
            val scale = resources.getDisplayMetrics().density
            var bitmap = bitmapParam
            var bitmapConfig: android.graphics.Bitmap.Config? = bitmap.config
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888
            }
            bitmap = bitmap.copy(bitmapConfig, true)

            val canvas = Canvas(bitmap)

            val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
            paint.setColor(Color.rgb(120, 61, 120))
            paint.setTextSize((140 * scale).toInt().toFloat())
            paint.setShadowLayer(1f, 1f, 1f, Color.WHITE)

            val textWidth = canvas.getWidth() - (160 * scale).toInt()

            // init StaticLayout for text
            val textLayout = StaticLayout(
                    gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)

            // get height of multiline text
            val textHeight = textLayout.height

            // get position of text's top left corner
            val x = ((bitmap.width - textWidth)).toFloat()
            val y = ((bitmap.height - textHeight)).toFloat()

            // draw text to the Canvas center
            canvas.save()
            canvas.translate(x, y)
            textLayout.draw(canvas)
            canvas.restore()

            return bitmap
        }

}
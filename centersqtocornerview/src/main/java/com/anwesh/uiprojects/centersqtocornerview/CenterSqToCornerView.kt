package com.anwesh.uiprojects.centersqtocornerview

/**
 * Created by anweshmishra on 07/08/20.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.RectF
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.Paint

val colors : Array<String> = arrayOf("#3F51B5", "#FFEB3B", "#4CAF50", "#F44336", "#2196F3")
val parts : Int = 5
val scGap : Float = 0.02f / parts
val sizeFactor : Float = 6.6f
val strokeFactor : Float = 90f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 360f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawCenterSqToCorner(scale : Float, w : Float, h : Float, paint : Paint) {
    val sf : Float = scale.sinify()
    val sf1 : Float = sf.divideScale(0, parts)
    val size : Float = Math.min(w, h) / sizeFactor
    save()
    translate(w / 2, h / 2)
    rotate(360f * sf1)
    scale(sf1, sf1)
    drawRect(RectF(-size, -size, size, size), paint)
    for (j in 1..(parts - 1)) {
        val sfj : Float = sf.divideScale(j, parts)
        save()
        rotate(90f * j)
        drawLine(0f, 0f, (w / 2 - size / 2) * sfj, -(h / 2 - size / 2) * sfj, paint)
        restore()
    }
    restore()
}

fun Canvas.drawCSTCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = Color.parseColor(colors[i])
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    drawCenterSqToCorner(scale, w, h, paint)
}

class CenterToSqCornerView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}
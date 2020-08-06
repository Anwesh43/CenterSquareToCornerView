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

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class CSTCNode(var i : Int, val state : State = State()) {

        private var next : CSTCNode? = null
        private var prev : CSTCNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = CSTCNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawCSTCNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : CSTCNode {
            var curr : CSTCNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class CenterSqToCorner(var i : Int) {

        private var curr : CSTCNode = CSTCNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : CenterToSqCornerView) {

        private val animator : Animator = Animator(view)
        private val cstc : CenterSqToCorner = CenterSqToCorner(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            cstc.draw(canvas, paint)
            animator.animate {
                cstc.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            cstc.startUpdating {
                animator.start()
            }
        }
    }
}
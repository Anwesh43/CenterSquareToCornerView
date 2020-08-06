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
val sizeFactor : Float = 5.6f
val strokeFactor : Float = 90f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

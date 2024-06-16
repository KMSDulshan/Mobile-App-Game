package com.example.carrace

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.example.carracegame.GameTask
import com.example.carracegame.R
import java.lang.Exception

class GameView (var c :Context,var gameTask: GameTask):View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var highScore = 0
    private var myCarPosition = 0
    private var otherCars = ArrayList<HashMap<String, Any>>()


    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight


        // Generate other cars

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            map["carType"] = if ((0..1).random() == 0) "car1" else "car1"
            otherCars.add(map)

        }
        time = time + 10 + speed
        val planeWidth = viewWidth / 5
        val planeHeight = planeWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.myplane, null)

        // Draw player's car

        d.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - planeHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + planeWidth - 25,
            viewHeight - 2
        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highestScore = 0

        // Draw and update other cars

        for (i in otherCars.indices) {
            try {
                val carX = otherCars[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                val carY = time - otherCars[i]["startTime"] as Int
                val carType = otherCars[i]["carType"] as String // Get the car type

                val carDrawable =
                    if (carType == "car1") resources.getDrawable(R.drawable.jet, null)
                    else resources.getDrawable(R.drawable.myplane, null)

                carDrawable.setBounds(
                    carX + 55, carY - planeHeight, carX + planeWidth - 25, carY
                )
                carDrawable.draw(canvas)

                // Check collision with player's car

                if (otherCars[i]["lane"] as Int == myCarPosition) {
                    if (carY > viewHeight - 2 - planeHeight
                        && carY < viewHeight - 2
                    ) {

                        gameTask.closeGame(score)
                    }
                }

                // Remove cars that passed the screen

                if (carY > viewHeight + planeHeight) {
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if (score > highestScore) {
                        highestScore = score
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Draw score and speed

        myPaint!!.color =Color.BLACK
        myPaint!!.textSize = 40F
        canvas.drawText("Score :$score",80f , 80f ,myPaint!!)
        canvas.drawText("Speed :$speed",380f , 80f ,myPaint!!)
        invalidate()

      }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myCarPosition > 0) {
                        myCarPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myCarPosition < 2) {
                        myCarPosition++
                    }
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {}
        }
        return true
    }

    // Reset the game
    fun resetGame(){
        time = 0
        score = 0
        speed = 1
        myCarPosition = 0
        otherCars.clear()

        invalidate()
    }
    }





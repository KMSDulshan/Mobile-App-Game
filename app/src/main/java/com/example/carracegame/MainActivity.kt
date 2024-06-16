package com.example.carracegame

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.carrace.GameView


class MainActivity : AppCompatActivity(),GameTask {
    lateinit var rootLayout: LinearLayout
    lateinit var startBtn: Button
    lateinit var imageView: ImageView
    lateinit var mGameView: GameView
    lateinit var score: TextView
    lateinit var highScoreTextView: TextView
    var highScore = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startBtn = findViewById(R.id.StartBtn)
        rootLayout = findViewById(R.id.rootLayout)
        imageView = findViewById(R.id.imageView)
        score = findViewById(R.id.score)
        highScoreTextView = findViewById(R.id.highscore)
        mGameView = GameView(this, this)

        updateHighestScore(highScore)

        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            imageView.visibility = View.GONE
            score.visibility = View.GONE
        }
    }

    override fun closeGame(mScore: Int) {
        score.text = "Score :$mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE

        startBtn.setOnClickListener {
            score.text = "Score:0"
            mGameView.resetGame()

            mGameView.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            imageView.visibility = View.GONE
            score.visibility = View.GONE
        }
        if (mScore > highScore) {
            highScore = mScore
            updateHighestScore(highScore)
        }

        if (mScore > 0) {

            mGameView = GameView(this, this)
            startBtn.setOnClickListener {
                mGameView.setBackgroundResource(R.drawable.road)
                rootLayout.addView(mGameView)
                startBtn.visibility = View.GONE
                score.visibility = View.GONE
                imageView.visibility = View.GONE


            }
        }
    }

    internal fun updateHighestScore(newScore: Int) {
        highScoreTextView.text = "Highest Score: $newScore"


    }
}

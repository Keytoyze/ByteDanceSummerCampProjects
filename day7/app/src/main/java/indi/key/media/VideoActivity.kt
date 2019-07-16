/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.media

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.updateLayoutParams
import indi.key.media.player.VideoPlayerListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.include_play_bottom.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.util.concurrent.TimeUnit

class VideoActivity : AppCompatActivity(), View.OnClickListener {

    internal var mVideoWidth = 0
    internal var mVideoHeight = 0

    private var isPortrait = false

    private var handler: Handler? = null

    private var menuVisible = true
    internal var isPlayFinish = false

    private var disposable: Disposable? = null

    companion object {
        val TAG: String = VideoActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        init()
        initIJKPlayer()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private var dragging = false
    private fun init() {

        btn_stop.setOnClickListener(this)
        ijkPlayer.setOnClickListener(this)
        btn_setting.setOnClickListener(this)
        btn_play.setOnClickListener(this)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                //进度改变
                if (b) {
                    refresh(ijkPlayer.duration * i / seekBar.max)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //开始拖动
                dragging = true

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //停止拖动
                dragging = false
                ijkPlayer.seekTo(ijkPlayer.duration * seekBar.progress / seekBar.max)
            }
        })

        disposable = Observable.interval(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!dragging) {
                    refresh(ijkPlayer.currentPosition)
                }
            }, {
                it.printStackTrace()
            })
    }

    private fun refresh(current: Long) {

        val duration = ijkPlayer.duration / 1000

        val currentSeconds = current / 1000
        val currentSecond = String.format("%02d", currentSeconds % 60)
        val currentMinute = String.format("%02d", currentSeconds / 60)
        val totalSecond = String.format("%02d", duration % 60)
        val totalMinute = String.format("%02d", duration / 60)
        val time = "$currentMinute:$currentSecond/$totalMinute:$totalSecond"
        tv_time.text = time
        if (duration != 0L) {
            seekBar.progress = (currentSeconds * seekBar.max / duration).toInt()
        }

    }

    private fun initIJKPlayer() {
        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null)
            IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        } catch (e: Exception) {
            this.finish()
        }

//        ijkPlayer.setListener(VideoPlayerListener())
        //ijkPlayer.setVideoResource(R.raw.yuminhong);
        ijkPlayer.setVideoResource(R.raw.big_buck_bunny)

        /*ijkPlayer.setVideoResource(R.raw.big_buck_bunny);
        ijkPlayer.setVideoPath("https://media.w3.org/2010/05/sintel/trailer.mp4");
        ijkPlayer.setVideoPath("http://vjs.zencdn.net/v/oceans.mp4");*/

        ijkPlayer.setListener(object : VideoPlayerListener() {
            override fun onBufferingUpdate(iMediaPlayer: IMediaPlayer, percent: Int) {}

            override fun onCompletion(iMediaPlayer: IMediaPlayer) {
                seekBar.progress = 100
                btn_play.text = "播放"
                btn_stop.text = "播放"
            }

            override fun onError(iMediaPlayer: IMediaPlayer, what: Int, extra: Int): Boolean {
                return false
            }

            override fun onInfo(iMediaPlayer: IMediaPlayer, what: Int, extra: Int): Boolean {
                return false
            }

            override fun onPrepared(iMediaPlayer: IMediaPlayer) {
                refresh(0)
                isPlayFinish = false
                mVideoWidth = iMediaPlayer.videoWidth
                mVideoHeight = iMediaPlayer.videoHeight
                videoScreenInit()
                //toggle();
                iMediaPlayer.start()
                rl_loading.visibility = View.GONE
            }

            override fun onSeekComplete(iMediaPlayer: IMediaPlayer) {}

            override fun onVideoSizeChanged(
                iMediaPlayer: IMediaPlayer, width: Int, height: Int,
                sar_num: Int, sar_den: Int
            ) {
                Log.e(TAG, "onResize")

                mVideoWidth = iMediaPlayer.videoWidth
                mVideoHeight = iMediaPlayer.videoHeight
            }
        })
    }

    override fun onStop() {
        super.onStop()
        if (ijkPlayer != null && ijkPlayer.isPlaying) {
            ijkPlayer!!.stop()
        }
        IjkMediaPlayer.native_profileEnd()
    }

    override fun onDestroy() {
        ijkPlayer.stop()
        ijkPlayer.release()
        disposable?.dispose()
        super.onDestroy()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ijkPlayer -> if (!menuVisible) {
                rl_bottom.visibility = View.VISIBLE
                val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.show_bottom)
                rl_bottom.startAnimation(animation)
                menuVisible = true
            } else {
                rl_bottom.visibility = View.INVISIBLE
                val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.move_bottom)
                rl_bottom.startAnimation(animation)
                menuVisible = false
            }
            R.id.btn_setting -> toggle()
            R.id.btn_play -> if (btn_play.text.toString() == resources.getString(R.string.pause)) {
                ijkPlayer!!.pause()
                btn_play.text = resources.getString(R.string.media_play)
            } else {
                ijkPlayer!!.start()
                btn_play.text = resources.getString(R.string.pause)
            }
            R.id.btn_stop -> if (btn_stop.text.toString() == resources.getString(R.string.stop)) {
                ijkPlayer!!.stop()
                /*ijkPlayer.mMediaPlayer.prepareAsync();
                    ijkPlayer.mMediaPlayer.seekTo(0);*/
                btn_stop.text = resources.getString(R.string.media_play)
            } else {
                ijkPlayer!!.setVideoResource(R.raw.big_buck_bunny)
                btn_stop.text = resources.getString(R.string.stop)
            }
        }
    }

    private fun videoScreenInit() {
        if (isPortrait) {
            portrait()
        } else {
            lanscape()
        }
    }

    private fun toggle() {
        if (!isPortrait) {
            portrait()
        } else {
            lanscape()
        }
    }

    private fun portrait() {
        val preState = ijkPlayer.isPlaying
        ijkPlayer.pause()
        isPortrait = true
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val wm = this
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outSize = Point()
        wm.defaultDisplay.getSize(outSize)
        val windowWidth = outSize.x.toFloat()
        val windowHeight = outSize.y.toFloat()
        var ratio = windowWidth / windowHeight
        if (windowWidth < windowHeight) {
            ratio = windowHeight / windowWidth
        }

        Log.e(TAG, ratio.toString())
        rl_player.updateLayoutParams {
            height = (windowHeight / ratio).toInt()
            width = windowHeight.toInt()
            Log.e(TAG, height.toString())
        }

        btn_setting.text = resources.getString(R.string.fullScreek)
        if (preState) ijkPlayer.start()
    }

    private fun lanscape() {
        val preState = ijkPlayer.isPlaying
        ijkPlayer.pause()
        isPortrait = false
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        rl_player.updateLayoutParams {
            height = RelativeLayout.LayoutParams.MATCH_PARENT
            width = RelativeLayout.LayoutParams.MATCH_PARENT
        }

        btn_setting.text = resources.getString(R.string.smallScreen)
        if (preState) ijkPlayer.start()
    }
}

package com.example.snakegmae_kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.TextView
import java.util.*

open class SnakeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : TileView(
    context,
    attrs) {

    private var mLastMove: Long = 0
    var mMode = READY
    var newMode = 0

    private var mSnakeTrail = ArrayList<Coordinate>() // 蛇的所有（点）tile的坐标数组

    private var mAppleList = ArrayList<Coordinate>() // 苹果的所有（点）tile的坐标数组


    private val RNG = Random()
    private var mStatusText: TextView? = null
    var mScore: Long = 0
    companion object{
        private const val RED_STAR = 1
        private const val YELLOW_STAR = 2
        private const val GREEN_STAR = 3
        private const val UP = 1
        private const val DOWN = 2
        private const val RIGHT = 3
        private const val LEFT = 4
        const val PAUSE = 0
        const val READY = 1
        val RUNNING = 2
        const val LOSE = 3
        const val QUIT = 4
        private val RNG = Random()

        var mDirection = RIGHT
        var mNextDirection = RIGHT
        var mMoveDelay :Int= 500
    }

    //    private static final String TAG = "SnakeView";
    var myHandler = MyHandler()

    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            update()
            invalidate() //请求重绘，不断调用ondraw方法
        }

        //调用sleep后,在一定时间后再sendmessage进行UI更新
        fun sleep(delayMillis: Int) {
            this.removeMessages(0) //清空消息队列
            sendMessageDelayed(obtainMessage(0), delayMillis.toLong())
        }
    }




    class Coordinate constructor(var x: Int, var y: Int){
        fun equals(coordinate: Coordinate) : Boolean{

            if (coordinate.x==x&&coordinate.y==y){
                return true
            }
            return false
        }

        override fun toString(): String {
            return return "Coordinate: [$x,$y]"
        }
    }

    private fun addRandomApple() {
        var newCoord: Coordinate? = null
        var found = false
        while (!found) {
            // apple生成的位置的坐标.mXT=24,mTY=35
            val newX = 1 + SnakeView.RNG.nextInt(24 - 2)
            val newY = 3 + SnakeView.RNG.nextInt(35 - 12)
            newCoord = Coordinate(newX, newY)
            var collision = false
            val snakelength = mSnakeTrail.size
            //遍历snake, 看新添加的apple是否在snake体内, 如果是,重新生成
            for (index in 0 until snakelength) {
                if (mSnakeTrail[index].equals(newCoord)) {
                    collision = true
                }
            }
            found = !collision
        }
        //        if (newCoord == null) {
//            Log.e(TAG, "Somehow ended up with a null newCoord!");
//        }
        mAppleList.add(newCoord!!)
    }

    //绘制边界的墙
    private fun updateWalls() {
        for (x in 0 until mXTileCount) {
            setTile(GREEN_STAR, x, 2)
            setTile(GREEN_STAR, x, mYTileCount - 8)
        }
        for (y in 2 until mYTileCount - 8) {
            setTile(GREEN_STAR, 0, y)
            setTile(GREEN_STAR, mXTileCount - 1, y)
        }
    }

    private fun updateSnake() {
        var growSnake = false
        val head = mSnakeTrail[0]
        var newHead = Coordinate(1, 1)
        mDirection = mNextDirection
        when (mDirection) {
            RIGHT -> {
                newHead = Coordinate(head.x + 1, head.y)
            }
            LEFT -> {
                newHead = Coordinate(head.x - 1, head.y)
            }
            UP -> {
                newHead = Coordinate(head.x, head.y - 1)
            }
            DOWN -> {
                newHead = Coordinate(head.x, head.y + 1)
            }
        }

        //检测是否撞墙
        if (newHead.x < 1 || newHead.y < 3 || newHead.x > mXTileCount - 2
            || newHead.y > mYTileCount - 9
        ) {
            setMode(LOSE)
            return
        }
        //检测蛇头是否撞到自己
        val snakelength = mSnakeTrail.size
        for (snakeindex in 0 until snakelength) {
            val c = mSnakeTrail[snakeindex]
            if (c.equals(newHead)) {
                setMode(LOSE)
                return
            }
        }
        //检测蛇是否吃到苹果
        val applecount = mAppleList.size
        for (appleindex in 0 until applecount) {
            val c = mAppleList[appleindex]
            if (c.equals(newHead)) {
                mAppleList.remove(c)
                addRandomApple()
                mScore++
                mMoveDelay *= (95/100)
                growSnake = true
            }
        }
        mSnakeTrail.add(0, newHead)
        if (!growSnake) {
            mSnakeTrail.removeAt(mSnakeTrail.size - 1)
        }
        //蛇头和蛇身分别设置图片
        var index = 0
        for (c in mSnakeTrail) {
            if (index == 0) {
                setTile(RED_STAR, c.x, c.y)
            } else {
                setTile(YELLOW_STAR, c.x, c.y)
            }
            index++
        }
    }

    private fun updateApples() {
        for (c in mAppleList) {
            setTile(YELLOW_STAR, c.x, c.y)
        }
    }

    fun update() {
        if (mMode == RUNNING) {
            val now = System.currentTimeMillis()
            if (now - mLastMove > mMoveDelay) {
                clearTiles()
                updateWalls()
                updateSnake()
                updateApples()
                mLastMove = now
            }
            myHandler.sleep(mMoveDelay)
        }
    }

    //图像初始化
    private fun initSnakeView() {
        isFocusable = true
        val r = this.context.resources
        //添加几种不同的tile
        resetTiles(4)
        //从文件中加载图片
        loadTile(RED_STAR, r.getDrawable(R.drawable.redstar))
        loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar))
        loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar))
        update()
    }

    fun initNewGame() {
        mSnakeTrail.clear()
        mAppleList.clear()
        //snake初始状态时的个数和位置,方向
        mSnakeTrail.add(Coordinate(8, 7))
        mSnakeTrail.add(Coordinate(7, 7))
        mSnakeTrail.add(Coordinate(6, 7))
        mSnakeTrail.add(Coordinate(5, 7))
        mSnakeTrail.add(Coordinate(4, 7))
        mSnakeTrail.add(Coordinate(3, 7))
        mDirection = RIGHT
        mNextDirection = RIGHT
        addRandomApple()
        mScore = 0
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint :Paint? = Paint()
        initSnakeView()
        //遍历地图绘制界面
        //遍历地图绘制界面
        for (x in 0 until mXTileCount) {
            for (y in 0 until mYTileCount) {
                if (mTileGrid[x][y] > 0) {
                    canvas?.drawBitmap(
                        mTileArray[mTileGrid[x][y]]!!,
                        (mXOffset + x * mTileSize).toFloat(),
                        (mYOffset + y * mTileSize).toFloat(),
                        paint
                    )
                }
            }
        }
    }

    //把蛇和苹果各点对应的坐标储存起来
    private fun coordArrayListToArray(cvec: ArrayList<Coordinate>): IntArray? {
        val count = cvec.size
        val rawArray = IntArray(count * 2)
        for (index in 0 until count) {
            val c = cvec[index]
            rawArray[2 * index] = c.x
            rawArray[2 * index + 1] = c.y
        }
        return rawArray
    }

    //将当前所有的游戏数据全部保存
    fun saveState(): Bundle? {
        val map = Bundle()
        map.putIntArray("mAppleList", coordArrayListToArray(mAppleList))
        map.putInt("mDirection", Integer.valueOf(mDirection))
        map.putInt("mNextDirection", Integer.valueOf(mNextDirection))
        map.putInt("mMoveDelay", Integer.valueOf(mMoveDelay))
        map.putLong("mScore", java.lang.Long.valueOf(mScore))
        map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail))
        return map
    }

    //是coordArrayListToArray()的逆过程，用来读取保存在Bundle中的数据
    private fun coordArrayToArrayList(rawArray: IntArray): ArrayList<Coordinate>? {
        val coordArrayList = ArrayList<Coordinate>()
        val coordCount = rawArray.size
        var index = 0
        while (index < coordCount) {
            val c = Coordinate(rawArray[index], rawArray[index + 1])
            coordArrayList.add(c)
            index += 2
        }
        return coordArrayList
    }

    fun restoreState(icicle: Bundle) {
        setMode(PAUSE)
        mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList")!!)!!
        mDirection = icicle.getInt("mDirection")
        mNextDirection = icicle.getInt("mNextDirection")
        mMoveDelay = icicle.getInt("mMoveDelay")
        mScore = icicle.getLong("mScore")
        mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail")!!)!!
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (mDirection != DOWN) {
                mNextDirection = UP
            }
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (mDirection != UP) {
                mNextDirection = DOWN
            }
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (mDirection != LEFT) {
                mNextDirection = RIGHT
            }
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mDirection != RIGHT) {
                mNextDirection = LEFT
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun setTextView(newView: TextView) {
        mStatusText = newView
    }

    fun setMode(newMode: Int) {
        this.newMode = newMode
        val oldMode = mMode
        mMode = newMode

        if ((newMode == RUNNING) and (oldMode != RUNNING)) {
            mStatusText!!.visibility = INVISIBLE
            update()
            return
        }
        val res = context.resources
        var str: CharSequence = ""
        if (newMode == PAUSE) {
            str = res.getText(R.string.mode_pause)
        }
        if (newMode == READY) {
            str = res.getText(R.string.mode_ready)
        }
        if (newMode == LOSE) {
            str = (res.getString(R.string.mode_lose_prefix) + mScore
                    + res.getString(R.string.mode_lose_suffix))
        }
        if (newMode == QUIT) {
            str = res.getText(R.string.mode_quit)
        }
        mStatusText!!.text = str
        mStatusText!!.visibility = VISIBLE
    }
}


package com.example.snakegmae_kotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

open class TileView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    constructor(context: Context?, attrs: AttributeSet?, int: Int) : this(context, attrs)
    var mTileSize = 32
    var mXTileCount = 0
    var mYTileCount = 0
    var mXOffset = 0
    var mYOffset = 0



    lateinit var mTileArray : Array<Bitmap?>

    lateinit var mTileGrid: Array<IntArray>

    //加载三幅小图片
    fun loadTile(key: Int, tile: Drawable) {
        var bitmap=Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888)
        var canvas =Canvas(bitmap)
        tile.setBounds(0, 0, mTileSize, mTileSize)
        tile.draw(canvas)
        mTileArray[key] = bitmap
    }

    fun setTile(tileIndex: Int, x: Int, y: Int){
        mTileGrid[x][y]=tileIndex
    }

    fun resetTiles(tileCount: Int){
        mTileArray = arrayOfNulls<Bitmap>(tileCount)
    }

    fun clearTiles() {
        for (x in 0 until mXTileCount) {
            for (y in 2 until mYTileCount - 8) {
                setTile(0, x, y)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //地图数初始化
        mXTileCount = Math.floor((w / mTileSize).toDouble()).toInt()
        mYTileCount = Math.floor((h / mTileSize).toDouble()).toInt()
        //        System.out.println("-------"+mXTileCount+"----------");
        //        System.out.println("-------"+mYTileCount+"----------");
        //够分成一格的分成一格, 剩下不够一格的分成两份,左边一份,右边一份
        //        System.out.println("-------"+mXTileCount+"----------");
        //        System.out.println("-------"+mYTileCount+"----------");
        //够分成一格的分成一格, 剩下不够一格的分成两份,左边一份,右边一份
        mXOffset = (w - mTileSize * mXTileCount) / 2
        mYOffset = (h - mTileSize * mYTileCount) / 2
        //        System.out.println("-------"+mXOffset+"----------");
        //        System.out.println("-------"+mYOffset+"----------");
        //        System.out.println("-------"+mXOffset+"----------");
        //        System.out.println("-------"+mYOffset+"----------");
        mTileGrid = Array(mXTileCount) { IntArray(mYTileCount) }
        clearTiles()
    }
}
package com.example.snakegmae_kotlin

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class MyRelativeLayout : RelativeLayout {
    constructor(context: Context,attributeSet: AttributeSet) :super(context,attributeSet)
    constructor(context: Context,attributeSet: AttributeSet,defaulInt: Int) :super(context,attributeSet,defaulInt)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
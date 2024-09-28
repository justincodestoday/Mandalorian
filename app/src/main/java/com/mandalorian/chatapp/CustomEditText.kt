package com.mandalorian.chatapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.BuildCompat
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.view.inputmethod.InputConnectionCompat.OnCommitContentListener
import androidx.core.view.inputmethod.InputContentInfoCompat

class CustomEditText : AppCompatEditText {
    private val imgTypeString = arrayOf(
        "image/png",
        "image/gif",
        "image/jpeg",
        "image/webp"
    )

    private var keyBoardInputCallbackListener: KeyBoardInputCallbackListener? = null

    constructor(context: Context?) : super(context!!) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initView()
    }

    private fun initView() {
        imeOptions = imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI
        setHorizontallyScrolling(false)
        maxLines = Integer.MAX_VALUE
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        val ic: InputConnection? = super.onCreateInputConnection(outAttrs)
        EditorInfoCompat.setContentMimeTypes(
            outAttrs,
            imgTypeString
        )
        return ic?.let { InputConnectionCompat.createWrapper(it, outAttrs, callback) }
    }

    interface KeyBoardInputCallbackListener {
        fun onCommitContent(
            inputContentInfo: InputContentInfoCompat?,
            flags: Int, opts: Bundle?
        ): Boolean
    }

    private val callback =
        OnCommitContentListener { inputContentInfo, flags, opts ->
            // read and display inputContentInfo asynchronously
            if (BuildCompat.isAtLeastNMR1() && flags and
                InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION != 0
            ) {
                try {
                    inputContentInfo.requestPermission()
                } catch (e: Exception) {
                    return@OnCommitContentListener false // return false if failed
                }
            }
            var supported = false
            for (mimeType in imgTypeString) {
                if (inputContentInfo.description.hasMimeType(mimeType)) {
                    supported = true
                    break
                }
            }
            if (!supported) {
                return@OnCommitContentListener false
            }
            if (keyBoardInputCallbackListener != null) {
                return@OnCommitContentListener keyBoardInputCallbackListener!!.onCommitContent(
                    inputContentInfo,
                    flags,
                    opts
                )
            }
            true // return true if succeeded
        }
}
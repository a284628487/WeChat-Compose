package com.compose.wechat.main.moments.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.compose.wechat.R
import com.compose.wechat.ui.theme.WeChatTheme

class MomentsFragment : Fragment() {

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // In order for savedState to work, the same ID needs to be used for all instances.
            id = R.id.main_home_fragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                WeChatTheme {
                    // actual composable state
                    val nativePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        textSize = 36f
                        color = android.graphics.Color.RED
                    }
                    val indexArray = arrayOf(
                        "*",
                        "A",
                        "B",
                        "C",
                        "D",
                        "E",
                        "F",
                        "G",
                        "H",
                        "I",
                        "J",
                        "K",
                        "L",
                        "M",
                        "N",
                        "O",
                        "P",
                        "Q",
                        "R",
                        "S",
                        "T",
                        "U",
                        "V",
                        "W",
                        "X",
                        "Y",
                        "Z",
                        "#"
                    )
                    Box(modifier = Modifier.fillMaxSize()) {
                        val swipeableState = rememberSwipeableState(initialValue = 0)
                        val squareSize = 1600F
                        val sizePx = with(LocalDensity.current) { squareSize }
                        val anchors =
                            mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

                        val offset = remember {
                            mutableStateOf(0f)
                        }

                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .fillMaxHeight()
                                .background(Color.Gray)
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scrollable(
                                        orientation = Orientation.Vertical,
                                        state = rememberScrollableState  { delta ->
                                            offset.value += delta
                                            delta
                                        }
                                    )
//                                    .swipeable(
//                                        state = swipeableState,
//                                        anchors = anchors,
//                                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
//                                        orientation = Orientation.Vertical
//                                    )
                                    .padding(top = 80.dp, bottom = 80.dp)
                                    .background(Color.Green)
                            ) {
                                val singleItemHeight = (size.height - 80F) / indexArray.size
                                val leftSpace = 20F
                                val topSpace = 80F
                                indexArray.forEachIndexed { index, s ->
                                    drawContext.canvas.nativeCanvas.drawText(
                                        s,
                                        leftSpace + if (index == 9) 6 else 0,
                                        singleItemHeight * index + topSpace,
                                        nativePaint
                                    )
                                }
                            }
                        }
                        Box(modifier = Modifier.size(50.dp, 50.dp)) {
                            Text(
                                text = "${offset.value}",
                                modifier = Modifier.offset(x = 40.dp, y = 20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
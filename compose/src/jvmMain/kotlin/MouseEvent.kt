package windowsApi

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.singleWindowApplication
import java.awt.event.MouseEvent

fun main() = singleWindowApplication {
    handleMouseMove()
}

@Preview
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun handleClick() {
    var count by remember { mutableStateOf(0) }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        var text by remember { mutableStateOf("Click magenta box!") }
        Column {
            Box(
                modifier = Modifier
                    .background(Color.Magenta)
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.2f)
                    .combinedClickable(
                        onClick = {
                            text = "Click! ${count++}"
                        },
                        onDoubleClick = {
                            text = "Double click! ${count++}"
                        },
                        onLongClick = {
                            text = "Long click! ${count++}"
                        }
                    )
            )
            Text(text = text, fontSize = 40.sp)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun handleMouseMove() {
    var color by remember { mutableStateOf(Color(0, 0, 0)) }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .fillMaxSize()
            .background(color = color)
            .onPointerEvent(PointerEventType.Move) {
                val position = it.changes.first().position
                color = Color(position.x.toInt() % 256, position.y.toInt() % 256, 0)
            }

    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun mouseEnter() {
    Column(
        Modifier.background(Color.White),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(10) { index ->
            var active by remember { mutableStateOf(false) }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (active) Color.Green else Color.White)
                    .onPointerEvent(PointerEventType.Enter) { active = true }
                    .onPointerEvent(PointerEventType.Exit) { active = false },
                fontSize = 30.sp,
                fontStyle = if (active) FontStyle.Italic else FontStyle.Normal,
                text = "Item $index"
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun mouseScroll() {
    var number by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Scroll) {
                number += it.changes.first().scrollDelta.y
            },
        contentAlignment = Alignment.Center
    ) {
        Text("Scroll to change the number: $number", fontSize = 30.sp)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun mouseClick() {
    var clickableText by remember { mutableStateOf("Click me!") }

    Text(
        modifier = Modifier.mouseClickable(
            onClick = {
                clickableText = buildString {
                    append("Buttons pressed:\n")
                    append("primary: ${buttons.isPrimaryPressed}\t")
                    append("secondary: ${buttons.isSecondaryPressed}\t")
                    append("tertiary: ${buttons.isTertiaryPressed}\t")

                    append("\n\nKeyboard modifiers pressed:\n")

                    append("alt: ${keyboardModifiers.isAltPressed}\t")
                    append("ctrl: ${keyboardModifiers.isCtrlPressed}\t")
                    append("meta: ${keyboardModifiers.isMetaPressed}\t")
                    append("shift: ${keyboardModifiers.isShiftPressed}\t")
                }
            }
        ),
        text = clickableText
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun handleMouseClickSimultaneously() {
    var text by remember { mutableStateOf("Press me") }

    Box(
        Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Press) {
                val position = it.changes.first().position
                text = when {
                    it.buttons.isPrimaryPressed &&
                            it.buttons.isSecondaryPressed -> "Left+Right click $position"
                    it.buttons.isSecondaryPressed -> "Right click $position"
                    it.buttons.isPrimaryPressed -> "Left click $position"
                    else -> text
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 30.sp)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun handleAWTEvent() {
    var text by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Move) {
                text = it.awtEventOrNull?.locationOnScreen?.toString().orEmpty()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun handleAdvanceClick() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        var text by remember { mutableStateOf("Click magenta box!") }
        Column {
            Box(
                modifier = Modifier
                    .background(Color.Magenta)
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.2f)
                    .onPointerEvent(PointerEventType.Press) {
                        when (it.awtEventOrNull?.button) {
                            MouseEvent.BUTTON1 ->
                                when (it.awtEventOrNull?.clickCount) {
                                    1 -> {
                                        text = "Single click"
                                    }
                                    2 -> {
                                        text = "Double click"
                                    }
                                }
                            MouseEvent.BUTTON3 -> { //BUTTON3 is right button
                                if (it.buttons.isPrimaryPressed) {
                                    text = "Right + left click"
                                } else {
                                    text = "Right click"
                                }
                            }
                        }
                    }
            )
            Text(text = text, fontSize = 40.sp)
        }
    }
}
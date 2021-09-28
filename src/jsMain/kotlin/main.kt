import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import style.InHeadRulesHolder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

object MountedStyleSheet : StyleSheet()
object InHeadStyleSheet : StyleSheet(InHeadRulesHolder())

@OptIn(ExperimentalTime::class)
private class PerSecondClock : MonotonicFrameClock {
    override suspend fun <R> withFrameNanos(
        onFrame: (Long) -> R
    ): R = suspendCoroutine { continuation ->
        window.setTimeout({
            val result = onFrame(Duration.milliseconds(1000).inWholeNanoseconds)
            continuation.resume(result)
        }, 1000)
    }
}

fun main() {
    renderComposable(document.getElementById("rootMounted")!!, PerSecondClock()) {
        Style(MountedStyleSheet)
        Content("Mounted", MountedStyleSheet)
    }

    renderComposable(document.getElementById("rootInHead")!!, PerSecondClock()) {
        Content("InHead", InHeadStyleSheet)
    }
}

@Composable
fun Content(name: String, styleSheet: StyleSheet) {
    var bool by remember { mutableStateOf(false) }
    Div({
        println("$name: Composing")
        classes(styleSheet.css {
            backgroundColor(if (bool) Color.red else Color.blue)
        })
        style {
            padding(2.cssRem)
            border {
                width = 0.5.cssRem
                color = Color.black
                style = LineStyle.Dashed
            }
        }
    }) {
        Button({
            onClick {
                println("$name: Click!")
                bool = !bool
            }
        }) { Text("Change color!") }
    }
}

package com.hfad.simplemanager.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Swapper change content using key value with enter and exit animations and delay between two
 * @author N.I. Antonenko nantonenko59@gmail.com
 * @param modifier [Modifier]
 * @param key with this value will be called content composable
 * @param isContinues if true next composable enter animation will be played simultaneously with
 *  current composable exit animation. if false next composable enter animation will wait until
 *  current composable exit animation is over.
 * @param delay delay next composable enter animation start. Caution, if animation is not continues
 *  then to this delay will be added delay of current composable exit animation time.
 * @param content content of Swapper.
 */
@Composable
fun <T>Swapper(
    modifier: Modifier = Modifier,
    key: T,
    isContinues: Boolean = false,
    delay: Long = 0L,
    enter: EnterTransition = slideInVertically() + fadeIn(),
    exit: ExitTransition = slideOutVertically() + fadeOut(),
    content: @Composable (key: T) -> Unit = {}
) {
    // buffer to hold content keys
    val keyBuffer = remember { mutableListOf<T>(key, key) }

    // indices to swap buffer values
    val i = remember { mutableListOf(0, 1) }

    // states of animations for to AnimatedVisibility
    val animVisStateList = listOf(
        remember { MutableTransitionState<Boolean>(true) },
        remember { MutableTransitionState<Boolean>(false) }
    )

    // for delays
    val scope = rememberCoroutineScope()

    // need to prevent recomposition during delay time
    var sync by remember { mutableStateOf(true) }

    // start enter animation and swap AnimatedVisibility indices
    fun onShow() {
        animVisStateList[i[1]].targetState = true
        i.reverse()
    }

    // start enter animation with delay
    @Composable
    fun onDelayedShow() {
        sync = false
        LaunchedEffect(key1 = key) {
            scope.launch {
                delay(delay)
                onShow()
                sync = true
            }
        }
    }

    // if detects that key of visible component doesn't correspond to new key and all animation is complete
    // and we are not in delay phase of swapper start process of changing content
    if (keyBuffer[i[0]] != key && animVisStateList[i[0]].isIdle && sync) {
        animVisStateList[i[0]].targetState = false
        keyBuffer[i[1]] = key

        if (isContinues) {
            if (delay == 0L) onShow()
            else onDelayedShow()
        }
    }

    // need for not continues mode. Wait until exit animation will be over and play enter animation
    if (!isContinues && animVisStateList[i[0]].isIdle && !animVisStateList[i[0]].currentState) {
        if (delay == 0L) {
            keyBuffer[i[0]] = key
            onShow()
        } else onDelayedShow()
    }

    /**
     * First reason to have two [AnimatedVisibility] is need to play enter animation of next
     * content and exit animation of current simultaneously.
     * Second reason is a bug (or maybe it is intended behavior) in [AnimatedVisibility] composable.
     * When trying to play enter animation right after exit animation in [AnimatedVisibility]
     * (listen state to be .isIdle = true and .currentState = false), instead of enter animation
     * reverse of exit animation is playing. But if you delay enter animation start then it will
     * play as intended. On my PC needed delay is about 300 ms.
     */
    Box(modifier = modifier) {
        AnimatedVisibility(
            visibleState = animVisStateList[0],
            enter = enter,
            exit = exit
        ) {
            content(keyBuffer[0])
        }

        AnimatedVisibility(
            visibleState = animVisStateList[1],
            enter = enter,
            exit = exit
        ) {
            content(keyBuffer[1])
        }
    }
}
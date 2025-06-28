package com.example.kushagraDemo

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class MviViewModel<STATE, EFFECT, EVENT> : MviViewModelBase<STATE, EFFECT, EVENT>() {
    private val _viewStates: MutableStateFlow<STATE> by lazy { MutableStateFlow(viewState) }
    override val viewStates: StateFlow<STATE>
        get() = _viewStates.asStateFlow()

    private var _viewState: STATE? = null
    var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            _viewState = value
            _viewStates.value = value
        }

    /**
     * With the shared flow, events are broadcast to an unknown number (zero or more) of subscribers.
     * In the absence of a subscriber, any posted event is immediately dropped. It is a design pattern
     * to use for events that must be processed immediately or not at all.
     *
     * With the channel, each event is delivered to a single subscriber.
     * An attempt to post an event without subscribers will suspend as soon as the channel buffer
     * becomes full, waiting for a subscriber to appear. Posted events are not dropped.
     */
    private val _viewEffects: Channel<EFFECT> = Channel(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        onUndeliveredElement = {})
    override val viewEffects: Flow<EFFECT>
        get() = _viewEffects.receiveAsFlow()

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect
            ?: throw UninitializedPropertyAccessException("\"viewEffect\" was queried before being initialized")
        set(value) {
            _viewEffect = value
            viewModelScope.launch { _viewEffects.send(value) }
        }

    @CallSuper
    override fun process(viewEvent: EVENT) {
        Log.d(MAIN_TAG, "$TAG -> processing viewEvent : $viewEvent")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(MAIN_TAG, "$TAG -> onCleared")
    }

    companion object {
        private const val TAG = "MviViewModel"
    }
}

interface ViewModelContract<EVENT> {
    fun process(viewEvent: EVENT)
}

abstract class MviViewModelBase<STATE, EFFECT, EVENT> :
    ViewModel(), ViewModelContract<EVENT> {
    abstract val viewStates: StateFlow<STATE>
    abstract val viewEffects: Flow<EFFECT>
}

internal const val MAIN_TAG = "ArchUtils"
package com.vvalitsky.stm.service

import com.vvalitsky.stm.configuration.Event
import com.vvalitsky.stm.configuration.State
import org.springframework.statemachine.action.Action

interface BusinessProcessService {
    fun actionOnCreateTask(): Action<State, Event>
    fun actionOnStartTask(): Action<State, Event>
    fun actionOnFinishTask(): Action<State, Event>
}

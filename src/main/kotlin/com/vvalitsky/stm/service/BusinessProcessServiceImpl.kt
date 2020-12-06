package com.vvalitsky.stm.service

import com.vvalitsky.stm.configuration.Event
import com.vvalitsky.stm.configuration.State
import com.vvalitsky.stm.repository.ProcessTaskRepository
import org.springframework.statemachine.action.Action
import org.springframework.stereotype.Service

@Service
class BusinessProcessServiceImpl(val processTaskRepository: ProcessTaskRepository) : BusinessProcessService {

    override fun actionOnCreateTask(): Action<State, Event> {
        return Action { stateContext ->
            run {
                println("State target name on create ${stateContext.target.id.name}")
                processTaskRepository.findAll().forEach {
                    println("Process task name: ${it?.name}")
                }
            }
        }
    }

    override fun actionOnStartTask(): Action<State, Event> {
        return Action { stateContext ->
            run {
                println("State target name on start ${stateContext.target.id.name}")
                processTaskRepository.findAll().forEach {
                    println("Process task name: ${it?.name}")
                }
            }
        }
    }

    override fun actionOnReviewTask(): Action<State, Event> {
        return Action { stateContext ->
            run {
                println("State target name on review ${stateContext.target.id.name}")
                processTaskRepository.findAll().forEach {
                    println("Process task name: ${it?.name}")
                }
            }
        }
    }

    override fun actionOnFinishTask(): Action<State, Event> {
        return Action { stateContext ->
            run {
                println("State target name on finish ${stateContext.target.id.name}")
                processTaskRepository.findAll().forEach {
                    println("Process task name:x ${it?.name}")
                }
            }
        }
    }
}

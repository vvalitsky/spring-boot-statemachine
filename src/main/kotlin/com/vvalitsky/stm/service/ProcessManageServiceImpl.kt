package com.vvalitsky.stm.service

import com.vvalitsky.stm.configuration.Event
import com.vvalitsky.stm.configuration.State
import com.vvalitsky.stm.dto.ProcessTaskCreateDto
import com.vvalitsky.stm.dto.ProcessTaskResponseDto
import com.vvalitsky.stm.model.ProcessTask
import com.vvalitsky.stm.repository.ProcessTaskRepository
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.statemachine.persist.StateMachinePersister
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProcessManageServiceImpl(
    private val stateMachinePersister: StateMachinePersister<State, Event, ProcessTask>,
    private val stateMachineFactory: StateMachineFactory<State, Event>,
    private val processTaskRepository: ProcessTaskRepository
) : ProcessManageService {

    override fun createProcessTask(processTaskCreateDto: ProcessTaskCreateDto): ProcessTaskResponseDto {
        val stateMachine = stateMachineFactory.stateMachine
        val processTask = ProcessTask(
                name = processTaskCreateDto.name
        )
        stateMachine.sendEvent(Event.CREATE_TASK)
        stateMachinePersister.persist(
                stateMachine,
                processTask
        )

        stateMachinePersister.restore(stateMachine, processTask)

        return ProcessTaskResponseDto(
                id = processTask.id!!,
                name = processTask.name,
                state = stateMachine.state.id.name
        )
    }

    override fun fireProcessTaskEvent(processTaskId: Long, event: Event): ProcessTaskResponseDto {
        return fireEventOnProcessTask(processTaskId, event)
    }

    @Transactional(readOnly = true)
    override fun getAllProcessTasks(): List<ProcessTaskResponseDto> {
        return processTaskRepository.findAll()
                .map {
                    processTask -> ProcessTaskResponseDto(
                        id = processTask?.id!!,
                        name = processTask.name,
                        state = processTask.processTaskState?.state ?: ""
                    )
                }
    }

    private fun fireEventOnProcessTask(processTaskId: Long, event: Event): ProcessTaskResponseDto {
        val processTask = processTaskRepository.findById(processTaskId)
        val stateMachine = stateMachineFactory.stateMachine
        stateMachinePersister.restore(stateMachine, processTask)
        stateMachine.sendEvent(event)

        stateMachinePersister.persist(
                stateMachine,
                processTask
        )

        stateMachinePersister.restore(stateMachine, processTask)
        return ProcessTaskResponseDto(
                id = processTask?.id!!,
                name = processTask.name,
                state = stateMachine.state.id.name
        )
    }
}

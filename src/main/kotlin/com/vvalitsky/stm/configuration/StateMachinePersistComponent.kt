package com.vvalitsky.stm.configuration

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.vvalitsky.stm.exceptions.StateMachineDeserializeException
import com.vvalitsky.stm.exceptions.StateMachinePersistException
import com.vvalitsky.stm.model.ProcessTask
import com.vvalitsky.stm.model.ProcessTaskState
import com.vvalitsky.stm.repository.ProcessTaskRepository
import com.vvalitsky.stm.repository.ProcessTaskStateRepository
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.UUID
import org.springframework.messaging.MessageHeaders
import org.springframework.statemachine.StateMachineContext
import org.springframework.statemachine.StateMachinePersist
import org.springframework.statemachine.kryo.MessageHeadersSerializer
import org.springframework.statemachine.kryo.StateMachineContextSerializer
import org.springframework.statemachine.kryo.UUIDSerializer
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StateMachinePersistComponent(
    private val processTaskRepository: ProcessTaskRepository,
    private val processTaskStateRepository: ProcessTaskStateRepository
) : StateMachinePersist<State, Event, ProcessTask> {

    companion object {
        private val kryoThreadLocal: ThreadLocal<Kryo> = object : ThreadLocal<Kryo>() {
            override fun initialValue(): Kryo {
                val kryo = Kryo()
                kryo.addDefaultSerializer(StateMachineContext::class.java, StateMachineContextSerializer<State, Event>())
                kryo.addDefaultSerializer(MessageHeaders::class.java, MessageHeadersSerializer())
                kryo.addDefaultSerializer(UUID::class.java, UUIDSerializer())
                return kryo
            }
        }
    }

    @Transactional
    override fun write(stateMachineContext: StateMachineContext<State, Event>, processTask: ProcessTask) {

        val task = if (processTask.id != null) {
            processTaskRepository.findById(processTask.id)
                    ?: throw StateMachinePersistException("Process task with id = ${processTask.id} not found")
        } else {
            processTaskRepository.save(processTask)
        }

        val processTaskState = processTaskStateRepository.findByProcessTaskId(task?.id)

        if (processTaskState == null) {
            processTaskStateRepository.save(
                    ProcessTaskState(
                            id = null,
                            processTask = task,
                            state = stateMachineContext.state.name,
                            stateContext = serialize(stateMachineContext)
                    )
            )
        } else {
            processTaskState.stateContext = serialize(stateMachineContext)
            processTaskState.state = stateMachineContext.state.name
            processTaskStateRepository.save(processTaskState)
        }
    }

    @Transactional(readOnly = true)
    override fun read(processTask: ProcessTask): StateMachineContext<State, Event> {
        processTask.id ?: throw StateMachinePersistException("Process task id can't be null")

        val task = processTaskRepository.findById(processTask.id)
                    ?: throw StateMachinePersistException("Process task with id=${processTask.id} not found")

        val processTaskState = processTaskStateRepository.findByProcessTaskId(task.id)
                ?: throw StateMachinePersistException("Process task state for task with id=${processTask.id} not found")

        return deserialize(processTaskState.stateContext)
    }

    private fun serialize(context: StateMachineContext<State, Event>): ByteArray {
        val kryo = kryoThreadLocal.get()
        val out = ByteArrayOutputStream()
        val output = Output(out)
        kryo.writeObject(output, context)
        output.close()
        return out.toByteArray()
    }

    private fun deserialize(data: ByteArray?): StateMachineContext<State, Event> {
        if (data == null || data.isEmpty()) {
            throw StateMachineDeserializeException("Deserialization data can't be null")
        }
        val kryo = kryoThreadLocal.get()
        val `in` = ByteArrayInputStream(data)
        val input = Input(`in`)
        return kryo.readObject(input, StateMachineContext::class.java) as StateMachineContext<State, Event>
    }
}

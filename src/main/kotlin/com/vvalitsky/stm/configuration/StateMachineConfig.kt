package com.vvalitsky.stm.configuration

import com.vvalitsky.stm.model.ProcessTask
import com.vvalitsky.stm.service.BusinessProcessService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.StateMachinePersist
import org.springframework.statemachine.config.EnableStateMachineFactory
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.listener.StateMachineListener
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.persist.DefaultStateMachinePersister
import org.springframework.statemachine.persist.StateMachinePersister

@Configuration
@EnableStateMachineFactory
class StateMachineConfig(
    val stateMachinePersister: StateMachinePersist<State, Event, ProcessTask>,
    val businessProcessService: BusinessProcessService
) : EnumStateMachineConfigurerAdapter<State, Event>() {

    @Throws(Exception::class)
    override fun configure(config: StateMachineConfigurationConfigurer<State, Event>) {
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener())
    }

    @Throws(Exception::class)
    override fun configure(states: StateMachineStateConfigurer<State, Event>) {
        states.withStates()
                .initial(State.TASK_CREATED)
                .state(State.TASK_STARTED)
                .state(State.TASK_ON_REVIEW)
                .state(State.TASK_FINISHED)
                .end(State.TASK_FINISHED)
    }

    @Throws(Exception::class)
    override fun configure(transitions: StateMachineTransitionConfigurer<State, Event>) {
        transitions
                .withExternal()
                    .source(State.TASK_CREATED)
                    .target(State.TASK_STARTED)
                    .event(Event.START_TASK)
                    .action(businessProcessService.actionOnStartTask())
                .and()
                    .withExternal()
                        .source(State.TASK_STARTED)
                        .target(State.TASK_ON_REVIEW)
                        .event(Event.REVIEW_TASK)
                .and()
                    .withExternal()
                        .source(State.TASK_ON_REVIEW)
                        .target(State.TASK_FINISHED)
                        .event(Event.FINISH_TASK)
    }

    @Bean
    fun listener(): StateMachineListener<State, Event>? {
        return object : StateMachineListenerAdapter<State, Event>() {
            override fun stateChanged(
                from: org.springframework.statemachine.state.State<State, Event>?,
                to: org.springframework.statemachine.state.State<State, Event>?
            ) {
                println("State change to " + to?.id)
            }
        }
    }

    @Bean
    fun persister(): StateMachinePersister<State, Event, ProcessTask>? {
        return DefaultStateMachinePersister<State, Event, ProcessTask>(
                stateMachinePersister
        )
    }
}

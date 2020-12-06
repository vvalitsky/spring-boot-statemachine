# spring-boot-statemachine
SpringBoot state machine usage with PostgreSql example
Official documentation -> https://docs.spring.io/spring-statemachine/docs/1.1.1.RELEASE/reference/htmlsingle/

This abstraction is useful for programming different business flows, which are based on some states.

In this example we have designed simple flow of some abstract task process flow.
###States configuration
```kotlin
    override fun configure(states: StateMachineStateConfigurer<State, Event>) {
        states
            .withStates()
              .initial(State.TASK_CREATED)
              .state(State.TASK_STARTED)
              .state(State.TASK_ON_REVIEW)
              .end(State.TASK_FINISHED)
     }
```

This step is about flow events wiring with states and describes a transition flow.
###Transitions configuration
```kotlin
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
```


### Build and running
Start of PostgreSql in Docker
```bash
   docker-compose up -d
```

Build project
```bash
   ./gradlew clean --refresh-dependencies bootRun
```
----
### Run result
![BootRunResult](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/boot_run_result.png)

### Swagger UI is available on http://localhost:8085/swagger-ui.html
![Swagger_1](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/swagger_1.png)

### Swagger UI is available on http://localhost:8085/swagger-ui.html
![SwaggerTaskCreation](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/swagger_task_creation.png)

### Response result
```json
{
  "id": 1,
  "name": "Super task example name",
  "state": "TASK_CREATED"
}
```

### Created task in database
![CreatedTaskInDb](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/database_created_task.png)

### Related task state in database
![CreatedTaskStateInDb](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/database_related_task_state.png)

----
### Start process task
![StartProcessTask](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/swagger_fire_start_task_event.png)

### Response result
```json
{
  "id": 1,
  "name": "Super task example name",
  "state": "TASK_STARTED"
}
```

### Process task state changed to TASK_STARTED
![ProcessTaskStateStart](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/database_task_state_start_state.png)

In logs You will see result of calling BusinessProcessService.actionOnStartTask()
```bash
State target name on start TASK_STARTED
```

----
### Review process task
![ReviewTaskProcess](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/swagger_fire_review_task_event.png)

### Response result
```json
{
  "id": 1,
  "name": "Super task example name",
  "state": "TASK_ON_REVIEW"
}
```

### Process task state changed to TASK_ON_REVIEW
![ProcessTaskStateReview](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/database_task_state_review_state.png)

----
### Finish process task
![FinishTaskProcess](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/swagger_final_state_finished.png)

### Response result
```json
{
  "id": 1,
  "name": "Super task example name",
  "state": "TASK_FINISHED"
}
```

### Process task state changed to TASK_FINISHED
![ProcessTaskStateFinished](https://raw.githubusercontent.com/vvalitsky/spring-boot-statemachine/master/screenshots/database_finish_state.png)




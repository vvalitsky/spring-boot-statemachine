package com.vvalitsky.stm.api

import com.vvalitsky.stm.dto.ProcessTaskCreateDto
import com.vvalitsky.stm.dto.ProcessTaskEventDto
import com.vvalitsky.stm.dto.ProcessTaskResponseDto
import com.vvalitsky.stm.service.ProcessManageService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(value = "ProcessTaskController", description = "API for process task managing")
@RestController
@RequestMapping("/api/v1/stm")
class ProcessTaskController(
    val processManageService: ProcessManageService
) {

    @ApiOperation(
            httpMethod = "POST",
            value = "Create process task",
            response = ProcessTaskResponseDto::class
    )
    @PostMapping("/task/create")
    fun createTask(processTaskCreate: ProcessTaskCreateDto): ResponseEntity<ProcessTaskResponseDto> {
        return ResponseEntity.ok(
                processManageService.createProcessTask(processTaskCreate)
        )
    }

    @ApiOperation(
            httpMethod = "POST",
            value = "Fire event on process task",
            response = ProcessTaskResponseDto::class
    )
    @PostMapping("/task/event/fire")
    fun fireEventOnProcessTask(@RequestBody processTaskEventDto: ProcessTaskEventDto): ResponseEntity<ProcessTaskResponseDto> {
        return ResponseEntity.ok(
                processManageService.fireProcessTaskEvent(
                        processTaskId = processTaskEventDto.processTaskId,
                        event = processTaskEventDto.event
                )
        )
    }

    @ApiOperation(
            httpMethod = "GET",
            value = "Get all process tasks",
            response = ProcessTaskResponseDto::class,
            responseContainer = "list"
    )
    @GetMapping("/task/all")
    fun getAllProcessTasks(): ResponseEntity<List<ProcessTaskResponseDto>> {
        return ResponseEntity.ok(
                processManageService.getAllProcessTasks()
        )
    }
}

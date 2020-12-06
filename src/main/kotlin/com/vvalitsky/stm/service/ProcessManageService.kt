package com.vvalitsky.stm.service

import com.vvalitsky.stm.configuration.Event
import com.vvalitsky.stm.dto.ProcessTaskCreateDto
import com.vvalitsky.stm.dto.ProcessTaskResponseDto

interface ProcessManageService {
    fun createProcessTask(processTaskCreateDto: ProcessTaskCreateDto): ProcessTaskResponseDto
    fun fireProcessTaskEvent(processTaskId: Long, event: Event): ProcessTaskResponseDto
    fun getAllProcessTasks(): List<ProcessTaskResponseDto>
}

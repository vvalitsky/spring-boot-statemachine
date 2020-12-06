package com.vvalitsky.stm.repository

import com.vvalitsky.stm.model.ProcessTaskState
import org.springframework.data.jpa.repository.JpaRepository

interface ProcessTaskStateRepository : JpaRepository<ProcessTaskState?, Long?> {
    fun findByProcessTaskId(processTaskId: Long?): ProcessTaskState?
}

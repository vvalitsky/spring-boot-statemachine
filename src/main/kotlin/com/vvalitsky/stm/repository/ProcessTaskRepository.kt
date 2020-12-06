package com.vvalitsky.stm.repository

import com.vvalitsky.stm.model.ProcessTask
import org.springframework.data.jpa.repository.JpaRepository

interface ProcessTaskRepository : JpaRepository<ProcessTask?, Long?> {
    fun findById(id: Long): ProcessTask?
}

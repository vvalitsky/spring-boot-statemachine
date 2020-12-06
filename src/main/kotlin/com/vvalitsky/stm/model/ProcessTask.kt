package com.vvalitsky.stm.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "process_task")
data class ProcessTask(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Column
    val name: String,

    @OneToOne(mappedBy = "processTask")
    var processTaskState: ProcessTaskState? = null
)

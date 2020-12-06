package com.vvalitsky.stm.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "process_task_state")
data class ProcessTaskState(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "processTaskId", updatable = false, insertable = false)
    val processTask: ProcessTask,

    @Column
    var state: String,

    @Column
    var stateContext: ByteArray
) {
    override fun toString(): String {
        return "ProcessTaskState(id=$id, processTask=${processTask.id}, state='$state')"
    }
}

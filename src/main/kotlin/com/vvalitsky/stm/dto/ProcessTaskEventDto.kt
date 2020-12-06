package com.vvalitsky.stm.dto

import com.vvalitsky.stm.configuration.Event
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Process task create dto")
data class ProcessTaskEventDto(

    @ApiModelProperty("Process task id")
    val processTaskId: Long,

    @ApiModelProperty("Process task event")
    val event: Event
)

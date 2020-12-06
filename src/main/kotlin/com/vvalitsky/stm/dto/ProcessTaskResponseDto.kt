package com.vvalitsky.stm.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Process task create response dto")
data class ProcessTaskResponseDto(

    @ApiModelProperty("Process task id")
    var id: Long,

    @ApiModelProperty("Process task name")
    var name: String,

    @ApiModelProperty("Process task state")
    var state: String
)

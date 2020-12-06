package com.vvalitsky.stm.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Process task create dto")
data class ProcessTaskCreateDto(
    @ApiModelProperty("Process task name")
    var name: String
)

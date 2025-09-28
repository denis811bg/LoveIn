package com.example.lovein.erozoneexplorer.dataclass

import com.example.lovein.common.models.EroZoneMutable

data class ZoneCounter(
    val zone: EroZoneMutable, var remaining: Int
)

package com.example.lovein.common.data

import android.os.Environment

enum class Dest(val relativePath: String) {
    Downloads(Environment.DIRECTORY_DOWNLOADS)
}

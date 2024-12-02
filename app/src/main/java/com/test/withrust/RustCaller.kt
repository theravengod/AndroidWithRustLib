package com.test.withrust

class RustCaller {
    companion object {
        init {
            System.loadLibrary("rustnat")
        }

        external fun hello(): String
    }
}
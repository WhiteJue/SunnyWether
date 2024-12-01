package com.sunnywether.android.logic.model

data class Wind(val speed: Float, val direction: Float) {
    fun speedDesc() : String? {
        if(speed <= 0) return null
        return when {
            speed < 0.3 -> "无风"
            speed < 1.6 -> "微风徐徐"
            speed < 3.4 -> "清风"
            speed < 5.5 -> "树叶摇摆"
            speed < 8.0 -> "树枝摇动"
            speed < 10.8 -> "风力强劲"
            speed < 13.9 -> "风力强劲"
            speed < 17.2 -> "风力超强"
            speed < 20.8 -> "狂风大作"
            speed < 24.5 -> "狂风呼啸"
            speed < 28.5 -> "暴风毁树"
            speed < 32.7 -> "暴风毁树"
            speed < 37.0 -> "飓风"
            speed < 41.5 -> "台风"
            speed < 46.2 -> "强台风"
            speed < 51.0 -> "强台风"
            speed < 56.1 -> "超强台风"
            speed < 61.3 -> "超强台风"
            else -> null
        }
    }

    fun speedGrade() : Int? {
        if(speed <= 0) return null
        return when {
            speed < 0.3 -> 0
            speed < 1.6 -> 1
            speed < 3.4 -> 2
            speed < 5.5 -> 3
            speed < 8.0 -> 4
            speed < 10.8 -> 5
            speed < 13.9 -> 6
            speed < 17.2 -> 7
            speed < 20.8 -> 8
            speed < 24.5 -> 9
            speed < 28.5 -> 10
            speed < 32.7 -> 11
            speed < 37.0 -> 12
            speed < 41.5 -> 13
            speed < 46.2 -> 14
            speed < 51.0 -> 15
            speed < 56.1 -> 16
            speed < 61.3 -> 17
            else -> null
        }
    }
}


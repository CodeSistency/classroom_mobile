package com.example.classroom.common.animations.gsap




class Timeline {
    private val animations = mutableListOf<suspend (Float, Float) -> Unit>()

    fun add(animation: suspend (Float, Float) -> Unit) {
        animations.add(animation)
    }

    suspend fun run(scrollPosition: Float, scrollRange: Float) {
        for (animation in animations) {
            animation(scrollPosition, scrollRange) // Pass scroll position to each animation
        }
    }
}


// Timeline helpers with scroll support
fun timeline(builder: Timeline.() -> Unit): Timeline {
    val timeline = Timeline()
    timeline.builder()
    return timeline
}

package com.apple.experience
import android.app.Activity
import android.os.Bundle
class FakeLauncherActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); finish() }
}

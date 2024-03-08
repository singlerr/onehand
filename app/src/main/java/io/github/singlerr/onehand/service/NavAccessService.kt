package io.github.singlerr.onehand.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent

class NavAccessService : AccessibilityService() {
    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onServiceConnected() {
        instance = this
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.DEFAULT.or(AccessibilityServiceInfo.FEEDBACK_HAPTIC)

        serviceInfo = info
    }

    fun executeAction(action: Int)  {
        performGlobalAction(action)
    }

    companion object {
        var instance: NavAccessService? = null
            private set
    }
}

package io.github.singlerr.onehand.service

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.torrydo.floatingbubbleview.service.expandable.BubbleBuilder
import com.torrydo.floatingbubbleview.service.expandable.ExpandableBubbleService
import com.torrydo.floatingbubbleview.service.expandable.ExpandedBubbleBuilder

class OverlayService : ExpandableBubbleService() {
    override fun onCreate() {
        super.onCreate()
        minimize()
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun configBubble(): BubbleBuilder {
        return BubbleBuilder(this)
            .bubbleDraggable(true)
            .bubbleCompose {
                Column(modifier = Modifier.background(color = Color.Transparent, shape = RoundedCornerShape(25.dp)).padding(10.dp)) {
                    IconButton(onClick = {
                        NavAccessService.instance?.executeAction(AccessibilityService.GLOBAL_ACTION_BACK)
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.Magenta)
                    }
                    IconButton(onClick = {
                        NavAccessService.instance?.executeAction(AccessibilityService.GLOBAL_ACTION_HOME)
                    }) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.Magenta)
                    }
                    IconButton(onClick = {
                        NavAccessService.instance?.executeAction(AccessibilityService.GLOBAL_ACTION_BACK)
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.Magenta)
                    }
                }
            }
            .bubbleStyle(null)
            .enableAnimateToEdge(true)
            .closeBubbleStyle(null)
    }

    override fun configExpandedBubble(): ExpandedBubbleBuilder {
        return ExpandedBubbleBuilder(this)
            .expandedCompose { }
            .startLocation(0, 0)
            .draggable(true)
            .style(null)
            .enableAnimateToEdge(true)
    }
}

package io.github.singlerr.onehand

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.github.singlerr.onehand.service.OverlayService
import io.github.singlerr.onehand.ui.theme.appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    PermissionManagement()
                }
            }
        }
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

private fun startOverlayService(context: Context) {
    ContextCompat.startForegroundService(context, Intent(context, OverlayService::class.java))
}

private fun checkAccessibility(context: Context): Boolean  {
    val mgr = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    return mgr.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT).any {
            app ->
        app.resolveInfo.serviceInfo.packageName == context.applicationInfo.packageName
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
fun PermissionManagement(modifier: Modifier = Modifier) {
    val permState = rememberPermissionState(permission = Manifest.permission.SYSTEM_ALERT_WINDOW)
    val context = LocalContext.current

    if (!checkAccessibility(context))
        {
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            return
        }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            when {
                permState.status.isGranted -> {
                    Icon(imageVector = Icons.Default.Build, contentDescription = null, Modifier.size(Dp.Hairline))
                    Text(text = stringResource(id = R.string.info_req_perm))
                    Text(text = stringResource(id = R.string.info_accessibility))
                    Button(onClick = {
                        permState.launchPermissionRequest()
                    }) {
                        Text(text = stringResource(id = R.string.req_parm))
                    }
                }
                else -> {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    Text(text = stringResource(id = R.string.ready_to_use))
                    startOverlayService(context)
                }
            }
        }
    }
}

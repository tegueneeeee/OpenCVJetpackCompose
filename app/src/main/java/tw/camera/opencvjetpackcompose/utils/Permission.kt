package tw.camera.opencvjetpackcompose.utils

import android.Manifest
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun Permission(
    permission: String = Manifest.permission.CAMERA,
    permissionNotAvailableContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val permissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Rationale(
                onRequestPermission = { permissionState.launchPermissionRequest()}
            )
                                      },
        permissionNotAvailableContent = permissionNotAvailableContent,
        content = content
    )
}

@Composable
private fun Rationale(
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text("Permission request")
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("OK")
            }
        }
    )
}
package com.example.trucast
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
// 1. Chota Phone (Low Resolution)
@Preview(name = "Small Phone", group = "Devices", device = "spec:width=320dp,height=480dp", showBackground = true)
// 2. Standard Phone (Pixel 7/8)
@Preview(name = "Standard Phone", group = "Devices", device = "spec:width=411dp,height=891dp", showBackground = true)
// 3. Tablet (Landscape)
@Preview(name = "Tablet", group = "Devices", device = "spec:width=1280dp,height=800dp", showBackground = true)
// 4. Foldable
@Preview(name = "Foldable", group = "Devices", device = "spec:width=673dp,height=841dp", showBackground = true)
annotation class DevicePreviews
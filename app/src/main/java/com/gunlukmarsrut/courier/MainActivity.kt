package com.gunlukmarsrut.courier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gunlukmarsrut.courier.navigation.GunlukMarsrutNavGraph
import com.gunlukmarsrut.courier.ui.theme.GunlukMarsrutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GunlukMarsrutTheme {
                GunlukMarsrutNavGraph()
            }
        }
    }
}

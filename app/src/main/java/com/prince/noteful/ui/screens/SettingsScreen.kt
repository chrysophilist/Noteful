package com.prince.noteful.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.prince.noteful.navigation.AppScaffoldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    setScaffoldState: (AppScaffoldState)-> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        setScaffoldState(
            AppScaffoldState(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent
                        ),
                        scrollBehavior = scrollBehavior
                    )
                }
            )
        )
    }

    SettingsContent(scrollBehavior = scrollBehavior)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(scrollBehavior: TopAppBarScrollBehavior){

    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    var syncEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileSection(
            onAccountSettings = {}
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.clip(RoundedCornerShape(22.dp))) {
            SettingsItem(icon = Icons.Outlined.Sync, title = "Sync", subtitle = "Keep your data synchronized"){
                Checkbox(
                    checked = syncEnabled, onCheckedChange = {syncEnabled = !syncEnabled}
                )
            }
            MyHorizontalDivider()
            SettingsItem(icon = Icons.Outlined.Palette, title = "Appearance", subtitle = "Customize the app's appearance", onClick = {})
            MyHorizontalDivider()
            SettingsItem(Icons.Outlined.Feedback, "Feedback", onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto: princekumarjnvmdb@gmail.com".toUri()
                }
                try {
                    context.startActivity(intent)
                } catch(e: Exception) {
                    Toast.makeText(context, "mailto: princekumarjnvmdb@gmail.com", Toast.LENGTH_SHORT).show()
                }
            })
            MyHorizontalDivider()
            SettingsItem(Icons.Outlined.Code, "Source Code", onClick = {uriHandler.openUri("https://github.com/chrysophilist/Noteful")})
            MyHorizontalDivider()
            SettingsItem(Icons.Outlined.Info, "About", onClick = {uriHandler.openUri("https://github.com/chrysophilist/Noteful?tab=readme-ov-file#-Noteful")})
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader("Developer")
        Column(modifier = Modifier.clip(RoundedCornerShape(22.dp))) {
            SettingsItem(Icons.Outlined.PersonOutline, "About Developer", onClick = {uriHandler.openUri("https://www.linkedin.com/in/princekr2480/")})
        }

        Spacer(modifier = Modifier.height(24.dp))


        SectionHeader("Other Apps")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OtherAppItem(icon = Icons.Rounded.Newspaper, label = "NewsApp", onClick = {uriHandler.openUri("https://github.com//chrysophilist/newsapp")})
            OtherAppItem(icon = Icons.Outlined.Movie, label = "Cinemon", onClick = {uriHandler.openUri("https://github.com/chrysophilist/movie-tv-discovery-android")})
            OtherAppItem(icon = Icons.Outlined.Store, label = "Ecomm", onClick = {uriHandler.openUri("https://github.com/chrysophilist/Ecomm")})
            OtherAppItem(icon = Icons.Outlined.Upcoming, label = "Upcoming", onClick = {
                uriHandler.openUri("https://github.com//chrysophilist")
                Toast.makeText(context, "Thanks for your interest!", Toast.LENGTH_SHORT).show()
            })
        }
        Spacer(modifier = Modifier.height(56.dp))
    }
}

@Composable
fun SectionHeader(text: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}



@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit) ?= null,
    content: (@Composable ()-> Unit) ?= null
) {
    val isClickable = if (onClick != null){
        Modifier.clickable{onClick()}
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(isClickable)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(title, fontSize = 16.sp)
            if (subtitle != null) {
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
        }
        content?.invoke()
    }
}
@Composable
fun OtherAppItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp) // Touch target padding
    ) {
        // The Circular Icon Container
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // The Label Below
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileSection(
    name: String ?= "User",
    onAccountSettings: ()-> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd) {

            val brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFEA4335),
                    Color(0xFFFBBC05),
                    Color(0xFF34A853),
                    Color(0xFF4285F4),
                    Color(0xFFEA4335)
                )
            )

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(3.dp, brush, CircleShape)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
            }


            Box(
                modifier = Modifier
                    .offset(x = 0.5.dp, y = 0.5.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                    .padding(4.dp)
                    .clickable(onClick = {}),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Hi, $name!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { onAccountSettings() },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Text("Manage your Account")
        }
    }
}


@Composable
fun MyHorizontalDivider() {
    HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.background)
}

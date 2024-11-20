package com.cashmate.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.material.icons.outlined.History

import androidx.compose.material3.Icon

@Composable
fun BottomBar(
    onNavigate: (String) -> Unit,
) {

    val settingsTab = TabBarItem(title = CahsMateScreen.Logs.name, selectedIcon = Icons.Filled.History, unselectedIcon = Icons.Outlined.History)
    val homeTab = TabBarItem(title = CahsMateScreen.Home.name, selectedIcon = Icons.Filled.Dashboard, unselectedIcon = Icons.Outlined.Dashboard)
    val moreTab = TabBarItem(title = CahsMateScreen.Stats.name, selectedIcon = Icons.Filled.PieChart, unselectedIcon = Icons.Outlined.PieChartOutline)

    val tabBarItems = listOf(settingsTab, homeTab,  moreTab)

    TabView(tabBarItems, onNavigate)
}

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val action: () -> Unit = {},
    val badgeAmount: Int? = null
)

@Composable
fun TabView(
    tabBarItems: List<TabBarItem>,
    onNavigate: (String) -> Unit,
) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(1)
    }

        NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    val wasSelected = selectedTabIndex == index
                    selectedTabIndex = index
                    if (!wasSelected) {
                        onNavigate(tabBarItem.title)
                    } else {
                        // Cambia la visibilidad del modal al hacer clic en el botón ya seleccionado
//                        onModalVisibilityChanged(true)
                        tabBarItem.action()
                    }
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = { "holq" },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = {
        if (badgeAmount != null) {
            // Your badge composable here, e.g., Text("$badgeAmount")
        }
    }) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = title
        )
    }
}
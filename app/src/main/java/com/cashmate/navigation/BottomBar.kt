package com.cashmate.navigation

import AddExpenseModal
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
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

@Composable
fun BottomBar(
    onNavigate: (String) -> Unit,
) {
    var isModalVisible by rememberSaveable { mutableStateOf(false) }

    val settingsTab = TabBarItem(title = CahsMateScreen.Logs.name, selectedIcon = Icons.Filled.Star, unselectedIcon = Icons.Outlined.Star)
    val homeTab = TabBarItem(title = CahsMateScreen.Home.name, selectedIcon = Icons.Filled.Add, unselectedIcon = Icons.Outlined.Home, action = { isModalVisible = true })
    val moreTab = TabBarItem(title = CahsMateScreen.Stats.name, selectedIcon = Icons.Filled.Person, unselectedIcon = Icons.Outlined.Person)

    val tabBarItems = listOf(settingsTab, homeTab,  moreTab)

    TabView(tabBarItems, onNavigate, onModalVisibilityChanged = { isVisible ->
        isModalVisible = isVisible
    })

    if (isModalVisible) {
        AddExpenseModal(
            onDismiss = { isModalVisible = false },
            onSave = { description, amount ->
                // Lógica para guardar el gasto
                isModalVisible = false
            }
        )
    }
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
    onModalVisibilityChanged: (Boolean) -> Unit  // Callback para cambiar la visibilidad del modal
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

@OptIn(ExperimentalMaterial3Api::class)
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
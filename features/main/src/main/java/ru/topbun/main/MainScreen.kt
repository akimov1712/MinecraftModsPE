package ru.topbun.main

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.topbun.navigation.SharedScreen
import ru.topbun.ui.R
import ru.topbun.ui.theme.Colors
import ru.topbun.ui.components.AppDropDown
import ru.topbun.ui.components.AppTextField
import ru.topbun.ui.components.InterstitialAd
import ru.topbun.ui.components.ModsList
import ru.topbun.ui.components.TabRow
import ru.topbun.ui.components.noRippleClickable

object MainScreen: Tab, Screen {

    override val options: TabOptions
    @Composable get() = TabOptions(0U, stringResource(R.string.tabs_main), painterResource(R.drawable.ic_tabs_main))

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BLACK_BG)
                .padding(top = 24.dp, start = 20.dp, end = 20.dp)
        ) {
            val activity = LocalActivity.currentOrThrow
            val parentNavigator = LocalNavigator.currentOrThrow.parent
            val viewModel = viewModel<MainViewModel>()
            val state by viewModel.state.collectAsState()
            val mods by viewModel.mods.collectAsState()
            TopBar(viewModel, state)
            Spacer(Modifier.height(20.dp))
            SortBar(viewModel, state)
            Spacer(Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xff464646))
            )
            ModsList(
                mods = mods,
                onClickFavorite = { viewModel.changeFavorite(it) },
                onClickMod = {
                    viewModel.changeOpenMod(it)
                }
            )
            state.openMod?.let {
                val detailScreen = rememberScreen(SharedScreen.DetailModScreen(it))
                InterstitialAd(activity) {
                    parentNavigator?.push(detailScreen)
                    viewModel.changeOpenMod(null)
                }
            }
            LaunchedEffect(this) {
                viewModel.loadMods()
            }
        }
    }

}



@Composable
private fun SortBar(viewModel: MainViewModel, state: MainState) {
    val mapSortWithTitle = state.sortTypes.map{
        it to stringResource(it.titleRes)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabRow(
            modifier = Modifier.weight(1f),
            items = state.modSorts.map { stringResource(it.textRes) },
            selectedIndex = state.modSortSelectedIndex
        ) {
            viewModel.changeModSort(it)
        }
        AppDropDown(
            value = stringResource(state.selectedSortType.titleRes),
            items = state.sortTypes.map { stringResource(it.titleRes) }
        ) { title ->
            viewModel.changeSortType(mapSortWithTitle.first { it.second == title }.first)
        }
    }
}


@Composable
private fun TopBar(viewModel: MainViewModel, state: MainState) {
    val navigator = LocalNavigator.currentOrThrow
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AppTextField(
            modifier = Modifier.weight(1f),
            value = state.search,
            placeholder = stringResource(R.string.search),
            onValueChange = { viewModel.changeSearch(it) },
            iconStart = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "search",
                    tint = Colors.GRAY
                )
            }
        )
        val favoriteScreen = rememberScreen(SharedScreen.FavoriteScreen)
        Image(
            modifier = Modifier
                .size(28.dp)
                .noRippleClickable { navigator.push(favoriteScreen) },
            painter = painterResource(R.drawable.ic_mine_heart_filled),
            contentDescription = "favorite mods",
        )
    }
}
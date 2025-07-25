package ru.topbun.favorite

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import ru.topbun.ui.theme.Fonts
import ru.topbun.ui.theme.Typography
import ru.topbun.ui.components.InterstitialAd
import ru.topbun.ui.components.ModItem
import ru.topbun.ui.components.NativeAd

object FavoriteScreen : Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(0U, stringResource(R.string.tabs_favorite), painterResource(R.drawable.ic_tabs_favorite))


    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BLACK_BG)
        ) {
            val activity = LocalActivity.currentOrThrow
            val parentNavigator = LocalNavigator.currentOrThrow.parent
            val viewModel = viewModel<FavoriteViewModel>()
            val state by viewModel.state.collectAsState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
            ) {
                item { Header(state) }
                if (state.mods.isNotEmpty()){
                    state.mods.forEachIndexed { index, mod ->
                        item{
                            ModItem(
                                mod = mod,
                                onClickFavorite = { viewModel.removeFavorite(mod) },
                                onClickMod = {
                                    viewModel.changeOpenMod(mod)
                                }
                            )
                        }
                        if (index != 0 && ((index + 1) % 2 == 0)){
                            item { NativeAd(activity.applicationContext) }
                        }
                        if (state.mods.size == 1){
                            item { NativeAd(activity.applicationContext) }
                        }
                    }
                } else {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "The list is empty :(",
                            style = Typography.APP_TEXT,
                            fontSize = 18.sp,
                            color = Colors.GRAY,
                            textAlign = TextAlign.Center,
                            fontFamily = Fonts.SF.BOLD,
                        )
                    }
                }
            }
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
private fun Header(state: FavoriteState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 14.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.favorite, state.mods.count()),
            style = Typography.APP_TEXT,
            fontSize = 22.sp,
            color = Colors.GRAY,
            fontFamily = Fonts.SF.BOLD,
        )
    }
}
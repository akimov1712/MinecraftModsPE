package ru.topbun.detail_mod

import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.parcelize.Parcelize
import ru.topbun.domain.entity.ModEntity
import ru.topbun.navigation.SharedScreen
import ru.topbun.ui.R
import ru.topbun.ui.components.AppButton
import ru.topbun.ui.components.IconWithButton
import ru.topbun.ui.components.NativeAd
import ru.topbun.ui.components.noRippleClickable
import ru.topbun.ui.theme.Colors
import ru.topbun.ui.theme.Fonts
import ru.topbun.ui.theme.Typography
import ru.topbun.ui.utils.getImageWithNameFile

@Parcelize
data class DetailModScreen(private val mod: ModEntity) : Screen, Parcelable {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current

        val viewModel = remember { DetailModViewModel(context, mod) }
        val state by viewModel.state.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.GRAY_BG)
                .navigationBarsPadding()
                .statusBarsPadding()
                .background(Colors.BLACK_BG)
        ) {
            Header(viewModel, state)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                ButtonInstruction(navigator)
                Spacer(Modifier.height(10.dp))
                Preview(state.mod)
                Spacer(Modifier.height(20.dp))
                TitleWithDescr(state.mod)
                Spacer(Modifier.height(10.dp))
                Metrics(state.mod)
                Spacer(Modifier.height(20.dp))
                SupportVersions(state)
                Spacer(Modifier.height(20.dp))
                NativeAd(context)
                Spacer(Modifier.height(20.dp))
                FileButtons(viewModel, state)
            }
        }
        state.choiceFilePathSetup?.let {
            SetupModDialog(it, viewModel) {
                viewModel.changeStageSetupMod(null)
            }
        }
        if (state.dontWorkAddonDialogIsOpen){
            DontWorkAddonDialog { viewModel.openDontWorkDialog(false) }
        }
    }

}


@Composable
private fun FileButtons(viewModel: DetailModViewModel, state: DetailModState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        state.mod.files.forEach {
            AppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                text = it,
                contentColor = Color(0xff4AD858),
                containerColor = Colors.GREEN_BG,
            ) {
                viewModel.changeStageSetupMod(it)
            }
        }
        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            text = stringResource(R.string.addon_don_t_work),
            contentColor = Colors.WHITE,
            containerColor = Color(0xffE03131),
        ) {
            viewModel.openDontWorkDialog(true)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SupportVersions(state: DetailModState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.supported_versions),
            style = Typography.APP_TEXT,
            fontSize = 18.sp,
            color = Colors.WHITE,
            fontFamily = Fonts.SF.SEMI_BOLD,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            state.mod.supportVersion.forEach { version ->
                SupportVersionItem(
                    value = version,
                )
            }
        }
    }
}

@Composable
private fun SupportVersionItem(value: String, actualVersion: Boolean = false) {
    Text(
        modifier = Modifier
            .background(if (actualVersion) Colors.GREEN else Colors.WHITE, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        text = value,
        style = Typography.APP_TEXT,
        fontSize = 15.sp,
        color = Colors.BLACK_BG,
        fontFamily = Fonts.SF.SEMI_BOLD,
    )
}

@Composable
private fun Metrics(mod: ModEntity) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        IconWithButton(mod.countDownload.toString(), R.drawable.ic_download)
        IconWithButton(mod.countFavorite.toString(), R.drawable.ic_favorite)
    }
}

@Composable
private fun TitleWithDescr(mod: ModEntity) {
    Text(
        text = mod.title,
        style = Typography.APP_TEXT,
        fontSize = 24.sp,
        color = Colors.WHITE,
        fontFamily = Fonts.SF.BOLD,
    )
    Spacer(Modifier.height(10.dp))
    Text(
        text = mod.description,
        style = Typography.APP_TEXT,
        fontSize = 14.sp,
        color = Colors.GRAY,
        fontFamily = Fonts.SF.MEDIUM,
    )
}

@Composable
private fun Preview(mod: ModEntity) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        painter = painterResource(getImageWithNameFile(mod.previewRes)),
        contentDescription = mod.title,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun ButtonInstruction(navigator: Navigator) {
    val instructionScreen = rememberScreen(SharedScreen.InstructionScreen)
    AppButton(
        text = stringResource(R.string.instructions),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        navigator.push(instructionScreen)
    }
}

@Composable
private fun Header(viewModel: DetailModViewModel, state: DetailModState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.GRAY_BG)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navigator = LocalNavigator.currentOrThrow
        Icon(
            modifier = Modifier
                .height(20.dp)
                .noRippleClickable { navigator.pop() },
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "button back",
            tint = Colors.GREEN
        )
        Text(
            text = stringResource(R.string.installation),
            style = Typography.APP_TEXT,
            fontSize = 18.sp,
            color = Colors.GRAY,
            fontFamily = Fonts.SF.BOLD,
        )
        Image(
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable { viewModel.changeFavorite() },
            painter = painterResource(
                if (state.mod.isFavorite) R.drawable.ic_mine_heart_filled else R.drawable.ic_mine_heart_stroke
            ),
            contentDescription = "favorite mods",
        )
    }
}
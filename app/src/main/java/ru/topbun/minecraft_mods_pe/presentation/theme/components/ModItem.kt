package ru.topbun.minecraft_mods_pe.presentation.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.domain.R
import ru.topbun.domain.entity.ModEntity
import ru.topbun.minecraft_mods_pe.presentation.theme.Colors
import ru.topbun.minecraft_mods_pe.presentation.theme.Fonts
import ru.topbun.minecraft_mods_pe.presentation.theme.Typography
import ru.topbun.minecraft_mods_pe.utills.getImageWithNameFile

@Composable
fun ModItem(mod: ModEntity, onClickFavorite: () -> Unit, onClickMod: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Colors.GRAY_BG)
            .clickable { onClickMod() }
            .padding(10.dp),
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp)),
            painter = painterResource(getImageWithNameFile(mod.previewRes)),
            contentDescription = "image mod",
            contentScale = ContentScale.FillWidth
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                modifier = Modifier.weight(1f),
                text = mod.title,
                style = Typography.APP_TEXT,
                fontSize = 18.sp,
                color = Colors.WHITE,
                fontFamily = Fonts.SF.BOLD,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
            Image(
                modifier = Modifier.size(24.dp).rippleClickable{ onClickFavorite() },
                painter = painterResource(if (mod.isFavorite) R.drawable.ic_mine_heart_filled else R.drawable.ic_mine_heart_stroke),
                contentDescription = "status favorite mod",
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = mod.description,
            style = Typography.APP_TEXT,
            fontSize = 15.sp,
            color = Colors.GRAY,
            fontFamily = Fonts.SF.MEDIUM,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconWithButton(mod.countDownload.toString(), R.drawable.ic_download)
            IconWithButton(mod.countFavorite.toString(), R.drawable.ic_favorite)
        }

    }
}

package com.example.gamecollection.presentation.games_listings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.util.GameAppColors
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.format.DateTimeFormatter

@Composable
fun GameListItem (
    game: Game,
    modifier: Modifier = Modifier,
    navigator: NavHostController,
    onGameClick: (Int?) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical=10.dp)
            .fillMaxWidth()
            .clickable { onGameClick(game.id) },
        elevation = 8.dp,
        shape = RoundedCornerShape(20.dp)

    ) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(20.dp)),
                    painter = rememberImagePainter(
                    game.picturePath,

                ),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5F)
                    .width(IntrinsicSize.Min)
                    .padding(start = 10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Text(
                        text = game.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(600),
                    )
                    Text(
                        text = game.producer,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = GameAppColors.GRAY_1,
                    )
                }

                Text(
                    text = game.platform.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = GameAppColors.PALE_BLUE,
                    modifier = modifier.padding(top=10.dp)
                )

            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,

            ) {
                Text(
                    text = "Last played - ${game.lastTimePlayed.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = GameAppColors.GRAY_2,
                )
                Text(
                    text = "${game.price}${game.currency.sign}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = GameAppColors.PALE_BLUE,
                    modifier = modifier.padding(top = 30.dp)
                )
            }

        }
    }
}
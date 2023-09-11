package com.example.gamecollection.presentation.game_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.gamecollection.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.gamecollection.data.local.Format
import com.example.gamecollection.util.GameAppColors
import java.time.format.DateTimeFormatter

@Composable
fun GameDetailsScreen (
    navController: NavHostController,
    viewModel: GameDetailsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val game = viewModel.gameState.value.game
    if (game != null) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {

            Box(
                modifier = modifier
                    .fillMaxHeight(0.35f)
                    .background(GameAppColors.PALE_BLUE)
                    .fillMaxWidth()

            ) {
                TextButton(
                    onClick = {
                        navController.navigateUp()
                    },
                    modifier = modifier
                        .align(Alignment.TopStart)
                        .padding(top = 40.dp)
                ) {
                    Text(
                        text = "Back",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500)
                    )
                }
                Text(
                    text = game.name,
                    modifier = modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 40.dp),
                    fontWeight = FontWeight(600),
                    fontSize = 30.sp,
                    color = Color.White
                )
                Column(modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 60.dp)
                    .fillMaxWidth()
                    .height(160.dp)
                ) {
                Image(
                    modifier = modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .align(Alignment.CenterHorizontally),
                    painter = rememberImagePainter(
                        game.picturePath
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                }
                Column(modifier = modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.White)
                    .zIndex(-1f)
                ){}
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth(),

                ) {
                    Text(
                        text = game.producer,
                        fontWeight = FontWeight(400),
                        fontSize = 30.sp,
                        modifier = modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "${game.platform.name} - ${game.price}${game.currency.sign}",
                        fontWeight = FontWeight(400),
                        fontSize = 24.sp,
                        color = GameAppColors.PALE_BLUE,
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = modifier.height(30.dp))
                    Row(modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)) {
                        Text(
                            text = "Purchased on: ",
                            fontSize = 20.sp,
                            color = GameAppColors.GRAY_1
                        )
                        Text(
                            text = game.purchaseDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            fontSize = 20.sp,
                            color = GameAppColors.PALE_BLUE
                        )
                    }
                    Spacer(modifier = modifier.height(16.dp))
                    Row(modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)) {
                        Text(
                            text = "Last played: ",
                            fontSize = 20.sp,
                            color = GameAppColors.GRAY_1
                        )
                        Text(
                            text = game.lastTimePlayed.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                            fontSize = 20.sp,
                            color = GameAppColors.PALE_BLUE
                        )
                    }
                    Spacer(modifier = modifier.height(16.dp))
                    Box(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 16.dp)
                        ) {
                            Text(
                                text = "Format",
                                fontSize = 20.sp,
                                color = GameAppColors.GRAY_1,
                                modifier = modifier.padding(bottom = 10.dp)
                            )
                            if (game.formatOfGame == Format.DISK) {
                                Image(
                                    modifier = modifier
                                        .size(60.dp),
                                    painter = painterResource(id = R.drawable.disk_removebg_preview),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,

                                )
                            }
                            else {
                                Image(
                                    modifier = modifier
                                        .size(60.dp),
                                    painter = painterResource(id = R.drawable.digital_removebg_preview),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,

                                    )
                            }
                        }
                        Column(
                            modifier = modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 16.dp)
                        ) {
                            Text(
                                text = "Format",
                                fontSize = 20.sp,
                                color = GameAppColors.GRAY_1,
                                modifier = modifier.padding(bottom = 10.dp)
                            )
                            if (game.formatOfGame == Format.DISK) {
                                Image(
                                    modifier = modifier
                                        .size(60.dp),
                                    painter = painterResource(id = R.drawable.disk_removebg_preview),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,

                                    )
                            }
                            else {
                                Image(
                                    modifier = modifier
                                        .size(60.dp),
                                    painter = painterResource(id = R.drawable.digital_removebg_preview),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,

                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}

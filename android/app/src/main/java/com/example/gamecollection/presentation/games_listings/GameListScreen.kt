package com.example.gamecollection.presentation.games_listings

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.presentation.add_update.AddGameViewModel
import com.example.gamecollection.presentation.error_handling.ErrorEvents
import com.example.gamecollection.util.GameAppColors
import com.ramcosta.composedestinations.annotation.Destination
import org.apache.commons.lang3.math.Fraction

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(start = true)
fun GameListScreen (
    navController: NavHostController,
    viewModel: GameListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onGameClick: (Int?) -> Unit
    ) {
    val state = viewModel.gamesState.value
    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.errorEvents.collect { event ->
            when (event) {
                is ErrorEvents.ErrorOnFetchingData -> {
                    Toast.makeText(
                        context,
                        "Error on fetching data",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigateUp()
                }
                is ErrorEvents.ErrorOnDeletingGame -> {
                    Toast.makeText(
                        context,
                        "Error on deleting game",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigateUp()
                }
            }

        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color(0xFFF3F3F3),
                modifier = modifier.height(70.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    FloatingActionButton(onClick = { navController.navigate("add-game") }, backgroundColor = GameAppColors.PALE_BLUE) {
                        Text(
                            text = "+",
                            color=Color(0xFFFFFFFF),
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        scaffoldState = scaffoldState
    ) {

    }
        Column(modifier = modifier.fillMaxSize()) {
            val openDialogDelete = remember { mutableStateOf(false)}
            val selectedGame: MutableState<Game?> = remember { mutableStateOf(null)}
            Text(
                text = "Your collection",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight(600)
            )

            Spacer(modifier = modifier.height(40.dp))

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp)
            ) {

                items(state.games) { game ->
                    val stateOfDissmissed = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToStart) {
                                selectedGame.value = game
                                openDialogDelete.value = true
                            }
                            if (it == DismissValue.DismissedToEnd) {
                                navController.navigate("update-game/${game.id}")
                            }
                            false
                        },

                    )
                    SwipeToDismiss(
                        state = stateOfDissmissed,
                        background = {
                            val color = when(stateOfDissmissed.dismissDirection) {
                                DismissDirection.StartToEnd -> GameAppColors.PALE_BLUE
                                DismissDirection.EndToStart -> Color.Red
                                null -> Color.Transparent
                            }
                            val icon = when(stateOfDissmissed.dismissDirection) {
                                DismissDirection.StartToEnd -> Default.Edit
                                DismissDirection.EndToStart -> Default.Delete
                                null -> Default.Bedtime
                            }
                            val align = when(stateOfDissmissed.dismissDirection) {
                                DismissDirection.StartToEnd -> Alignment.CenterStart
                                DismissDirection.EndToStart -> Alignment.CenterEnd
                                null -> Alignment.Center
                            }
                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
                                    .background(color)
                            )  {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.align(align)
                                        .padding(horizontal = 24.dp)
                                )
                            }
                        },
                        dismissContent = {
                            GameListItem(
                                game = game,
                                navigator = navController,
                                onGameClick = onGameClick
                            )
                        },
                        dismissThresholds = {FractionalThreshold(0.2f)}
                    )

                }
            }
            if (openDialogDelete.value) {
                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialogDelete.value = false
                        selectedGame.value = null
                    },
                    title = {
                        Text(text = "Delete")
                    },
                    text = {
                        Text("Are you sure you want to delete ${selectedGame.value?.name}")
                    },
                    confirmButton = {
                        Button(

                            onClick = {
                                openDialogDelete.value = false
                                viewModel.deleteGame(selectedGame.value?.id)
                                selectedGame.value = null
                            }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        Button(

                            onClick = {
                                openDialogDelete.value = false
                                selectedGame.value = null
                            }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
}
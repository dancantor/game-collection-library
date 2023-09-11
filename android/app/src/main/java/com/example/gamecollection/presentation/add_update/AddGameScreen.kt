package com.example.gamecollection.presentation.add_update

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gamecollection.presentation.error_handling.ErrorEvents
import com.example.gamecollection.presentation.games_listings.GameListViewModel
import com.example.gamecollection.util.GameAppColors
import kotlinx.coroutines.flow.collect


@Composable
fun AddGameScreen (
    navController: NavHostController,
    viewModel: AddGameViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    gameId: Int? = null
) {
    val context = LocalContext.current
    val formState = viewModel.addFormState.value
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is AddGameViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Successfully added game",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigateUp()
                }
                is AddGameViewModel.ValidationEvent.Loading -> {
                }
            }

        }
    }
    LaunchedEffect(key1 = context) {
        viewModel.errorEvents.collect { event ->
            when (event) {
                is ErrorEvents.ErrorOnAddingGame -> {
                    Toast.makeText(
                        context,
                        "Error on adding game",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is ErrorEvents.ErrorOnUpdatingGame -> {
                    Toast.makeText(
                        context,
                        "Error on updating game",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
    }
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = formState.name,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.NameChanged(it))
            },
            isError = formState.nameError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Name*") },
            placeholder = { Text(text = "Name") },
        )
        if(formState.nameError != null) {
            Text(
                text = formState.nameError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        TextField(
            value = formState.producer,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.ProducerChanged(it))
            },
            isError = formState.producerError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Producer*") },
            placeholder = { Text(text = "Producer") },
        )
        if(formState.producerError != null) {
            Text(
                text = formState.producerError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        TextField(
            value = formState.purchaseDate,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.PurchaseDateChanged(it))
            },
            isError = formState.purchaseDateError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Purchase date*") },
            placeholder = { Text(text = "dd/mm/yyyy") },
        )
        if(formState.purchaseDateError != null) {
            Text(
                text = formState.purchaseDateError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        TextField(
            value = formState.lastTimePlayed,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.LastTimePlayedChanged(it))
            },
            isError = formState.lastTimePlayedError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Last time played*") },
            placeholder = { Text(text = "dd/mm/yyyy HH:mm") },
        )
        if(formState.lastTimePlayedError != null) {
            Text(
                text = formState.lastTimePlayedError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        TextField(
            value = formState.price,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.PriceChanged(it))
            },
            isError = formState.priceError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Price*") },
            placeholder = { Text(text = "Price") },
        )
        if(formState.priceError != null) {
            Text(
                text = formState.priceError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        TextField(
            value = formState.currency,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.CurrencyChanged(it))
            },
            isError = formState.currencyError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Currency*") },
            placeholder = { Text(text = "EUR/DOL/RON") },
        )
        if(formState.currencyError != null) {
            Text(
                text = formState.currencyError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        TextField(
            value = formState.platform,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.PlatformChanged(it))
            },
            isError = formState.platformError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Platform*") },
            placeholder = { Text(text = "PC/PS/XBOX") },
        )
        if(formState.platformError != null) {
            Text(
                text = formState.platformError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        TextField(
            value = formState.formatOfGame,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.FormatChanged(it))
            },
            isError = formState.formatOfGameError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Format*") },
            placeholder = { Text(text = "DISK/DIGITAL") },
            singleLine = true
        )
        if(formState.formatOfGameError != null) {
            Text(
                text = formState.formatOfGameError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        TextField(
            value = formState.picturePath,
            onValueChange = {
                viewModel.onEvent(AddUpdateFormEvent.PictureChanged(it))
            },
            isError = formState.picturePathError != null,
            modifier = modifier.fillMaxWidth(),
            label = { Text(text = "Picture*") },
            placeholder = { Text(text = "Picture") },
        )
        if(formState.picturePathError != null) {
            Text(
                text = formState.picturePathError,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.onEvent(AddUpdateFormEvent.Submit)
            },
            modifier = modifier.align(Alignment.End)
        ) {
            Text(text= "Submit")
        }

    }
}
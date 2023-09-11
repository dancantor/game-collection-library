package com.example.gamecollection.presentation.add_update

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecollection.GameLibraryApplication
import com.example.gamecollection.data.local.Currency
import com.example.gamecollection.data.local.Format
import com.example.gamecollection.data.local.Platform
import com.example.gamecollection.data.mapper.toGame
import com.example.gamecollection.data.mapper.toGameEntity
import com.example.gamecollection.di.ApiRepository
import com.example.gamecollection.di.DaoRepository
import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.domain.repository.GameRepository
import com.example.gamecollection.domain.use_case.*
import com.example.gamecollection.presentation.error_handling.ErrorEvents
import com.example.gamecollection.presentation.games_listings.GameListState
import com.example.gamecollection.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.internal.format
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddGameViewModel @Inject constructor (
    private val validateRequired: ValidateRequired,
    private val validateDate: ValidateDate,
    private val validateDateTime: ValidateDateTime,
    private val validateDouble: ValidateDouble,
    private val validateCurrency: ValidateCurrency,
    private val validatePlatform: ValidatePlatform,
    private val validateFormat: ValidateFormat,
    @DaoRepository private val repository: GameRepository,
    @ApiRepository private val apiRepository: GameRepository,
    private val savedStateHandle: SavedStateHandle,
    app: Application
): AndroidViewModel(app) {
    private val _addFormState = mutableStateOf(AddUpdateFormState())
    val addFormState: State<AddUpdateFormState> = _addFormState
    private val validationEventChannel = Channel<ValidationEvent> {  }
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val errorEventChannel = Channel<ErrorEvents> {  }
    val errorEvents = errorEventChannel.receiveAsFlow()

    private var gameId: Int? = -1

    init {
        gameId = savedStateHandle.get<Int>("gameId")
        gameId?.let { setGameInfo(it) }
    }

    fun onEvent(event: AddUpdateFormEvent) {
        when(event) {
            is AddUpdateFormEvent.NameChanged -> {
                _addFormState.value = addFormState.value.copy(name = event.name)
            }
            is AddUpdateFormEvent.ProducerChanged -> {
                _addFormState.value = addFormState.value.copy(producer = event.producer )
            }
            is AddUpdateFormEvent.PurchaseDateChanged -> {
                _addFormState.value = addFormState.value.copy(purchaseDate = event.purchaseDate)
            }
            is AddUpdateFormEvent.LastTimePlayedChanged -> {
                _addFormState.value = addFormState.value.copy(lastTimePlayed = event.lastTimePlayed)
            }
            is AddUpdateFormEvent.PriceChanged -> {
                _addFormState.value = addFormState.value.copy(price = event.price)
            }
            is AddUpdateFormEvent.CurrencyChanged -> {
                _addFormState.value = addFormState.value.copy(currency = event.currency)
            }
            is AddUpdateFormEvent.PlatformChanged -> {
                _addFormState.value = addFormState.value.copy(platform = event.platform)
            }
            is AddUpdateFormEvent.FormatChanged -> {
                _addFormState.value = addFormState.value.copy(formatOfGame = event.format)
            }
            is AddUpdateFormEvent.PictureChanged -> {
                _addFormState.value = addFormState.value.copy(picturePath = event.pictureURL)
            }
            is AddUpdateFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData(): Unit {
        val nameResult = validateRequired.execute(addFormState.value.name)
        val producerResult = validateRequired.execute(addFormState.value.producer)
        val purchaseDateResult = validateDate.execute(addFormState.value.purchaseDate)
        val lastTimePlayedResult = validateDateTime.execute(addFormState.value.lastTimePlayed)
        val priceResult = validateDouble.execute(addFormState.value.price)
        val currencyResult = validateCurrency.execute(addFormState.value.currency)
        val platformResult = validatePlatform.execute(addFormState.value.platform)
        val formatResult = validateFormat.execute(addFormState.value.formatOfGame)
        val pictureResult = validateRequired.execute(addFormState.value.picturePath)
        val hasError = listOf(
            nameResult,
            producerResult,
            purchaseDateResult,
            lastTimePlayedResult,
            priceResult,
            currencyResult,
            platformResult,
            formatResult,
            pictureResult
        ).any { !it.isSuccessful}
        if (hasError) {
            _addFormState.value = addFormState.value.copy(
                nameError = nameResult.errorMessage,
                producerError =  producerResult.errorMessage,
                purchaseDateError = purchaseDateResult.errorMessage,
                lastTimePlayedError = lastTimePlayedResult.errorMessage,
                priceError = priceResult.errorMessage,
                currencyError = currencyResult.errorMessage,
                platformError = platformResult.errorMessage,
                formatOfGameError = formatResult.errorMessage,
                picturePathError = pictureResult.errorMessage
            )
            return
        }
        if(gameId == null) {
            viewModelScope.launch {
                addGameToLibrary()
            }
        }
        else {
            viewModelScope.launch {
                updateGame()
            }
        }
    }

    private fun updateGame() {
        if (hasInternetConnection()) {
            viewModelScope.launch {
                val gameToUpdate = Game(
                    id = gameId,
                    name = addFormState.value.name,
                    producer = addFormState.value.producer,
                    purchaseDate = LocalDate.parse(addFormState.value.purchaseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    lastTimePlayed = LocalDateTime.parse(addFormState.value.lastTimePlayed, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    isPlaying = false,
                    price = addFormState.value.price.toDouble(),
                    currency = Currency.valueOf(addFormState.value.currency),
                    platform = Platform.valueOf(addFormState.value.platform),
                    formatOfGame = Format.valueOf(addFormState.value.formatOfGame),
                    picturePath = addFormState.value.picturePath,
                    syncFlag = "UpToDate"
                )
                apiRepository
                    .updateGame(gameToUpdate, "Update")
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                validationEventChannel.send(ValidationEvent.Success)
                                repository.updateGame(gameToUpdate).collect{}
                            }
                            is Resource.Failure -> {
                                errorEventChannel.send(ErrorEvents.ErrorOnUpdatingGame)
                            }
                            is Resource.Loading -> {
                                validationEventChannel.send(ValidationEvent.Loading)
                            }
                        }
                    }
            }

        }
        else {
            viewModelScope.launch {
                repository
                    .updateGame(
                        Game(
                            id = gameId,
                            name = addFormState.value.name,
                            producer = addFormState.value.producer,
                            purchaseDate = LocalDate.parse(
                                addFormState.value.purchaseDate,
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            ),
                            lastTimePlayed = LocalDateTime.parse(
                                addFormState.value.lastTimePlayed,
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                            ),
                            isPlaying = false,
                            price = addFormState.value.price.toDouble(),
                            currency = Currency.valueOf(addFormState.value.currency),
                            platform = Platform.valueOf(addFormState.value.platform),
                            formatOfGame = Format.valueOf(addFormState.value.formatOfGame),
                            picturePath = addFormState.value.picturePath,
                            syncFlag = "Update"
                        ), "Update"
                    ).collect{ result ->
                        when (result) {
                            is Resource.Success -> {
                                validationEventChannel.send(ValidationEvent.Success)
                            }
                            is Resource.Failure -> {
                                errorEventChannel.send(ErrorEvents.ErrorOnAddingGame)
                            }
                            is Resource.Loading -> {
                                validationEventChannel.send(ValidationEvent.Loading)
                            }
                        }
                    }
            }
        }
    }

    sealed class ValidationEvent() {
        object Success: ValidationEvent()
        object Loading: ValidationEvent()
    }

    private fun addGameToLibrary() {
        if (hasInternetConnection()) {
            viewModelScope.launch {
                apiRepository
                    .addGameToLibrary(Game(
                        id = null,
                        name = addFormState.value.name,
                        producer = addFormState.value.producer,
                        purchaseDate = LocalDate.parse(addFormState.value.purchaseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        lastTimePlayed = LocalDateTime.parse(addFormState.value.lastTimePlayed, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        isPlaying = false,
                        price = addFormState.value.price.toDouble(),
                        currency = Currency.valueOf(addFormState.value.currency),
                        platform = Platform.valueOf(addFormState.value.platform),
                        formatOfGame = Format.valueOf(addFormState.value.formatOfGame),
                        picturePath = addFormState.value.picturePath,
                        syncFlag = "UpToDate"
                        )
                    )
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                validationEventChannel.send(ValidationEvent.Success)
                                if (result.data != null) {
                                    repository.addGameToLibrary(result.data).collect {}
                                }
                            }
                            is Resource.Failure -> {
                                errorEventChannel.send(ErrorEvents.ErrorOnAddingGame)
                            }
                            is Resource.Loading -> {
                                validationEventChannel.send(ValidationEvent.Loading)
                            }
                        }
                    }
            }
        }
        else {
            viewModelScope.launch {
                repository
                    .addGameToLibrary(
                        Game(
                            id = null,
                            name = addFormState.value.name,
                            producer = addFormState.value.producer,
                            purchaseDate = LocalDate.parse(
                                addFormState.value.purchaseDate,
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            ),
                            lastTimePlayed = LocalDateTime.parse(
                                addFormState.value.lastTimePlayed,
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                            ),
                            isPlaying = false,
                            price = addFormState.value.price.toDouble(),
                            currency = Currency.valueOf(addFormState.value.currency),
                            platform = Platform.valueOf(addFormState.value.platform),
                            formatOfGame = Format.valueOf(addFormState.value.formatOfGame),
                            picturePath = addFormState.value.picturePath,
                            syncFlag = "Add"
                        ), "Add"
                    ).collect{ result ->
                        when (result) {
                            is Resource.Success -> {
                                validationEventChannel.send(ValidationEvent.Success)
                            }
                            is Resource.Failure -> {
                                errorEventChannel.send(ErrorEvents.ErrorOnAddingGame)
                            }
                            is Resource.Loading -> {
                                validationEventChannel.send(ValidationEvent.Loading)
                            }
                        }
                    }
            }
        }
    }

    private fun setGameInfo(gameId: Int) {
        if (hasInternetConnection()) {
            viewModelScope.launch {
                apiRepository.getGameById(gameId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val game = result.data
                            if (game != null) {
                                _addFormState.value = addFormState.value.copy(name = game.name)
                                _addFormState.value = addFormState.value.copy(producer = game.producer)
                                _addFormState.value = addFormState.value.copy(purchaseDate = game.purchaseDate.format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                _addFormState.value = addFormState.value.copy(lastTimePlayed = game.lastTimePlayed.format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                                ))
                                _addFormState.value = addFormState.value.copy(price = game.price.toString())
                                _addFormState.value = addFormState.value.copy(currency = game.currency.name)
                                _addFormState.value = addFormState.value.copy(platform = game.platform.name)
                                _addFormState.value = addFormState.value.copy(formatOfGame = game.formatOfGame.name)
                                _addFormState.value = addFormState.value.copy(picturePath = game.picturePath)
                            }
                        }
                        is Resource.Failure -> {

                        }
                        is Resource.Loading -> {
                            validationEventChannel.send(ValidationEvent.Loading)
                        }
                    }
                }
            }

        }
        else {
            viewModelScope.launch {
                repository.getGameById(gameId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            if (result.data != null) {
                                val game = result.data
                                _addFormState.value = addFormState.value.copy(name = game.name)
                                _addFormState.value = addFormState.value.copy(producer = game.producer)
                                _addFormState.value = addFormState.value.copy(purchaseDate = game.purchaseDate.format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                _addFormState.value = addFormState.value.copy(lastTimePlayed = game.lastTimePlayed.format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                                ))
                                _addFormState.value = addFormState.value.copy(price = game.price.toString())
                                _addFormState.value = addFormState.value.copy(currency = game.currency.name)
                                _addFormState.value = addFormState.value.copy(platform = game.platform.name)
                                _addFormState.value = addFormState.value.copy(formatOfGame = game.formatOfGame.name)
                                _addFormState.value = addFormState.value.copy(picturePath = game.picturePath)
                            }
                        }
                        is Resource.Failure -> {
                        }
                        is Resource.Loading -> {
                        }
                    }
                }
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<GameLibraryApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
package com.example.kushagraDemo.holdings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.customviews.ConfigurableText
import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.absoluteValue

@Serializable
data object HoldingsRoute

@Serializable
data object HoldingsBaseRoute

fun NavGraphBuilder.holdingsScreen(isOffline: Boolean) {
    navigation<HoldingsBaseRoute>(startDestination = HoldingsRoute) {
        composable<HoldingsRoute> {
            HoldingsScreen(isOffline = isOffline)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HoldingsScreen(
    holdingsViewModel: HoldingsViewModel = hiltViewModel(),
    isOffline: Boolean
) {
    val viewState by
    holdingsViewModel.viewStates.collectAsStateWithLifecycle()
    val stockDetail = viewState.stocksDetail
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Box(Modifier.fillMaxSize()) {
        if (!isOffline && stockDetail.isEmpty() && viewState.fetchStatus != FetchStatus.Fetching) {
            Column(modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    holdingsViewModel.fetchUserStockDetails()
                }) {
                Icon(Icons.Filled.Refresh, "refresh")
                ConfigurableText("You are now connected to the internet. Refresh")
            }
        } else {
            when (viewState.fetchStatus) {
                FetchStatus.Fetching -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(modifier = Modifier.padding(bottom = 36.dp)) {
                        items(count = stockDetail.size) { index ->
                            val stockData = stockDetail[index]
                            val symbol = stockData.symbol ?: ""
                            val ltp = stockData.ltp
                            val quantity = stockData.quantity
                            val totalPnlStock = stockData.pnlValue

                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .padding(
                                        top = if (index != 0) 12.dp else 8.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 12.dp
                                    )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    ConfigurableText(
                                        symbol,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Spacer(Modifier.weight(1f))
                                    CustomText("LTP: ", formatCurrency(ltp))
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    CustomText("NET QTY: ", quantity.toString())
                                    Spacer(Modifier.weight(1f))
                                    CustomText(
                                        "P&L: ",
                                        formatCurrency(totalPnlStock.absoluteValue),
                                        colorSubTitle = if (totalPnlStock > 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                    if (!showBottomSheet) {
                        PnlNotExpandedRow(
                            false,
                            showBottomSheet1 = { value ->
                                showBottomSheet = value
                            },
                            viewState,
                            Modifier
                                .align(Alignment.BottomCenter)
                                .background(Color.LightGray)
                                .padding(12.dp)
                        )
                    } else {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = false
                            },
                            sheetState = sheetState,
                            containerColor = Color.LightGray
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                PnlRows("Current Value", viewState.totalCurrentValue)
                                PnlRows("Total investment", viewState.totalInvestment)
                                PnlRows("Today's Profit & Loss", viewState.todayPnlValue)
                                HorizontalDivider()
                                PnlNotExpandedRow(
                                    showBottomSheet = showBottomSheet,
                                    showBottomSheet1 = { value ->
                                        showBottomSheet = value
                                    },
                                    viewState = viewState,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PnlNotExpandedRow(
    showBottomSheet: Boolean,
    showBottomSheet1: (Boolean) -> Unit,
    viewState: StocksDetailsState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConfigurableText(
            "Profit & Loss",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Icon(
            if (showBottomSheet) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
            "down",
            Modifier.clickable {
                showBottomSheet1(!showBottomSheet)
            })
        Spacer(Modifier.weight(1f))
        ConfigurableText(
            "${formatCurrency(viewState.totalPnlValue)}(" + "%.2f".format((viewState.totalPnlValue * 100f) / viewState.totalInvestment) + "%)",
            style = MaterialTheme.typography.titleSmall,
            color = if (viewState.totalPnlValue < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
        )
    }
}

fun formatCurrency(amount: Float): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return formatter.format(amount)
}

@Composable
fun PnlRows(title: String, value: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ConfigurableText(
            title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.weight(1f))
        ConfigurableText(
            formatCurrency(value),
            style = MaterialTheme.typography.titleSmall,
            color = if (value < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun CustomText(
    title: String,
    subTitle: String,
    colorSubTitle: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ConfigurableText(
            title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        ConfigurableText(
            subTitle,
            style = MaterialTheme.typography.titleMedium,
            color = colorSubTitle
        )
    }
}
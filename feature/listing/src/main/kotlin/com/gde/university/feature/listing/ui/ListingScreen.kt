package com.gde.university.feature.listing.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.gde.university.core.theme.*
import com.gde.university.domain.model.UniversityModel
import com.gde.university.feature.listing.R
import com.gde.university.feature.listing.mvi.ListingEffect
import com.gde.university.feature.listing.mvi.ListingIntent
import com.gde.university.feature.listing.mvi.ListingState
import com.gde.university.feature.listing.mvi.ListingViewModel

@Composable
fun ListingScreen(
    viewModel: ListingViewModel,
    onNavigateToDetails: (UniversityModel) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ListingEffect.NavigateToDetails -> onNavigateToDetails(effect.universityModel)
                is ListingEffect.ShowError -> {
                    Toast.makeText(context, context.getString(effect.messageResId), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ListingContent(
        state = state,
        onIntent = { viewModel.sendIntent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingContent(
    state: ListingState,
    onIntent: (ListingIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.listing_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White
                )
            )
        },
        containerColor = BackgroundGray
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                state.errorResId?.let { errorResId ->
                    ErrorView(
                        message = stringResource(errorResId),
                        onRetry = { onIntent(ListingIntent.ForceRefreshData) }
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (state.isLoading) {
                        items(10) {
                            UniversityDummyItem()
                        }
                    } else {
                        items(state.universities) { university ->
                            UniversityItem(
                                universityModel = university,
                                onClick = {
                                    onIntent(
                                        ListingIntent.OnItemClicked(
                                            university
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ErrorLightRed,
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = ErrorDarkRed,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ErrorDarkRed,
                    contentColor = White
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = stringResource(R.string.retry),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun UniversityItem(universityModel: UniversityModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = universityModel.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = universityModel.stateProvince ?: universityModel.country,
                style = MaterialTheme.typography.bodyMedium,
                color = Gray
            )
        }
    }
}

@Composable
fun UniversityDummyItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(20.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(5.dp)
                    )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 36)
@Composable
fun ListingScreenPreview() {
    GDEUniversityTheme {
        ListingContent(
            state = ListingState(
                universities = sampleUniversities
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, apiLevel = 36)
@Composable
fun ListingScreenLoadingPreview() {
    GDEUniversityTheme {
        ListingContent(
            state = ListingState(
                isLoading = true
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, apiLevel = 36)
@Composable
fun ErrorViewPreview() {
    GDEUniversityTheme {
        ErrorView(
            message = "Something went wrong. Please try again later.",
            onRetry = {}
        )
    }
}

private val sampleUniversities = listOf(
    UniversityModel(
        name = "Middle East Technical University",
        country = "Turkey",
        stateProvince = "Ankara",
        webPages = listOf("http://www.metu.edu.tr/"),
        domains = listOf("metu.edu.tr"),
        alphaTwoCode = "TR"
    ),
    UniversityModel(
        name = "Istanbul Technical University",
        country = "Turkey",
        stateProvince = "Istanbul",
        webPages = listOf("http://www.itu.edu.tr/"),
        domains = listOf("itu.edu.tr"),
        alphaTwoCode = "TR"
    )
)

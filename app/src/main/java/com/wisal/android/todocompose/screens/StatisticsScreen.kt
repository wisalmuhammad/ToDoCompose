package com.wisal.android.todocompose.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wisal.android.todocompose.R
import com.wisal.android.todocompose.util.StatsResult
import com.wisal.android.todocompose.viewmodels.StatisticsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val stateResult = viewModel.stats.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = stringResource(id = R.string.todo)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "TODO")
                    }
                }
            )
        },
    ) {
        StatisticsScreenContent(modifier = modifier.padding(it),stateResult.value)
    }
}

@Composable
fun StatisticsScreenContent(
    modifier: Modifier = Modifier,
    stateResult: StatsResult
) {
    modifier.padding(8.dp)
    Column {
        Text(
            text = "Active Tasks: ${String.format("%.2f",stateResult.activeTasksPercent)}%",
            fontSize = 18.sp,
            modifier = modifier.padding(8.dp),
        )
        Text(
            text = "Completed Tasks: ${String.format("%.2f",stateResult.completedTasksPercent)}%",
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp),
        )
    }
}
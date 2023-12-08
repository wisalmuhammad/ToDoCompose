package com.wisal.android.todocompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wisal.android.todocompose.Screen
import com.wisal.android.todocompose.viewmodels.AddEditViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AddEditViewModel = hiltViewModel(),
    taskId: String? = null
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = taskId?.let { "Edit Task" } ?: "New Task") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ){
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "TODO"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveTask()
                },
                shape = CircleShape
                ) {
                Icon(Icons.Filled.Check, contentDescription = "TODO")
            }
        }
    ) { paddingValues ->
        AddEditScreenContent(
            modifier = modifier.padding(paddingValues),
            viewModel = viewModel,
        )
    }
    LaunchedEffect(key1 = Unit, block = {
        viewModel.snackbarMessage.collect {
            it?.let {msg ->
                snackBarHostState.showSnackbar(msg)
            }
        }
    })
    LaunchedEffect(key1 = Unit, block = {
        viewModel.updateTaskState.collect {
            if (it.isTaskAdded) {
                navController.popBackStack(
                    Screen.TasksScreen.route,
                    inclusive = false
                )
            }
        }
    })
    DisposableEffect(key1 = Unit) {
        viewModel.fetchTaskById(taskId)
        onDispose {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenContent(
    modifier: Modifier = Modifier,
    viewModel: AddEditViewModel,
) {
    val focusRequester = remember { FocusRequester() }
    Column(
            modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val taskTitle by viewModel.taskTitle.collectAsStateWithLifecycle()
        val taskDetail by viewModel.taskDetail.collectAsStateWithLifecycle()

        TextField(
            value = TextFieldValue(
                text = taskTitle,
                selection = TextRange(taskTitle.length)
            ),
            onValueChange = { newText -> viewModel.updateTaskTitle(newText.text) },
            placeholder = {
                Text("Title",
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal,)
                          },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            textStyle = LocalTextStyle.current.copy( fontSize = 19.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        TextField(
            value = taskDetail,
            onValueChange = { newText -> viewModel.updateTaskDetail(newText) },
            placeholder = {
                Text("Enter your task here",)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,imeAction = ImeAction.Done),
//            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp)
        )
    }
    LaunchedEffect(key1 = Unit, block = {
        focusRequester.requestFocus()
    })
}
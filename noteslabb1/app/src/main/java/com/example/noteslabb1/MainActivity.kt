
package com.example.noteslabb1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteslabb1.ui.theme.Noteslabb1Theme

data class TodoItem(
    val id: Int,
    var title: String,
    var subtitle: String,
    val check: MutableState<Boolean> = mutableStateOf(false)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Noteslabb1Theme {
                TodoApp()
            }
            }
        }
    }


@Composable
fun TodoApp() {
    val navController = rememberNavController()
    val todoList = remember { mutableStateListOf<TodoItem>() }

    NavHost(navController = navController, startDestination = "todoList") {
        composable("todoList") { TodoListScreen(navController, todoList) }
        composable("addTodo") { AddTodoScreen(navController, todoList) }
        composable("editTodo/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
            val todoItem = todoList.find { it.id == itemId }
            todoItem?.let { EditTodoScreen(navController, it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(navController: NavController, todoList: MutableList<TodoItem>) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Todo List") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addTodo") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Todo")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(todoList) { item ->
                ListItem(
                    leadingContent = {
                        Checkbox(
                            checked = item.check.value,
                            onCheckedChange = {
                                item.check.value = !item.check.value
                            })},
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.subtitle)},
                    trailingContent = {
                        Row {
                            IconButton(
                                onClick = { navController.navigate("editTodo/${item.id}") }
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Todo")
                            }
                            IconButton(
                                onClick = { todoList.remove(item) }
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete Todo")
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(navController: NavController, todoList: MutableList<TodoItem>) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }
    var subtitleError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Todo") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = it.length !in 3..50
                },
                label = { Text("Todo") },
                isError = titleError,
                supportingText = {
                    if (titleError) {
                        Text("Title must be 3 to 50 characters.")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = subtitle,
                onValueChange = {
                    subtitle = it
                    subtitleError = it.length > 120 },
                label = { Text("Details") },
                supportingText = {
                    if(subtitleError){
                        Text("description max 120 characters.")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (!titleError && !subtitleError  && title.isNotBlank() && subtitle.isNotBlank()) {
                    todoList.add(TodoItem(id = todoList.size, title = title, subtitle = subtitle))
                    navController.popBackStack()
                }
            }) {
                Text("Add Todo")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(navController: NavController, todoItem: TodoItem) {
    var title by remember { mutableStateOf(todoItem.title) }
    var subtitle by remember { mutableStateOf(todoItem.subtitle) }
    var titleError by remember { mutableStateOf(false) }
    var subtitleError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Todo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = it.length !in 3..50
                },
                label = { Text("Todo") },
                isError = titleError,
                supportingText = {
                    if (titleError) {
                        Text("Title must be 3 to 50 characters.")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = subtitle,

                onValueChange = {
                    subtitle = it
                    subtitleError = it.length >120},
                label = { Text("Details") },
                isError= subtitleError,
                supportingText = {
                    if(subtitleError){
                    Text("max 120 characters")
                }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (!titleError && !subtitleError && title.isNotBlank() && subtitle.isNotBlank()) {
                    todoItem.title = title
                    todoItem.subtitle = subtitle
                    navController.popBackStack()
                }
            }) {
                Text("Save Todo")
            }
        }
    }
}
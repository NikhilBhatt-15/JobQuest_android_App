package com.example.job_test.ui.screens
import java.text.SimpleDateFormat
import java.util.Locale
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.job_test.R
import com.example.job_test.data.model.Education
import com.example.job_test.data.model.Experience
import com.example.job_test.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class MonthYearPickerDialog(context: Context, private val listener: (year: Int, month: Int) -> Unit) {
    private val calendar = Calendar.getInstance()
    private val dialog: DatePickerDialog

    init {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, _ ->
            listener(year, month)
        }

        dialog = DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, dateSetListener,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        dialog.datePicker.findViewById<View>(context.resources.getIdentifier("day", "id", "android"))?.visibility = View.GONE
    }

    fun show() {
        dialog.show()
    }
}

sealed class EditFormType {
    object Profile : EditFormType()
    object Experience : EditFormType()
    object Education : EditFormType()
    object Resume : EditFormType()
    object Skills : EditFormType()
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(context: Context, viewModel: ProfileViewModel, onLogout: () -> Unit) {
    val profile by viewModel.profile.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var currentEditForm by remember { mutableStateOf<EditFormType?>(null) }
    val isSheetOpen =  remember { mutableStateOf(false) }


   if(isSheetOpen.value){
       ModalBottomSheet(
           sheetState = sheetState,
           onDismissRequest = { coroutineScope.launch { sheetState.hide()
                        isSheetOpen.value = false
           } }
       ) {
           currentEditForm?.let { formType ->
               when (formType) {
                   EditFormType.Profile -> ProfileEditForm(viewModel,sheetState){
                            coroutineScope.launch { sheetState.hide() }
                            isSheetOpen.value = false
                   }
                   EditFormType.Experience -> ExperienceEditForm(viewModel,sheetState){
                          coroutineScope.launch { sheetState.hide() }
                          isSheetOpen.value = false
                   }
                   EditFormType.Education -> EducationEditForm(viewModel,sheetState)
                   {
                          coroutineScope.launch { sheetState.hide() }
                          isSheetOpen.value = false
                   }
                   EditFormType.Resume -> ResumeEditForm(viewModel,sheetState){
                            coroutineScope.launch { sheetState.hide() }
                            isSheetOpen.value = false
                   }
                   EditFormType.Skills -> SkillsEditForm(viewModel,sheetState){
                            coroutineScope.launch { sheetState.hide() }
                            isSheetOpen.value = false
                   }
               }
           }
       }
   }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF262C35))
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileCard(
            imageUrl = profile?.profile?.imageUrl ?: "",
            name = profile?.profile?.firstname + " " + profile?.profile?.lastname,
            phone = profile?.profile?.phone_no ?: "",
            location = profile?.profile?.location ?: "",
            bio= profile?.profile?.bio ?: ""
        )
        Profile_Edit_Row(onEdit = { formType ->
            currentEditForm = formType
            isSheetOpen.value = true
            coroutineScope.launch { sheetState.show() }
        })
        ExperienceSection(
            experienceList = profile?.profile?.experience ?: listOf(),
            onEdit = {
                isSheetOpen.value = true
                currentEditForm = EditFormType.Experience
                coroutineScope.launch { sheetState.show() }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        EducationSection(
            educationList = profile?.profile?.education ?: listOf(),
            onEdit = {
                isSheetOpen.value = true
                currentEditForm = EditFormType.Education
                coroutineScope.launch { sheetState.show() }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SkillSection(
            skills = profile?.profile?.skills ?: listOf(),
            onEdit = {
                isSheetOpen.value = true
                currentEditForm = EditFormType.Skills
                coroutineScope.launch { sheetState.show() }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ResumeSection(
            resumeUrl = profile?.profile?.resumeUrl,
            onEdit = {
                isSheetOpen.value= true
                currentEditForm = EditFormType.Resume
                coroutineScope.launch { sheetState.show() }
            }
        )
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(text = "Logout", color = Color.White)
        }
    }
}

@Composable
fun ResumeSection(resumeUrl: String?,onEdit: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Resume", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            resumeUrl?.let {
               Image(painter = rememberAsyncImagePainter(resumeUrl),
                    contentDescription = "Resume",
                    modifier = Modifier.size(500.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_square_24),
                    contentDescription = "Edit Resume"
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsEditForm(viewModel: ProfileViewModel, sheetState: SheetState,onSubmit:()->Unit) {
    val profile = viewModel.profile.collectAsState().value
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val availableSkills = listOf(
        // Programming Languages
        "Kotlin", "Java", "Python", "JavaScript", "TypeScript", "C++", "C#", "PHP", "Ruby", "Swift",
        // Mobile Development
        "Android (Jetpack Compose, XML layouts)", "iOS (SwiftUI, UIKit)", "React Native", "Flutter", "Dart",
        // Web Development
        "HTML", "CSS", "JavaScript", "React.js", "Angular", "Vue.js", "Svelte", "Next.js", "Tailwind CSS",
        // Backend Development
        "Node.js", "Express.js", "Django", "Flask", "Laravel", "Spring Boot", "ASP.NET",
        // Databases
        "PostgreSQL", "MySQL", "MongoDB", "SQLite", "Redis", "Cassandra", "DynamoDB",
        // Cloud & DevOps
        "Docker", "Kubernetes", "AWS (Lambda, EC2, S3)", "Google Cloud Platform", "Microsoft Azure", "Jenkins", "GitHub Actions", "Terraform",
        // APIs
        "REST APIs", "GraphQL", "WebSockets", "gRPC"
    )
    val selectedSkills = remember { mutableStateListOf<String>() }

    // Load existing skills if available
    LaunchedEffect(profile?.profile?.skills) {
        profile?.profile?.skills?.let { skills ->
            selectedSkills.clear()
            selectedSkills.addAll(skills)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Text("Skills Edit Form", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(availableSkills) { skill ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (selectedSkills.contains(skill)) {
                            selectedSkills.remove(skill)
                        } else {
                            selectedSkills.add(skill)
                        }
                    }
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = selectedSkills.contains(skill),
                    onCheckedChange = {
                        if (it) {
                            selectedSkills.add(skill)
                        } else {
                            selectedSkills.remove(skill)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = skill)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.addSkills(selectedSkills) {
                       onSubmit()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Skills")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeEditForm(viewModel: ProfileViewModel, sheetState: SheetState,onSubmit: () -> Unit) {
    val profile = viewModel.profile.collectAsState().value
    val context = LocalContext.current
    var resumeUri by remember { mutableStateOf<Uri?>(null) }
    var resumeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Load existing resume if available
    LaunchedEffect(profile?.profile?.resumeUrl) {
        profile?.profile?.resumeUrl?.let { url ->
            try {
                val inputStream = context.contentResolver.openInputStream(Uri.parse(url))
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                resumeBitmap = bitmap?.asImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        resumeUri = uri
        resumeUri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                resumeBitmap = bitmap?.asImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Resume Edit Form")

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Resume")
        }

        resumeBitmap?.let { bitmap ->
            Image(bitmap = bitmap, contentDescription = "Selected Resume", modifier = Modifier.size(128.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                resumeUri?.let { uri ->
                    val resumeFile = createFileFromUri2(context, uri)
                    val resumePart = resumeFile?.let { viewModel.createResumePart(it) }
                    if (resumePart != null) {
                        isSubmitting = true
                        viewModel.addResume(resumePart) {
                            isSubmitting = false
                            coroutineScope.launch {
                                try {
                                    onSubmit()
                                    // Clear state after the sheet is hidden
                                    resumeUri = null
                                    resumeBitmap = null
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(text = if (isSubmitting) "Submitting..." else "Add Resume")
        }
    }
}

fun createFileFromUri2(context: Context, uri: Uri): File? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("resume", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationEditForm(viewModel: ProfileViewModel, sheetState: SheetState, onSubmit: () -> Unit) {
    val degree = remember { mutableStateOf("") }
    val school = remember { mutableStateOf("") }
    val field = remember { mutableStateOf("") }
    val from = remember { mutableStateOf("") }
    val to = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    val degreeError = remember { mutableStateOf<String?>(null) }
    val schoolError = remember { mutableStateOf<String?>(null) }
    val fieldError = remember { mutableStateOf<String?>(null) }
    val fromError = remember { mutableStateOf<String?>(null) }
    val toError = remember { mutableStateOf<String?>(null) }

    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    fun validateForm(): Boolean {
        var isValid = true

        if (degree.value.isBlank()) {
            degreeError.value = "Degree is required"
            isValid = false
        } else {
            degreeError.value = null
        }

        if (school.value.isBlank()) {
            schoolError.value = "School is required"
            isValid = false
        } else {
            schoolError.value = null
        }

        if (field.value.isBlank()) {
            fieldError.value = "Field is required"
            isValid = false
        } else {
            fieldError.value = null
        }

        if (from.value.isBlank()) {
            fromError.value = "From date is required"
            isValid = false
        } else {
            fromError.value = null
        }

        if (to.value.isBlank()) {
            toError.value = "To date is required"
            isValid = false
        } else {
            toError.value = null
        }

        return isValid
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Education Edit Form")

        OutlinedTextField(
            value = degree.value,
            onValueChange = { degree.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("Degree") },
            modifier = Modifier.fillMaxWidth(),
            isError = degreeError.value != null
        )
        degreeError.value?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = school.value,
            onValueChange = { school.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("School") },
            modifier = Modifier.fillMaxWidth(),
            isError = schoolError.value != null
        )
        schoolError.value?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = field.value,
            onValueChange = { field.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("Field") },
            modifier = Modifier.fillMaxWidth(),
            isError = fieldError.value != null
        )
        fieldError.value?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))

        ClickableDatePicker(
            label = "From",
            value = from.value,
            onClick = { showFromDatePicker = true },
            error = fromError.value
        )
        Spacer(modifier = Modifier.height(8.dp))

        ClickableDatePicker(
            label = "To",
            value = to.value,
            onClick = { showToDatePicker = true },
            error = toError.value
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (validateForm()) {
                    isSubmitting = true
                    viewModel.addEducation(
                        Education(
                            degree = degree.value,
                            school = school.value,
                            field = field.value,
                            from = from.value,
                            to = to.value,
                            description = description.value
                        )
                    ) {
                        isSubmitting = false
                        onSubmit()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(text = if (isSubmitting) "Submitting..." else "Add Education")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    DatePickerDialog(
        showDialog = showFromDatePicker,
        onDismissRequest = { showFromDatePicker = false },
        onDateSelected = { selectedDate -> from.value = selectedDate }
    )

    DatePickerDialog(
        showDialog = showToDatePicker,
        onDismissRequest = { showToDatePicker = false },
        onDateSelected = { selectedDate -> to.value = selectedDate }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceEditForm(
    viewModel: ProfileViewModel,
    sheetState: SheetState,
    onSubmit: () -> Unit
) {
    val title = remember { mutableStateOf("") }
    val company = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val from = remember { mutableStateOf("") }
    val to = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    val titleError = remember { mutableStateOf<String?>(null) }
    val companyError = remember { mutableStateOf<String?>(null) }
    val fromError = remember { mutableStateOf<String?>(null) }
    val toError = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Experience Edit Form", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            singleLine = true,
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = titleError.value != null
        )
        titleError.value?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = company.value,
            onValueChange = { company.value = it },
            singleLine = true,
            label = { Text("Company") },
            modifier = Modifier.fillMaxWidth(),
            isError = companyError.value != null
        )
        companyError.value?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location.value,
            onValueChange = { location.value = it },
            singleLine = true,
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ClickableDatePicker(
            label = "From",
            value = from.value,
            onClick = { showFromDatePicker = true },
            error = fromError.value
        )
        fromError.value?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))

        ClickableDatePicker(
            label = "To",
            value = to.value,
            onClick = { showToDatePicker = true },
            error = toError.value
        )
        toError.value?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = {
                if (validateExperienceForm(
                        title.value, company.value, from.value, to.value,
                        titleError, companyError, fromError, toError
                    )
                ) {
                    isSubmitting = true
                    viewModel.addExperience(
                        Experience(
                            title = title.value,
                            company = company.value,
                            location = location.value,
                            from = from.value,
                            to = to.value,
                            description = description.value
                        )
                    ) {
                        isSubmitting = false
                        coroutineScope.launch {
                            onSubmit()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(if (isSubmitting) "Submitting..." else "Add Experience")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    DatePickerDialog(
        showDialog = showFromDatePicker,
        onDismissRequest = { showFromDatePicker = false },
        onDateSelected = { selectedDate -> from.value = selectedDate }
    )

    DatePickerDialog(
        showDialog = showToDatePicker,
        onDismissRequest = { showToDatePicker = false },
        onDateSelected = { selectedDate -> to.value = selectedDate }
    )
}

fun validateExperienceForm(
    title: String,
    company: String,
    from: String,
    to: String,
    titleError: MutableState<String?>,
    companyError: MutableState<String?>,
    fromError: MutableState<String?>,
    toError: MutableState<String?>
): Boolean {
    var isValid = true

    if (title.isBlank()) {
        titleError.value = "Job title is required"
        isValid = false
    } else {
        titleError.value = null
    }

    if (company.isBlank()) {
        companyError.value = "Company is required"
        isValid = false
    } else {
        companyError.value = null
    }

    if (from.isBlank()) {
        fromError.value = "From date is required"
        isValid = false
    } else {
        fromError.value = null
    }

    if (to.isBlank()) {
        toError.value = "To date is required"
        isValid = false
    } else {
        toError.value = null
    }

    return isValid
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditForm(viewModel: ProfileViewModel,sheetState: SheetState,onSubmit: () -> Unit) {
    val profile by viewModel.profile.collectAsState()
    val bio = remember { mutableStateOf(profile?.profile?.bio ?: "") }
    val age = remember { mutableStateOf(profile?.profile?.age ?: "") }
    val location = remember { mutableStateOf(profile?.profile?.location ?: "") }
    val phone = remember { mutableStateOf(profile?.profile?.phone_no ?: "") }
    val firstName = remember { mutableStateOf(profile?.profile?.firstname ?: "") }
    val lastName = remember { mutableStateOf(profile?.profile?.lastname ?: "") }
    val email = remember { mutableStateOf(profile?.profile?.email ?: "") }
    val password = remember { mutableStateOf("") }
    val genderOptions = listOf("MALE", "FEMALE", "OTHER")
    val selectedGender = remember { mutableStateOf(profile?.profile?.gender ?: genderOptions[0]) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = firstName.value,
            onValueChange = { firstName.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName.value,
            onValueChange = { lastName.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bio.value,
            onValueChange = { bio.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age.value.toString(),
            onValueChange = { age.value = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location.value,
            onValueChange = { location.value = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone.value,
            onValueChange = { phone.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Gender", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        genderOptions.forEach { gender ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedGender.value = gender }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedGender.value == gender,
                    onClick = { selectedGender.value = gender }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = gender)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.editProfile(
                    bio = bio.value,
                    age = age.value.toString(),
                    location = location.value,
                    phone_no = phone.value,
                    gender = selectedGender.value,
                    firstName = firstName.value,
                    lastName = lastName.value,
                    email = email.value,
                    password = password.value
                )
                onSubmit()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save")
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ProfileCard(modifier: Modifier=  Modifier,imageUrl:String,name:String,phone:String,location:String,bio:String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF669BBC))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "User Profile Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // User Details
                Column {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = bio,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = phone,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = location,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

}



@Composable
fun Profile_Edit_Row(
    modifier: Modifier = Modifier,
    item: List<ProfileEditItem> = listOf(
        ProfileEditItem("Profile", R.drawable.baseline_person_outline_24, EditFormType.Profile),
        ProfileEditItem("Experience", R.drawable.baseline_assignment_24, EditFormType.Experience),
        ProfileEditItem("Education", R.drawable.baseline_library_books_24, EditFormType.Education),
        ProfileEditItem("Resume", R.drawable.baseline_insert_drive_file_24, EditFormType.Resume),
        ProfileEditItem("Skills", R.drawable.baseline_star_24, EditFormType.Skills),
    ),
    onEdit: (EditFormType) -> Unit
) {
    LazyRow {
        items(item.size) {
            Edit_Card(
                title = item[it].title,
                icon = item[it].icon,
                onClick = { onEdit(item[it].formType) }
            )
        }
    }
}


data class ProfileEditItem(
    val title: String,
    val icon: Int,
    val formType: EditFormType
)



@Composable
fun Edit_Card(
    modifier: Modifier = Modifier,
    title:String="Title",
    icon: Int=R.drawable.baseline_person_outline_24,
    onClick:()->Unit
){
    Card(
        shape = RoundedCornerShape(16.dp),

        modifier = modifier
            .width(120.dp)
            .height(150.dp)
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.width(80.dp),
                shape = RoundedCornerShape( 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF669BBC))
            ) {
                Text(
                    text = "Edit",
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ExperienceSection(
    modifier: Modifier = Modifier,
    experienceList: List<Experience> = listOf(
        Experience("Google", "Jun 2020", "New Delhi", "Software Engineer", "Jul 2021", "Worked on Android Development"),
        Experience("Microsoft", "Aug 2021", "Bangalore", "Senior Developer", "Present", "Leading a team in mobile development")
    ),
    onEdit:()->Unit= {}
) {
    // State to track if all experiences should be shown
    var showAll by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Experience",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_square_24),
                    contentDescription = "Edit"
                )
            }
        }
        // Show one experience or all based on `showAll` state
        val experiencesToShow = if (showAll) experienceList else experienceList.take(1)

        experiencesToShow.forEach { experience ->
            ExperienceCard(experience)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // "Show More" or "Show Less" button
        if (experienceList.size > 1) {
            Text(
                text = if (showAll) "Show Less" else "Show More",
                textAlign = TextAlign.End,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { showAll = !showAll }.fillMaxWidth()
            )
        }
    }
}


@Composable
fun ExperienceCard(
    experience: Experience = Experience("Google", "Jun 2020", "New Delhi", "Software Engineer", to = "Jul 2021", description = "Worked on Android Development")
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = experience.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = experience.company,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = experience.from + " - " + experience.to,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.widthIn(max = 100.dp) // Set a maximum width for the date text
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = experience.description ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
fun EducationSection(
    modifier: Modifier = Modifier,
    educationList: List<Education> ,
    onEdit: () -> Unit = {}
) {
    // State to track if all educations should be shown
    var showAll by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Education",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_square_24),
                    contentDescription = "Edit"
                )
            }
        }
        // Show one education or all based on `showAll` state
        val educationsToShow = if (showAll) educationList else educationList.take(1)

        educationsToShow.forEach { education ->
            EducationCard(education)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // "Show More" or "Show Less" button
        if (educationList.size > 1) {
            Text(
                text = if (showAll) "Show Less" else "Show More",
                textAlign = TextAlign.End,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { showAll = !showAll }.fillMaxWidth()
            )
        }
    }
}


@Composable
fun EducationCard(
    education: Education
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = education.degree,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = education.school,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = education.from + " - " + education.to,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.widthIn(max = 100.dp) // Set a maximum width for the date text
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = education.field,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillSection(
    skills: List<String>,
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.background)
        .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Skills",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_square_24),
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    skills.forEach { skill ->
                        SkillChip(skill)
                    }
                }
            }
        }
    }


@Composable
fun SkillChip(skill: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = skill,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ClickableDatePicker(
    label: String,
    value: String,
    onClick: () -> Unit,
    error: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                .border(
                    width = 1.dp,
                    color = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Text(
                text = if (value.isNotEmpty()) value else "Select $label",
                color = if (value.isNotEmpty()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun DatePickerDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = {}) { // Disable dismiss on outside touch
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                val context = LocalContext.current

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Select Month and Year",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Button(
                        onClick = {
                            MonthYearPickerDialog(context) { year, month ->
                                val formattedDate = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                                    .format(SimpleDateFormat("MM yyyy", Locale.getDefault())
                                        .parse("${month + 1} $year")!!)
                                onDateSelected(formattedDate)
                                onDismissRequest() // Close dialog after selecting a date
                            }.show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Pick Date")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}


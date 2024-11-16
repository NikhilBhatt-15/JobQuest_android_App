package com.example.job_test.ui.screens

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.job_test.R
import com.example.job_test.data.model.Education
import com.example.job_test.data.model.Experience
import com.example.job_test.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import kotlin.math.sin


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
                   EditFormType.Profile -> ProfileEditForm(viewModel,sheetState)
                   EditFormType.Experience -> ExperienceEditForm()
                   EditFormType.Education -> EducationEditForm()
                   EditFormType.Resume -> ResumeEditForm()
                   EditFormType.Skills -> SkillsEditForm()
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
                currentEditForm = EditFormType.Experience
                coroutineScope.launch { sheetState.show() }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        EducationSection(
            educationList = profile?.profile?.education ?: listOf(),
            onEdit = {
                currentEditForm = EditFormType.Education
                coroutineScope.launch { sheetState.show() }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SkillSection(
            skills = profile?.profile?.skills ?: listOf()
        )
    }
}

@Composable
fun SkillsEditForm() {

        Column {
            Text("Skills Edit Form")
        }
}

@Composable
fun ResumeEditForm() {

    Column {
        Text("Resume Edit Form")
    }
}

@Composable
fun EducationEditForm() {

    Column {
        Text("Education Edit Form")
    }
}

@Composable
fun ExperienceEditForm() {

    Column {
        Text("Experience Edit Form")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditForm(viewModel: ProfileViewModel,sheetState: SheetState) {
    val profile by viewModel.profile.collectAsState()
    val bio = remember { mutableStateOf(profile?.profile?.bio ?: "") }
    val age = remember { mutableStateOf(profile?.profile?.age ?: "") }
    val location = remember { mutableStateOf(profile?.profile?.location ?: "") }
    val phone = remember { mutableStateOf(profile?.profile?.phone_no ?: "") }
    val firstName = remember { mutableStateOf(profile?.profile?.firstname ?: "") }
    val lastName = remember { mutableStateOf(profile?.profile?.lastname ?: "") }
    val email = remember { mutableStateOf(profile?.profile?.email ?: "") }
    val password = remember { mutableStateOf("") }
    val genderOptions = listOf("Male", "Female", "Other")
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
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save")
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ProfileCard(modifier: Modifier=  Modifier,imageUrl:String= "https://res.cloudinary.com/dnv6ajx3b/image/upload/w_1000,c_fill,ar_1:1,g_auto,r_max,bo_5px_solid_red,b_rgb:262c35/v1725947540/cld-sample.jpg",name:String="Adam",phone:String="931068948",location:String="Street 1 House 2",bio:String="I am a software engineer") {
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
    educationList: List<Education> = listOf(
        Education("Stanford University", "B.Tech", "2016", "Computer Science", "2020"),
        Education("Harvard University", "Masters", "2020", "Computer Science", "2022")
    ),
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
    education: Education = Education("Stanford University", "B.Tech", "2016", "Computer Science", "2020")
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
@Preview
@Composable
fun SkillSection(
    skills: List<String> = listOf("Android", "Kotlin", "Java", "Swift", "Objective-C","r","ewg","ewg"),
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
            IconButton(onClick = { /* Handle edit click */ }) {
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
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp
        )
    }
}
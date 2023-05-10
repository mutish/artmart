package com.example.artmart

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmart.ui.theme.ArtmartTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtmartTheme() {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
                    Scaffold(topBar = {
                        TopAppBar(backgroundColor = Color.LightGray,
                        title = {
                            Text(
                                text = "Sign In",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Left,
                                color = Color.Black
                            )
                        })
                    }) {
                        Column(modifier = Modifier.padding(it)) {
                            phoneauthenticationUI(LocalContext.current)

                        }
                    }

                }

            }


        }
    }
}

// OTP REQUEST METHODS
// sign in methods will include google sign in and sms otp
@Composable
fun phoneauthenticationUI(context : Context){
    //capturing state entry
    val phoneNumber = remember{
        mutableStateOf("")
    }
    //otp ref
    val otp = remember{
        mutableStateOf("")
    }
    //verification id if OTP Token is invalid
    val verificationID = remember{
        mutableStateOf("")
    }
    //message to save/store user info
    val message = remember{
        mutableStateOf("")
    }

    //Firebase Authentication
    var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    // UI
    //Phone number field
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(Color.White),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = phoneNumber.value,
            onValueChange ={ phoneNumber.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "Enter phone number")},
            modifier = Modifier
                .padding(17.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        //otp call button
        Button(onClick = {
            // checking if the phone No variable is empty
            if(TextUtils.isEmpty(phoneNumber.value.toString())){
                Toast.makeText(context, "Phone number cannot be empty",Toast.LENGTH_LONG).show()
            }else {
                // COUNTRY CODE
                val number = "+254${phoneNumber}"
                sendVerificationCode(number,mAuth,context as Activity, callback)
            }

        }) {
            Text(text ="Get OTP", modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))

        // OTP fields

        TextField(
            value = otp.value,
            onValueChange = {otp.value = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "Enter OTP code")},
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            // check if the otp variable is empty
            if (TextUtils.isEmpty(otp.value.toString())){
                Toast.makeText(context, "OTP cannot be empty", Toast.LENGTH_LONG).show()
            } else {
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationID.value, otp.value)
                // login in with the OTP Credentials
                signInWithPhoneAuthCredentials(credential,mAuth,context as Activity, context, message)
            }

        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Verify OTP" , modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.height(5.dp))

        //Text field to communicate with the user if OTP was successful or not
        Text(text = message.value, style = TextStyle(color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold))

        //callback interaction for verification status
        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                message.value = "Verification Successful"
                Toast.makeText(context, "Verification Successful", Toast.LENGTH_LONG).show()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                message.value = "Verification Failed" + p0.message
                Toast.makeText(context, "Verification Failed ...\n Please try again later", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationID.value = p0
            }
        }
    }
}

private fun sendVerificationCode(
    number: String,
    mAuth: FirebaseAuth,
    activity: Activity,
    callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
) {
    //code generation
    val options = PhoneAuthOptions.newBuilder(mAuth)
        .setPhoneNumber(number)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(callback)
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}

private fun signInWithPhoneAuthCredentials(
    credential: PhoneAuthCredential,
    mAuth: FirebaseAuth,
    activity: Activity,
    context: Activity,
    message: MutableState<String>
) {
    //sign in with a verified code
    mAuth.signInWithCredential(credential).addOnCompleteListener(activity) {
        if (it.isSuccessful) {
            message.value = "Verification is Successful"
            Toast.makeText(context, "Verification is successful", Toast.LENGTH_LONG).show()
            // go to relevant activity

        } else {
            if (it.exception is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(
                    context,
                    "Verification Failed... " + (it.exception as FirebaseAuthInvalidCredentialsException).message,
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}


//after verification the user is taken to a home screen



package com.nexware.machinetask.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.nexware.machinetask.R
import com.nexware.machinetask.util.AppConstant
import com.nexware.machinetask.util.SessionManager
import com.nexware.machinetask.viewmodel.LoginViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel
    private val sessionManager = SessionManager()
    private val emailTextField: TextInputLayout by lazy { findViewById(R.id.emailTextField) }
    private val email: TextInputEditText by lazy { findViewById(R.id.email) }
    private val passTextField: TextInputLayout by lazy { findViewById(R.id.passTextField) }
    private val password: TextInputEditText by lazy { findViewById(R.id.password) }
    private val signin: MaterialButton by lazy { findViewById(R.id.signin) }
    private val loader: LottieAnimationView by lazy { findViewById(R.id.loader) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionManager.SessionManager(this)


        if (sessionManager.isLogin()) {
            startActivity(Intent(this, UserActivity::class.java))
            finish()
        }
        ViewModelProvider(this).get(LoginViewModel::class.java).also { it.also { this.viewModel = it } }
        loader.visibility = View.GONE
        signin.setOnClickListener(this)

        this.viewModel.mShowApiError.observeForever {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        this.viewModel.mShowNetworkError.observeForever {
            Toast.makeText(this, AppConstant.NoInternet, Toast.LENGTH_SHORT).show()
        }

        this.viewModel.mShowProgressBar.observeForever {
            if (it) {
                loader.visibility = View.VISIBLE
            } else {
                loader.visibility = View.GONE
            }
        }

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                emailTextField.error = null
            }
        })
        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                passTextField.error = null
            }
        })

    }

    override fun onClick(v: View?) {
        when(v){
            signin -> {
                login()
            }
        }
    }

    private fun login() {
        if (email.text.toString().isEmpty() && password.text.toString().isEmpty()) {
            emailTextField.error = "Enter Email"
            passTextField.error = "Enter Password"
        } else if (email.text.toString().isEmpty()) {
            emailTextField.error = "Enter Email"
        } else if (password.text.toString().isEmpty()) {
            passTextField.error = "Enter Password"
        } else {
            val loginData= JsonObject()
            loginData.addProperty("email", email.text.toString())
            loginData.addProperty("password", password.text.toString())
            this.viewModel.login(loginData).observeForever {
                if (it != null) {
                    sessionManager.storeSessionIntvalue(
                        AppConstant.LOGIN_SESSION_NAME,
                        AppConstant.LOGIN_SESSION_USER_ID,
                        1
                    )
                    sessionManager.storeSessionStringvalue(
                        AppConstant.LOGIN_SESSION_NAME,
                        AppConstant.LOGIN_SESSION_Token,
                        it.token
                    )
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                }
            }
        }
    }

}
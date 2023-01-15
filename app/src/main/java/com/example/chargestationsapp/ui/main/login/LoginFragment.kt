package com.example.chargestationsapp.ui.main.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.chargestationsapp.databinding.FragmentLoginBinding
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    var username: String = ""
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var islogout = false
    private var mAuthManager: AccountAuthService? = null
    private var mAuthParam: AccountAuthParams? = null
    val REQUEST_SIGN_IN_LOGIN = 1002

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.networkerror.visibility = View.GONE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        binding.loginbutton.setOnClickListener {
            signIn()
        }

        viewModel.isSignIn.observe(viewLifecycleOwner, {
            if (it) {
                islogout = true
                val action = LoginFragmentDirections.actionLoginFragmentToSearchFragment(username)
                view.findNavController().navigate(action)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGN_IN_LOGIN) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {

                val authAccount = authAccountTask.result
                username = authAccount.displayName

                viewModel.userSignedIn(true)

            } else {

                Toast.makeText(requireContext(), "Error,Please Try Again", Toast.LENGTH_LONG)
                viewModel.userSignedIn(false)

                binding.networkerror.visibility = View.VISIBLE
            }
        }
    }

    private fun signIn() {
        mAuthParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .setProfile()
            .setAccessToken()
            .createParams()
        mAuthManager = AccountAuthManager.getService(requireContext(), mAuthParam)
        startActivityForResult(mAuthManager?.signInIntent, REQUEST_SIGN_IN_LOGIN)

    }


}
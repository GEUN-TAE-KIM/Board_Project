package com.apliecream.boardproject.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.apliecream.boardproject.MainActivity
import com.apliecream.boardproject.R
import com.apliecream.boardproject.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinBtn.setOnClickListener {

            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            var notjoing = true

            if (email.isEmpty()) {
                Toast.makeText(this,"이메일 입력", Toast.LENGTH_SHORT).show()
                notjoing = false
            }

            if (password1.isEmpty()) {
                Toast.makeText(this,"비밀번호 입력", Toast.LENGTH_SHORT).show()
                notjoing = false
            }

            if (password2.isEmpty()) {
                Toast.makeText(this,"비밀번호 확인 입력", Toast.LENGTH_SHORT).show()
                notjoing = false
            }

            if (!password1.equals(password2)) {
                Toast.makeText(this,"비밀번호를 똑같이 입력", Toast.LENGTH_SHORT).show()
                notjoing = false
            }

            if (password1.length < 6) {
                Toast.makeText(this,"최소 6자 이상 입력", Toast.LENGTH_SHORT).show()
                notjoing = false
            }
            
            if (notjoing) {
                auth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this) {
                        task -> if (task.isSuccessful) {
                            Toast.makeText(this,"성공",Toast.LENGTH_SHORT).show()
                            
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                    } else {
                            Toast.makeText(this,"실패",Toast.LENGTH_SHORT).show()

                }
                }
            }
        }

    }
}
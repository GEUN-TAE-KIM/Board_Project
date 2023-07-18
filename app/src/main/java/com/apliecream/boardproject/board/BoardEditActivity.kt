package com.apliecream.boardproject.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.apliecream.boardproject.R
import com.apliecream.boardproject.databinding.ActivityBoardEditBinding
import com.apliecream.boardproject.util.FBAuth
import com.apliecream.boardproject.util.FBRef
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key : String
    private lateinit var binding : ActivityBoardEditBinding
    private lateinit var writeUid : String
    private val TAG = BoardEditActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)


        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }

    }

    private fun editBoardData(key : String) {

        FBRef.boardRef
            .child(key)
            .setValue(
                BoardModel(binding.titleArea.text.toString(),
                        binding.contentArea.text.toString(),
                        writeUid,
                        FBAuth.getTime())

            )
        Toast.makeText(this,"수정완료",Toast.LENGTH_SHORT).show()

        finish()

    }

    private fun getImageData(key : String){

        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            } else {

            }
        })


    }

    private fun getBoardData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.content)
                writeUid = dataModel!!.uid


            }

            override fun onCancelled(datbaseError: DatabaseError) {
                Log.w(TAG,"취소", datbaseError.toException())

            }

        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }





}
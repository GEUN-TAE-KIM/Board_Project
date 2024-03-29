package com.apliecream.boardproject.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.apliecream.boardproject.R
import com.apliecream.boardproject.comment.CommentLVAdapter
import com.apliecream.boardproject.comment.CommentModel
import com.apliecream.boardproject.databinding.ActivityBoardInsideBinding
import com.apliecream.boardproject.util.FBAuth
import com.apliecream.boardproject.util.FBRef
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding
    private lateinit var key : String

    private val commentDataList = mutableListOf<CommentModel>()
    private lateinit var commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_inside)

        binding.boardSettingIcon.setOnClickListener{
            showDialog()
        }

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }

        getCommentData(key)

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter


    }

    private fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }

                commentAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)


    }

    private fun insertComment(key : String){

        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getTime()
                )
            )

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")

    }

    private fun showDialog() {
        
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정 / 삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            
            Toast.makeText(this,"수정",Toast.LENGTH_SHORT).show()

            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this,"삭제",Toast.LENGTH_SHORT).show()
            finish()
        }


    }


    
    // 이미지 업로드 파이어베이스 코드 제공
    private fun getImageData(key: String) {
        
        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                binding.getImageArea.isVisible = false
            }
        })

    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {

                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    // uid가 같으면 설정이 보임
                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid
                    
                    if (myUid.equals(writerUid)){
                        Log.d(TAG,"내가 쓴글")
                        binding.boardSettingIcon.isVisible = true
                    } else {
                        Log.d(TAG,"내가 쓴글 아님")
                    }
                } catch (e : Exception) {
                    Log.d(TAG,"삭제 완료")
                }
            }

            override fun onCancelled(datbaseError: DatabaseError) {
                Log.w(TAG,"취소", datbaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }

}
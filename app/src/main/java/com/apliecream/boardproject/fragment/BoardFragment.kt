package com.apliecream.boardproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.apliecream.boardproject.R
import com.apliecream.boardproject.board.BoardInsideActivity
import com.apliecream.boardproject.board.BoardListLVAdapter
import com.apliecream.boardproject.board.BoardModel
import com.apliecream.boardproject.board.BoardWriteActivity
import com.apliecream.boardproject.databinding.FragmentBoardBinding
import com.apliecream.boardproject.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardFragment : Fragment() {

    private lateinit var binding : FragmentBoardBinding
    private lateinit var boardRVAdapter : BoardListLVAdapter
    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()
    private val TAG = BoardFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)

        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter
        
        // 게시글 확인
        binding.boardListView.setOnItemClickListener{ parent, view, position, id ->
            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)
        }
        
        binding.writeBtn.setOnClickListener{
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }


        binding.homeTap.setOnClickListener{

            it.findNavController().navigate(R.id.action_boardFragment_to_homeFragment)

        }

        binding.markTap.setOnClickListener{

            it.findNavController().navigate(R.id.action_boardFragment_to_markFragment)

        }
        
        getFBBoardData()

        return binding.root
    }
    
    //게시글 리스트 나열
    private  fun getFBBoardData() {
        val postListener = object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                
                //초기화
                boardDataList.clear()
                
                // 값 호출
                for(dataModel in datasnapshot.children) {
                    
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }

                boardKeyList.reverse()
                boardDataList.reverse()

                boardRVAdapter.notifyDataSetChanged()

                Log.d(TAG,boardDataList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
               Log.d(TAG,"로드:호출", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }


}
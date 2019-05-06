package com.example.finalappkotlin.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalappkotlin.R
import com.example.finalappkotlin.adapters.ChatAdapter
import com.example.finalappkotlin.models.Message
import com.example.finalappkotlin.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import kotlin.collections.HashMap

class ChatFragment : Fragment() {

    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    //Recycler
    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        setUpChatDB()
        setUpCurrentUser()
        SetUpRecyclerView()
        setUpChatBtn()

        suscribeToChatMessages()


        return _view
    }

    private fun setUpChatDB() {
        chatDBRef = store.collection("chat")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun SetUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList, currentUser.uid)

        _view.rvChat.setHasFixedSize(true)
        _view.rvChat.layoutManager = layoutManager
        _view.rvChat.itemAnimator = DefaultItemAnimator()
        _view.rvChat.adapter = adapter
    }

    private fun setUpChatBtn() {
        _view.btnSend.setOnClickListener {
            val messageText = etMessage.text.toString()
            if(messageText.isNotEmpty()){
                val message = Message(currentUser.uid,messageText,currentUser.photoUrl.toString(), Date())
                saveMessage(message)
                _view.etMessage.setText("")
            }
        }
    }

    private fun saveMessage(message: Message){
        val newMessage = HashMap <String, Any>()
        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageURL"] = message.profileImageURL
        newMessage["sentAt"] = message.sentAt

        chatDBRef.add(newMessage)
                 .addOnCompleteListener{
                   activity!!.toast("Message added!")
                }
                .addOnFailureListener {
                    activity!!.toast("Message failed try again!")
                }
    }

    private fun suscribeToChatMessages(){
        chatDBRef.addSnapshotListener(object :EventListener,com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                exception?.let{
                    activity!!.toast("Exception!!")
                    return
                }
                snapshot?.let{
                    messageList.clear()
                    val messages= it.toObjects(Message::class.java)
                    messageList.addAll(messages)
                    adapter.notifyDataSetChanged()
                }            }

        })
    }
}

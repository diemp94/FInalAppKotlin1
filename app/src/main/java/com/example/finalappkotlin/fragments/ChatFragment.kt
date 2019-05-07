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
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import java.util.EventListener
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

    private var chatSuscription: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        setUpChatDB()
        setUpCurrentUser()
        SetUpRecyclerView()
        setUpChatBtn()
        subscribeToChatMessages()


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
                val photo = currentUser.photoUrl?.let { currentUser.photoUrl.toString() } ?: run { "" }
                val message = Message(currentUser.uid,messageText,photo, Date())
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

    private fun subscribeToChatMessages(){
        chatSuscription = chatDBRef
            .orderBy("sentAt",Query.Direction.DESCENDING)
            .addSnapshotListener(object :EventListener,com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                querySnapshot?.let {
                    messageList.clear()
                    val messages = it.toObjects(Message::class.java)
                    messageList.addAll(messages.asReversed())
                    adapter.notifyDataSetChanged()
                    _view.rvChat.smoothScrollToPosition(messageList.size)
                }
                exception?.let {
                    activity!!.toast("Exception!")
                    return
                }
            }

        })
    }

    override fun onDestroy() {
        chatSuscription?.remove()
        super.onDestroy()
    }


}

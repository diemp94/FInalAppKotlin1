package com.example.finalappkotlin.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finalappkotlin.R
import com.example.finalappkotlin.toast
import com.example.finalappkotlin.utils.CircleTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info.view.*
import java.util.*
import java.util.EventListener

class InfoFragment : Fragment() {

    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference
    private var infoSuscription: ListenerRegistration? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _view = inflater.inflate(R.layout.fragment_info, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpCurrenUserInfoUI()
        subscribeToTotalMessagesFirebaseStyle()

        //Total messages FirebaseSyle

        return _view
    }

    private fun setUpChatDB() {
        chatDBRef = store.collection("chat")

    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrenUserInfoUI() {
        _view.tvInfoEmail.text = currentUser.email
        _view.tvInfoName.text =
            currentUser.displayName?.let { currentUser.displayName } ?: run { getString(R.string.info_no_name) }
        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.displayName).resize(300, 300)
                .centerCrop().transform(CircleTransform()).into(_view.ivInfoAvatar)
        } ?: run {
            Picasso.get().load(R.drawable.ic_person).resize(300, 100)
                .centerCrop().transform(CircleTransform()).into(_view.ivInfoAvatar)
        }
    }


    private fun subscribeToTotalMessagesFirebaseStyle() {

        (object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                querySnapshot?.let {
                    _view.tvInfoTotalMessages.text= "${it.size()}"
                }
                exception.let {
                    activity!!.toast("Exception!")
                }
            }
        })
    }


}

package com.example.finalappkotlin.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalappkotlin.R
import com.example.finalappkotlin.inflate
import com.example.finalappkotlin.models.Message
import com.example.finalappkotlin.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat_item_left.view.*
import kotlinx.android.synthetic.main.fragment_chat_item_right.view.*
import java.text.SimpleDateFormat

class ChatAdapter(val items: List<Message>, val userId: String): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val GLOBAL_MESSAGE = 1
    private val MY_MESSAGE = 2

    private val layoutRight = R.layout.fragment_chat_item_right
    private val layoutLeft = R.layout.fragment_chat_item_left




    override fun getItemViewType(position: Int) = if(items[position].authorId == userId) MY_MESSAGE else GLOBAL_MESSAGE

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            MY_MESSAGE -> ViewHolderR(parent.inflate(layoutRight))
            else -> ViewHolderL(parent.inflate(layoutLeft))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType){
            MY_MESSAGE -> (holder as ViewHolderR).bind(items[position])
            GLOBAL_MESSAGE -> (holder as ViewHolderL).bind(items[position])
        }
    }

    class ViewHolderR(intemView : View):RecyclerView.ViewHolder(intemView){
        fun bind(message: Message) = with(itemView){
            tvMessageRight.text = message.message
            tvTimeRight.text = SimpleDateFormat("hh:mm").format(message.sentAt)
            //Picasso load image here
            if(message.profileImageURL.isEmpty()){
                Picasso.get().load(R.drawable.ic_person).resize(100,100)
                    .centerCrop().transform(CircleTransform()).into(ivProfileRight)
            }else{
                Picasso.get().load(message.profileImageURL).resize(100,100)
                    .centerCrop().transform(CircleTransform()).into(ivProfileRight)
            }

        }
    }

    class ViewHolderL(intemView : View):RecyclerView.ViewHolder(intemView){
        fun bind(message: Message) = with(itemView){
            tvMessageLeft.text = message.message
            tvTimeLeft.text = SimpleDateFormat("hh:mm").format(message.sentAt)
            //Picasso load image here
            if(message.profileImageURL.isEmpty()){
                Picasso.get().load(R.drawable.ic_person).resize(100,100)
                    .centerCrop().transform(CircleTransform()).into(ivProfileLeft)
            }else{
                Picasso.get().load(message.profileImageURL).resize(100,100)
                    .centerCrop().transform(CircleTransform()).into(ivProfileLeft)
            }
        }
    }

}
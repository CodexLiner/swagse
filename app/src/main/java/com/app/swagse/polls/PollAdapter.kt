package com.app.swagse.polls


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.app.swagse.R
import com.app.swagse.constants.Constants
import com.app.swagse.sharedpreferences.PrefConnect
import com.bumptech.glide.Glide
import java.util.*

class PollAdapter(val list: MutableList<PollModel>?) : RecyclerView.Adapter<PollAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userImage: ImageView = itemView.findViewById<ImageView>(R.id.userImage)
        val userName: TextView = itemView.findViewById<TextView>(R.id.users_name)
        val Question: TextView = itemView.findViewById<TextView>(R.id.askedQuestion)
        val RadioGroup: RadioGroup = itemView.findViewById<RadioGroup>(R.id.RadioGroup)
        val Vote = itemView.findViewById<Button>(R.id.Vote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.active_polls_layout , parent , false);
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Question.text = "HEllo Bla Bla Bla"
        holder.userName.text = PrefConnect.readString(holder.userName.context, Constants.USERNAME, "")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() }
        Glide.with(holder.userImage).load(PrefConnect.readString(holder.userImage.context, Constants.USERPIC, "")).into(holder.userImage)

        for (i in list?.get(position)?.list!!){
            Log.d("TAG", "radiobutton : ${position}")
            val button = RadioButton(holder.RadioGroup.context)
            button.text = "Button bla bla bla $i"
            holder.RadioGroup.addView(button)

        }

        holder.Vote.setOnClickListener {
            val rdb = holder.RadioGroup.checkedRadioButtonId
            val rb = holder.RadioGroup.findViewById<RadioButton>(rdb)
        }
    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}
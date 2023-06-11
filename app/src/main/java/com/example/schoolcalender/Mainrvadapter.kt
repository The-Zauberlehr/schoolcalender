package com.example.schoolcalender

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.oneclass.view.*
import kotlinx.android.synthetic.main.weekday.view.*
import kotlinx.coroutines.currentCoroutineContext
import okhttp3.internal.notify
import java.time.format.DateTimeFormatter

class Mainrvadapter(private val classinformation: MutableList<jsondata>): RecyclerView.Adapter<CustomViewHolder>(){
    var purpleswitch:Boolean = false
    val DATE = 0
    val LESSON = 1

    override fun getItemCount(): Int {
        return classinformation?.size //Anzahl der Liste elemente
    }
    fun addclass(jsinfo:jsondata, switch:Boolean){ //fügt neues Element hinzu
        classinformation.add(jsinfo)
        purpleswitch = switch
        notifyItemInserted(classinformation.size -1)
    }
    fun removeclass(){ //leert RecyclerView
        var i:Int = classinformation?.size -1
        while (i>=0){
            classinformation.removeAt(i)
            notifyItemRemoved(i)
            i -=1
        }
    }
    override fun getItemViewType(position: Int): Int {
        if (classinformation[position].startTime ==0) { //unterscheidung Tag Stunde
            return DATE
        } else  {
            return LESSON
        }
        return -1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var cellForRow:View
        val layoutInflater = LayoutInflater.from(parent?.context)
        cellForRow = when(viewType){
            DATE ->{//lädt die zugehörigen xml Dateien
                layoutInflater.inflate(R.layout.weekday,parent,false)
            }
            LESSON ->{
                layoutInflater.inflate(R.layout.oneclass,parent, false)
            }
            else ->{
                layoutInflater.inflate(R.layout.oneclass,parent, false)
            }
        }
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder,  position: Int) {
        val starttime:String
        val endtime:String
        val curitem = classinformation[position]
        when(holder.itemViewType){
            DATE ->{holder?.view?.tvday.text = curitem.lessonCode}
            LESSON -> {
                if (purpleswitch){
                    holder?.view?.clitem.setBackgroundResource(R.drawable.background2)
                }
                if (curitem.cellState =="SUBSTITUTION"){ //falls Vertretung Lila
                    holder?.view?.clitem.setBackgroundColor(Color.parseColor("#7600bc"))
                }
                holder?.view?.tvclasstype.text = curitem.studentGroup.toString()
                if (curitem.startTime.toString().length == 3 ){
                    starttime = "0${curitem.startTime}"
                }
                else starttime = curitem.startTime.toString()
                if (curitem.endTime.toString().length == 3 ){
                    endtime = "0${curitem.endTime}"
                }
                else endtime = curitem.endTime.toString()
                holder?.view?.tvtime.text = "${starttime.addCharAtIndex(':', 2)} - ${endtime.addCharAtIndex(':', 2)}" //setzt Doppelpunkte in Uhrzeiten
                holder?.view?.tvroom.text = curitem.text.toString()}
            else ->{holder?.view?.tvclasstype.text = "an error occured"}
        }

    }
}
class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

}
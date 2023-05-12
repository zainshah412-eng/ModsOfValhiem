package com.appcake.modsforvalheim.core.adapter.command

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.model.DropDownData

class DropDownCommandAdapter(
    private val contextDD: Context, private val listDD: ArrayList<DropDownData>
) : BaseExpandableListAdapter() {
    override fun getGroupCount() = listDD.size

    override fun getChildrenCount(ddp0: Int) = 1

    override fun getGroup(grouppositionDD: Int): Any {
        val groupPosDD = grouppositionDD
        return listDD[groupPosDD]
    }

    override fun getChild(grouppositionDD: Int, childpositionDD: Int): Any {
        return listDD[grouppositionDD]
    }

    override fun getGroupId(grouppositionDD: Int): Long {
        val groupPosDD = grouppositionDD
        return groupPosDD.toLong()
    }

    override fun getChildId(grouppositionDD: Int, childpositionDD: Int): Long {
        val childPosDD = childpositionDD
        return childPosDD.toLong()
    }

    override fun hasStableIds(): Boolean {
        val isStableId = false
        return isStableId
    }

    override fun getGroupView(
        grouppositionDD: Int, isExpandedDD: Boolean, convertViewDD: View?, parentDD: ViewGroup?
    ): View {
        var convertViewDD = convertViewDD

        if (convertViewDD == null) {
            val inflaterDD =
                contextDD.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewDD = inflaterDD.inflate(R.layout.rv_drop_down_parent_item, null)
        }

        val lblListHeaderDD = convertViewDD!!.findViewById<TextView>(R.id.tvDDTitle)
        val arrowDD = convertViewDD.findViewById<ImageView>(R.id.ivDDArrow)


        if (isExpandedDD) {
            arrowDD.rotation = 90f
        } else {
            arrowDD.rotation = -90f
        }
        lblListHeaderDD.text = listDD[grouppositionDD].dropdownTitle
        return convertViewDD
    }

    override fun getChildView(
        grouppositionDD: Int,
        childpositionDD: Int,
        isLastChildDD: Boolean,
        convertViewDD: View?,
        parentDD: ViewGroup?
    ): View {
        var convertViewDD = convertViewDD


        if (convertViewDD == null) {
            val inflaterDD =
                contextDD.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewDD = inflaterDD.inflate(R.layout.rv_drop_down_child_item, null)
        }
        val tvDescription = convertViewDD!!.findViewById<TextView>(R.id.textDesc)
        val btnCopyChats = convertViewDD.findViewById<ImageView>(R.id.dropdownButton)
        tvDescription.text = listDD[grouppositionDD].dropdownDescription
        btnCopyChats.setOnClickListener {
            val clipboardDD =
                contextDD.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipFc = ClipData.newPlainText("commands", listDD[grouppositionDD].dropdownDescription)
            clipboardDD.setPrimaryClip(clipFc)
            Toast.makeText(contextDD, "Command Copied", Toast.LENGTH_SHORT).show()
        }
        return convertViewDD
    }

    override fun isChildSelectable(ddp0: Int, ddp1: Int) = false
}
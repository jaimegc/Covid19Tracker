package com.jaimegc.covid19tracker.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.setTextColor
import com.jaimegc.covid19tracker.databinding.ItemPlaceSpinnerBinding
import com.jaimegc.covid19tracker.ui.model.PlaceUI

class PlaceSpinnerAdapter(
    private val context: Context,
    places: MutableList<PlaceUI>
) : BaseAdapter() {

    private val placesSpinner: List<PlaceUI>

    init {
        places.add(0, addSelectPlace(context))
        placesSpinner = places
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: ItemPlaceSpinnerBinding.inflate(
            LayoutInflater.from(parent?.context), parent, false).root

        val binding = ItemPlaceSpinnerBinding.bind(view)

        if (position != 0) {
            binding.placeName.setTextColor(Color.BLACK)
        } else {
            binding.placeName.setTextColor(context, R.color.select_place)
        }

        binding.placeName.text = placesSpinner[position].name

        return binding.root
    }

    override fun getItem(position: Int): PlaceUI = placesSpinner[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = placesSpinner.size

    fun getId(pos: Int): String = placesSpinner[pos].id

    private fun addSelectPlace(context: Context): PlaceUI =
        PlaceUI(id = "", name = context.getString(R.string.select_place), nameEs = "")
}
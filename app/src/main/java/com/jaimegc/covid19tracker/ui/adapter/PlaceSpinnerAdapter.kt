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
    placesSpinner: MutableList<PlaceUI>
) : BaseAdapter() {

    private val places: List<PlaceUI>
    private var currentPosition = 0

    init {
        placesSpinner.add(0, addSelectPlace(context))
        places = placesSpinner
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

        binding.placeName.text = places[position].name

        return binding.root
    }

    override fun getItem(position: Int): PlaceUI = places[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = places.size

    fun getId(pos: Int): String = places[pos].id

    fun saveCurrentPosition(pos: Int) {
        currentPosition = pos
    }

    fun getCurrentPlaceId(): String = places[currentPosition].id

    private fun addSelectPlace(context: Context): PlaceUI =
        PlaceUI(id = "", name = context.getString(R.string.select_all_places), nameEs = "")
}
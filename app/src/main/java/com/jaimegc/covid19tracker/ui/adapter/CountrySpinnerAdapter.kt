package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.jaimegc.covid19tracker.databinding.ItemCountrySpinnerBinding
import com.jaimegc.covid19tracker.common.extensions.setEmojiCountry
import com.jaimegc.covid19tracker.ui.model.CountryUI

class CountrySpinnerAdapter(
    private val countries: List<CountryUI>
) : BaseAdapter() {

    private var currentPosition = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: ItemCountrySpinnerBinding.inflate(
            LayoutInflater.from(parent?.context),
            parent,
            false
        ).root

        val binding = ItemCountrySpinnerBinding.bind(view)

        binding.countryName.text = countries[position].name
        binding.icCountryEmoji.setEmojiCountry(countries[position].code)
        currentPosition = position

        return binding.root
    }

    override fun getItem(position: Int): CountryUI = countries[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = countries.size

    fun getCountryId(pos: Int): String = countries[pos].id

    fun saveCurrentPosition(pos: Int) {
        currentPosition = pos
    }

    fun getCurrentCountryId(): String = countries[currentPosition].id
}
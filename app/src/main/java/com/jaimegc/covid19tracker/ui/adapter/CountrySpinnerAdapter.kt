package com.jaimegc.covid19tracker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.jaimegc.covid19tracker.databinding.ItemCountrySpinnerBinding
import com.jaimegc.covid19tracker.extensions.setEmojiCountry
import com.jaimegc.covid19tracker.ui.model.CountryUI

class CountrySpinnerAdapter(
    private val context: Context,
    private val countries: List<CountryUI>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: ItemCountrySpinnerBinding.inflate(
            LayoutInflater.from(parent?.context), parent, false).root

        val binding = ItemCountrySpinnerBinding.bind(view)

        binding.countryName.text = countries[position].name
        binding.icCountryEmoji.setEmojiCountry(countries[position].code)

        return binding.root
    }

    override fun getItem(position: Int): CountryUI = countries[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = countries.size
}
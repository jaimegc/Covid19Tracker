package com.jaimegc.covid19tracker.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.DialogFragment
import com.jaimegc.covid19tracker.common.extensions.toPx
import com.jaimegc.covid19tracker.databinding.DialogUpdateDatabaseBinding


class DialogUpdateDatabase(
) : DialogFragment() {

    companion object {
        private val TAG = DialogUpdateDatabase::class.java.simpleName
        private val WIDTH = 310.toPx()
        private val HEIGHT = 360.toPx()

        fun newInstance(): DialogUpdateDatabase = DialogUpdateDatabase()
    }

    private var dialogUpdateDatabase: Dialog? = null
    private lateinit var binding: DialogUpdateDatabaseBinding

    override fun setupDialog(dialog: Dialog, style: Int) {
        this.dialogUpdateDatabase = dialog
        binding = DialogUpdateDatabaseBinding.inflate(layoutInflater)
        val contentView = binding.root

        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(ContextCompat.getColor(requireContext(),
            android.R.color.transparent))
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(WIDTH, HEIGHT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialogUpdateDatabase = null
    }

    fun updateInfoStatus(infoStatus: String) {
        if (::binding.isInitialized) binding.text.text = infoStatus
    }

    fun close() {
        dialogUpdateDatabase?.let {
            dismiss().also { dialogUpdateDatabase = null }
        }
    }

    fun open(fragmentManager: FragmentManager) {
        if (dialogUpdateDatabase == null || dialogUpdateDatabase!!.isShowing.not()) show(fragmentManager, TAG)
    }
}
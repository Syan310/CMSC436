package com.addressbook.android.util

import android.app.AlertDialog
import android.content.Context

class DialogUtils {


    /**
     * @param listener           listener to get click event of dialog
     * @param title              title of dialog
     * @param desc               description of dialog
     * @param positiveButtonText text of positive button
     * @param negativeButtonText text of negative
     * @param isCancelable       set true if you want cancelable dialog
     */
    fun showDialog(context: Context, listener: OnDialogClickListener,
        title: String, desc: String?, positiveButtonText: String?,
        negativeButtonText: String?,
        isCancelable: Boolean, position: Int) {
        if (desc == null || desc.isEmpty())
            return
        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(isCancelable)
        dialog.setTitle(title).setMessage(desc)

        if (positiveButtonText != null && positiveButtonText.isNotEmpty())
            dialog.setPositiveButton(positiveButtonText) { dialog, _ ->
                dialog.cancel()
                listener.OnDialogPositiveClick(position)
            }
        else
            dialog.setPositiveButton("", null)
        if (negativeButtonText != null && negativeButtonText.isNotEmpty())
            dialog.setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.cancel()
                listener.OnDialogNegativeClick()
            }
        else {
            dialog.setNegativeButton("", null)
        }

        dialog.create().show()
    }

    interface OnDialogClickListener {
        fun OnDialogPositiveClick(position: Int)

        fun OnDialogNegativeClick()
    }


}
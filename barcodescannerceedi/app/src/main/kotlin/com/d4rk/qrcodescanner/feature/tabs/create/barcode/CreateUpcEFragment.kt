package com.d4rk.qrcodescanner.feature.tabs.create.barcode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.d4rk.qrcodescanner.R
import com.d4rk.qrcodescanner.extension.textString
import com.d4rk.qrcodescanner.feature.tabs.create.BaseCreateBarcodeFragment
import com.d4rk.qrcodescanner.model.schema.Other
import com.d4rk.qrcodescanner.model.schema.Schema
import kotlinx.android.synthetic.main.fragment_create_upc_e.edit_text
class CreateUpcEFragment : BaseCreateBarcodeFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_upc_e, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_text.requestFocus()
        edit_text.addTextChangedListener {
            parentActivity.isCreateBarcodeButtonEnabled = edit_text.text!!.length == 7
        }
    }
    override fun getBarcodeSchema(): Schema {
        return Other(edit_text.textString)
    }
}
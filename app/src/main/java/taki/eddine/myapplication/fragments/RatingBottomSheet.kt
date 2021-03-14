package taki.eddine.myapplication.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hsalf.smileyrating.SmileyRating
import taki.eddine.myapplication.databinding.RatinglayoutBinding


class RatingBottomSheet() : BottomSheetDialogFragment() {
    private var binding: RatinglayoutBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = RatinglayoutBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.smileRating.setSmileySelectedListener { type ->
            when {
                SmileyRating.Type.BAD == type -> {
                    // Toast.makeText(context, "Sorry for the bad experience", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                SmileyRating.Type.GOOD == type -> {
                    //  Toast.makeText(context, "Thank you for rating our app.", Toast.LENGTH_SHORT).show()
                    rateAppOnSite()
                    dismiss()
                }
                SmileyRating.Type.GREAT == type -> {
                   // Toast.makeText(context, "Thank you for rating our app.", Toast.LENGTH_SHORT).show()
                    rateAppOnSite()
                    dismiss()
                }
                SmileyRating.Type.OKAY == type -> {
                  //  Toast.makeText(context, "Thank you for rating our app.", Toast.LENGTH_SHORT).show()
                    rateAppOnSite()
                    dismiss()
                }
                SmileyRating.Type.TERRIBLE == type -> {
                  //  Toast.makeText(context, "Sorry for the bad experience", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }

    private fun rateAppOnSite() {
        val packageName = requireActivity().packageName
        val uri = Uri.parse("market://details?id=$packageName")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), " unable to find market app", Toast.LENGTH_LONG).show()
        }
    }
}
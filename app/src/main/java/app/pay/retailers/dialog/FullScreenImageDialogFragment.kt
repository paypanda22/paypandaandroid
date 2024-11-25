import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import app.pay.retailers.R
import app.pay.retailers.helperclasses.MyGlide
import app.pay.retailers.retrofit.Constant

class FullScreenImageDialogFragment : DialogFragment() {
    private lateinit var imageView: ImageView
    private lateinit var imageUrl: String

    // Inflate the layout and return it
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment
        val view = inflater.inflate(R.layout.dialog_full_screen_image, container, false)

        // Get the image URL from arguments
        imageUrl = arguments?.getString("imageUrl") ?: ""

        imageView = view.findViewById(R.id.fullScreenImageView)

        // Load the image into the ImageView (e.g., using Glide)
        MyGlide.with(requireContext(), Uri.parse(Constant.PIMAGE_URL + imageUrl), imageView)

        // Return the inflated view
        return view
    }

    // Create the dialog and set the inflated view
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Set the view for the dialog (don't call requireView() here)
            val dialogView = onCreateView(LayoutInflater.from(context), null, savedInstanceState)
            builder.setView(dialogView)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}


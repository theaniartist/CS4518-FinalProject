package com.group18.android.reminderapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.group18.android.reminderapplication.model.Card
import java.io.File
import java.util.*

private const val TAG = "CardFragment"
private const val ARG_CARD_ID = "card_id"
private const val ARG_CARD_EMAIL = "card_email"
private const val REQUEST_PHOTO = 0
private const val EXTRA_PHOTO = "com.group18.android.reminderapplication.photo"
private const val EXTRA_TEMPLATE = "com.group18.android.reminderapplication.template"
private const val EXTRA_EMAIL = "com.group18.android.reminderapplication.email"
private const val EXTRA_MESSAGE = "com.group18.android.reminderapplication.message"

class CardFragment : Fragment() {
    private lateinit var card: Card
    private lateinit var photoFile: File
    private var photoUri: Uri? = null
    private lateinit var titleField: TextView
    private lateinit var descField: TextView
    private lateinit var messageField: EditText
    private lateinit var sendButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var eventEmail: String

    private val cardViewModel: CardViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = Card()
        val cardId: UUID = arguments?.getSerializable(ARG_CARD_ID) as UUID
        eventEmail = arguments?.getSerializable(ARG_CARD_EMAIL) as String
        cardViewModel.loadCard(cardId)
        Log.d(TAG, "onCreate() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card, container, false)

        titleField = view.findViewById(R.id.card_title_custom) as TextView
        descField = view.findViewById(R.id.card_description) as TextView
        messageField = view.findViewById(R.id.card_message_edit) as EditText
        sendButton = view.findViewById(R.id.card_send) as Button
        photoButton = view.findViewById(R.id.card_camera) as ImageButton
        photoView = view.findViewById(R.id.card_photo) as ImageView

        Log.d(TAG, "onCreateView() called")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called")
        cardViewModel.cardLiveData.observe(viewLifecycleOwner, { card ->
            card?.let {
                this.card = card
                photoFile = cardViewModel.getPhotoFile(card)

                photoUri = FileProvider.getUriForFile(
                    requireActivity(),
                    "com.group18.android.reminderapplication.fileprovider",
                    photoFile
                )
                updateUI()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        val messageWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                card.message = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {}
        }

        messageField.addTextChangedListener(messageWatcher)

        sendButton.setOnClickListener {
            val intent = Intent(this@CardFragment.context, ChatActivity::class.java).apply {
                //photoFile.path gets path from data/data/[our project name]/files
                putExtra(EXTRA_PHOTO, photoFile.path)
                //card template icon, path from URI and resID: android.resource://[project name]/resID
                putExtra(EXTRA_TEMPLATE, getImageUriPath(card.title))
                putExtra(EXTRA_EMAIL, eventEmail)
                putExtra(EXTRA_MESSAGE, card.message)
            }
            startActivity(intent)
            requireActivity().finish()
        }

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(
                    captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            if (resolvedActivity == null) {
                isEnabled = false
            }
            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(
                        captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PHOTO) {
            requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            updatePhotoView()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
        requireActivity().revokeUriPermission(
            photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }

    private fun updateUI() {
        titleField.text = card.title
        descField.text = card.desc
        updatePhotoView()
    }

    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
    }

    private fun getImageUriPath(cardTitle: String) : String {
        when (cardTitle) {
            "Happy Birthday Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_birthday}
            "Happy Anniversary Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_anniversary}
            "Graduation Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_grad}
            "Christmas Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_christmas}
            "Happy New Year's Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_firework}
            "Happy Mother's Day Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_mom}
            "Happy Father's Day Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_dad}
            "Valentine's Day Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_heart}
            "Wedding Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_ring}
            "Halloween Card" -> { return "android.resource://" +
                    BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_halloween}
        }
        return ""
    }

    companion object {
        fun newInstance(cardId: UUID, cardEmail: String): CardFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CARD_ID, cardId)
                putSerializable(ARG_CARD_EMAIL, cardEmail)
            }
            return CardFragment().apply {
                arguments = args
            }
        }
    }
}
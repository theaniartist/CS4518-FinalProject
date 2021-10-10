package com.group18.android.reminderapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
private const val REQUEST_CONTACT = 0
private const val REQUEST_PHOTO = 1
private const val REQUEST_CODE_CHAT = 2


class CardFragment : Fragment() {

    private lateinit var card: Card
    private lateinit var photoFile: File
    private var photoUri: Uri? = null
    private lateinit var titleField: EditText
    private lateinit var descField: TextView
    private lateinit var messageField: EditText
    private lateinit var contactButton: Button
    private lateinit var sendButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView

    private val cardViewModel: CardViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = Card()
        val cardId: UUID = arguments?.getSerializable(ARG_CARD_ID) as UUID
//        cardViewModel.loadCard(cardId)
        Log.d(TAG, "onCreate() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card, container, false)

        titleField = view.findViewById(R.id.card_title_edit) as EditText
        descField = view.findViewById(R.id.card_description) as TextView
        messageField = view.findViewById(R.id.card_message_edit) as EditText
        contactButton = view.findViewById(R.id.card_contact) as Button
        sendButton = view.findViewById(R.id.card_send) as Button
        photoButton = view.findViewById(R.id.card_camera) as ImageButton
        photoView = view.findViewById(R.id.card_photo) as ImageView

        Log.d(TAG, "onCreateView() called")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called")
//        cardViewModel.cardLiveData.observe(viewLifecycleOwner, { card ->
//            card?.let {
//                this.card = card
//                photoFile = cardViewModel.getPhotoFile(card)
//
//                photoUri = FileProvider.getUriForFile(
//                    requireActivity(),
//                    "com.group18.android.reminderapplication.fileprovider",
//                    photoFile
//                )
//                updateUI()
//            }
//        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                card.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        val messageWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                card.message = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        titleField.addTextChangedListener(titleWatcher)
        messageField.addTextChangedListener(messageWatcher)

        contactButton.apply {
            val pickContactIntent =
                Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
        }

        sendButton.setOnClickListener {
            val intent = Intent(this@CardFragment.context, ChatActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CHAT)
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

        if (requestCode == REQUEST_CONTACT && data != null) {
            val contactUri: Uri? = data.data
            val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

            val cursor = contactUri?.let {
                requireActivity().contentResolver
                    .query(it, queryFields, null, null, null)
            }
            cursor?.use {
                if (it.count == 0) {
                    return
                }

                it.moveToFirst()
                val recipient = it.getString(0)
                card.recipient = recipient
//                cardViewModel.saveCard(card)
                contactButton.text = recipient
            }
        }

        if (requestCode == REQUEST_PHOTO) {
            requireActivity().revokeUriPermission(photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            updatePhotoView()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
//        cardViewModel.saveCard(card)
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }

    private fun updateUI() {
        titleField.setText(card.title)
        descField.text = card.desc
        messageField.setText(card.message)

        if (card.recipient.isNotEmpty()) {
            contactButton.text = card.recipient
        }
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

    companion object {
        fun newInstance(cardId: UUID): CardFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CARD_ID, cardId)
            }
            return CardFragment().apply {
                arguments = args
            }
        }
    }
}
package com.hawwas.ulibrary.ui.display

import android.content.*
import android.net.*
import android.os.*
import android.text.*
import android.util.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.view.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.R
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import com.hawwas.ulibrary.ui.*
import dagger.hilt.android.*
import okio.*
import javax.inject.*

@AndroidEntryPoint
class ItemsSelectorActivity: AppCompatActivity() {

    private lateinit var binding: ActivityItemsSelectorBinding
    @Inject lateinit var remoteRepo: RemoteRepo
    @Inject lateinit var appDataRepo: AppDataRepo
    @Inject lateinit var localStorage: LocalStorage

    private lateinit var selectedSubject: Subject
    private lateinit var selectedCategory: String
    private lateinit var itemsDisplayAdapter: ItemsDisplayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsSelectorBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val extra = intent.getStringExtra("selectedItems")
        try {
            readExtra(extra)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "Error:${e.message}", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        itemsDisplayAdapter = ItemsDisplayAdapter(
            remoteRepo, appDataRepo, this, localStorage, selectedSubject, selectedCategory
        )

        binding.addItemBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.select_a_file)),
                REQUEST_CODE_PICK_FILE
            )

        }
        binding.subjectTitleTv.text = selectedSubject.name
        binding.categoryTitleTv.text = selectedCategory
        binding.itemsRv.adapter = itemsDisplayAdapter
        binding.itemsRv.layoutManager = LinearLayoutManager(this)
    }
    // Handle the result in onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE_PICK_FILE || resultCode != RESULT_OK) return
        if (data == null || data.data == null) return
        val uri = data?.data ?: return
        var fileInfo: FileInfo?
        try {

            fileInfo = localStorage.getFileInfo(uri)
        } catch (e: IOException) {
            Log.d(TAG, "error: (${e.message}): ${uri.path}")
            return
        }
        val existedVersion = selectedSubject.items.firstOrNull { it.version == fileInfo.identifier }
        if (existedVersion == null) {
            showOverrideFileNameDialog(uri, fileInfo)
            return
        }
        if (existedVersion.downloadStatus == DownloadStatus.DOWNLOADED || existedVersion.downloadStatus == DownloadStatus.LOCAL) {
            Toast.makeText(this, getString(R.string.file_already_exists), Toast.LENGTH_SHORT).show()
            return
        }


        val item = Item(
            existedVersion.name,
            getString(R.string.you),
            selectedCategory,
            fileInfo.identifier,
            "",
            selectedSubject.name
        )
        localStorage.copyItem(uri, item) {
            appDataRepo.updateSubject(selectedSubject)
            //    itemsDisplayAdapter.notifyDataSetChanged()
            itemsDisplayAdapter.notifyItemChanged(selectedSubject.items.indexOf(existedVersion))
        }
    }


    private fun readExtra(extras: String?) {
        extras?.split("/")?.let { extra ->
            val selectedSubjectName = extra[0]
            selectedCategory = extra[1]
            selectedSubject = appDataRepo.getSubjectsLive().value!!.firstOrNull {
                it.name == selectedSubjectName
            } ?: throw IllegalArgumentException("Subject not found")
        } ?: throw IllegalArgumentException("Invalid extra")
    }

    override fun onPause() {
        super.onPause()
        localStorage.saveSubjectData(selectedSubject)
    }

    companion object {
        private const val REQUEST_CODE_PICK_FILE = 1
        private const val TAG = "KH_ItemsSelectorActivity"
    }


    private fun showOverrideFileNameDialog(uri: Uri, fileInfo: FileInfo) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.override_file_name))

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.hint = getString(R.string.enter_new_file_name_optional)
        input.setText(fileInfo.name)
        builder.setView(input)
        builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            val newFileName = input.text.toString().trim()
            if (!validFileName(newFileName)) {
                Toast.makeText(this, getString(R.string.invalid_file_name), Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()

                return@setPositiveButton
            }
            val item = Item(
                newFileName,
                getString(R.string.you),
                selectedCategory,
                fileInfo.identifier,
                "",//it's local
                selectedSubject.name
            )
            localStorage.copyItem(uri, item) {
                selectedSubject.items.add(item)
                appDataRepo.updateSubject(selectedSubject)
                itemsDisplayAdapter.notifyItemInserted(selectedSubject.items.indexOf(item))
            }

        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }

        builder.show()
    }


}
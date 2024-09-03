package com.hawwas.ulibrary.ui.display

import android.content.*
import android.util.*
import android.view.*
import androidx.core.content.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import com.hawwas.ulibrary.ui.*
import java.io.*

class ItemsDisplayAdapter(
    private val remoteRepo: RemoteRepo,
    private val appDataRepo: AppDataRepo,
    private val lifecycleOwner: LifecycleOwner,
    private val localStorage: LocalStorage,
    private val selectedSubject: Subject,
    private val selectedCategory: String
): RecyclerView.Adapter<ItemsDisplayAdapter.ViewHolder>() {

    private lateinit var parent: ViewGroup
    private val live = appDataRepo.getSubjectsLive()
    private var items: List<Item> =
        live.value?.find { subject -> subject == selectedSubject }?.items
            ?.filter { item -> item.category == selectedCategory }
            ?: emptyList()

    init {
        live.observe(lifecycleOwner) {
            try {
                items =
                    it.find { subject -> subject == selectedSubject }?.items
                        ?.filter { item -> item.category == selectedCategory }
                        ?: emptyList()
                notifyDataSetChanged()
            } catch (e: IllegalStateException) {
                Log.d(TAG, "race something ")
            }
        }
        appDataRepo.downloadedItem().observe(lifecycleOwner) {
            try {
                val itemName = it.substringAfterLast('/')
                    val foundItem=items.find { item -> item.name == itemName };
                if (foundItem == null) return@observe
                foundItem.downloadStatus = DownloadStatus.DOWNLOADED
                notifyItemInserted(items.indexOf(foundItem))
            } catch (e: IllegalStateException) {
                Log.d(TAG, "race something ")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        this.parent = parent

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, position: Int) {
            binding.apply {
                itemNameTv.text = item.name.substringBefore('.')
                itemAuthorTv.text = item.author
                itemSizeTv.text = if (item.downloadStatus.exists()) {
                    getSize(localStorage.getItemSize(item))
                } else ""
                itemDownloadBtn.setOnClickListener {
                    if (!remoteRepo.downloadItem(item)) {
                        openItem(item, position)
                    } else updateDownloadIcon(item)
                }

                updateDownloadIcon(item)
                updateStarred(item.starred)
                starBtn.setOnClickListener {
                    item.starred = !item.starred
                    updateStarred(item.starred)
                }
                itemPreviewLayout.setOnClickListener { openItem(item, position) }
                itemLayout.setOnClickListener { openItem(item, position) }
                lastWatchedTv.text = getLastWatched(item.lastWatched, this.root.context)
            }
        }

        private fun updateDownloadIcon(item: Item) {
            binding.itemDownloadBtn.setImageResource(
                when (item.downloadStatus) {
                    DownloadStatus.LOCAL -> R.drawable.local_24px
                    DownloadStatus.NOT_STARTED -> R.drawable.download_24px
                    DownloadStatus.DOWNLOADING -> R.drawable.downloading_24px
                    else -> R.drawable.download_done_24px
                }
            )
        }

        private fun openItem(item: Item, position: Int) {
            if (item.downloadStatus.downloadable()) {
                remoteRepo.downloadItem(item)
                return
            }
            item.lastWatched = System.currentTimeMillis()
            val uri = FileProvider.getUriForFile(
                parent.context, "${BuildConfig.APPLICATION_ID}.fileprovider", File(
                    localStorage.getAppDir(), LocalStorage.getItemPath(item)
                )
            )
            val mime = getMIMEType(item.name.substringAfterLast('.'))
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mime)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            notifyItemChanged(position)
            binding.root.context.startActivity(Intent.createChooser(intent, "Open ${item.name}"))
        }

        private fun updateStarred(starred: Boolean) {
            binding.starBtn.setImageResource(
                if (starred) R.drawable.star_on_24px else R.drawable.star_off_24px
            )
        }
    }

    companion object {
        private const val TAG = "KH_ItemsDisplayAdapter"
    }
}

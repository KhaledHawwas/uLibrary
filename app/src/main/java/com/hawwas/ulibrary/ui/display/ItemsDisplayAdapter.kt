package com.hawwas.ulibrary.ui.display

import android.content.*
import android.os.*
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
    private val localStorage: LocalStorage
): RecyclerView.Adapter<ItemsDisplayAdapter.ViewHolder>() {

    private lateinit var parent: ViewGroup
    var items: List<Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        this.parent = parent
        appDataRepo.downloadedItem().observe(lifecycleOwner) {
            try {
                val itemName = it.substringAfterLast('/')
                items.find { item -> item.name == itemName }?.downloaded = DownloadStatus.DOWNLOADED
                notifyDataSetChanged()//TODO: optimize
            } catch (e: IllegalStateException) {
                Log.e(TAG, "onCreateViewHolder: ", e)
            }
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.apply {
                itemNameTv.text = item.name.substringBefore('.')
                itemAuthorTv.text = item.author
                itemSizeTv.text = if (item.downloaded == DownloadStatus.DOWNLOADED) {
                    getSize(localStorage.getItemSize(item))
                } else
                    ""
                itemDownloadBtn.setOnClickListener {
                    if (!remoteRepo.downloadItem(item)) {
                        openItem(item)
                    } else updateDownloadIcon(item)
                }

                updateDownloadIcon(item)
                updateStarred(item.starred)
                starBtn.setOnClickListener {
                    item.starred = !item.starred
                    updateStarred(item.starred)
                }
                itemPreviewLayout.setOnClickListener { openItem(item) }
                itemLayout.setOnClickListener { openItem(item) }
                lastWatchedTv.text = getLastWatched(item.lastWatched, this.root.context)
            }
        }

        private fun updateDownloadIcon(item: Item) {
            binding.itemDownloadBtn.setImageResource(
                when (item.downloaded) {
                    DownloadStatus.NOT_STARTED -> R.drawable.download_24px
                    DownloadStatus.DOWNLOADING -> R.drawable.downloading_24px
                    else -> R.drawable.download_done_24px
                }
            )
        }

        private fun openItem(item: Item) {
            if (item.downloaded != DownloadStatus.DOWNLOADED) {
                remoteRepo.downloadItem(item)
                return
            }
            item.lastWatched = System.currentTimeMillis()
            val uri = FileProvider.getUriForFile(
                parent.context, "${BuildConfig.APPLICATION_ID}.fileprovider", File(
                    Environment.getExternalStorageDirectory(),
                    "Android/media/${BuildConfig.APPLICATION_ID}/${LocalStorage.getItemPath(item)}"
                )
            )

            val mime = getMIMEType(item.name.substringAfterLast('.'))
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mime)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
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

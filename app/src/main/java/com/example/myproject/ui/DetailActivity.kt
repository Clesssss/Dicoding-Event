package com.example.myproject.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myproject.R
import com.example.myproject.data.response.Event
import com.example.myproject.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel



    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideUIElements()

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        Log.d("DetailActivity", "Received Event ID: $eventId")

        val factory = DetailViewModelFactory(eventId.toString())
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        // Observe the event detail LiveData
        detailViewModel.eventDetail.observe(this) { eventDetail ->
            if (eventDetail != null) {
                showUIElements()
                updateUI(eventDetail)
            }
        }
        detailViewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                detailViewModel.clearErrorMessage() // Reset the error message after showing it
            }
        }

        // Observe loading state to show/hide the progress bar
        detailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        binding.button.setOnClickListener {
            detailViewModel.eventDetail.value?.link?.let { link ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Invalid link: $link", Toast.LENGTH_SHORT).show()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun updateUI(eventDetail: Event) {
        binding.tvTitle.text = eventDetail.name
        binding.tvOrganizer.text = buildString {
        append("by ")
        append(eventDetail.ownerName)
        }
        binding.tvDuration.text = buildString {
        append("Starts ")
        append(eventDetail.beginTime)
        }
        binding.tvCategory.text = eventDetail.category
        binding.tvQuota.text = eventDetail.quota?.let { quota ->
            eventDetail.registrants?.let { registrants ->
                "${quota - registrants} quotas left"
            }
        } ?: "Unknown quotas left"
        binding.tvDescription.text = eventDetail.description
        Glide.with(this)
            .load(eventDetail.imageLogo)
            .into(binding.imageView)

    }

    private fun hideUIElements() {
        binding.tvTitle.visibility = View.GONE
        binding.tvOrganizer.visibility = View.GONE
        binding.tvDuration.visibility = View.GONE
        binding.tvCategory.visibility = View.GONE
        binding.tvQuota.visibility = View.GONE
        binding.tvDescription.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.tvDescription2.visibility = View.GONE
    }

    // Shows the UI elements once data is fetched
    private fun showUIElements() {
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvOrganizer.visibility = View.VISIBLE
        binding.tvDuration.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvQuota.visibility = View.VISIBLE
        binding.tvDescription.visibility = View.VISIBLE
        binding.imageView.visibility = View.VISIBLE
        binding.tvDescription2.visibility = View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
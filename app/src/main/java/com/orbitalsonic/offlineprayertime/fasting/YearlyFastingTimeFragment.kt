package com.orbitalsonic.offlineprayertime.fasting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orbitalsonic.offlineprayertime.databinding.FragmentYearlyFastingTimeBinding
import com.orbitalsonic.opt.enums.HighLatitudeAdjustment
import com.orbitalsonic.opt.enums.JuristicMethod
import com.orbitalsonic.opt.enums.OrganizationStandard
import com.orbitalsonic.opt.enums.TimeFormat
import com.orbitalsonic.opt.manager.PrayerTimeManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class YearlyFastingTimeFragment : Fragment() {

    private var _binding: FragmentYearlyFastingTimeBinding? = null
    private val binding get() = _binding!!

    // Sample Latitude & Longitude of Islamabad
    private val latitude: Double = 33.4979105
    private val longitude: Double = 73.0722461

    private val prayerTimeManager = PrayerTimeManager()

    private val logBuilder = StringBuilder()

    // Initialize SimpleDateFormat globally to save memory
    private val dateFormatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYearlyFastingTimeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch and display yearly fasting times
        prayerTimeManager.fetchCurrentYearFastingTimes(
            latitude = latitude,
            longitude = longitude,
            highLatitudeAdjustment = HighLatitudeAdjustment.NONE,
            juristicMethod = JuristicMethod.HANAFI,
            organizationStandard = OrganizationStandard.KARACHI,
            timeFormat = TimeFormat.HOUR_12
        ) { result ->
            result.onSuccess { fastingTimes ->
                logBuilder.appendLine("----Yearly Fasting Times----")
                logBuilder.appendLine("")

                fastingTimes.forEachIndexed { monthIndex, monthlyFastingList ->
                    logBuilder.appendLine("Month ${monthIndex + 1}")
                    logBuilder.appendLine("")

                    monthlyFastingList.forEachIndexed { dayIndex, fastingItem ->
                        val formattedDate = dateFormatter.format(Date(fastingItem.date))

                        logBuilder.appendLine("Day ${dayIndex + 1} - Date: $formattedDate")
                        logBuilder.appendLine("Sehri: ${fastingItem.sehriTime}, Iftar: ${fastingItem.iftaarTime}")
                        logBuilder.appendLine("")

                    }

                    logBuilder.appendLine("")
                }

                updateTextView()
            }.onFailure { exception ->
                logBuilder.appendLine("Error fetching yearly fasting times: ${exception.message}")
                updateTextView()
            }
        }
    }

    private fun updateTextView() {
        binding.mtvFastingTime.text = logBuilder.toString()
        Log.d("PrayerTimeTag", logBuilder.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

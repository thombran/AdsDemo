package cis.gvsu.edu.admobdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.ads.*

/**
 * This is our demo banner ad activity which demonstrates the functionality of a couple
 * different banner types and how they can be displayed on the screen when desired
 * @author Brandon Thomas & Daniel Floyd
 * @version 0.1.0
 */
class BannerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        MobileAds.initialize(this) // Start up mobile ad service for this activity

        val adButton = findViewById<Button>(R.id.adButton)

        // Every time the button is pressed, a new ad should be loaded in the standard banner format
        adButton.setOnClickListener {
            val exAdView = findViewById<AdView>(R.id.exAd)
            exAdView.visibility = VISIBLE
            val adRequest = AdRequest.Builder().build()
            exAdView.loadAd(adRequest)

            exAdView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Toast.makeText(it.context, "The ad loaded successfully!", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Toast.makeText(it.context, "The ad failed to load", Toast.LENGTH_SHORT).show()
                }

                override fun onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                override fun onAdClicked() {
                    Toast.makeText(it.context, "You selected the ad!", Toast.LENGTH_SHORT).show()
                }

                override fun onAdClosed() {
                    Toast.makeText(it.context, "You closed the ad!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val adSmart = findViewById<Button>(R.id.adSmartButton)
        // When this button is pressed, a new "Smart" banner should be loaded each time
        adSmart.setOnClickListener {
            val exAdView = findViewById<AdView>(R.id.exAdSmart)
            exAdView.visibility = VISIBLE
            val adRequest = AdRequest.Builder().build()
            exAdView.loadAd(adRequest)
        }

        // Move to next ad demo screen
        val nextScreen = findViewById<ImageView>(R.id.main2interstitial)
        nextScreen.setOnClickListener {
            val intent = Intent(this, InterstitialActivity::class.java)
            startActivity(intent)
        }
    }
}
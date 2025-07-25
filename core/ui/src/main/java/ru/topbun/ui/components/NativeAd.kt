package ru.topbun.ui.components

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdException
import com.yandex.mobile.ads.nativeads.NativeAdView
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder
import ru.topbun.android.utills.LocationAd.OTHER
import ru.topbun.android.utills.LocationAd.RU
import ru.topbun.android.utills.getLocation
import ru.topbun.ui.R
import ru.topbun.ui.ads.ApplovinNativeAdViewModel
import ru.topbun.ui.ads.YandexNativeAdViewModel

@Composable
fun NativeAd(context: Context) {
    NativeAdApp.Yandex()
    val location = context.getLocation()
    when(location){
        RU -> NativeAdApp.Yandex()
        OTHER -> NativeAdApp.Applovin()
    }
}


sealed interface NativeAdApp {

    object Yandex :NativeAdApp{

        @Composable
        operator fun invoke(viewModel: YandexNativeAdViewModel = viewModel()) {
            val nativeAd by viewModel.nativeAd.collectAsState()

            if (nativeAd != null) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { context ->
                        LayoutInflater.from(context)
                            .inflate(R.layout.yandex_native_ad_container, null) as NativeAdView
                    },
                    update = { adView ->
                        bindAdToView(nativeAd!!, adView)
                    }
                )
            }
        }

        private fun bindAdToView(ad: NativeAd, adView: NativeAdView) {
            val binder = NativeAdViewBinder.Builder(adView)
                .setTitleView(adView.findViewById(R.id.title))
                .setDomainView(adView.findViewById(R.id.domain))
                .setWarningView(adView.findViewById(R.id.warning))
                .setSponsoredView(adView.findViewById(R.id.sponsored))
                .setFeedbackView(adView.findViewById(R.id.feedback))
                .setCallToActionView(adView.findViewById(R.id.call_to_action))
                .setMediaView(adView.findViewById(R.id.media))
                .setIconView(adView.findViewById(R.id.icon))
                .setPriceView(adView.findViewById(R.id.price))
                .setBodyView(adView.findViewById(R.id.body))
                .build()

            try {
                ad.bindNativeAd(binder)

                ad.setNativeAdEventListener(object : NativeAdEventListener {
                    override fun onAdClicked() {
                        Log.d("NativeAd", "Ad clicked")
                    }

                    override fun onLeftApplication() {}

                    override fun onReturnedToApplication() {}

                    override fun onImpression(impressionData: ImpressionData?) {
                        Log.d("NativeAd", "Ad impression")
                    }
                })
            } catch (e: NativeAdException) {
                Log.e("NativeAd", "Error binding ad: ${e.message}")
            }
        }


    }

    object Applovin :NativeAdApp{


        @Composable
        operator fun invoke(viewModel: ApplovinNativeAdViewModel = viewModel()) {

            AndroidView(
                factory = { ctx ->
                    FrameLayout(ctx).apply {
                        viewModel.nativeAdContainerView = this
                        viewModel.createNativeAdLoader()
                        viewModel.loadNativeAd()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

    }

}

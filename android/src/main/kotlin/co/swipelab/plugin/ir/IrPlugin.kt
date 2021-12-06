package co.swipelab.plugin.ir

import android.content.Context
import android.hardware.ConsumerIrManager
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

@RequiresApi(Build.VERSION_CODES.KITKAT)
class IrPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var irManager: ConsumerIrManager

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "ir")
        channel.setMethodCallHandler(this)
        irManager = flutterPluginBinding.applicationContext.getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "hasIrEmitter" -> result.success(irManager.hasIrEmitter())
            "carrierFrequencies" -> result.success(
                    irManager.carrierFrequencies?.fold(mutableListOf<Int>()) { acc, e ->
                        acc.add(e.minFrequency);
                        acc.add(e.maxFrequency);
                        acc
                    }?.toIntArray())
            "transmit" -> result.success(
                    call.argument<Int>("carrierFrequency")?.let { carrierFrequency ->
                        call.argument<IntArray>("pattern")?.let { pattern ->
                            irManager.transmit(carrierFrequency, pattern)
                        }
                    })
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}

import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

class Ir {
  static const MethodChannel _channel = const MethodChannel('ir');

  static Future<bool?> get hasIrEmitter =>
      _channel.invokeMethod('hasIrEmitter');

  static Future<List<int>?> get carrierFrequencies async {
    Int32List? result = await _channel.invokeMethod('carrierFrequencies');
    return result;
  }

  static Future<void> transmit({
    required int carrierFrequency,
    required List<int> pattern,
  }) async {
    await _channel.invokeMethod('transmit', {
      'carrierFrequency': carrierFrequency,
      'pattern': Int32List.fromList(pattern),
    });
  }
}

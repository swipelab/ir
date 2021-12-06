import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

class Ir {
  static const MethodChannel _channel = const OptionalMethodChannel('ir');

  static Future<bool> get hasIrEmitter async =>
      await _channel.invokeMethod<bool>('hasIrEmitter') ?? false;

  static Future<List<int>> get carrierFrequencies async {
    return await _channel.invokeMethod<Int32List>('carrierFrequencies') ?? [];
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

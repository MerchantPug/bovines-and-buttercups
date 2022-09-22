package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.registry.RegistrationProvider;
import com.google.auto.service.AutoService;

@AutoService(RegistrationProvider.Factory.class)
public class FabricRegistrationFactory extends FabriQuiltRegistrationFactory {
}

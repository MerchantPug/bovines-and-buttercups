package com.github.merchantpug.bovinesandbuttercups;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {

	public static final String MOD_ID = "bovinesandbuttercups";
	public static final String MOD_NAME = "Bovines and Buttercups";
	public static final Logger LOG = LogManager.getLogger(MOD_NAME);

	public static ResourceLocation resourceLocation(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
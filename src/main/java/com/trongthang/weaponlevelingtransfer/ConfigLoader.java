package com.trongthang.weaponlevelingtransfer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigLoader {
    private static final String CONFIG_FILE_NAME = "weapon_leveling_transfer.json";
    private static ConfigLoader INSTANCE;

    @Expose
    @SerializedName("startXP")
    public int startXP = 100;

    @Expose
    @SerializedName("xpIncrementPerLevel")
    public int xpIncrementPerLevel = 160;

    @Expose
    @SerializedName("maxLevel")
    public int maxLevel = 500;

    public static void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME);
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                INSTANCE = gson.fromJson(reader, ConfigLoader.class);
                if (INSTANCE == null) {
                    INSTANCE = new ConfigLoader(); // Fallback to default if JSON was empty or malformed
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            INSTANCE = new ConfigLoader();
        }

        saveConfig(gson, configFile); // Save current config, including defaults if they were missing
    }

    private static void saveConfig(Gson gson, File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigLoader getInstance() {
        return INSTANCE;
    }
}

package com.example.customhud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class HudConfig {
    public static final String[] IDS = {"xyz", "nether", "facing", "ping", "tps", "fps", "biome", "speed", "light"};
    public static final String[] NAMES = {"XYZ", "Nether XYZ", "Facing", "Ping", "TPS", "FPS", "Biome", "Speed", "Light Level"};
    public static final String[] DEFAULTS = {"&r&lXYZ: &f{x} {y} {z}", "&r&lNether XYZ: &f{nx} {nz}", "&r&lFacing: &f{facing}", "&r&lPing: &f{ping} ms", "&r&lTPS: &f{tps}", "&r&lFPS: &f{fps}", "&r&lBiome: &f{biome}", "&r&lSpeed: &f{speed}", "&r&lLight: &f{light}"};
    public static Map<String, Entry> entries = new LinkedHashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path file() { return MinecraftClient.getInstance().runDirectory.toPath().resolve("config/custom-hud-gui.json"); }
    public static class Entry { public boolean enabled = true; public String template; public String color = "&f"; public float scale = 1.0f; public String anchor = "TOP_LEFT"; public int offsetX = 6, offsetY = 6; public Entry(String t) { template=t; } }
    public static void load() {
        for (int i=0;i<IDS.length;i++) entries.putIfAbsent(IDS[i], new Entry(DEFAULTS[i]));
        try { Path p=file(); if(Files.exists(p)) { ConfigFile data=GSON.fromJson(Files.readString(p), ConfigFile.class); if(data!=null && data.entries!=null) entries.putAll(data.entries); } } catch(Exception ignored) {}
        for (int i=0;i<IDS.length;i++) if(entries.get(IDS[i])==null) entries.put(IDS[i], new Entry(DEFAULTS[i]));
    }
    public static void save() { try { Files.createDirectories(file().getParent()); Files.writeString(file(), GSON.toJson(new ConfigFile(entries))); } catch(Exception ignored) {} }
    private static class ConfigFile { Map<String,Entry> entries; ConfigFile(Map<String,Entry> e){entries=e;} }
}

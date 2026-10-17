package com.example.customhud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class CustomHudClient implements ClientModInitializer {
    public static KeyBinding OPEN_MENU;
    @Override public void onInitializeClient() {
        HudConfig.load();
        OPEN_MENU = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.custom_hud_gui.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.custom_hud_gui"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU.wasPressed()) client.setScreen(new HudMenuScreen());
        });
        HudRenderCallback.EVENT.register((context, tickDelta) -> HudRenderer.render(context));
    }
}

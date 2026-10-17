package com.example.customhud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class HudMenuScreen extends Screen {
    public HudMenuScreen(){ super(Text.literal("GUI")); }
    @Override protected void init() {
        int left=width/2-150, top=58;
        for(int i=0;i<HudConfig.IDS.length;i++) {
            final int idx=i; final String id=HudConfig.IDS[i]; HudConfig.Entry e=HudConfig.entries.get(id); int y=top+i*23;
            addDrawableChild(ButtonWidget.builder(Text.literal(HudConfig.NAMES[i]), b -> client.setScreen(new HudDetailScreen(id))).dimensions(left,y,190,20).build());
            addDrawableChild(ButtonWidget.builder(Text.literal(e.enabled?"ON":"OFF"), b -> {e.enabled=!e.enabled; HudConfig.save(); client.setScreen(new HudMenuScreen());}).dimensions(left+195,y,48,20).build());
            addDrawableChild(ButtonWidget.builder(Text.literal("Settings"), b -> client.setScreen(new HudDetailScreen(id))).dimensions(left+248,y,52,20).build());
        }
    }
    @Override public void render(DrawContext context,int mouseX,int mouseY,float delta) {
        renderBackground(context); context.drawCenteredTextWithShadow(textRenderer, Text.literal("GUI"), width/2, 18, 0xFFFFFFFF); context.drawCenteredTextWithShadow(textRenderer, Text.literal("CUSTOM HUD SETTINGS"), width/2, 38, 0xFF9FA8B7); super.render(context,mouseX,mouseY,delta);
    }
    @Override public boolean shouldPause(){return false;}
}

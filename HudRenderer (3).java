package com.example.customhud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Formatting;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.text.Text;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import java.util.Locale;

public final class HudRenderer {
    public static void render(DrawContext ctx) {
        MinecraftClient mc=MinecraftClient.getInstance(); if(mc.player==null || mc.world==null) return;
        int sw=ctx.getScaledWindowWidth(), sh=ctx.getScaledWindowHeight(), line=0;
        for(String id:HudConfig.IDS) {
            HudConfig.Entry e=HudConfig.entries.get(id); if(e==null||!e.enabled) continue;
            String s=values(id,e.template,mc); if(e.color!=null && !e.color.isBlank()) s=e.color + s.replaceAll("&[0-9a-fArR]", ""); int width=(int)(mc.textRenderer.getWidth(stripCodes(s))*e.scale), height=(int)(mc.textRenderer.fontHeight*e.scale);
            int x=e.offsetX, y=e.offsetY+line;
            switch(e.anchor) { case "TOP_CENTER" -> x=(sw-width)/2; case "TOP_RIGHT" -> x=sw-width-e.offsetX; case "MIDDLE_LEFT" -> {x=e.offsetX;y=(sh-height)/2+line;} case "MIDDLE_RIGHT" -> {x=sw-width-e.offsetX;y=(sh-height)/2+line;} case "BOTTOM_LEFT" -> y=sh-height-e.offsetY-line; case "BOTTOM_CENTER" -> {x=(sw-width)/2;y=sh-height-e.offsetY-line;} case "BOTTOM_RIGHT" -> {x=sw-width-e.offsetX;y=sh-height-e.offsetY-line;} default -> {} }
            ctx.getMatrices().push(); ctx.getMatrices().scale(e.scale,e.scale,1f); int color=0xFFFFFFFF;
            Text text=legacy(s); ctx.drawTextWithShadow(mc.textRenderer, text, (int)(x/e.scale), (int)(y/e.scale), color); ctx.getMatrices().pop();
            line += Math.max(height+2, 10);
        }
    }
    private static String values(String id,String t,MinecraftClient mc) {
        double x=mc.player.getX(),y=mc.player.getY(),z=mc.player.getZ();
        String facing=mc.player.getHorizontalFacing().getName().toUpperCase(Locale.ROOT);
        int ping=-1; if(mc.getNetworkHandler()!=null) { PlayerListEntry p=mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()); if(p!=null) ping=p.getLatency(); }
        String biome=mc.world.getBiome(mc.player.getBlockPos()).getKey().map(k->k.getValue().toString()).orElse("unknown");
        double speed=Math.sqrt(mc.player.getVelocity().x*mc.player.getVelocity().x+mc.player.getVelocity().z*mc.player.getVelocity().z)*20;
        int light=mc.world.getLightLevel(mc.player.getBlockPos());
        String s=t.replace("{x}",String.valueOf((int)Math.floor(x))).replace("{y}",String.valueOf((int)Math.floor(y))).replace("{z}",String.valueOf((int)Math.floor(z))).replace("{nx}",String.valueOf((int)Math.floor(x/8.0))).replace("{nz}",String.valueOf((int)Math.floor(z/8.0))).replace("{facing}",facing).replace("{ping}",ping<0?"N/A":String.valueOf(ping)).replace("{tps}","N/A").replace("{fps}",String.valueOf(mc.getCurrentFps())).replace("{biome}",biome).replace("{speed}",String.format(Locale.ROOT,"%.2f",speed)).replace("{light}",String.valueOf(light));
        return s;
    }
    private static String stripCodes(String s){return s.replaceAll("&[0-9a-fk-or]", "");}
    private static Text legacy(String s) {
        MutableText out = Text.empty(); StringBuilder part = new StringBuilder(); Style style = Style.EMPTY;
        int start=0;
        if(s.matches("(?s)^#[0-9a-fA-F]{6}.*")) { style=Style.EMPTY.withColor(TextColor.fromRgb(Integer.parseInt(s.substring(1,7),16))); start=7; }
        for (int i=start;i<s.length();i++) {
            char ch=s.charAt(i);
            if(ch=='&' && i+1<s.length()) {
                char code=Character.toLowerCase(s.charAt(i+1)); Formatting f=Formatting.byCode(code);
                if(f!=null) {
                    if(part.length()>0){out.append(Text.literal(part.toString()).setStyle(style));part.setLength(0);}
                    if(f.isColor()) style=Style.EMPTY.withColor(f); else if(f==Formatting.BOLD) style=style.withBold(true); else if(f==Formatting.ITALIC) style=style.withItalic(true); else if(f==Formatting.UNDERLINE) style=style.withUnderline(true); else if(f==Formatting.STRIKETHROUGH) style=style.withStrikethrough(true); else if(f==Formatting.OBFUSCATED) style=style.withObfuscated(true); else if(f==Formatting.RESET) style=Style.EMPTY;
                    i++; continue;
                }
            }
            part.append(ch);
        }
        if(part.length()>0) out.append(Text.literal(part.toString()).setStyle(style));
        return out;
    }
}

package com.example.customhud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class HudDetailScreen extends Screen {
    private final String id; private HudConfig.Entry entry; private TextFieldWidget template, color; private int anchorIndex;
    private static final String[] ANCHORS={"TOP_LEFT","TOP_CENTER","TOP_RIGHT","MIDDLE_LEFT","MIDDLE_RIGHT","BOTTOM_LEFT","BOTTOM_CENTER","BOTTOM_RIGHT"};
    public HudDetailScreen(String id){super(Text.literal("HUD Settings"));this.id=id;}
    @Override protected void init(){
        entry=HudConfig.entries.get(id); anchorIndex=0; for(int i=0;i<ANCHORS.length;i++)if(ANCHORS[i].equals(entry.anchor))anchorIndex=i;
        int x=width/2-150;
        template=new TextFieldWidget(textRenderer,x,65,300,20,Text.literal("Text template")); template.setMaxLength(180); template.setText(entry.template); addDrawableChild(template);
        color=new TextFieldWidget(textRenderer,x,111,300,20,Text.literal("Colour code")); color.setMaxLength(16); color.setText(entry.color); addDrawableChild(color);
        addDrawableChild(ButtonWidget.builder(Text.literal("Anchor: "+ANCHORS[anchorIndex]),b->{anchorIndex=(anchorIndex+1)%ANCHORS.length; b.setMessage(Text.literal("Anchor: "+ANCHORS[anchorIndex])); entry.anchor=ANCHORS[anchorIndex]; HudConfig.save();}).dimensions(x,145,300,20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("- Size"),b->{entry.scale=Math.max(0.5f,entry.scale-0.1f); HudConfig.save(); refreshSizeButtons();}).dimensions(x,174,90,20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Size: "+String.format(java.util.Locale.ROOT,"%.1f",entry.scale)),b->{}).dimensions(x+95,174,110,20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("+ Size"),b->{entry.scale=Math.min(3.0f,entry.scale+0.1f); HudConfig.save(); refreshSizeButtons();}).dimensions(x+210,174,90,20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Save"),b->{entry.template=template.getText();entry.color=color.getText(); if(!entry.template.contains("&")){} HudConfig.save(); client.setScreen(new HudMenuScreen());}).dimensions(x,210,145,20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Back"),b->client.setScreen(new HudMenuScreen())).dimensions(x+155,210,145,20).build());
    }
    private void refreshSizeButtons(){ clearChildren(); init(); }
    @Override public void render(DrawContext c,int mx,int my,float d){renderBackground(c);c.drawCenteredTextWithShadow(textRenderer,Text.literal(HudConfig.NAMES[java.util.Arrays.asList(HudConfig.IDS).indexOf(id)]),width/2,30,0xFFFFFFFF);c.drawTextWithShadow(textRenderer,"Text template (& codes and {tokens})",width/2-150,52,0xFFB8C0CC);c.drawTextWithShadow(textRenderer,"Colour Codes (e.g. &f, &b, #55FFFF)",width/2-150,98,0xFFB8C0CC);c.drawTextWithShadow(textRenderer,"Tokens: {x} {y} {z} {nx} {nz} {facing} {ping} {tps} {fps} {biome} {speed} {light}",Math.max(8,width/2-210),245,0xFFB8C0CC);super.render(c,mx,my,d);}
    @Override public boolean shouldPause(){return false;}
}

/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public abstract class BossEvent
/*     */ {
/*     */   private final UUID id;
/*     */   protected Component name;
/*     */   protected float progress;
/*     */   protected BossBarColor color;
/*     */   protected BossBarOverlay overlay;
/*     */   protected boolean darkenScreen;
/*     */   protected boolean playBossMusic;
/*     */   protected boolean createWorldFog;
/*     */   
/*     */   public BossEvent(UUID id, Component name, BossBarColor color, BossBarOverlay overlay) {
/*  21 */     this.id = id;
/*  22 */     this.name = name;
/*  23 */     this.color = color;
/*  24 */     this.overlay = overlay;
/*  25 */     this.progress = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*  29 */   public UUID getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/*  33 */   public Component getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*  37 */   public void setName(Component name) { this.name = name; }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public float getProgress() { return this.progress; }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public void setProgress(float progress) { this.progress = progress; }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public BossBarColor getColor() { return this.color; }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public void setColor(BossBarColor color) { this.color = color; }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public BossBarOverlay getOverlay() { return this.overlay; }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public void setOverlay(BossBarOverlay overlay) { this.overlay = overlay; }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public boolean shouldDarkenScreen() { return this.darkenScreen; }
/*     */ 
/*     */   
/*     */   public BossEvent setDarkenScreen(boolean darkenScreen) {
/*  69 */     this.darkenScreen = darkenScreen;
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*  74 */   public boolean shouldPlayBossMusic() { return this.playBossMusic; }
/*     */ 
/*     */   
/*     */   public BossEvent setPlayBossMusic(boolean playBossMusic) {
/*  78 */     this.playBossMusic = playBossMusic;
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public BossEvent setCreateWorldFog(boolean createWorldFog) {
/*  83 */     this.createWorldFog = createWorldFog;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   
/*  88 */   public boolean shouldCreateWorldFog() { return this.createWorldFog; }
/*     */   
/*     */   public enum BossBarColor implements StringRepresentable {
/*     */     public static final Codec<BossBarColor> CODEC;
/*  92 */     PINK("pink", ChatFormatting.RED),
/*  93 */     BLUE("blue", ChatFormatting.BLUE),
/*  94 */     RED("red", ChatFormatting.DARK_RED),
/*  95 */     GREEN("green", ChatFormatting.GREEN),
/*  96 */     YELLOW("yellow", ChatFormatting.YELLOW),
/*  97 */     PURPLE("purple", ChatFormatting.DARK_BLUE),
/*  98 */     WHITE("white", ChatFormatting.WHITE);
/*     */     
/*     */     static  {
/* 101 */       CODEC = StringRepresentable.fromEnum(BossBarColor::values);
/*     */     }
/*     */     
/*     */     private final String name;
/*     */     
/*     */     BossBarColor(String name, ChatFormatting formatting) {
/* 107 */       this.name = name;
/* 108 */       this.formatting = formatting;
/*     */     }
/*     */     private final ChatFormatting formatting;
/*     */     
/* 112 */     public ChatFormatting getFormatting() { return this.formatting; }
/*     */ 
/*     */ 
/*     */     
/* 116 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */   
/*     */   public enum BossBarOverlay
/*     */     implements StringRepresentable {
/* 126 */     PROGRESS("progress"),
/* 127 */     NOTCHED_6("notched_6"),
/* 128 */     NOTCHED_10("notched_10"),
/* 129 */     NOTCHED_12("notched_12"),
/* 130 */     NOTCHED_20("notched_20"); public static final Codec<BossBarOverlay> CODEC;
/*     */     
/*     */     static  {
/* 133 */       CODEC = StringRepresentable.fromEnum(BossBarOverlay::values);
/*     */     }
/*     */     
/*     */     private final String name;
/*     */     
/* 138 */     BossBarOverlay(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */     
/* 142 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\BossEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
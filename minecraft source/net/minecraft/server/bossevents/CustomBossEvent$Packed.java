/*     */ package net.minecraft.server.bossevents;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Packed
/*     */   extends Record
/*     */ {
/*     */   private final Component name;
/*     */   private final boolean visible;
/*     */   private final int value;
/*     */   private final int max;
/*     */   private final BossEvent.BossBarColor color;
/*     */   private final BossEvent.BossBarOverlay overlay;
/*     */   private final boolean darkenScreen;
/*     */   private final boolean playBossMusic;
/*     */   private final boolean createWorldFog;
/*     */   private final Set<UUID> players;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #161	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #161	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #161	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 161 */   public Packed(Component name, boolean visible, int value, int max, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playBossMusic, boolean createWorldFog, Set<UUID> players) { this.name = name; this.visible = visible; this.value = value; this.max = max; this.color = color; this.overlay = overlay; this.darkenScreen = darkenScreen; this.playBossMusic = playBossMusic; this.createWorldFog = createWorldFog; this.players = players; } public Component name() { return this.name; } public boolean visible() { return this.visible; } public int value() { return this.value; } public int max() { return this.max; } public BossEvent.BossBarColor color() { return this.color; } public BossEvent.BossBarOverlay overlay() { return this.overlay; } public boolean darkenScreen() { return this.darkenScreen; } public boolean playBossMusic() { return this.playBossMusic; } public boolean createWorldFog() { return this.createWorldFog; } public Set<UUID> players() { return this.players; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(ComponentSerialization.CODEC
/* 174 */         .fieldOf("Name").forGetter(Packed::name), Codec.BOOL
/* 175 */         .optionalFieldOf("Visible", Boolean.valueOf(false)).forGetter(Packed::visible), Codec.INT
/* 176 */         .optionalFieldOf("Value", Integer.valueOf(0)).forGetter(Packed::value), Codec.INT
/* 177 */         .optionalFieldOf("Max", Integer.valueOf(100)).forGetter(Packed::max), BossEvent.BossBarColor.CODEC
/* 178 */         .optionalFieldOf("Color", BossEvent.BossBarColor.WHITE).forGetter(Packed::color), BossEvent.BossBarOverlay.CODEC
/* 179 */         .optionalFieldOf("Overlay", BossEvent.BossBarOverlay.PROGRESS).forGetter(Packed::overlay), Codec.BOOL
/* 180 */         .optionalFieldOf("DarkenScreen", Boolean.valueOf(false)).forGetter(Packed::darkenScreen), Codec.BOOL
/* 181 */         .optionalFieldOf("PlayBossMusic", Boolean.valueOf(false)).forGetter(Packed::playBossMusic), Codec.BOOL
/* 182 */         .optionalFieldOf("CreateWorldFog", Boolean.valueOf(false)).forGetter(Packed::createWorldFog), UUIDUtil.CODEC_SET
/* 183 */         .optionalFieldOf("Players", Set.of()).forGetter(Packed::players))
/* 184 */       .apply(i, Packed::new));
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\bossevents\CustomBossEvent$Packed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.server.bossevents;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class CustomBossEvent extends ServerBossEvent {
/*     */   private static final int DEFAULT_MAX = 100;
/*     */   private final Identifier id;
/*  24 */   private final Set<UUID> players = Sets.newHashSet();
/*     */   private int value;
/*  26 */   private int max = 100;
/*     */   
/*     */   public CustomBossEvent(Identifier id, Component name) {
/*  29 */     super(name, BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
/*  30 */     this.id = id;
/*  31 */     setProgress(0.0F);
/*     */   }
/*     */ 
/*     */   
/*  35 */   public Identifier getTextId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPlayer(ServerPlayer player) {
/*  40 */     super.addPlayer(player);
/*  41 */     this.players.add(player.getUUID());
/*     */   }
/*     */ 
/*     */   
/*  45 */   public void addOfflinePlayer(UUID player) { this.players.add(player); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePlayer(ServerPlayer player) {
/*  50 */     super.removePlayer(player);
/*  51 */     this.players.remove(player.getUUID());
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAllPlayers() {
/*  56 */     super.removeAllPlayers();
/*  57 */     this.players.clear();
/*     */   }
/*     */ 
/*     */   
/*  61 */   public int getValue() { return this.value; }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public int getMax() { return this.max; }
/*     */ 
/*     */   
/*     */   public void setValue(int value) {
/*  69 */     this.value = value;
/*  70 */     setProgress(Mth.clamp(value / this.max, 0.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public void setMax(int max) {
/*  74 */     this.max = max;
/*  75 */     setProgress(Mth.clamp(this.value / max, 0.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public final Component getDisplayName() {
/*  79 */     return ComponentUtils.wrapInSquareBrackets(getName()).withStyle(s -> s
/*  80 */         .withColor(getColor().getFormatting())
/*  81 */         .withHoverEvent(new HoverEvent.ShowText(Component.literal(getTextId().toString())))
/*  82 */         .withInsertion(getTextId().toString()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setPlayers(Collection<ServerPlayer> players) {
/*  87 */     Set<UUID> toRemove = Sets.newHashSet();
/*  88 */     Set<ServerPlayer> toAdd = Sets.newHashSet();
/*     */     
/*  90 */     for (UUID uuid : this.players) {
/*  91 */       boolean found = false;
/*  92 */       for (ServerPlayer player : players) {
/*  93 */         if (player.getUUID().equals(uuid)) {
/*  94 */           found = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*  98 */       if (!found) {
/*  99 */         toRemove.add(uuid);
/*     */       }
/*     */     } 
/*     */     
/* 103 */     for (ServerPlayer player : players) {
/* 104 */       boolean found = false;
/* 105 */       for (UUID uuid : this.players) {
/* 106 */         if (player.getUUID().equals(uuid)) {
/* 107 */           found = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 111 */       if (!found) {
/* 112 */         toAdd.add(player);
/*     */       }
/*     */     } 
/*     */     
/* 116 */     for (UUID uuid : toRemove) {
/* 117 */       for (ServerPlayer player : getPlayers()) {
/* 118 */         if (player.getUUID().equals(uuid)) {
/* 119 */           removePlayer(player);
/*     */           break;
/*     */         } 
/*     */       } 
/* 123 */       this.players.remove(uuid);
/*     */     } 
/*     */     
/* 126 */     for (ServerPlayer player : toAdd) {
/* 127 */       addPlayer(player);
/*     */     }
/*     */     
/* 130 */     return (!toRemove.isEmpty() || !toAdd.isEmpty());
/*     */   }
/*     */   
/*     */   public static CustomBossEvent load(Identifier id, Packed packed) {
/* 134 */     CustomBossEvent event = new CustomBossEvent(id, packed.name);
/* 135 */     event.setVisible(packed.visible);
/* 136 */     event.setValue(packed.value);
/* 137 */     event.setMax(packed.max);
/* 138 */     event.setColor(packed.color);
/* 139 */     event.setOverlay(packed.overlay);
/* 140 */     event.setDarkenScreen(packed.darkenScreen);
/* 141 */     event.setPlayBossMusic(packed.playBossMusic);
/* 142 */     event.setCreateWorldFog(packed.createWorldFog);
/* 143 */     Objects.requireNonNull(event); packed.players.forEach(event::addOfflinePlayer);
/* 144 */     return event;
/*     */   }
/*     */ 
/*     */   
/* 148 */   public Packed pack() { return new Packed(getName(), isVisible(), getValue(), getMax(), getColor(), getOverlay(), shouldDarkenScreen(), shouldPlayBossMusic(), shouldCreateWorldFog(), Set.copyOf(this.players)); }
/*     */ 
/*     */   
/*     */   public void onPlayerConnect(ServerPlayer player) {
/* 152 */     if (this.players.contains(player.getUUID())) {
/* 153 */       addPlayer(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 158 */   public void onPlayerDisconnect(ServerPlayer player) { super.removePlayer(player); }
/*     */   public static final class Packed extends Record { private final Component name; private final boolean visible; private final int value; private final int max; private final BossEvent.BossBarColor color;
/*     */     
/* 161 */     public Packed(Component name, boolean visible, int value, int max, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playBossMusic, boolean createWorldFog, Set<UUID> players) { this.name = name; this.visible = visible; this.value = value; this.max = max; this.color = color; this.overlay = overlay; this.darkenScreen = darkenScreen; this.playBossMusic = playBossMusic; this.createWorldFog = createWorldFog; this.players = players; } private final BossEvent.BossBarOverlay overlay; private final boolean darkenScreen; private final boolean playBossMusic; private final boolean createWorldFog; private final Set<UUID> players; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #161	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #161	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #161	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;
/* 161 */       //   0	8	1	o	Ljava/lang/Object; } public Component name() { return this.name; } public boolean visible() { return this.visible; } public int value() { return this.value; } public int max() { return this.max; } public BossEvent.BossBarColor color() { return this.color; } public BossEvent.BossBarOverlay overlay() { return this.overlay; } public boolean darkenScreen() { return this.darkenScreen; } public boolean playBossMusic() { return this.playBossMusic; } public boolean createWorldFog() { return this.createWorldFog; } public Set<UUID> players() { return this.players; }
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
/* 173 */     public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(ComponentSerialization.CODEC
/* 174 */           .fieldOf("Name").forGetter(Packed::name), Codec.BOOL
/* 175 */           .optionalFieldOf("Visible", Boolean.valueOf(false)).forGetter(Packed::visible), Codec.INT
/* 176 */           .optionalFieldOf("Value", Integer.valueOf(0)).forGetter(Packed::value), Codec.INT
/* 177 */           .optionalFieldOf("Max", Integer.valueOf(100)).forGetter(Packed::max), BossEvent.BossBarColor.CODEC
/* 178 */           .optionalFieldOf("Color", BossEvent.BossBarColor.WHITE).forGetter(Packed::color), BossEvent.BossBarOverlay.CODEC
/* 179 */           .optionalFieldOf("Overlay", BossEvent.BossBarOverlay.PROGRESS).forGetter(Packed::overlay), Codec.BOOL
/* 180 */           .optionalFieldOf("DarkenScreen", Boolean.valueOf(false)).forGetter(Packed::darkenScreen), Codec.BOOL
/* 181 */           .optionalFieldOf("PlayBossMusic", Boolean.valueOf(false)).forGetter(Packed::playBossMusic), Codec.BOOL
/* 182 */           .optionalFieldOf("CreateWorldFog", Boolean.valueOf(false)).forGetter(Packed::createWorldFog), UUIDUtil.CODEC_SET
/* 183 */           .optionalFieldOf("Players", Set.of()).forGetter(Packed::players))
/* 184 */         .apply(i, Packed::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\bossevents\CustomBossEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.player.Abilities;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public static enum GameType
/*     */   implements StringRepresentable
/*     */ {
/*  18 */   SURVIVAL(0, "survival"),
/*  19 */   CREATIVE(1, "creative"),
/*  20 */   ADVENTURE(2, "adventure"),
/*  21 */   SPECTATOR(3, "spectator"); public static final GameType DEFAULT_MODE; public static final StringRepresentable.EnumCodec<GameType> CODEC;
/*     */   
/*     */   static  {
/*  24 */     DEFAULT_MODE = SURVIVAL;
/*     */     
/*  26 */     CODEC = StringRepresentable.fromEnum(GameType::values);
/*     */     
/*  28 */     BY_ID = ByIdMap.continuous(GameType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  29 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, GameType::getId);
/*     */ 
/*     */     
/*  32 */     LEGACY_ID_CODEC = Codec.INT.xmap(GameType::byId, GameType::getId);
/*     */   }
/*     */   private static final IntFunction<GameType> BY_ID; public static final StreamCodec<ByteBuf, GameType> STREAM_CODEC; @Deprecated
/*     */   public static final Codec<GameType> LEGACY_ID_CODEC; private static final int NOT_SET = -1;
/*     */   private final int id;
/*     */   private final String name;
/*     */   private final Component shortName;
/*     */   private final Component longName;
/*     */   
/*     */   GameType(int id, String name) {
/*  42 */     this.id = id;
/*  43 */     this.name = name;
/*  44 */     this.shortName = Component.translatable("selectWorld.gameMode." + name);
/*  45 */     this.longName = Component.translatable("gameMode." + name);
/*     */   }
/*     */ 
/*     */   
/*  49 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public Component getLongDisplayName() { return this.longName; }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public Component getShortDisplayName() { return this.shortName; }
/*     */ 
/*     */   
/*     */   public void updatePlayerAbilities(Abilities abilities) {
/*  70 */     if (this == CREATIVE) {
/*  71 */       abilities.mayfly = true;
/*  72 */       abilities.instabuild = true;
/*  73 */       abilities.invulnerable = true;
/*  74 */     } else if (this == SPECTATOR) {
/*  75 */       abilities.mayfly = true;
/*  76 */       abilities.instabuild = false;
/*  77 */       abilities.invulnerable = true;
/*  78 */       abilities.flying = true;
/*     */     } else {
/*  80 */       abilities.mayfly = false;
/*  81 */       abilities.instabuild = false;
/*  82 */       abilities.invulnerable = false;
/*  83 */       abilities.flying = false;
/*     */     } 
/*  85 */     abilities.mayBuild = !isBlockPlacingRestricted();
/*     */   }
/*     */ 
/*     */   
/*  89 */   public boolean isBlockPlacingRestricted() { return (this == ADVENTURE || this == SPECTATOR); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public boolean isCreative() { return (this == CREATIVE); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public boolean isSurvival() { return (this == SURVIVAL || this == ADVENTURE); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static GameType byId(int id) { return (GameType)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public static GameType byName(String name) { return byName(name, SURVIVAL); }
/*     */ 
/*     */   
/*     */   @Contract("_,!null->!null;_,null->_")
/*     */   public static GameType byName(String name, GameType defaultMode) {
/* 110 */     GameType result = (GameType)CODEC.byName(name);
/* 111 */     return (result != null) ? result : defaultMode;
/*     */   }
/*     */ 
/*     */   
/* 115 */   public static int getNullableId(GameType gameType) { return (gameType != null) ? gameType.id : -1; }
/*     */ 
/*     */   
/*     */   public static GameType byNullableId(int id) {
/* 119 */     if (id == -1) {
/* 120 */       return null;
/*     */     }
/* 122 */     return byId(id);
/*     */   }
/*     */ 
/*     */   
/* 126 */   public static boolean isValidId(int id) { return Arrays.stream(values())
/* 127 */       .anyMatch(gameType -> (gameType.id == id)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\GameType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
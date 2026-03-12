/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ public abstract class Team
/*     */ {
/*     */   public boolean isAlliedTo(Team other) {
/*  19 */     if (other == null) {
/*  20 */       return false;
/*     */     }
/*  22 */     if (this == other) {
/*  23 */       return true;
/*     */     }
/*  25 */     return false;
/*     */   }
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract MutableComponent getFormattedName(Component paramComponent);
/*     */   
/*     */   public abstract boolean canSeeFriendlyInvisibles();
/*     */   
/*     */   public abstract boolean isAllowFriendlyFire();
/*     */   
/*     */   public abstract Visibility getNameTagVisibility();
/*     */   
/*     */   public abstract ChatFormatting getColor();
/*     */   
/*     */   public abstract Collection<String> getPlayers();
/*     */   
/*     */   public abstract Visibility getDeathMessageVisibility();
/*     */   
/*     */   public abstract CollisionRule getCollisionRule();
/*     */   
/*     */   public enum Visibility implements StringRepresentable { public static final Codec<Visibility> CODEC;
/*  47 */     ALWAYS("always", 0),
/*  48 */     NEVER("never", 1),
/*  49 */     HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
/*  50 */     HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3); private static final IntFunction<Visibility> BY_ID;
/*     */     static  {
/*  52 */       CODEC = StringRepresentable.fromEnum(Visibility::values);
/*     */       
/*  54 */       BY_ID = ByIdMap.continuous(v -> v.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  55 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, v -> v.id);
/*     */     }
/*     */     public static final StreamCodec<ByteBuf, Visibility> STREAM_CODEC; public final String name;
/*     */     public final int id;
/*     */     
/*     */     Visibility(String name, int id) {
/*  61 */       this.name = name;
/*  62 */       this.id = id;
/*     */     }
/*     */ 
/*     */     
/*  66 */     public Component getDisplayName() { return Component.translatable("team.visibility." + this.name); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     public String getSerializedName() { return this.name; } }
/*     */ 
/*     */   
/*     */   public enum CollisionRule
/*     */     implements StringRepresentable {
/*  76 */     ALWAYS("always", 0),
/*  77 */     NEVER("never", 1),
/*  78 */     PUSH_OTHER_TEAMS("pushOtherTeams", 2),
/*  79 */     PUSH_OWN_TEAM("pushOwnTeam", 3); public static final Codec<CollisionRule> CODEC; private static final IntFunction<CollisionRule> BY_ID;
/*     */     static  {
/*  81 */       CODEC = StringRepresentable.fromEnum(CollisionRule::values);
/*     */       
/*  83 */       BY_ID = ByIdMap.continuous(r -> r.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  84 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, r -> r.id);
/*     */     }
/*     */     public static final StreamCodec<ByteBuf, CollisionRule> STREAM_CODEC; public final String name;
/*     */     public final int id;
/*     */     
/*     */     CollisionRule(String name, int id) {
/*  90 */       this.name = name;
/*  91 */       this.id = id;
/*     */     }
/*     */ 
/*     */     
/*  95 */     public Component getDisplayName() { return Component.translatable("team.collision." + this.name); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\Team.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
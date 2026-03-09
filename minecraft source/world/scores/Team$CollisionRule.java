/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
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
/*     */ public static enum CollisionRule
/*     */   implements StringRepresentable
/*     */ {
/*     */   public static final Codec<CollisionRule> CODEC;
/*     */   private static final IntFunction<CollisionRule> BY_ID;
/*  76 */   ALWAYS("always", 0),
/*  77 */   NEVER("never", 1),
/*  78 */   PUSH_OTHER_TEAMS("pushOtherTeams", 2),
/*  79 */   PUSH_OWN_TEAM("pushOwnTeam", 3);
/*     */   static  {
/*  81 */     CODEC = StringRepresentable.fromEnum(CollisionRule::values);
/*     */     
/*  83 */     BY_ID = ByIdMap.continuous(r -> r.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  84 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, r -> r.id);
/*     */   }
/*     */   
/*     */   public static final StreamCodec<ByteBuf, CollisionRule> STREAM_CODEC;
/*     */   
/*     */   CollisionRule(String name, int id) {
/*  90 */     this.name = name;
/*  91 */     this.id = id;
/*     */   }
/*     */   public final String name; public final int id;
/*     */   
/*  95 */   public Component getDisplayName() { return Component.translatable("team.collision." + this.name); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public String getSerializedName() { return this.name; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\Team$CollisionRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
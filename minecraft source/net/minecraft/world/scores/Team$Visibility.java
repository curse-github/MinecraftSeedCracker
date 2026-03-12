/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Visibility
/*    */   implements StringRepresentable
/*    */ {
/*    */   public static final Codec<Visibility> CODEC;
/*    */   private static final IntFunction<Visibility> BY_ID;
/* 47 */   ALWAYS("always", 0),
/* 48 */   NEVER("never", 1),
/* 49 */   HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
/* 50 */   HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);
/*    */   static  {
/* 52 */     CODEC = StringRepresentable.fromEnum(Visibility::values);
/*    */     
/* 54 */     BY_ID = ByIdMap.continuous(v -> v.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 55 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, v -> v.id);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Visibility> STREAM_CODEC;
/*    */   
/*    */   Visibility(String name, int id) {
/* 61 */     this.name = name;
/* 62 */     this.id = id;
/*    */   }
/*    */   public final String name; public final int id;
/*    */   
/* 66 */   public Component getDisplayName() { return Component.translatable("team.visibility." + this.name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\Team$Visibility.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
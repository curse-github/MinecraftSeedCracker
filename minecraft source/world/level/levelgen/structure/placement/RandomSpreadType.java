/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum RandomSpreadType implements StringRepresentable {
/*  8 */   LINEAR("linear"),
/*  9 */   TRIANGULAR("triangular"); public static final Codec<RandomSpreadType> CODEC; private final String id;
/*    */   static  {
/* 11 */     CODEC = StringRepresentable.fromEnum(RandomSpreadType::values);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 16 */   RandomSpreadType(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public String getSerializedName() { return this.id; }
/*    */ 
/*    */   
/*    */   public int evaluate(RandomSource random, int limit) {
/* 25 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return (
/*    */       
/* 27 */       random.nextInt(limit) + random.nextInt(limit)) / 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\placement\RandomSpreadType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
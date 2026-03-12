/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum DamageScaling implements StringRepresentable {
/*  7 */   NEVER("never"),
/*  8 */   WHEN_CAUSED_BY_LIVING_NON_PLAYER("when_caused_by_living_non_player"),
/*  9 */   ALWAYS("always");
/*    */   
/*    */   static  {
/* 12 */     CODEC = StringRepresentable.fromEnum(DamageScaling::values);
/*    */   }
/*    */   public static final Codec<DamageScaling> CODEC;
/*    */   private final String id;
/*    */   
/* 17 */   DamageScaling(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\DamageScaling.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
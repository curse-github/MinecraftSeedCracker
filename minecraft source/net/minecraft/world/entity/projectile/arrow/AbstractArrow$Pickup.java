/*    */ package net.minecraft.world.entity.projectile.arrow;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Pickup
/*    */ {
/*    */   public static final Codec<Pickup> LEGACY_CODEC;
/* 73 */   DISALLOWED, ALLOWED, CREATIVE_ONLY;
/*    */   static  {
/* 75 */     LEGACY_CODEC = Codec.BYTE.xmap(Pickup::byOrdinal, p -> Byte.valueOf((byte)p.ordinal()));
/*    */   }
/*    */   public static Pickup byOrdinal(int ordinal) {
/* 78 */     if (ordinal < 0 || ordinal > values().length) {
/* 79 */       ordinal = 0;
/*    */     }
/*    */     
/* 82 */     return values()[ordinal];
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\arrow\AbstractArrow$Pickup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
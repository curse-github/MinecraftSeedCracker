/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum EnchantmentTarget implements StringRepresentable {
/*  7 */   ATTACKER("attacker"),
/*  8 */   DAMAGING_ENTITY("damaging_entity"),
/*  9 */   VICTIM("victim");
/*    */   static  {
/* 11 */     CODEC = StringRepresentable.fromEnum(EnchantmentTarget::values);
/*    */   }
/*    */   public static final Codec<EnchantmentTarget> CODEC;
/*    */   private final String id;
/*    */   
/* 16 */   EnchantmentTarget(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\EnchantmentTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
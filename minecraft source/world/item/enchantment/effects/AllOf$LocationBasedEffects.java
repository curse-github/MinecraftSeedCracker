/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.phys.Vec3;
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
/*    */ public final class LocationBasedEffects
/*    */   extends Record
/*    */   implements EnchantmentLocationBasedEffect
/*    */ {
/*    */   private final List<EnchantmentLocationBasedEffect> effects;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 52 */   public LocationBasedEffects(List<EnchantmentLocationBasedEffect> effects) { this.effects = effects; } public List<EnchantmentLocationBasedEffect> effects() { return this.effects; }
/* 53 */   public static final MapCodec<LocationBasedEffects> CODEC = AllOf.codec(EnchantmentLocationBasedEffect.CODEC, LocationBasedEffects::new, LocationBasedEffects::effects);
/*    */ 
/*    */   
/*    */   public void onChangedBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, boolean becameActive) {
/* 57 */     for (EnchantmentLocationBasedEffect effect : this.effects) {
/* 58 */       effect.onChangedBlock(serverLevel, enchantmentLevel, item, entity, position, becameActive);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 position, int level) {
/* 64 */     for (EnchantmentLocationBasedEffect effect : this.effects) {
/* 65 */       effect.onDeactivated(item, entity, position, level);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public MapCodec<LocationBasedEffects> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\AllOf$LocationBasedEffects.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ApplyExhaustion extends Record implements EnchantmentEntityEffect {
/* 12 */   public ApplyExhaustion(LevelBasedValue amount) { this.amount = amount; } private final LevelBasedValue amount; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ApplyExhaustion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyExhaustion; } public LevelBasedValue amount() { return this.amount; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ApplyExhaustion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyExhaustion; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ApplyExhaustion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyExhaustion;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 15 */   public static final MapCodec<ApplyExhaustion> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 16 */         .fieldOf("amount").forGetter(ApplyExhaustion::amount))
/* 17 */       .apply(i, ApplyExhaustion::new));
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 21 */     if (entity instanceof Player) { Player livingEntity = (Player)entity;
/* 22 */       livingEntity.causeFoodExhaustion(this.amount.calculate(enchantmentLevel)); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<ApplyExhaustion> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\ApplyExhaustion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
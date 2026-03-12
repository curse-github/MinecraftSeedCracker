/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class ChangeItemDamage extends Record implements EnchantmentEntityEffect {
/* 14 */   public ChangeItemDamage(LevelBasedValue amount) { this.amount = amount; } private final LevelBasedValue amount; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage; } public LevelBasedValue amount() { return this.amount; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 17 */   public static final MapCodec<ChangeItemDamage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 18 */         .fieldOf("amount").forGetter(()))
/* 19 */       .apply(i, ChangeItemDamage::new));
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 23 */     ItemStack itemStack = item.itemStack();
/* 24 */     if (itemStack.has(DataComponents.MAX_DAMAGE) && itemStack.has(DataComponents.DAMAGE)) {
/* 25 */       LivingEntity livingEntity = item.owner(); ServerPlayer sp = (ServerPlayer)livingEntity, player = (livingEntity instanceof ServerPlayer) ? sp : null;
/* 26 */       int change = (int)this.amount.calculate(enchantmentLevel);
/* 27 */       itemStack.hurtAndBreak(change, serverLevel, player, item.onBreak());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public MapCodec<ChangeItemDamage> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\ChangeItemDamage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class Ignite extends Record implements EnchantmentEntityEffect {
/* 11 */   public Ignite(LevelBasedValue duration) { this.duration = duration; } private final LevelBasedValue duration; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/Ignite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/Ignite; } public LevelBasedValue duration() { return this.duration; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/Ignite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/Ignite; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/Ignite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/Ignite;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 14 */   public static final MapCodec<Ignite> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 15 */         .fieldOf("duration").forGetter(()))
/* 16 */       .apply(i, Ignite::new));
/*    */ 
/*    */ 
/*    */   
/* 20 */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) { entity.igniteForSeconds(this.duration.calculate(enchantmentLevel)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapCodec<Ignite> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\Ignite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
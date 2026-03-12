/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ApplyEntityImpulse extends Record implements EnchantmentEntityEffect {
/*    */   private final Vec3 direction;
/*    */   private final Vec3 coordinateScale;
/*    */   private final LevelBasedValue magnitude;
/*    */   
/* 12 */   public ApplyEntityImpulse(Vec3 direction, Vec3 coordinateScale, LevelBasedValue magnitude) { this.direction = direction; this.coordinateScale = coordinateScale; this.magnitude = magnitude; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse; } public Vec3 direction() { return this.direction; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Vec3 coordinateScale() { return this.coordinateScale; } public LevelBasedValue magnitude() { return this.magnitude; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final MapCodec<ApplyEntityImpulse> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Vec3.CODEC
/* 18 */         .fieldOf("direction").forGetter(ApplyEntityImpulse::direction), Vec3.CODEC
/* 19 */         .fieldOf("coordinate_scale").forGetter(ApplyEntityImpulse::coordinateScale), LevelBasedValue.CODEC
/* 20 */         .fieldOf("magnitude").forGetter(ApplyEntityImpulse::magnitude))
/* 21 */       .apply(i, ApplyEntityImpulse::new));
/*    */   
/*    */   private static final int POST_IMPULSE_CONTEXT_RESET_GRACE_TIME_TICKS = 10;
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 27 */     Vec3 look = entity.getLookAngle();
/* 28 */     Vec3 direction = look.addLocalCoordinates(this.direction).multiply(this.coordinateScale).scale(this.magnitude.calculate(enchantmentLevel));
/* 29 */     entity.addDeltaMovement(direction);
/* 30 */     entity.hurtMarked = true;
/* 31 */     entity.needsSync = true;
/* 32 */     if (entity instanceof Player) { Player player = (Player)entity;
/* 33 */       player.applyPostImpulseGraceTime(10); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public MapCodec<ApplyEntityImpulse> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\ApplyEntityImpulse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
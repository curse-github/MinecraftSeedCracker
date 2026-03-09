/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ 
/*    */ public class SimpleExplosionDamageCalculator
/*    */   extends ExplosionDamageCalculator
/*    */ {
/*    */   private final boolean explodesBlocks;
/*    */   private final boolean damagesEntities;
/*    */   private final Optional<Float> knockbackMultiplier;
/*    */   private final Optional<HolderSet<Block>> immuneBlocks;
/*    */   
/*    */   public SimpleExplosionDamageCalculator(boolean explodesBlocks, boolean damagesEntities, Optional<Float> knockbackMultiplier, Optional<HolderSet<Block>> immuneBlocks) {
/* 21 */     this.explodesBlocks = explodesBlocks;
/* 22 */     this.damagesEntities = damagesEntities;
/* 23 */     this.knockbackMultiplier = knockbackMultiplier;
/* 24 */     this.immuneBlocks = immuneBlocks;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
/* 29 */     if (this.immuneBlocks.isPresent()) {
/* 30 */       if (block.is((HolderSet)this.immuneBlocks.get())) {
/* 31 */         return Optional.of(Float.valueOf(3600000.0F));
/*    */       }
/* 33 */       return Optional.empty();
/*    */     } 
/* 35 */     return super.getBlockExplosionResistance(explosion, level, pos, block, fluid);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) { return this.explodesBlocks; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public boolean shouldDamageEntity(Explosion explosion, Entity entity) { return this.damagesEntities; }
/*    */   
/*    */   public float getKnockbackMultiplier(Entity entity) { // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: instanceof net/minecraft/world/entity/player/Player
/*    */     //   4: ifeq -> 26
/*    */     //   7: aload_1
/*    */     //   8: checkcast net/minecraft/world/entity/player/Player
/*    */     //   11: astore_3
/*    */     //   12: aload_3
/*    */     //   13: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*    */     //   16: getfield flying : Z
/*    */     //   19: ifeq -> 26
/*    */     //   22: iconst_1
/*    */     //   23: goto -> 27
/*    */     //   26: iconst_0
/*    */     //   27: istore_2
/*    */     //   28: iload_2
/*    */     //   29: ifeq -> 36
/*    */     //   32: fconst_0
/*    */     //   33: goto -> 56
/*    */     //   36: aload_0
/*    */     //   37: getfield knockbackMultiplier : Ljava/util/Optional;
/*    */     //   40: aload_0
/*    */     //   41: aload_1
/*    */     //   42: <illegal opcode> get : (Lnet/minecraft/world/level/SimpleExplosionDamageCalculator;Lnet/minecraft/world/entity/Entity;)Ljava/util/function/Supplier;
/*    */     //   47: invokevirtual orElseGet : (Ljava/util/function/Supplier;)Ljava/lang/Object;
/*    */     //   50: checkcast java/lang/Float
/*    */     //   53: invokevirtual floatValue : ()F
/*    */     //   56: freturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     //   #51	-> 28
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   12	14	3	player	Lnet/minecraft/world/entity/player/Player;
/*    */     //   0	57	0	this	Lnet/minecraft/world/level/SimpleExplosionDamageCalculator;
/*    */     //   0	57	1	entity	Lnet/minecraft/world/entity/Entity;
/*    */     //   28	29	2	creativeFlying	Z }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\SimpleExplosionDamageCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LightningBolt;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LightningBoltPredicate extends Record implements EntitySubPredicate {
/*    */   private final MinMaxBounds.Ints blocksSetOnFire;
/*    */   private final Optional<EntityPredicate> entityStruck;
/*    */   
/* 13 */   public LightningBoltPredicate(MinMaxBounds.Ints blocksSetOnFire, Optional<EntityPredicate> entityStruck) { this.blocksSetOnFire = blocksSetOnFire; this.entityStruck = entityStruck; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/LightningBoltPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/LightningBoltPredicate; } public MinMaxBounds.Ints blocksSetOnFire() { return this.blocksSetOnFire; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/LightningBoltPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/LightningBoltPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/LightningBoltPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/LightningBoltPredicate;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<EntityPredicate> entityStruck() { return this.entityStruck; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final MapCodec<LightningBoltPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(MinMaxBounds.Ints.CODEC
/* 18 */         .optionalFieldOf("blocks_set_on_fire", MinMaxBounds.Ints.ANY).forGetter(LightningBoltPredicate::blocksSetOnFire), EntityPredicate.CODEC
/* 19 */         .optionalFieldOf("entity_struck").forGetter(LightningBoltPredicate::entityStruck))
/* 20 */       .apply(i, LightningBoltPredicate::new));
/*    */ 
/*    */   
/* 23 */   public static LightningBoltPredicate blockSetOnFire(MinMaxBounds.Ints count) { return new LightningBoltPredicate(count, Optional.empty()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<LightningBoltPredicate> codec() { return EntitySubPredicates.LIGHTNING; }
/*    */ 
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/*    */     LightningBolt bolt;
/* 33 */     if (entity instanceof LightningBolt) { bolt = (LightningBolt)entity; }
/* 34 */     else { return false; }
/*    */ 
/*    */     
/* 37 */     return (this.blocksSetOnFire.matches(bolt.getBlocksSetOnFire()) && (this.entityStruck
/* 38 */       .isEmpty() || bolt.getHitEntities().anyMatch(e -> ((EntityPredicate)this.entityStruck.get()).matches(level, position, e))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\LightningBoltPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
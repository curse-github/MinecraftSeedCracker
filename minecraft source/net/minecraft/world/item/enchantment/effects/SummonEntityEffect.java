/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LightningBolt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class SummonEntityEffect extends Record implements EnchantmentEntityEffect {
/*    */   private final HolderSet<EntityType<?>> entityTypes;
/*    */   private final boolean joinTeam;
/*    */   
/* 23 */   public SummonEntityEffect(HolderSet<EntityType<?>> entityTypes, boolean joinTeam) { this.entityTypes = entityTypes; this.joinTeam = joinTeam; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SummonEntityEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SummonEntityEffect; } public HolderSet<EntityType<?>> entityTypes() { return this.entityTypes; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SummonEntityEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SummonEntityEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SummonEntityEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SummonEntityEffect;
/* 23 */     //   0	8	1	o	Ljava/lang/Object; } public boolean joinTeam() { return this.joinTeam; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final MapCodec<SummonEntityEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 28 */         RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(SummonEntityEffect::entityTypes), Codec.BOOL
/* 29 */         .optionalFieldOf("join_team", Boolean.valueOf(false)).forGetter(SummonEntityEffect::joinTeam))
/* 30 */       .apply(i, SummonEntityEffect::new));
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 34 */     BlockPos blockPos = BlockPos.containing(position);
/* 35 */     if (!Level.isInSpawnableBounds(blockPos)) {
/*    */       return;
/*    */     }
/*    */     
/* 39 */     Optional<Holder<EntityType<?>>> entityType = entityTypes().getRandomElement(serverLevel.getRandom());
/* 40 */     if (entityType.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 44 */     Entity spawned = ((EntityType)((Holder)entityType.get()).value()).spawn(serverLevel, blockPos, EntitySpawnReason.TRIGGERED);
/* 45 */     if (spawned == null) {
/*    */       return;
/*    */     }
/*    */     
/* 49 */     if (spawned instanceof LightningBolt) { LightningBolt lightningBolt = (LightningBolt)spawned; LivingEntity livingEntity = item.owner(); if (livingEntity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)livingEntity;
/* 50 */         lightningBolt.setCause(player); }
/*    */        }
/*    */     
/* 53 */     if (this.joinTeam && entity.getTeam() != null) {
/* 54 */       serverLevel.getScoreboard().addPlayerToTeam(spawned.getScoreboardName(), entity.getTeam());
/*    */     }
/*    */     
/* 57 */     spawned.snapTo(position.x, position.y, position.z, spawned.getYRot(), spawned.getXRot());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public MapCodec<SummonEntityEffect> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\SummonEntityEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface EnchantmentEntityEffect
/*    */   extends EnchantmentLocationBasedEffect
/*    */ {
/*    */   static MapCodec<? extends EnchantmentEntityEffect> bootstrap(Registry<MapCodec<? extends EnchantmentEntityEffect>> registry) {
/* 17 */     Registry.register(registry, "all_of", AllOf.EntityEffects.CODEC);
/* 18 */     Registry.register(registry, "apply_mob_effect", ApplyMobEffect.CODEC);
/* 19 */     Registry.register(registry, "change_item_damage", ChangeItemDamage.CODEC);
/* 20 */     Registry.register(registry, "damage_entity", DamageEntity.CODEC);
/* 21 */     Registry.register(registry, "explode", ExplodeEffect.CODEC);
/* 22 */     Registry.register(registry, "ignite", Ignite.CODEC);
/* 23 */     Registry.register(registry, "apply_impulse", ApplyEntityImpulse.CODEC);
/* 24 */     Registry.register(registry, "apply_exhaustion", ApplyExhaustion.CODEC);
/* 25 */     Registry.register(registry, "play_sound", PlaySoundEffect.CODEC);
/* 26 */     Registry.register(registry, "replace_block", ReplaceBlock.CODEC);
/* 27 */     Registry.register(registry, "replace_disk", ReplaceDisk.CODEC);
/* 28 */     Registry.register(registry, "run_function", RunFunction.CODEC);
/* 29 */     Registry.register(registry, "set_block_properties", SetBlockProperties.CODEC);
/* 30 */     Registry.register(registry, "spawn_particles", SpawnParticlesEffect.CODEC);
/* 31 */     return (MapCodec)Registry.register(registry, "summon_entity", SummonEntityEffect.CODEC);
/*    */   }
/*    */   
/* 34 */   public static final Codec<EnchantmentEntityEffect> CODEC = BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE.byNameCodec().dispatch(EnchantmentEntityEffect::codec, Function.identity());
/*    */ 
/*    */   
/*    */   void apply(ServerLevel paramServerLevel, int paramInt, EnchantedItemInUse paramEnchantedItemInUse, Entity paramEntity, Vec3 paramVec3);
/*    */ 
/*    */   
/* 40 */   default void onChangedBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, boolean becameActive) { apply(serverLevel, enchantmentLevel, item, entity, position); }
/*    */   
/*    */   MapCodec<? extends EnchantmentEntityEffect> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\EnchantmentEntityEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
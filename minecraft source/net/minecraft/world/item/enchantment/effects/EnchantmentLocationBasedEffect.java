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
/*    */ 
/*    */ public interface EnchantmentLocationBasedEffect
/*    */ {
/*    */   static MapCodec<? extends EnchantmentLocationBasedEffect> bootstrap(Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> registry) {
/* 17 */     Registry.register(registry, "all_of", AllOf.LocationBasedEffects.CODEC);
/* 18 */     Registry.register(registry, "apply_mob_effect", ApplyMobEffect.CODEC);
/* 19 */     Registry.register(registry, "attribute", EnchantmentAttributeEffect.CODEC);
/* 20 */     Registry.register(registry, "change_item_damage", ChangeItemDamage.CODEC);
/* 21 */     Registry.register(registry, "damage_entity", DamageEntity.CODEC);
/* 22 */     Registry.register(registry, "explode", ExplodeEffect.CODEC);
/* 23 */     Registry.register(registry, "ignite", Ignite.CODEC);
/* 24 */     Registry.register(registry, "apply_impulse", ApplyEntityImpulse.CODEC);
/* 25 */     Registry.register(registry, "apply_exhaustion", ApplyExhaustion.CODEC);
/* 26 */     Registry.register(registry, "play_sound", PlaySoundEffect.CODEC);
/* 27 */     Registry.register(registry, "replace_block", ReplaceBlock.CODEC);
/* 28 */     Registry.register(registry, "replace_disk", ReplaceDisk.CODEC);
/* 29 */     Registry.register(registry, "run_function", RunFunction.CODEC);
/* 30 */     Registry.register(registry, "set_block_properties", SetBlockProperties.CODEC);
/* 31 */     Registry.register(registry, "spawn_particles", SpawnParticlesEffect.CODEC);
/* 32 */     return (MapCodec)Registry.register(registry, "summon_entity", SummonEntityEffect.CODEC);
/*    */   }
/*    */   
/* 35 */   public static final Codec<EnchantmentLocationBasedEffect> CODEC = BuiltInRegistries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE.byNameCodec().dispatch(EnchantmentLocationBasedEffect::codec, Function.identity());
/*    */   
/*    */   void onChangedBlock(ServerLevel paramServerLevel, int paramInt, EnchantedItemInUse paramEnchantedItemInUse, Entity paramEntity, Vec3 paramVec3, boolean paramBoolean);
/*    */   
/*    */   default void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 position, int level) {}
/*    */   
/*    */   MapCodec<? extends EnchantmentLocationBasedEffect> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\EnchantmentLocationBasedEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
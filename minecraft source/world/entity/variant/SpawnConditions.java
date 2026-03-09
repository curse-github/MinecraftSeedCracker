/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class SpawnConditions
/*    */ {
/*    */   public static MapCodec<? extends SpawnCondition> bootstrap(Registry<MapCodec<? extends SpawnCondition>> registry) {
/*  9 */     Registry.register(registry, "structure", StructureCheck.MAP_CODEC);
/* 10 */     Registry.register(registry, "moon_brightness", MoonBrightnessCheck.MAP_CODEC);
/* 11 */     return (MapCodec)Registry.register(registry, "biome", BiomeCheck.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\SpawnConditions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.dimension;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class BuiltinDimensionTypes {
/*  8 */   public static final ResourceKey<DimensionType> OVERWORLD = register("overworld");
/*  9 */   public static final ResourceKey<DimensionType> NETHER = register("the_nether");
/* 10 */   public static final ResourceKey<DimensionType> END = register("the_end");
/* 11 */   public static final ResourceKey<DimensionType> OVERWORLD_CAVES = register("overworld_caves");
/*    */ 
/*    */   
/* 14 */   private static ResourceKey<DimensionType> register(String id) { return ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.withDefaultNamespace(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\BuiltinDimensionTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
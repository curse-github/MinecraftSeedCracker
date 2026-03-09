/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface StructurePoolElementType<P extends StructurePoolElement>
/*    */ {
/*  9 */   public static final StructurePoolElementType<SinglePoolElement> SINGLE = register("single_pool_element", SinglePoolElement.CODEC);
/* 10 */   public static final StructurePoolElementType<ListPoolElement> LIST = register("list_pool_element", ListPoolElement.CODEC);
/* 11 */   public static final StructurePoolElementType<FeaturePoolElement> FEATURE = register("feature_pool_element", FeaturePoolElement.CODEC);
/* 12 */   public static final StructurePoolElementType<EmptyPoolElement> EMPTY = register("empty_pool_element", EmptyPoolElement.CODEC);
/* 13 */   public static final StructurePoolElementType<LegacySinglePoolElement> LEGACY = register("legacy_single_pool_element", LegacySinglePoolElement.CODEC);
/*    */ 
/*    */   
/*    */   MapCodec<P> codec();
/*    */ 
/*    */   
/* 19 */   static <P extends StructurePoolElement> StructurePoolElementType<P> register(String id, MapCodec<P> codec) { return (StructurePoolElementType)Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, id, () -> codec); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\StructurePoolElementType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
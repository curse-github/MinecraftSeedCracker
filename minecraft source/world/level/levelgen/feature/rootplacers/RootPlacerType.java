/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class RootPlacerType<P extends RootPlacer>
/*    */   extends Object {
/*  9 */   public static final RootPlacerType<MangroveRootPlacer> MANGROVE_ROOT_PLACER = register("mangrove_root_placer", MangroveRootPlacer.CODEC);
/*    */   private final MapCodec<P> codec;
/*    */   
/* 12 */   private static <P extends RootPlacer> RootPlacerType<P> register(String name, MapCodec<P> codec) { return (RootPlacerType)Registry.register(BuiltInRegistries.ROOT_PLACER_TYPE, name, new RootPlacerType(codec)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   private RootPlacerType(MapCodec<P> codec) { this.codec = codec; }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<P> codec() { return this.codec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\RootPlacerType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
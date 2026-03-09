/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FluidTags
/*    */ {
/* 11 */   public static final TagKey<Fluid> WATER = create("water");
/* 12 */   public static final TagKey<Fluid> LAVA = create("lava");
/*    */ 
/*    */   
/* 15 */   private static TagKey<Fluid> create(String name) { return TagKey.create(Registries.FLUID, Identifier.withDefaultNamespace(name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\FluidTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
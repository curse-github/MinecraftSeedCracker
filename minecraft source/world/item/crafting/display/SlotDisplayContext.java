/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ import net.minecraft.util.context.ContextMap;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.FuelValues;
/*    */ 
/*    */ public class SlotDisplayContext {
/* 11 */   public static final ContextKey<FuelValues> FUEL_VALUES = ContextKey.vanilla("fuel_values");
/*    */   
/* 13 */   public static final ContextKey<HolderLookup.Provider> REGISTRIES = ContextKey.vanilla("registries");
/*    */   
/* 15 */   public static final ContextKeySet CONTEXT = (new ContextKeySet.Builder())
/* 16 */     .optional(FUEL_VALUES)
/* 17 */     .optional(REGISTRIES)
/* 18 */     .build();
/*    */   
/*    */   public static ContextMap fromLevel(Level level) {
/* 21 */     return (new ContextMap.Builder())
/* 22 */       .withParameter(FUEL_VALUES, level.fuelValues())
/* 23 */       .withParameter(REGISTRIES, level.registryAccess())
/* 24 */       .create(CONTEXT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\SlotDisplayContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
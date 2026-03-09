/*   */ package net.minecraft.world.entity;
/*   */ 
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface EntityProcessor
/*   */ {
/* 7 */   public static final EntityProcessor NOP = input -> input;
/*   */   
/*   */   Entity process(Entity paramEntity);
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
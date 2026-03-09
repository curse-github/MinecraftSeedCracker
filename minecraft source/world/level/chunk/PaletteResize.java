/*   */ package net.minecraft.world.level.chunk;
/*   */ 
/*   */ public interface PaletteResize<T>
/*   */ {
/*   */   int onResize(int paramInt, T paramT);
/*   */   
/*   */   static <T> PaletteResize<T> noResizeExpected() {
/* 8 */     return (bits, lastAddedValue) -> {
/* 9 */         throw new IllegalArgumentException("Unexpected palette resize, bits = " + bits + ", added value = " + String.valueOf(lastAddedValue));
/*   */       };
/*   */   }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\PaletteResize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
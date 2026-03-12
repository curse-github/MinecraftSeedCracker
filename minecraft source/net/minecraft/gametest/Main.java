/*   */ package net.minecraft.gametest;
/*   */ 
/*   */ import net.minecraft.SharedConstants;
/*   */ import net.minecraft.gametest.framework.GameTestMainUtil;
/*   */ 
/*   */ public class Main {
/*   */   public static void main(String[] args) throws Exception {
/* 8 */     SharedConstants.tryDetectVersion();
/* 9 */     GameTestMainUtil.runGameTestServer(args, path -> {
/*   */         
/*   */         });
/*   */   }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\Main.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
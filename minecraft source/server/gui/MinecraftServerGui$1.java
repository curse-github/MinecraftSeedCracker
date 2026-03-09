/*    */ package net.minecraft.server.gui;
/*    */ 
/*    */ import java.awt.event.WindowAdapter;
/*    */ import java.awt.event.WindowEvent;
/*    */ import javax.swing.JFrame;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends WindowAdapter
/*    */ {
/*    */   public void windowClosing(WindowEvent event) {
/* 62 */     if (!this.val$gui.isClosing.getAndSet(true)) {
/* 63 */       frame.setTitle("Minecraft server - shutting down!");
/* 64 */       server.halt(true);
/* 65 */       gui.runFinalizers();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\gui\MinecraftServerGui$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class LevelStorageException extends RuntimeException {
/*    */   private final Component messageComponent;
/*    */   
/*    */   public LevelStorageException(Component message) {
/*  9 */     super(message.getString());
/* 10 */     this.messageComponent = message;
/*    */   }
/*    */ 
/*    */   
/* 14 */   public Component getMessageComponent() { return this.messageComponent; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\LevelStorageException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
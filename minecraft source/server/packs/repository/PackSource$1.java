/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.network.chat.Component;
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
/*    */   implements PackSource
/*    */ {
/* 28 */   public Component decorate(Component packDescription) { return (Component)decorator.apply(packDescription); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public boolean shouldAddAutomatically() { return addAutomatically; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\PackSource$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
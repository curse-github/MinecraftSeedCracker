/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Type
/*    */ {
/* 15 */   private static final Int2ObjectMap<Type> TYPES = new Int2ObjectOpenHashMap();
/*    */   
/*    */   private final int id;
/*    */   
/*    */   public Type(int id) {
/* 20 */     this.id = id;
/* 21 */     TYPES.put(id, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundGameEventPacket$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
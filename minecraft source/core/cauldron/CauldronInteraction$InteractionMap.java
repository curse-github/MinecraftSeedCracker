/*    */ package net.minecraft.core.cauldron;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.world.item.Item;
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
/*    */ public final class InteractionMap
/*    */   extends Record
/*    */ {
/*    */   private final String name;
/*    */   private final Map<Item, CauldronInteraction> map;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 37 */   public InteractionMap(String name, Map<Item, CauldronInteraction> map) { this.name = name; this.map = map; } public String name() { return this.name; } public Map<Item, CauldronInteraction> map() { return this.map; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\cauldron\CauldronInteraction$InteractionMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
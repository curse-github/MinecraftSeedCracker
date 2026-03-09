/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum SideChainPart implements StringRepresentable {
/*  6 */   UNCONNECTED("unconnected"),
/*  7 */   RIGHT("right"),
/*  8 */   CENTER("center"),
/*  9 */   LEFT("left");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 14 */   SideChainPart(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public String toString() { return getSerializedName(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public boolean isConnected() { return (this != UNCONNECTED); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public boolean isConnectionTowards(SideChainPart endPart) { return (this == CENTER || this == endPart); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean isChainEnd() { return (this != CENTER); }
/*    */ 
/*    */   
/*    */   public SideChainPart whenConnectedToTheRight() {
/* 40 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 3: case 1: case 2: break; }  return 
/*    */       
/* 42 */       CENTER;
/*    */   }
/*    */ 
/*    */   
/*    */   public SideChainPart whenConnectedToTheLeft() {
/* 47 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: break; }  return 
/*    */       
/* 49 */       CENTER;
/*    */   }
/*    */ 
/*    */   
/*    */   public SideChainPart whenDisconnectedFromTheRight() {
/* 54 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 3: case 1: case 2: break; }  return 
/*    */       
/* 56 */       RIGHT;
/*    */   }
/*    */ 
/*    */   
/*    */   public SideChainPart whenDisconnectedFromTheLeft() {
/* 61 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: break; }  return 
/*    */       
/* 63 */       LEFT;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\SideChainPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class Target extends Node {
/*  6 */   private float bestHeuristic = Float.MAX_VALUE;
/*    */   
/*    */   private Node bestNode;
/*    */   private boolean reached;
/*    */   
/* 11 */   public Target(Node node) { super(node.x, node.y, node.z); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public Target(int x, int y, int z) { super(x, y, z); }
/*    */ 
/*    */   
/*    */   public void updateBest(float heuristic, Node node) {
/* 19 */     if (heuristic < this.bestHeuristic) {
/* 20 */       this.bestHeuristic = heuristic;
/* 21 */       this.bestNode = node;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 26 */   public Node getBestNode() { return this.bestNode; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void setReached() { this.reached = true; }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public boolean isReached() { return this.reached; }
/*    */ 
/*    */   
/*    */   public static Target createFromStream(FriendlyByteBuf buffer) {
/* 38 */     Target node = new Target(buffer.readInt(), buffer.readInt(), buffer.readInt());
/* 39 */     readContents(buffer, node);
/* 40 */     return node;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\Target.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
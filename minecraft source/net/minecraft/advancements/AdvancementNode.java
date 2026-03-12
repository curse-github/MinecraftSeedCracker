/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class AdvancementNode {
/*    */   private final AdvancementHolder holder;
/*    */   
/*    */   @VisibleForTesting
/*    */   public AdvancementNode(AdvancementHolder holder, AdvancementNode parent) {
/* 12 */     this.children = new ReferenceOpenHashSet();
/*    */ 
/*    */ 
/*    */     
/* 16 */     this.holder = holder;
/* 17 */     this.parent = parent;
/*    */   }
/*    */   private final AdvancementNode parent; private final Set<AdvancementNode> children;
/*    */   
/* 21 */   public Advancement advancement() { return this.holder.value(); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public AdvancementHolder holder() { return this.holder; }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public AdvancementNode parent() { return this.parent; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public AdvancementNode root() { return getRoot(this); }
/*    */ 
/*    */   
/*    */   public static AdvancementNode getRoot(AdvancementNode advancement) {
/* 37 */     AdvancementNode root = advancement;
/*    */     while (true) {
/* 39 */       AdvancementNode parent = root.parent();
/* 40 */       if (parent == null) {
/* 41 */         return root;
/*    */       }
/* 43 */       root = parent;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 48 */   public Iterable<AdvancementNode> children() { return this.children; }
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 53 */   public void addChild(AdvancementNode child) { this.children.add(child); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 58 */     if (this == obj) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (obj instanceof AdvancementNode) { AdvancementNode that = (AdvancementNode)obj; if (this.holder.equals(that.holder)); }  return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public int hashCode() { return this.holder.hashCode(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public String toString() { return this.holder.id().toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\AdvancementNode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
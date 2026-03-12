/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class TreeNodePosition
/*     */ {
/*     */   private final AdvancementNode node;
/*     */   private final TreeNodePosition parent;
/*     */   private final TreeNodePosition previousSibling;
/*     */   private final int childIndex;
/*  13 */   private final List<TreeNodePosition> children = Lists.newArrayList();
/*     */   private TreeNodePosition ancestor;
/*     */   private TreeNodePosition thread;
/*     */   private int x;
/*     */   private float y;
/*     */   private float mod;
/*     */   private float change;
/*     */   private float shift;
/*     */   
/*     */   public TreeNodePosition(AdvancementNode node, TreeNodePosition parent, TreeNodePosition previousSibling, int childIndex, int depth) {
/*  23 */     if (node.advancement().display().isEmpty()) {
/*  24 */       throw new IllegalArgumentException("Can't position an invisible advancement!");
/*     */     }
/*  26 */     this.node = node;
/*  27 */     this.parent = parent;
/*  28 */     this.previousSibling = previousSibling;
/*  29 */     this.childIndex = childIndex;
/*  30 */     this.ancestor = this;
/*  31 */     this.x = depth;
/*  32 */     this.y = -1.0F;
/*     */     
/*  34 */     TreeNodePosition previous = null;
/*  35 */     for (AdvancementNode child : node.children()) {
/*  36 */       previous = addChild(child, previous);
/*     */     }
/*     */   }
/*     */   
/*     */   private TreeNodePosition addChild(AdvancementNode node, TreeNodePosition previous) {
/*  41 */     if (node.advancement().display().isPresent()) {
/*  42 */       previous = new TreeNodePosition(node, this, previous, this.children.size() + 1, this.x + 1);
/*  43 */       this.children.add(previous);
/*     */     } else {
/*  45 */       for (AdvancementNode grandchild : node.children()) {
/*  46 */         previous = addChild(grandchild, previous);
/*     */       }
/*     */     } 
/*  49 */     return previous;
/*     */   }
/*     */   
/*     */   private void firstWalk() {
/*  53 */     if (this.children.isEmpty()) {
/*  54 */       if (this.previousSibling != null) {
/*  55 */         this.previousSibling.y++;
/*     */       } else {
/*  57 */         this.y = 0.0F;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  62 */     TreeNodePosition defaultAncestor = null;
/*  63 */     for (TreeNodePosition child : this.children) {
/*  64 */       child.firstWalk();
/*  65 */       defaultAncestor = child.apportion((defaultAncestor == null) ? child : defaultAncestor);
/*     */     } 
/*  67 */     executeShifts();
/*     */     
/*  69 */     float midpoint = (((TreeNodePosition)this.children.get(0)).y + ((TreeNodePosition)this.children.get(this.children.size() - 1)).y) / 2.0F;
/*  70 */     if (this.previousSibling != null) {
/*  71 */       this.previousSibling.y++;
/*  72 */       this.mod = this.y - midpoint;
/*     */     } else {
/*  74 */       this.y = midpoint;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float secondWalk(float modSum, int depth, float min) {
/*  79 */     this.y += modSum;
/*  80 */     this.x = depth;
/*     */     
/*  82 */     if (this.y < min) {
/*  83 */       min = this.y;
/*     */     }
/*     */     
/*  86 */     for (TreeNodePosition child : this.children) {
/*  87 */       min = child.secondWalk(modSum + this.mod, depth + 1, min);
/*     */     }
/*     */     
/*  90 */     return min;
/*     */   }
/*     */   
/*     */   private void thirdWalk(float offset) {
/*  94 */     this.y += offset;
/*  95 */     for (TreeNodePosition child : this.children) {
/*  96 */       child.thirdWalk(offset);
/*     */     }
/*     */   }
/*     */   
/*     */   private void executeShifts() {
/* 101 */     float shift = 0.0F;
/* 102 */     float change = 0.0F;
/* 103 */     for (int i = this.children.size() - 1; i >= 0; i--) {
/* 104 */       TreeNodePosition child = (TreeNodePosition)this.children.get(i);
/* 105 */       child.y += shift;
/* 106 */       child.mod += shift;
/* 107 */       change += child.change;
/* 108 */       shift += child.shift + change;
/*     */     } 
/*     */   }
/*     */   
/*     */   private TreeNodePosition previousOrThread() {
/* 113 */     if (this.thread != null) {
/* 114 */       return this.thread;
/*     */     }
/* 116 */     if (!this.children.isEmpty()) {
/* 117 */       return (TreeNodePosition)this.children.get(0);
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   private TreeNodePosition nextOrThread() {
/* 123 */     if (this.thread != null) {
/* 124 */       return this.thread;
/*     */     }
/* 126 */     if (!this.children.isEmpty()) {
/* 127 */       return (TreeNodePosition)this.children.get(this.children.size() - 1);
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   private TreeNodePosition apportion(TreeNodePosition defaultAncestor) {
/* 133 */     if (this.previousSibling == null) {
/* 134 */       return defaultAncestor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 139 */     TreeNodePosition vir = this;
/* 140 */     TreeNodePosition vor = this;
/* 141 */     TreeNodePosition vil = this.previousSibling;
/* 142 */     TreeNodePosition vol = (TreeNodePosition)this.parent.children.get(0);
/* 143 */     float sir = this.mod;
/* 144 */     float sor = this.mod;
/* 145 */     float sil = vil.mod;
/* 146 */     float sol = vol.mod;
/*     */     
/* 148 */     while (vil.nextOrThread() != null && vir.previousOrThread() != null) {
/* 149 */       vil = vil.nextOrThread();
/* 150 */       vir = vir.previousOrThread();
/* 151 */       vol = vol.previousOrThread();
/* 152 */       vor = vor.nextOrThread();
/* 153 */       vor.ancestor = this;
/* 154 */       float shift = vil.y + sil - vir.y + sir + 1.0F;
/* 155 */       if (shift > 0.0F) {
/* 156 */         vil.getAncestor(this, defaultAncestor).moveSubtree(this, shift);
/* 157 */         sir += shift;
/* 158 */         sor += shift;
/*     */       } 
/* 160 */       sil += vil.mod;
/* 161 */       sir += vir.mod;
/* 162 */       sol += vol.mod;
/* 163 */       sor += vor.mod;
/*     */     } 
/* 165 */     if (vil.nextOrThread() != null && vor.nextOrThread() == null) {
/* 166 */       vor.thread = vil.nextOrThread();
/* 167 */       vor.mod += sil - sor;
/*     */     } else {
/* 169 */       if (vir.previousOrThread() != null && vol.previousOrThread() == null) {
/* 170 */         vol.thread = vir.previousOrThread();
/* 171 */         vol.mod += sir - sol;
/*     */       } 
/* 173 */       defaultAncestor = this;
/*     */     } 
/*     */     
/* 176 */     return defaultAncestor;
/*     */   }
/*     */   
/*     */   private void moveSubtree(TreeNodePosition right, float shift) {
/* 180 */     float subtrees = (right.childIndex - this.childIndex);
/* 181 */     if (subtrees != 0.0F) {
/* 182 */       right.change -= shift / subtrees;
/* 183 */       this.change += shift / subtrees;
/*     */     } 
/* 185 */     right.shift += shift;
/* 186 */     right.y += shift;
/* 187 */     right.mod += shift;
/*     */   }
/*     */ 
/*     */   
/*     */   private TreeNodePosition getAncestor(TreeNodePosition other, TreeNodePosition defaultAncestor) {
/* 192 */     if (this.ancestor != null && other.parent.children.contains(this.ancestor)) {
/* 193 */       return this.ancestor;
/*     */     }
/* 195 */     return defaultAncestor;
/*     */   }
/*     */ 
/*     */   
/*     */   private void finalizePosition() {
/* 200 */     this.node.advancement().display().ifPresent(display -> display.setLocation(this.x, this.y));
/*     */     
/* 202 */     if (!this.children.isEmpty()) {
/* 203 */       for (TreeNodePosition child : this.children) {
/* 204 */         child.finalizePosition();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void run(AdvancementNode node) {
/* 210 */     if (node.advancement().display().isEmpty()) {
/* 211 */       throw new IllegalArgumentException("Can't position children of an invisible root!");
/*     */     }
/* 213 */     TreeNodePosition root = new TreeNodePosition(node, null, null, 1, 0);
/* 214 */     root.firstWalk();
/* 215 */     float min = root.secondWalk(0.0F, 0, root.y);
/* 216 */     if (min < 0.0F) {
/* 217 */       root.thirdWalk(-min);
/*     */     }
/* 219 */     root.finalizePosition();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\TreeNodePosition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
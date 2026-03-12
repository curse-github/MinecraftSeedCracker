/*    */ package net.minecraft.server.advancements;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.Stack;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementNode;
/*    */ import net.minecraft.advancements.DisplayInfo;
/*    */ 
/*    */ public class AdvancementVisibilityEvaluator
/*    */ {
/*    */   private static final int VISIBILITY_DEPTH = 2;
/*    */   
/*    */   private enum VisibilityRule {
/* 16 */     SHOW,
/* 17 */     HIDE,
/* 18 */     NO_CHANGE;
/*    */   }
/*    */   
/*    */   private static VisibilityRule evaluateVisibilityRule(Advancement advancement, boolean isDone) {
/* 22 */     Optional<DisplayInfo> display = advancement.display();
/* 23 */     if (display.isEmpty()) {
/* 24 */       return VisibilityRule.HIDE;
/*    */     }
/* 26 */     if (isDone) {
/* 27 */       return VisibilityRule.SHOW;
/*    */     }
/* 29 */     if (((DisplayInfo)display.get()).isHidden()) {
/* 30 */       return VisibilityRule.HIDE;
/*    */     }
/* 32 */     return VisibilityRule.NO_CHANGE;
/*    */   }
/*    */   
/*    */   private static boolean evaluateVisiblityForUnfinishedNode(Stack<VisibilityRule> ascendants) {
/* 36 */     for (int i = 0; i <= 2; i++) {
/* 37 */       VisibilityRule visibility = (VisibilityRule)ascendants.peek(i);
/* 38 */       if (visibility == VisibilityRule.SHOW)
/* 39 */         return true; 
/* 40 */       if (visibility == VisibilityRule.HIDE) {
/* 41 */         return false;
/*    */       }
/*    */     } 
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean evaluateVisibility(AdvancementNode node, Stack<VisibilityRule> ascendants, Predicate<AdvancementNode> isDoneTest, Output output) {
/* 48 */     boolean isSelfDone = isDoneTest.test(node);
/* 49 */     VisibilityRule descendantVisibility = evaluateVisibilityRule(node.advancement(), isSelfDone);
/*    */     
/* 51 */     boolean isSelfOrDescendantDone = isSelfDone;
/* 52 */     ascendants.push(descendantVisibility);
/* 53 */     for (AdvancementNode child : node.children()) {
/* 54 */       isSelfOrDescendantDone |= evaluateVisibility(child, ascendants, isDoneTest, output);
/*    */     }
/*    */     
/* 57 */     boolean visiblity = (isSelfOrDescendantDone || evaluateVisiblityForUnfinishedNode(ascendants));
/* 58 */     ascendants.pop();
/*    */     
/* 60 */     output.accept(node, visiblity);
/* 61 */     return isSelfOrDescendantDone;
/*    */   }
/*    */   
/*    */   public static void evaluateVisibility(AdvancementNode node, Predicate<AdvancementNode> isDone, Output output) {
/* 65 */     AdvancementNode root = node.root();
/*    */     
/* 67 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/*    */     
/* 69 */     for (int i = 0; i <= 2; i++) {
/* 70 */       objectArrayList.push(VisibilityRule.NO_CHANGE);
/*    */     }
/* 72 */     evaluateVisibility(root, objectArrayList, isDone, output);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Output {
/*    */     void accept(AdvancementNode param1AdvancementNode, boolean param1Boolean);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\advancements\AdvancementVisibilityEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
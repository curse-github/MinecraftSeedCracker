/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ProblemTreeNode
/*     */   extends Record
/*     */ {
/*     */   private final ProblemReporter.PathElement element;
/*     */   private final List<ProblemReporter.Problem> problems;
/*     */   private final Map<ProblemReporter.PathElement, ProblemTreeNode> children;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #180	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #180	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #180	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 180 */   private ProblemTreeNode(ProblemReporter.PathElement element, List<ProblemReporter.Problem> problems, Map<ProblemReporter.PathElement, ProblemTreeNode> children) { this.element = element; this.problems = problems; this.children = children; } public ProblemReporter.PathElement element() { return this.element; } public List<ProblemReporter.Problem> problems() { return this.problems; } public Map<ProblemReporter.PathElement, ProblemTreeNode> children() { return this.children; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public ProblemTreeNode(ProblemReporter.PathElement pathElement) { this(pathElement, new ArrayList(), new LinkedHashMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public ProblemTreeNode child(ProblemReporter.PathElement id) { return (ProblemTreeNode)this.children.computeIfAbsent(id, ProblemTreeNode::new); }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getLines() {
/* 199 */     int problemCount = this.problems.size();
/* 200 */     int childrenCount = this.children.size();
/*     */     
/* 202 */     if (problemCount == 0 && childrenCount == 0) {
/* 203 */       return List.of();
/*     */     }
/* 205 */     if (problemCount == 0 && childrenCount == 1) {
/* 206 */       List<String> lines = new ArrayList<String>();
/* 207 */       this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 208 */       lines.set(0, this.element.get() + this.element.get());
/* 209 */       return lines;
/*     */     } 
/* 211 */     if (problemCount == 1 && childrenCount == 0) {
/* 212 */       return List.of(this.element.get() + ": " + this.element.get());
/*     */     }
/*     */     
/* 215 */     List<String> lines = new ArrayList<String>();
/* 216 */     this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 217 */     lines.replaceAll(s -> "  " + s);
/*     */     
/* 219 */     for (ProblemReporter.Problem problem : this.problems) {
/* 220 */       lines.add("  " + problem.description());
/*     */     }
/*     */     
/* 223 */     lines.addFirst(this.element.get() + ":");
/* 224 */     return lines;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ProblemReporter$Collector$ProblemTreeNode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
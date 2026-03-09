/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface ProblemReporter {
/*  19 */   public static final ProblemReporter DISCARDING = new ProblemReporter()
/*     */     {
/*     */       public ProblemReporter forChild(PathElement path) {
/*  22 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public void report(Problem problem) {}
/*     */     };
/*     */ 
/*     */   
/*     */   ProblemReporter forChild(PathElement paramPathElement);
/*     */ 
/*     */   
/*     */   void report(Problem paramProblem);
/*     */ 
/*     */   
/*     */   public static final class RootFieldPathElement
/*     */     extends Record
/*     */     implements PathElement
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */     
/*  43 */     public RootFieldPathElement(String name) { this.name = name; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootFieldPathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootFieldPathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;
/*  43 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; }
/*     */ 
/*     */     
/*  46 */     public String get() { return this.name; } }
/*     */   
/*     */   public static final class RootElementPathElement extends Record implements PathElement { private final ResourceKey<?> id;
/*     */     
/*  50 */     public RootElementPathElement(ResourceKey<?> id) { this.id = id; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement;
/*  50 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<?> id() { return this.id; }
/*     */ 
/*     */     
/*  53 */     public String get() { return "{" + String.valueOf(this.id.identifier()) + "@" + String.valueOf(this.id.registry()) + "}"; } }
/*     */   
/*     */   public static final class FieldPathElement extends Record implements PathElement { private final String name;
/*     */     
/*  57 */     public FieldPathElement(String name) { this.name = name; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$FieldPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$FieldPathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$FieldPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$FieldPathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$FieldPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$FieldPathElement;
/*  57 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; }
/*     */ 
/*     */     
/*  60 */     public String get() { return "." + this.name; } }
/*     */   public static final class IndexedFieldPathElement extends Record implements PathElement { private final String name;
/*     */     private final int index;
/*     */     
/*  64 */     public IndexedFieldPathElement(String name, int index) { this.name = name; this.index = index; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;
/*  64 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public int index() { return this.index; }
/*     */ 
/*     */     
/*  67 */     public String get() { return "." + this.name + "[" + this.index + "]"; } }
/*     */   
/*     */   public static final class IndexedPathElement extends Record implements PathElement { private final int index;
/*     */     
/*  71 */     public IndexedPathElement(int index) { this.index = index; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$IndexedPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedPathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$IndexedPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedPathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$IndexedPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$IndexedPathElement;
/*  71 */       //   0	8	1	o	Ljava/lang/Object; } public int index() { return this.index; }
/*     */ 
/*     */     
/*  74 */     public String get() { return "[" + this.index + "]"; } }
/*     */   
/*     */   public static final class ElementReferencePathElement extends Record implements PathElement { private final ResourceKey<?> id;
/*     */     
/*  78 */     public ElementReferencePathElement(ResourceKey<?> id) { this.id = id; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;
/*  78 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<?> id() { return this.id; }
/*     */ 
/*     */     
/*  81 */     public String get() { return "->{" + String.valueOf(this.id.identifier()) + "@" + String.valueOf(this.id.registry()) + "}"; } }
/*     */   
/*     */   public static class Collector implements ProblemReporter { private static final class Entry extends Record { private final ProblemReporter.Collector source;
/*     */       private final ProblemReporter.Problem problem;
/*     */       
/*  86 */       private Entry(ProblemReporter.Collector source, ProblemReporter.Problem problem) { this.source = source; this.problem = problem; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #86	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #86	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #86	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*  86 */         //   0	8	1	o	Ljava/lang/Object; } public ProblemReporter.Collector source() { return this.source; } public ProblemReporter.Problem problem() { return this.problem; } }
/*     */     
/*  88 */     public static final ProblemReporter.PathElement EMPTY_ROOT = () -> "";
/*     */     
/*     */     private final Collector parent;
/*     */     
/*     */     private final ProblemReporter.PathElement element;
/*     */     
/*     */     private final Set<Entry> problems;
/*     */ 
/*     */     
/*  97 */     public Collector() { this(EMPTY_ROOT); }
/*     */ 
/*     */     
/*     */     public Collector(ProblemReporter.PathElement root) {
/* 101 */       this.parent = null;
/* 102 */       this.problems = new LinkedHashSet();
/* 103 */       this.element = root;
/*     */     }
/*     */     
/*     */     private Collector(Collector parent, ProblemReporter.PathElement path) {
/* 107 */       this.problems = parent.problems;
/* 108 */       this.parent = parent;
/* 109 */       this.element = path;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 114 */     public ProblemReporter forChild(ProblemReporter.PathElement path) { return new Collector(this, path); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     public void report(ProblemReporter.Problem problem) { this.problems.add(new Entry(this, problem)); }
/*     */ 
/*     */ 
/*     */     
/* 123 */     public boolean isEmpty() { return this.problems.isEmpty(); }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<String, ProblemReporter.Problem> output) {
/* 127 */       List<ProblemReporter.PathElement> pathElements = new ArrayList<ProblemReporter.PathElement>();
/* 128 */       StringBuilder pathString = new StringBuilder();
/*     */       
/* 130 */       for (Entry entry : this.problems) {
/* 131 */         Collector current = entry.source;
/* 132 */         while (current != null) {
/* 133 */           pathElements.add(current.element);
/* 134 */           current = current.parent;
/*     */         } 
/*     */         
/* 137 */         for (int i = pathElements.size() - 1; i >= 0; i--) {
/* 138 */           pathString.append(((ProblemReporter.PathElement)pathElements.get(i)).get());
/*     */         }
/* 140 */         output.accept(pathString.toString(), entry.problem());
/*     */         
/* 142 */         pathString.setLength(0);
/* 143 */         pathElements.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getReport() {
/* 148 */       HashMultimap hashMultimap = HashMultimap.create();
/* 149 */       Objects.requireNonNull(hashMultimap); forEach(hashMultimap::put);
/*     */       
/* 151 */       return (String)hashMultimap.asMap().entrySet().stream()
/* 152 */         .map(entry -> " at " + (String)entry.getKey() + ": " + (String)((Collection)entry.getValue()).stream().map(ProblemReporter.Problem::description).collect(Collectors.joining("; ")))
/* 153 */         .collect(Collectors.joining("\n"));
/*     */     }
/*     */     
/*     */     public String getTreeReport() {
/* 157 */       List<ProblemReporter.PathElement> pathElements = new ArrayList<ProblemReporter.PathElement>();
/*     */       
/* 159 */       ProblemTreeNode root = new ProblemTreeNode(this.element);
/*     */       
/* 161 */       for (Entry entry : this.problems) {
/* 162 */         Collector current = entry.source;
/* 163 */         while (current != this) {
/* 164 */           pathElements.add(current.element);
/* 165 */           current = current.parent;
/*     */         } 
/*     */         
/* 168 */         ProblemTreeNode node = root;
/* 169 */         for (int i = pathElements.size() - 1; i >= 0; i--) {
/* 170 */           node = node.child((ProblemReporter.PathElement)pathElements.get(i));
/*     */         }
/* 172 */         pathElements.clear();
/*     */         
/* 174 */         node.problems.add(entry.problem);
/*     */       } 
/*     */       
/* 177 */       return String.join("\n", root.getLines());
/*     */     }
/*     */     private static final class ProblemTreeNode extends Record { private final ProblemReporter.PathElement element; private final List<ProblemReporter.Problem> problems; private final Map<ProblemReporter.PathElement, ProblemTreeNode> children;
/* 180 */       private ProblemTreeNode(ProblemReporter.PathElement element, List<ProblemReporter.Problem> problems, Map<ProblemReporter.PathElement, ProblemTreeNode> children) { this.element = element; this.problems = problems; this.children = children; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #180	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #180	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #180	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/* 180 */         //   0	8	1	o	Ljava/lang/Object; } public ProblemReporter.PathElement element() { return this.element; } public List<ProblemReporter.Problem> problems() { return this.problems; } public Map<ProblemReporter.PathElement, ProblemTreeNode> children() { return this.children; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 186 */       public ProblemTreeNode(ProblemReporter.PathElement pathElement) { this(pathElement, new ArrayList(), new LinkedHashMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 194 */       public ProblemTreeNode child(ProblemReporter.PathElement id) { return (ProblemTreeNode)this.children.computeIfAbsent(id, ProblemTreeNode::new); }
/*     */ 
/*     */       
/*     */       public List<String> getLines()
/*     */       {
/* 199 */         int problemCount = this.problems.size();
/* 200 */         int childrenCount = this.children.size();
/*     */         
/* 202 */         if (problemCount == 0 && childrenCount == 0) {
/* 203 */           return List.of();
/*     */         }
/* 205 */         if (problemCount == 0 && childrenCount == 1) {
/* 206 */           List<String> lines = new ArrayList<String>();
/* 207 */           this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 208 */           lines.set(0, this.element.get() + this.element.get());
/* 209 */           return lines;
/*     */         } 
/* 211 */         if (problemCount == 1 && childrenCount == 0) {
/* 212 */           return List.of(this.element.get() + ": " + this.element.get());
/*     */         }
/*     */         
/* 215 */         List<String> lines = new ArrayList<String>();
/* 216 */         this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 217 */         lines.replaceAll(s -> "  " + s);
/*     */         
/* 219 */         for (ProblemReporter.Problem problem : this.problems) {
/* 220 */           lines.add("  " + problem.description());
/*     */         }
/*     */         
/* 223 */         lines.addFirst(this.element.get() + ":");
/* 224 */         return lines; } } } private static final class Entry extends Record { private final ProblemReporter.Collector source; private final ProblemReporter.Problem problem; private Entry(ProblemReporter.Collector source, ProblemReporter.Problem problem) { this.source = source; this.problem = problem; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/* 224 */       //   0	8	1	o	Ljava/lang/Object; } public ProblemReporter.Collector source() { return this.source; } public ProblemReporter.Problem problem() { return this.problem; } } private static final class ProblemTreeNode extends Record { private final ProblemReporter.PathElement element; private final List<ProblemReporter.Problem> problems; private final Map<ProblemReporter.PathElement, ProblemTreeNode> children; public List<String> getLines() { int problemCount = this.problems.size(); int childrenCount = this.children.size(); if (problemCount == 0 && childrenCount == 0) return List.of();  if (problemCount == 0 && childrenCount == 1) { List<String> lines = new ArrayList<String>(); this.children.forEach((element, child) -> lines.addAll(child.getLines())); lines.set(0, this.element.get() + this.element.get()); return lines; }  if (problemCount == 1 && childrenCount == 0) return List.of(this.element.get() + ": " + this.element.get());  List<String> lines = new ArrayList<String>(); this.children.forEach((element, child) -> lines.addAll(child.getLines())); lines.replaceAll(s -> "  " + s); for (ProblemReporter.Problem problem : this.problems) lines.add("  " + problem.description());  lines.addFirst(this.element.get() + ":"); return lines; }
/*     */      private ProblemTreeNode(ProblemReporter.PathElement element, List<ProblemReporter.Problem> problems, Map<ProblemReporter.PathElement, ProblemTreeNode> children) { this.element = element;
/*     */       this.problems = problems;
/*     */       this.children = children; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public ProblemReporter.PathElement element() { return this.element; }
/*     */     public List<ProblemReporter.Problem> problems() { return this.problems; }
/*     */     public Map<ProblemReporter.PathElement, ProblemTreeNode> children() { return this.children; }
/*     */     public ProblemTreeNode(ProblemReporter.PathElement pathElement) { this(pathElement, new ArrayList(), new LinkedHashMap()); }
/*     */     public ProblemTreeNode child(ProblemReporter.PathElement id) { return (ProblemTreeNode)this.children.computeIfAbsent(id, ProblemTreeNode::new); } }
/*     */   public static class ScopedCollector extends Collector implements AutoCloseable { private final Logger logger;
/* 233 */     public ScopedCollector(Logger logger) { this.logger = logger; }
/*     */ 
/*     */     
/*     */     public ScopedCollector(ProblemReporter.PathElement root, Logger logger) {
/* 237 */       super(root);
/* 238 */       this.logger = logger;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 243 */       if (!isEmpty())
/*     */       {
/* 245 */         this.logger.warn("[{}] Serialization errors:\n{}", this.logger.getName(), getTreeReport());
/*     */       }
/*     */     } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PathElement {
/*     */     String get();
/*     */   }
/*     */   
/*     */   public static interface Problem {
/*     */     String description();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ProblemReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
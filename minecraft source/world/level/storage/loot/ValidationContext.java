/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValidationContext
/*     */ {
/*     */   private final ProblemReporter reporter;
/*     */   private final ContextKeySet contextKeySet;
/*     */   private final Optional<HolderGetter.Provider> resolver;
/*     */   private final Set<ResourceKey<?>> visitedElements;
/*     */   
/*  22 */   public ValidationContext(ProblemReporter reporter, ContextKeySet contextKeySet, HolderGetter.Provider resolver) { this(reporter, contextKeySet, Optional.of(resolver), Set.of()); }
/*     */ 
/*     */ 
/*     */   
/*  26 */   public ValidationContext(ProblemReporter reporter, ContextKeySet contextKeySet) { this(reporter, contextKeySet, Optional.empty(), Set.of()); }
/*     */ 
/*     */   
/*     */   private ValidationContext(ProblemReporter reporter, ContextKeySet contextKeySet, Optional<HolderGetter.Provider> resolver, Set<ResourceKey<?>> visitedElements) {
/*  30 */     this.reporter = reporter;
/*  31 */     this.contextKeySet = contextKeySet;
/*  32 */     this.resolver = resolver;
/*  33 */     this.visitedElements = visitedElements;
/*     */   }
/*     */ 
/*     */   
/*  37 */   public ValidationContext forChild(ProblemReporter.PathElement subContext) { return new ValidationContext(this.reporter.forChild(subContext), this.contextKeySet, this.resolver, this.visitedElements); }
/*     */ 
/*     */   
/*     */   public ValidationContext enterElement(ProblemReporter.PathElement subContext, ResourceKey<?> element) {
/*  41 */     ImmutableSet immutableSet = ImmutableSet.builder().addAll(this.visitedElements).add(element).build();
/*  42 */     return new ValidationContext(this.reporter.forChild(subContext), this.contextKeySet, this.resolver, immutableSet);
/*     */   }
/*     */ 
/*     */   
/*  46 */   public boolean hasVisitedElement(ResourceKey<?> element) { return this.visitedElements.contains(element); }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public void reportProblem(ProblemReporter.Problem description) { this.reporter.report(description); }
/*     */ 
/*     */   
/*     */   public void validateContextUsage(LootContextUser lootContextUser) {
/*  54 */     Set<ContextKey<?>> allReferenced = lootContextUser.getReferencedContextParams();
/*  55 */     Sets.SetView setView = Sets.difference(allReferenced, this.contextKeySet.allowed());
/*  56 */     if (!setView.isEmpty()) {
/*  57 */       this.reporter.report(new ParametersNotProvidedProblem(setView));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  62 */   public HolderGetter.Provider resolver() { return (HolderGetter.Provider)this.resolver.orElseThrow(() -> new UnsupportedOperationException("References not allowed")); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public boolean allowsReferences() { return this.resolver.isPresent(); }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public ValidationContext setContextKeySet(ContextKeySet contextKeySet) { return new ValidationContext(this.reporter, contextKeySet, this.resolver, this.visitedElements); }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public ProblemReporter reporter() { return this.reporter; }
/*     */   public static final class ParametersNotProvidedProblem extends Record implements ProblemReporter.Problem { private final Set<ContextKey<?>> notProvided;
/*     */     
/*  77 */     public ParametersNotProvidedProblem(Set<ContextKey<?>> notProvided) { this.notProvided = notProvided; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  77 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem; } public Set<ContextKey<?>> notProvided() { return this.notProvided; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*  80 */     public String description() { return "Parameters " + String.valueOf(this.notProvided) + " are not provided in this context"; } }
/*     */   
/*     */   public static final class ReferenceNotAllowedProblem extends Record implements ProblemReporter.Problem { private final ResourceKey<?> referenced;
/*     */     
/*  84 */     public ReferenceNotAllowedProblem(ResourceKey<?> referenced) { this.referenced = referenced; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  84 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem; } public ResourceKey<?> referenced() { return this.referenced; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*  87 */     public String description() { return "Reference to " + String.valueOf(this.referenced.identifier()) + " of type " + String.valueOf(this.referenced.registry()) + " was used, but references are not allowed"; } }
/*     */   
/*     */   public static final class RecursiveReferenceProblem extends Record implements ProblemReporter.Problem { private final ResourceKey<?> referenced;
/*     */     
/*  91 */     public RecursiveReferenceProblem(ResourceKey<?> referenced) { this.referenced = referenced; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  91 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem; } public ResourceKey<?> referenced() { return this.referenced; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     public String description() {
/*  94 */       return String.valueOf(this.referenced.identifier()) + " of type " + String.valueOf(this.referenced.identifier()) + " is recursively called";
/*     */     } }
/*     */   public static final class MissingReferenceProblem extends Record implements ProblemReporter.Problem { private final ResourceKey<?> referenced;
/*     */     
/*  98 */     public MissingReferenceProblem(ResourceKey<?> referenced) { this.referenced = referenced; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;
/*  98 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<?> referenced() { return this.referenced; }
/*     */ 
/*     */     
/* 101 */     public String description() { return "Missing element " + String.valueOf(this.referenced.identifier()) + " of type " + String.valueOf(this.referenced.registry()); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\ValidationContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
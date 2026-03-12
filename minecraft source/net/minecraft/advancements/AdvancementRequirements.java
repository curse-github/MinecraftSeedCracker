/*     */ package net.minecraft.advancements;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ public final class AdvancementRequirements extends Record {
/*     */   private final List<List<String>> requirements;
/*     */   
/*  14 */   public AdvancementRequirements(List<List<String>> requirements) { this.requirements = requirements; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/AdvancementRequirements;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  14 */     //   0	7	0	this	Lnet/minecraft/advancements/AdvancementRequirements; } public List<List<String>> requirements() { return this.requirements; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/AdvancementRequirements;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/AdvancementRequirements;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*  15 */   public static final Codec<AdvancementRequirements> CODEC = Codec.STRING.listOf().listOf().xmap(AdvancementRequirements::new, AdvancementRequirements::requirements);
/*     */   
/*  17 */   public static final AdvancementRequirements EMPTY = new AdvancementRequirements(List.of());
/*     */ 
/*     */   
/*  20 */   public AdvancementRequirements(FriendlyByteBuf input) { this(input.readList(in -> in.readList(FriendlyByteBuf::readUtf))); }
/*     */ 
/*     */ 
/*     */   
/*  24 */   public void write(FriendlyByteBuf output) { output.writeCollection(this.requirements, (out, set) -> out.writeCollection(set, FriendlyByteBuf::writeUtf)); }
/*     */ 
/*     */ 
/*     */   
/*  28 */   public static AdvancementRequirements allOf(Collection<String> criteria) { return new AdvancementRequirements(criteria.stream().map(List::of).toList()); }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public static AdvancementRequirements anyOf(Collection<String> criteria) { return new AdvancementRequirements(List.of(List.copyOf(criteria))); }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public int size() { return this.requirements.size(); }
/*     */ 
/*     */   
/*     */   public boolean test(Predicate<String> predicate) {
/*  40 */     if (this.requirements.isEmpty()) {
/*  41 */       return false;
/*     */     }
/*  43 */     for (List<String> set : this.requirements) {
/*  44 */       if (!anyMatch(set, predicate)) {
/*  45 */         return false;
/*     */       }
/*     */     } 
/*  48 */     return true;
/*     */   }
/*     */   
/*     */   public int count(Predicate<String> predicate) {
/*  52 */     int count = 0;
/*  53 */     for (List<String> set : this.requirements) {
/*  54 */       if (anyMatch(set, predicate)) {
/*  55 */         count++;
/*     */       }
/*     */     } 
/*  58 */     return count;
/*     */   }
/*     */   
/*     */   private static boolean anyMatch(List<String> criteria, Predicate<String> predicate) {
/*  62 */     for (String criterion : criteria) {
/*  63 */       if (predicate.test(criterion)) {
/*  64 */         return true;
/*     */       }
/*     */     } 
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public DataResult<AdvancementRequirements> validate(Set<String> expectedCriteria) {
/*  71 */     ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet();
/*  72 */     for (List<String> set : this.requirements) {
/*  73 */       if (set.isEmpty() && expectedCriteria.isEmpty()) {
/*  74 */         return DataResult.error(() -> "Requirement entry cannot be empty");
/*     */       }
/*  76 */       objectOpenHashSet.addAll(set);
/*     */     } 
/*  78 */     if (!expectedCriteria.equals(objectOpenHashSet)) {
/*  79 */       Sets.SetView setView1 = Sets.difference(expectedCriteria, objectOpenHashSet);
/*  80 */       Sets.SetView setView2 = Sets.difference(objectOpenHashSet, expectedCriteria);
/*  81 */       return DataResult.error(() -> "Advancement completion requirements did not exactly match specified criteria. Missing: " + String.valueOf(missingCriteria) + ". Unknown: " + String.valueOf(unknownCriteria));
/*     */     } 
/*  83 */     return DataResult.success(this);
/*     */   }
/*     */ 
/*     */   
/*  87 */   public boolean isEmpty() { return this.requirements.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public String toString() { return this.requirements.toString(); }
/*     */ 
/*     */   
/*     */   public Set<String> names() {
/*  96 */     ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet();
/*  97 */     for (List<String> set : this.requirements) {
/*  98 */       objectOpenHashSet.addAll(set);
/*     */     }
/* 100 */     return objectOpenHashSet;
/*     */   }
/*     */   
/*     */   public static interface Strategy {
/* 104 */     public static final Strategy AND = AdvancementRequirements::allOf;
/* 105 */     public static final Strategy OR = AdvancementRequirements::anyOf;
/*     */     
/*     */     AdvancementRequirements create(Collection<String> param1Collection);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\AdvancementRequirements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */